package com.corhuila.sgie.Site.IRepository;

import com.corhuila.sgie.Site.Entity.Continente;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IContinenteRepository extends IBaseRepository<Continente, Long> {
}
