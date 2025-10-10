package com.corhuila.sgie.Site.IRepository;

import com.corhuila.sgie.Site.Entity.Pais;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPaisRepository extends IBaseRepository<Pais, Long> {
    List<Pais> findByContinenteIdAndStateTrue(Long continenteId);
}
