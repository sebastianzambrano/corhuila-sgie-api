package com.corhuila.sgie.Site.IRepository;

import com.corhuila.sgie.Site.Entity.Municipio;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMunicipioRepository extends IBaseRepository<Municipio, Long> {
    List<Municipio> findByDepartamentoIdAndStateTrue(Long departamentoId);
}
