package com.corhuila.sgie.Site.IRepository;

import com.corhuila.sgie.Site.DTO.CampusReporteDTO;
import com.corhuila.sgie.Site.Entity.Campus;
import com.corhuila.sgie.common.IBaseRepository;
import jakarta.persistence.QueryHint;
import org.hibernate.jpa.HibernateHints;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface ICampusRepository extends IBaseRepository<Campus, Long> {
    List<Campus> findByMunicipioIdAndStateTrue(Long municipioId);

    @Query("""
            SELECT new com.corhuila.sgie.Site.DTO.CampusReporteDTO(
                ca.id,
                co.nombre,
                pa.nombre,
                de.nombre,
                mu.nombre,
                ca.nombre
                )
            FROM Campus ca
            JOIN ca.municipio mu
            JOIN mu.departamento de
            JOIN de.pais pa
            JOIN pa.continente co
            """)
    @QueryHints(@QueryHint(name = HibernateHints.HINT_FETCH_SIZE, value = "1000"))
    Stream<CampusReporteDTO> generarReporteCampuss();
}
