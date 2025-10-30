package com.corhuila.sgie.Site.Service;

import com.corhuila.sgie.Site.DTO.IInstalacionCampusDTO;
import com.corhuila.sgie.Site.DTO.InstalacionReporteDTO;
import com.corhuila.sgie.Site.IRepository.IInstalacionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InstalacionServiceTest {

    @Mock
    private IInstalacionRepository repository;
    @InjectMocks
    private InstalacionService service;

    @Test
    void findInstalacionesCampusConsultaRepositorio() {
        IInstalacionCampusDTO dto = org.mockito.Mockito.mock(IInstalacionCampusDTO.class);
        when(repository.findInstalacionesCampus("lab", "pri")).thenReturn(List.of(dto));

        List<IInstalacionCampusDTO> result = service.findInstalacionesCampus("lab", "pri");

        assertThat(result).containsExactly(dto);
        verify(repository).findInstalacionesCampus("lab", "pri");
    }

    @Test
    void proveedorStreamExponeStreamRepositorio() {
        InstalacionReporteDTO dto = new InstalacionReporteDTO(
                1L,
                "América",
                "Colombia",
                "Huila",
                "Neiva",
                "Principal",
                "Laboratorio",
                "Categoría");
        when(repository.generarReporteInstalaciones()).thenReturn(Stream.of(dto));

        try (Stream<InstalacionReporteDTO> stream = service.proveedorStream().get()) {
            assertThat(stream).containsExactly(dto);
        }
    }

    @Test
    void obtenerDatosEnMemoriaRecolectaStream() {
        InstalacionReporteDTO dto = new InstalacionReporteDTO(
                1L,
                "América",
                "Colombia",
                "Huila",
                "Neiva",
                "Principal",
                "Laboratorio",
                "Categoría");
        when(repository.generarReporteInstalaciones()).thenReturn(Stream.of(dto));

        List<InstalacionReporteDTO> datos = service.obtenerDatosEnMemoria();

        assertThat(datos).containsExactly(dto);
        verify(repository).generarReporteInstalaciones();
    }
}
