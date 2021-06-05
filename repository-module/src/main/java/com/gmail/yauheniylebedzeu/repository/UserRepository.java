package com.gmail.yauheniylebedzeu.repository;

import com.gmail.yauheniylebedzeu.repository.model.User;

import java.util.Optional;

public interface UserRepository extends GenericRepository<Long, User> {

    Optional<User> findByEmail(String email);
}