package com.gmail.yauheniylebedzeu.service.converter;

import com.gmail.yauheniylebedzeu.repository.model.Review;
import com.gmail.yauheniylebedzeu.service.model.ReviewDTO;

public interface ReviewConverter {

    Review convertReviewDTOToReview(ReviewDTO reviewDTO);

    ReviewDTO convertReviewToReviewDTO(Review review);

}
