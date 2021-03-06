package com.gmail.yauheniylebedzeu.repository;

import com.gmail.yauheniylebedzeu.repository.enums.RoleEnum;
import com.gmail.yauheniylebedzeu.repository.model.Role;

import java.util.Optional;

public interface RoleRepository extends GenericRepository<Long, Role> {

    Optional<Role> findByName(RoleEnum name);
}