package com.corhuila.sgie.User.IRepository;

import com.corhuila.sgie.User.DTO.IPersonaUsuarioDTO;
import com.corhuila.sgie.User.Entity.Persona;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPersonaRepository extends IBaseRepository<Persona, Long> {
    @Query(value = """
        SELECT 
            pe.tipo_documento AS tipoDocumento, 
            pe.numero_identificacion AS numeroIdentificacionPersona,
            pe.nombres AS nombres, 
            pe.apellidos AS apellidos, 
            us.email AS email, 
            us.state AS estado
        FROM usuario us 
        INNER JOIN persona pe ON us.id_persona = pe.id
        WHERE (:numeroIdentificacionPersona IS NULL OR :numeroIdentificacionPersona = '' OR pe.numero_identificacion = :numeroIdentificacionPersona)
        """, nativeQuery = true)
    List<IPersonaUsuarioDTO> findUsuariosPersonaPorIdentificacion(
            @Param("numeroIdentificacionPersona") String numeroIdentificacionPersona
    );
}
