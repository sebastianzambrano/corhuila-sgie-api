package com.corhuila.sgie.common;

import org.springframework.dao.DataAccessException;

import java.util.List;

public interface IBaseService<T extends Auditoria> {
    List<T> all();

    List<T> findByStateTrue();

    T findById(Long id) throws DataAccessException;

    T save(T entity) throws DataAccessException;

    void update(Long id, T entity) throws DataAccessException;

    void delete(Long id) throws DataAccessException;

    void cambiarEstado(Long id, Boolean estado) throws DataAccessException;
}
