package com.gmail.yauheniylebedzeu.service;

import com.gmail.yauheniylebedzeu.service.model.PageDTO;
import com.gmail.yauheniylebedzeu.service.model.ReviewDTO;

import java.util.List;
import java.util.Optional;

public interface ReviewService {

    PageDTO<ReviewDTO> getReviewPage(int pageNumber, int maxResult, String sortParameter);

    void removeByUuid(String uuid);

    List<ReviewDTO> changeVisibilityByUuids(List<String> checkedUuids, List<String> previouslyCheckedUuids);

    PageDTO<ReviewDTO> getVisibleReviewsPage(int pageNumber, int pageSize, String sortParameter);
}
