package com.gmail.yauheniylebedzeu.service;

import com.gmail.yauheniylebedzeu.service.model.ReviewDTO;

import java.util.List;
import java.util.Optional;

public interface ReviewService {

    Long getCountOfReviews();

    List<ReviewDTO> getReviewList(int startPosition, int maxResult, String sortParameter);

    void removeByUuid(String uuid);

    Optional<ReviewDTO> changeVisibilityByUuid(String uuid);

    List<ReviewDTO> findAllVisible(int startPosition, int maxResult, String sortFieldName);

    Long getCountOfVisible();

}
