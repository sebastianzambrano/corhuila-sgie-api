package com.corhuila.sgie.Site.Service;

import com.corhuila.sgie.Site.DTO.CampusReporteDTO;
import com.corhuila.sgie.Site.IRepository.ICampusRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CampusServiceTest {

    @Mock
    private ICampusRepository repository;

    @InjectMocks
    private CampusService service;

    @Test
    void proveedorStreamUsaRepositorio() {
        CampusReporteDTO dto = new CampusReporteDTO(1L, "América", "Colombia",
                "Huila", "Neiva", "Campus principal");
        when(repository.generarReporteCampuss()).thenReturn(Stream.of(dto));

        try (Stream<CampusReporteDTO> stream = service.proveedorStream().get()) {
            assertThat(stream).containsExactly(dto);
        }
    }

    @Test
    void obtenerDatosEnMemoriaRecolectaDatos() {
        CampusReporteDTO dto = new CampusReporteDTO(1L, "América", "Colombia",
                "Huila", "Neiva", "Campus principal");
        when(repository.generarReporteCampuss()).thenReturn(Stream.of(dto));

        List<CampusReporteDTO> datos = service.obtenerDatosEnMemoria();
        assertThat(datos).containsExactly(dto);
    }
}
