package com.corhuila.sgie.Site.IRepository;

import com.corhuila.sgie.Site.Entity.Campus;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICampusRepository extends IBaseRepository<Campus, Long> {
    List<Campus> findByMunicipioIdAndStateTrue(Long municipioId);
}
