package com.gmail.yauheniylebedzeu.repository;

import com.gmail.yauheniylebedzeu.repository.model.Review;

import java.util.List;

public interface ReviewRepository extends GenericRepository<Long, Review> {

    List<Review> findVisibleReviews(int startPosition, int maxResult, String sortParameter);

    Long getCountOfVisible();
}