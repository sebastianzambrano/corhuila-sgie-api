package com.corhuila.sgie.Site.IRepository;

import com.corhuila.sgie.Site.Entity.Instalacion;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IInstalacionRepository extends IBaseRepository<Instalacion, Long> {
}
