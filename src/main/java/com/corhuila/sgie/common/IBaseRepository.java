package com.corhuila.sgie.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;
/*
@Repository
@EnableJpaRepositories
public interface IBaseRepository <T extends Auditoria, ID> extends JpaRepository<T,ID> {
}
*/
@NoRepositoryBean
public interface IBaseRepository<T, ID> extends JpaRepository<T, ID> {
}