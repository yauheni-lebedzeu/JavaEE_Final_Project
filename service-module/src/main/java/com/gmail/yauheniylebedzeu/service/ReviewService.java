package com.gmail.yauheniylebedzeu.service;

import com.gmail.yauheniylebedzeu.service.model.ReviewDTO;

import java.util.List;
import java.util.Optional;

public interface ReviewService {

    Long getCountOfReviews();

    List<ReviewDTO> findAll(int startPosition, int maxResult, String sortFieldName);

    void removeByUuid(String uuid);

    Optional<ReviewDTO> changeVisibilityByUuid(String uuid);

}
