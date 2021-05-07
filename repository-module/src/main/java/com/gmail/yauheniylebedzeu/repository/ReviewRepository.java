package com.gmail.yauheniylebedzeu.repository;

import com.gmail.yauheniylebedzeu.repository.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends GenericRepository<Long, Review> {

    Optional<Review> findByUuid(String uuid);

    List<Review> findAllVisible(int startPosition, int maxResult, String sortParameter);

    Long getCountOfVisible();

}
