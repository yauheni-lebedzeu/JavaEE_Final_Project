package com.gmail.yauheniylebedzeu.repository;

import com.gmail.yauheniylebedzeu.repository.model.Review;

import java.util.Optional;

public interface ReviewRepository extends GenericRepository<Long, Review> {

    Optional<Review> findByUuid(String uuid);

}
