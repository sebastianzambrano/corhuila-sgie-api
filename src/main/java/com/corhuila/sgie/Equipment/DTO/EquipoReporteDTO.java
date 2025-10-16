package com.corhuila.sgie.Equipment.DTO;

import com.corhuila.sgie.common.Reporting.ReportColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EquipoReporteDTO {

    @ReportColumn(header = "ID", order = 0)
    private Long idEquipo;

    @ReportColumn(header = "Código", order = 1)
    private String codigoEquipo;

    @ReportColumn(header = "Nombre equipo", order = 2)
    private String nombreEquipo;

    @ReportColumn(header = "Estado equipo", order = 3)
    private Boolean estadoEquipo;

    @ReportColumn(header = "Instalación", order = 4)
    private String nombreInstalacion;

    @ReportColumn(header = "Categoría", order = 5)
    private String nombreCategoriaEquipo;

    @ReportColumn(header = "Campus", order = 6)
    private String nombreCampus;
}
