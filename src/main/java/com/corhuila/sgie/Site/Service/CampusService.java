package com.corhuila.sgie.Site.Service;

import com.corhuila.sgie.Site.Entity.Campus;
import com.corhuila.sgie.Site.IRepository.ICampusRepository;
import com.corhuila.sgie.Site.IService.ICapusService;
import com.corhuila.sgie.common.BaseService;
import com.corhuila.sgie.common.IBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CampusService extends BaseService<Campus> implements ICapusService {
    @Autowired
    private ICampusRepository repository;

    @Override
    protected IBaseRepository<Campus, Long> getRepository() {
        return repository;
    }
}
