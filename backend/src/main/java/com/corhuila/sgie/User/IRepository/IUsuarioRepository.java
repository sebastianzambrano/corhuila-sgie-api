package com.corhuila.sgie.User.IRepository;

import com.corhuila.sgie.User.Entity.Usuario;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUsuarioRepository extends IBaseRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
}
