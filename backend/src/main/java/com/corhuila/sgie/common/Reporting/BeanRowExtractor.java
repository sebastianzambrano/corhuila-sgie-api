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
import java.util.function.UnaryOperator;

public final class BeanRowExtractor {

    private static final Map<Class<?>, List<ColumnMeta>> CACHE = new ConcurrentHashMap<>();

    private BeanRowExtractor() {
    }

    public static List<String> headers(Class<?> dtoType) {
        return metas(dtoType).stream()
                .map(ColumnMeta::header)
                .toList();
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

        processAnnotatedFields(dtoType, annotated, explicitProperties);
        processAnnotatedMethods(dtoType, annotated, explicitProperties);

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
            if (!explicitProperties.contains(fallback.property())) {
                annotatedList.add(fallback.withOrder(nextOrder++));
            }
        }

        annotatedList.sort(Comparator.comparingInt(ColumnMeta::order).thenComparing(ColumnMeta::header));
        return List.copyOf(annotatedList);
    }

    private static void processAnnotatedFields(Class<?> dtoType, Map<String, ColumnMeta> annotated, Set<String> explicitProperties) {
        for (Field field : allFields(dtoType)) {
            ReportColumn column = field.getAnnotation(ReportColumn.class);
            if (column == null) {
                continue;
            }
            UnaryOperator<Object> getter = bean -> getFieldValue(field, bean);

            ColumnMeta meta = ColumnMeta.builder()
                    .property(field.getName())
                    .header(column.header())
                    .order(column.order())
                    .format(column.format())
                    .width(column.width())
                    .autosize(column.autosize())
                    .wrap(column.wrap())
                    .text(column.text())
                    .getter(getter)
                    .build();
            explicitProperties.add(field.getName());
            annotated.put(field.getName(), meta);
        }
    }

    private static void processAnnotatedMethods(Class<?> dtoType, Map<String, ColumnMeta> annotated, Set<String> explicitProperties) {
        for (Method method : dtoType.getMethods()) {
            if (method.getParameterCount() == 0 && method.getAnnotation(ReportColumn.class) != null) {
                ReportColumn column = method.getAnnotation(ReportColumn.class);
                UnaryOperator<Object> getter = bean -> invokeMethod(method, bean);
                String propertyName = propertyNameFromGetter(method.getName());

                ColumnMeta meta = ColumnMeta.builder()
                        .property(propertyName)
                        .header(column.header())
                        .order(column.order())
                        .format(column.format())
                        .width(column.width())
                        .autosize(column.autosize())
                        .wrap(column.wrap())
                        .text(column.text())
                        .getter(getter)
                        .build();
                explicitProperties.add(propertyName);
                annotated.put(propertyName, meta);
            }
        }
    }


    private static List<ColumnMeta> defaultColumns(Class<?> dtoType) {
        Map<String, ColumnMeta> map = new LinkedHashMap<>();
        AtomicInteger order = new AtomicInteger(0);

        processFields(dtoType, map, order);
        processBeanProperties(dtoType, map, order);
        processRecordComponents(dtoType, map, order);

        return new ArrayList<>(map.values());
    }

    private static void processFields(Class<?> dtoType, Map<String, ColumnMeta> map, AtomicInteger order) {
        for (Field field : allFields(dtoType)) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            map.computeIfAbsent(field.getName(), name -> ColumnMeta.builder()
                    .property(name)
                    .header(defaultHeader(name))
                    .order(order.getAndIncrement())
                    .format("")
                    .width(-1)
                    .autosize(false)
                    .wrap(false)
                    .text(false)
                    .getter(bean -> getFieldValue(field, bean))
                    .build());
        }
    }

    private static void processBeanProperties(Class<?> dtoType, Map<String, ColumnMeta> map, AtomicInteger order) {
        try {
            PropertyDescriptor[] descriptors = Introspector.getBeanInfo(dtoType).getPropertyDescriptors();
            for (PropertyDescriptor descriptor : descriptors) {
                String name = descriptor.getName();
                Method readMethod = descriptor.getReadMethod();

                if (!"class".equals(name) && !map.containsKey(name) && readMethod != null) {

                    map.put(name, ColumnMeta.builder()
                            .property(name)
                            .header(defaultHeader(name))
                            .order(order.getAndIncrement())
                            .format("")
                            .width(-1)
                            .autosize(false)
                            .wrap(false)
                            .text(false)
                            .getter(bean -> invokeMethod(readMethod, bean))
                            .build());
                }
            }
        } catch (IntrospectionException ignored) {
            // Si ocurre se ignora, ya se cubrió por campos.
        }
    }

    private static void processRecordComponents(Class<?> dtoType, Map<String, ColumnMeta> map, AtomicInteger order) {
        if (!dtoType.isRecord()) {
            return;
        }

        for (RecordComponent component : dtoType.getRecordComponents()) {
            String name = component.getName();
            if (map.containsKey(name)) {
                continue;
            }
            Method accessor = component.getAccessor();

            map.put(name, ColumnMeta.builder()
                    .property(name)
                    .header(defaultHeader(name))
                    .order(order.getAndIncrement())
                    .format("")
                    .width(-1)
                    .autosize(false)
                    .wrap(false)
                    .text(false)
                    .getter(bean -> invokeMethod(accessor, bean))
                    .build());
        }
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


    private static Object getFieldValue(Field field, Object bean) {
        try {
            // Intenta acceso directo primero
            if (field.canAccess(bean)) {
                return field.get(bean);
            }

            // Usa MethodHandles como alternativa segura
            var lookup = java.lang.invoke.MethodHandles.privateLookupIn(
                    field.getDeclaringClass(),
                    java.lang.invoke.MethodHandles.lookup()
            );
            var handle = lookup.unreflectGetter(field);
            return handle.invoke(bean);
        } catch (Throwable e) {
            throw new IllegalStateException("No se pudo leer el campo " + field.getName(), e);
        }
    }

    private static Object invokeMethod(Method method, Object bean) {
        try {
            // Intenta acceso directo primero
            if (method.canAccess(bean)) {
                return method.invoke(bean);
            }

            // Usa MethodHandles como alternativa segura
            var lookup = java.lang.invoke.MethodHandles.privateLookupIn(
                    method.getDeclaringClass(),
                    java.lang.invoke.MethodHandles.lookup()
            );
            var handle = lookup.unreflect(method);
            return handle.invoke(bean);
        } catch (Throwable e) {
            throw new IllegalStateException("No se pudo invocar el método " + method.getName(), e);
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
        private final UnaryOperator<Object> getter;

        private ColumnMeta(Builder builder) {
            this.property = builder.property;
            this.header = builder.header;
            this.order = builder.order;
            this.format = builder.format;
            this.width = builder.width;
            this.autosize = builder.autosize;
            this.wrap = builder.wrap;
            this.text = builder.text;
            this.getter = builder.getter;
        }

        public static Builder builder() {
            return new Builder();
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
            return builder()
                    .property(property)
                    .header(header)
                    .order(newOrder)
                    .format(format)
                    .width(width)
                    .autosize(autosize)
                    .wrap(wrap)
                    .text(text)
                    .getter(getter)
                    .build();
        }

        public static class Builder {
            private String property;
            private String header;
            private int order;
            private String format = "";
            private int width = -1;
            private boolean autosize;
            private boolean wrap;
            private boolean text;
            private UnaryOperator<Object> getter;

            public Builder property(String property) {
                this.property = property;
                return this;
            }

            public Builder header(String header) {
                this.header = header;
                return this;
            }

            public Builder order(int order) {
                this.order = order;
                return this;
            }

            public Builder format(String format) {
                this.format = format;
                return this;
            }

            public Builder width(int width) {
                this.width = width;
                return this;
            }

            public Builder autosize(boolean autosize) {
                this.autosize = autosize;
                return this;
            }

            public Builder wrap(boolean wrap) {
                this.wrap = wrap;
                return this;
            }

            public Builder text(boolean text) {
                this.text = text;
                return this;
            }

            public Builder getter(UnaryOperator<Object> getter) {
                this.getter = getter;
                return this;
            }

            public ColumnMeta build() {
                return new ColumnMeta(this);
            }
        }
    }

}

