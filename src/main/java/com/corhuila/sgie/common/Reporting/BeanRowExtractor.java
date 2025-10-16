package com.corhuila.sgie.common.Reporting;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.RecordComponent;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class BeanRowExtractor {

    private static final Map<Class<?>, List<ColumnMeta>> CACHE = new ConcurrentHashMap<>();

    private BeanRowExtractor() {
    }

    public static List<String> headers(Class<?> dtoType) {
        return metas(dtoType).stream()
                .map(ColumnMeta::header)
                .collect(Collectors.toList());
    }

    public static List<Object> values(Object row) {
        if (row == null) {
            return List.of();
        }
        List<ColumnMeta> metas = metas(row.getClass());
        List<Object> values = new ArrayList<>(metas.size());
        for (ColumnMeta meta : metas) {
            values.add(meta.get(row));
        }
        return values;
    }

    public static List<ColumnMeta> metas(Class<?> dtoType) {
        if (dtoType == null) {
            throw new IllegalArgumentException("El tipo de DTO no puede ser nulo");
        }
        return CACHE.computeIfAbsent(dtoType, BeanRowExtractor::inspect);
    }

    private static List<ColumnMeta> inspect(Class<?> dtoType) {
        Map<String, ColumnMeta> annotated = new LinkedHashMap<>();
        Set<String> explicitProperties = new HashSet<>();

        for (Field field : allFields(dtoType)) {
            ReportColumn column = field.getAnnotation(ReportColumn.class);
            if (column == null) {
                continue;
            }
            makeAccessible(field);
            Function<Object, Object> getter = bean -> {
                try {
                    return field.get(bean);
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException("No se pudo leer el campo " + field.getName(), e);
                }
            };
            ColumnMeta meta = new ColumnMeta(
                    field.getName(),
                    column.header(),
                    column.order(),
                    column.format(),
                    column.width(),
                    column.autosize(),
                    column.wrap(),
                    column.text(),
                    getter);
            explicitProperties.add(field.getName());
            annotated.put(field.getName(), meta);
        }

        for (Method method : dtoType.getMethods()) {
            if (method.getParameterCount() != 0) {
                continue;
            }
            ReportColumn column = method.getAnnotation(ReportColumn.class);
            if (column == null) {
                continue;
            }
            makeAccessible(method);
            String propertyName = propertyNameFromGetter(method.getName());
            Function<Object, Object> getter = bean -> {
                try {
                    return method.invoke(bean);
                } catch (Exception e) {
                    throw new IllegalStateException("No se pudo invocar el getter " + method.getName(), e);
                }
            };
            ColumnMeta meta = new ColumnMeta(
                    propertyName,
                    column.header(),
                    column.order(),
                    column.format(),
                    column.width(),
                    column.autosize(),
                    column.wrap(),
                    column.text(),
                    getter);
            explicitProperties.add(propertyName);
            annotated.put(propertyName, meta);
        }

        List<ColumnMeta> annotatedList = new ArrayList<>(annotated.values());
        annotatedList.sort(Comparator.comparingInt(ColumnMeta::order).thenComparing(ColumnMeta::header));

        List<ColumnMeta> defaults = defaultColumns(dtoType);
        if (annotatedList.isEmpty()) {
            return List.copyOf(defaults);
        }

        int nextOrder = annotatedList.stream()
                .mapToInt(ColumnMeta::order)
                .max()
                .orElse(-1) + 1;

        for (ColumnMeta fallback : defaults) {
            if (explicitProperties.contains(fallback.property())) {
                continue;
            }
            annotatedList.add(fallback.withOrder(nextOrder++));
        }

        annotatedList.sort(Comparator.comparingInt(ColumnMeta::order).thenComparing(ColumnMeta::header));
        return List.copyOf(annotatedList);
    }

    private static List<ColumnMeta> defaultColumns(Class<?> dtoType) {
        Map<String, ColumnMeta> map = new LinkedHashMap<>();
        AtomicInteger order = new AtomicInteger(0);

        for (Field field : allFields(dtoType)) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            makeAccessible(field);
            map.computeIfAbsent(field.getName(), name -> new ColumnMeta(
                    name,
                    defaultHeader(name),
                    order.getAndIncrement(),
                    "",
                    -1,
                    false,
                    false,
                    false,
                    bean -> {
                        try {
                            return field.get(bean);
                        } catch (IllegalAccessException e) {
                            throw new IllegalStateException("No se pudo leer el campo " + name, e);
                        }
                    }
            ));
        }

        try {
            PropertyDescriptor[] descriptors = Introspector.getBeanInfo(dtoType).getPropertyDescriptors();
            for (PropertyDescriptor descriptor : descriptors) {
                String name = descriptor.getName();
                if ("class".equals(name) || map.containsKey(name)) {
                    continue;
                }
                Method readMethod = descriptor.getReadMethod();
                if (readMethod == null) {
                    continue;
                }
                makeAccessible(readMethod);
                map.put(name, new ColumnMeta(
                        name,
                        defaultHeader(name),
                        order.getAndIncrement(),
                        "",
                        -1,
                        false,
                        false,
                        false,
                        bean -> {
                            try {
                                return readMethod.invoke(bean);
                            } catch (Exception e) {
                                throw new IllegalStateException("No se pudo invocar el getter " + readMethod.getName(), e);
                            }
                        }
                ));
            }
        } catch (IntrospectionException ignored) {
            // Si ocurre se ignora, ya se cubrió por campos.
        }

        if (dtoType.isRecord()) {
            for (RecordComponent component : dtoType.getRecordComponents()) {
                String name = component.getName();
                if (map.containsKey(name)) {
                    continue;
                }
                Method accessor = component.getAccessor();
                makeAccessible(accessor);
                map.put(name, new ColumnMeta(
                        name,
                        defaultHeader(name),
                        order.getAndIncrement(),
                        "",
                        -1,
                        false,
                        false,
                        false,
                        bean -> {
                            try {
                                return accessor.invoke(bean);
                            } catch (Exception e) {
                                throw new IllegalStateException("No se pudo invocar el accessor del record " + name, e);
                            }
                        }
                ));
            }
        }

        return new ArrayList<>(map.values());
    }

    private static List<Field> allFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();
        Class<?> current = type;
        while (current != null && current != Object.class) {
            fields.addAll(Arrays.asList(current.getDeclaredFields()));
            current = current.getSuperclass();
        }
        return fields;
    }

    private static void makeAccessible(Field field) {
        try {
            field.setAccessible(true);
        } catch (Throwable ignored) {
        }
    }

    private static void makeAccessible(Method method) {
        try {
            method.setAccessible(true);
        } catch (Throwable ignored) {
        }
    }

    private static String propertyNameFromGetter(String methodName) {
        if (methodName.startsWith("get") && methodName.length() > 3) {
            return decapitalize(methodName.substring(3));
        }
        if (methodName.startsWith("is") && methodName.length() > 2) {
            return decapitalize(methodName.substring(2));
        }
        return decapitalize(methodName);
    }

    private static String decapitalize(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        return Character.toLowerCase(value.charAt(0)) + value.substring(1);
    }

    private static String defaultHeader(String property) {
        if (property == null || property.isBlank()) {
            return "";
        }
        StringBuilder sb = new StringBuilder(property.length() + 4);
        char[] chars = property.toCharArray();
        sb.append(Character.toUpperCase(chars[0]));
        for (int i = 1; i < chars.length; i++) {
            char c = chars[i];
            if (Character.isUpperCase(c) || Character.isDigit(c)) {
                sb.append(' ');
            }
            sb.append(c);
        }
        return sb.toString().replaceAll(" +", " ").trim();
    }

    public static final class ColumnMeta {
        private final String property;
        private final String header;
        private final int order;
        private final String format;
        private final int width;
        private final boolean autosize;
        private final boolean wrap;
        private final boolean text;
        private final Function<Object, Object> getter;

        ColumnMeta(String property,
                   String header,
                   int order,
                   String format,
                   int width,
                   boolean autosize,
                   boolean wrap,
                   boolean text,
                   Function<Object, Object> getter) {
            this.property = property;
            this.header = header;
            this.order = order;
            this.format = format;
            this.width = width;
            this.autosize = autosize;
            this.wrap = wrap;
            this.text = text;
            this.getter = getter;
        }

        public String property() {
            return property;
        }

        public String header() {
            return header;
        }

        public int order() {
            return order;
        }

        public String format() {
            return format;
        }

        public int width() {
            return width;
        }

        public boolean autosize() {
            return autosize;
        }

        public boolean wrap() {
            return wrap;
        }

        public boolean text() {
            return text;
        }

        public Object get(Object bean) {
            try {
                return getter.apply(bean);
            } catch (Exception e) {
                return null;
            }
        }

        ColumnMeta withOrder(int newOrder) {
            return new ColumnMeta(property, header, newOrder, format, width, autosize, wrap, text, getter);
        }
    }
}
