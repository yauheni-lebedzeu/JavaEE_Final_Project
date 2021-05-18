package com.gmail.yauheniylebedzeu.service.converter;

import com.gmail.yauheniylebedzeu.repository.model.Review;
import com.gmail.yauheniylebedzeu.service.model.ReviewDTO;

import java.util.List;

public interface ReviewConverter {

    Review convertReviewDTOToReview(ReviewDTO reviewDTO);

    ReviewDTO convertReviewToReviewDTO(Review review);

    List<ReviewDTO> convertReviewListToReviewDTOList(List<Review> reviews);

}
