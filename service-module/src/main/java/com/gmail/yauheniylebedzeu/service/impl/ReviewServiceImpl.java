package com.gmail.yauheniylebedzeu.service.impl;

import com.gmail.yauheniylebedzeu.repository.ReviewRepository;
import com.gmail.yauheniylebedzeu.repository.model.Review;
import com.gmail.yauheniylebedzeu.service.ReviewService;
import com.gmail.yauheniylebedzeu.service.converter.ReviewConverter;
import com.gmail.yauheniylebedzeu.service.model.ReviewDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewConverter reviewConverter;

    @Override
    @Transactional
    public Long getCountOfReviews() {
        return reviewRepository.getCountOfEntities();
    }

    @Override
    @Transactional
    public List<ReviewDTO> getReviewList(int startPosition, int maxResult, String sortFieldName) {
        List<Review> reviews = reviewRepository.findAll(startPosition, maxResult, sortFieldName);
        return reviews.stream()
                .map(reviewConverter::convertReviewToReviewDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void removeByUuid(String uuid) {
        Optional<Review> optionalReview = reviewRepository.findByUuid(uuid);
        if (optionalReview.isPresent()) {
            Review review = optionalReview.get();
            reviewRepository.remove(review);
        }
    }

    @Override
    @Transactional
    public Optional<ReviewDTO> changeVisibilityByUuid(String uuid) {
        Optional<Review> optionalReview = reviewRepository.findByUuid(uuid);
        if (optionalReview.isPresent()) {
            Review review = optionalReview.get();
            Boolean isVisible = review.getIsVisible();
            isVisible = !isVisible;
            review.setIsVisible(isVisible);
            reviewRepository.merge(review);
            ReviewDTO reviewDTO = reviewConverter.convertReviewToReviewDTO(review);
            return Optional.ofNullable(reviewDTO);
        } else {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public List<ReviewDTO> findAllVisible(int startPosition, int maxResult, String sortFieldName) {
        List<Review> reviews = reviewRepository.findAllVisible(startPosition, maxResult, sortFieldName);
        return reviews.stream()
                .map(reviewConverter::convertReviewToReviewDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Long getCountOfVisible() {
        return reviewRepository.getCountOfVisible();
    }
}
