package com.gmail.yauheniylebedzeu.service.converter.impl;

import com.gmail.yauheniylebedzeu.repository.model.Review;
import com.gmail.yauheniylebedzeu.repository.model.User;
import com.gmail.yauheniylebedzeu.service.converter.ReviewConverter;
import com.gmail.yauheniylebedzeu.service.model.ReviewDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.gmail.yauheniylebedzeu.service.util.EntitiesServiceUtil.getUser;
import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.formatDateTime;
import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.getFullName;

@Component
public class ReviewConverterImpl implements ReviewConverter {

    @Override
    public Review convertReviewDTOToReview(ReviewDTO reviewDTO) {
        Review review = new Review();
        review.setAdditionDateTime(LocalDateTime.now());
        review.setContent(reviewDTO.getContent());
        return review;
    }

    @Override
    public ReviewDTO convertReviewToReviewDTO(Review review) {
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setId(review.getId());
        reviewDTO.setUuid(review.getUuid());
        LocalDateTime additionDateTime = review.getAdditionDateTime();
        String formattedDateTime = formatDateTime(additionDateTime);
        reviewDTO.setAdditionDateTime(formattedDateTime);
        reviewDTO.setContent(review.getContent());
        reviewDTO.setIsVisible(review.getIsVisible());
        User user = getUser(review);
        String fullName = getFullName(user);
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