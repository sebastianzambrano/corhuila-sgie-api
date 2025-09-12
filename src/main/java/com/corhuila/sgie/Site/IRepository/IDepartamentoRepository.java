package com.corhuila.sgie.Site.IRepository;

import com.corhuila.sgie.Site.Entity.Departamento;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDepartamentoRepository extends IBaseRepository<Departamento, Long> {
}
