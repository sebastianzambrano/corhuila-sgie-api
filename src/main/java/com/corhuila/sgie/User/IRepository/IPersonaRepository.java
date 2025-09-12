package com.corhuila.sgie.User.IRepository;

import com.corhuila.sgie.User.Entity.Persona;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPersonaRepository extends IBaseRepository<Persona, Long> {
}
