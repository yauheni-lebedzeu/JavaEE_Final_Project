package com.gmail.yauheniylebedzeu.service.converter.impl;

import com.gmail.yauheniylebedzeu.repository.model.Review;
import com.gmail.yauheniylebedzeu.repository.model.User;
import com.gmail.yauheniylebedzeu.service.converter.ReviewConverter;
import com.gmail.yauheniylebedzeu.service.exception.UserNotReceivedException;
import com.gmail.yauheniylebedzeu.service.model.ReviewDTO;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ReviewConverterImpl implements ReviewConverter {

    @Override
    public Review convertReviewDTOToReview(ReviewDTO reviewDTO) {
        Review review = new Review();
        review.setContent(reviewDTO.getContent());
        return review;
    }

    @Override
    public ReviewDTO convertReviewToReviewDTO(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(review.getId());
        reviewDTO.setUuid(review.getUuid());
        reviewDTO.setAdditionDate(review.getAdditionDate());
        reviewDTO.setContent(review.getContent());
        reviewDTO.setIsVisible(review.getIsVisible());
        User user = review.getUser();
        if (Objects.isNull(user)) {
            throw new UserNotReceivedException(String.format("Couldn't get the user from the database for the review" +
                    " with id = %s", review.getId()));
        }
        reviewDTO.setUserLastName(user.getLastName());
        reviewDTO.setUserFirstName(user.getFirstName());
        reviewDTO.setUserPatronymic(user.getPatronymic());
        return reviewDTO;
    }
}
