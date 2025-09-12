package com.corhuila.sgie.User.Service;

import com.corhuila.sgie.User.Entity.Usuario;
import com.corhuila.sgie.User.IRepository.IUsuarioRepository;
import com.corhuila.sgie.User.IService.IUsuarioService;
import com.corhuila.sgie.common.BaseService;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService extends BaseService<Usuario> implements IUsuarioService {
    @Autowired
    private IUsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected IBaseRepository<Usuario, Long> getRepository() {
        return repository;
    }

    @Override
    public Usuario save(Usuario entity) throws Exception {
        if (entity.getPassword() != null && !entity.getPassword().isBlank()) {
            entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        }
        return super.save(entity);
    }

    @Override
    public void update(Long id, Usuario entity) throws Exception {
        // Si se proporciona password en update, encriptar antes de copiar
        if (entity.getPassword() != null && !entity.getPassword().isBlank()) {
            entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        } else {
            // evitar sobrescribir password con null en BeanUtils.copyProperties (handled by BaseService ignore)
        }
        super.update(id, entity);
    }

}
