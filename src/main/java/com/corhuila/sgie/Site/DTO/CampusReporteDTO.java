package com.corhuila.sgie.Site.DTO;

import com.corhuila.sgie.common.Reporting.ReportColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CampusReporteDTO {
    @ReportColumn(header = "ID", order = 0)
    private Long idCampus;

    @ReportColumn(header = "Continente", order = 1)
    private String nombreContinente;

    @ReportColumn(header = "Pais", order = 2)
    private String nombrePais;

    @ReportColumn(header = "Departamento", order = 3)
    private String nombreDepartamento;

    @ReportColumn(header = "Municipio", order = 4)
    private String nombreMunicipio;

    @ReportColumn(header = "Campus", order = 5)
    private String nombreCampus;

}
