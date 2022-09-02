package com.example.brunch.repository;

import com.example.brunch.dbmodel.RoleEntity;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<RoleEntity, Integer> {

    RoleEntity getByCode(String code);
}
