package com.gmail.yauheniylebedzeu.service.converter.impl;

import com.gmail.yauheniylebedzeu.repository.model.Review;
import com.gmail.yauheniylebedzeu.repository.model.User;
import com.gmail.yauheniylebedzeu.service.converter.ReviewConverter;
import com.gmail.yauheniylebedzeu.service.exception.UserNotReceivedException;
import com.gmail.yauheniylebedzeu.service.model.ReviewDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String patronymic = user.getPatronymic();
        String fullName = lastName + " " + firstName + " " + patronymic;
        reviewDTO.setFullName(fullName);
        return reviewDTO;
    }

    @Override
    public List<ReviewDTO> convertReviewListToReviewDTOList(List<Review> reviews) {
        return reviews.stream()
                .map(this::convertReviewToReviewDTO)
                .collect(Collectors.toList());
    }
}