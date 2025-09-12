package com.corhuila.sgie.Site.IRepository;

import com.corhuila.sgie.Site.Entity.Campus;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICampusRepository extends IBaseRepository<Campus, Long> {
}
