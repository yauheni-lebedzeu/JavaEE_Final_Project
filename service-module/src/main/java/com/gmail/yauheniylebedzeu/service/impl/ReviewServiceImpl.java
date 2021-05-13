package com.gmail.yauheniylebedzeu.service.impl;

import com.gmail.yauheniylebedzeu.repository.ReviewRepository;
import com.gmail.yauheniylebedzeu.repository.model.Review;
import com.gmail.yauheniylebedzeu.service.ReviewService;
import com.gmail.yauheniylebedzeu.service.converter.ReviewConverter;
import com.gmail.yauheniylebedzeu.service.exception.ReviewNotFoundException;
import com.gmail.yauheniylebedzeu.service.model.PageDTO;
import com.gmail.yauheniylebedzeu.service.model.ReviewDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.getCountOfPages;
import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.getStartPosition;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewConverter reviewConverter;

    @Override
    @Transactional
    public PageDTO<ReviewDTO> getReviewPage(int pageNumber, int pageSize, String sortParameter) {
        PageDTO<ReviewDTO> page = new PageDTO<>();
        Long countOfReviews = reviewRepository.getCountOfEntities();
        int countOfPages = getCountOfPages(countOfReviews, pageSize);
        page.setCountOfPages(countOfPages);
        if (pageNumber > countOfPages && countOfPages != 0) {
            pageNumber = countOfPages;
        }
        page.setPageNumber(pageNumber);
        int startPosition = getStartPosition(pageNumber, pageSize);
        List<Review> reviews = reviewRepository.findEntitiesWithLimits(startPosition, pageSize, sortParameter);
        List<ReviewDTO> reviewsDTOs = reviews.stream()
                .map(reviewConverter::convertReviewToReviewDTO)
                .collect(Collectors.toList());
        List<ReviewDTO> reviewsOnPage = page.getObjects();
        reviewsOnPage.addAll(reviewsDTOs);
        return page;
    }

    @Override
    @Transactional
    public PageDTO<ReviewDTO> getVisibleReviewsPage(int pageNumber, int pageSize, String sortParameter) {
        PageDTO<ReviewDTO> page = new PageDTO<>();
        Long countOfReviews = reviewRepository.getCountOfVisible();
        int countOfPages = getCountOfPages(countOfReviews, pageSize);
        page.setCountOfPages(countOfPages);
        if (pageNumber > countOfPages && countOfPages != 0) {
            pageNumber = countOfPages;
        }
        page.setPageNumber(pageNumber);
        int startPosition = getStartPosition(pageNumber, pageSize);
        List<Review> reviews = reviewRepository.findVisibleReviews(startPosition, pageSize, sortParameter);
        List<ReviewDTO> reviewsDTOs = reviews.stream()
                .map(reviewConverter::convertReviewToReviewDTO)
                .collect(Collectors.toList());
        List<ReviewDTO> reviewsOnPage = page.getObjects();
        reviewsOnPage.addAll(reviewsDTOs);
        return page;
    }

    @Override
    @Transactional
    public void removeByUuid(String uuid) {
        Optional<Review> optionalReview = reviewRepository.findByUuid(uuid);
        if (optionalReview.isPresent()) {
            Review review = optionalReview.get();
            reviewRepository.remove(review);
        } else {
            throw new ReviewNotFoundException(String.format("Review with uuid %s was not found in the database", uuid));
        }
    }

    @Override
    @Transactional
    public List<ReviewDTO> changeVisibilityByUuids(List<String> checkedUuids, List<String> previouslyCheckedUuids) {
        List<ReviewDTO> reviewDTOs = new ArrayList<>();
        if (!Objects.isNull(checkedUuids) && !Objects.isNull(previouslyCheckedUuids)) {
            for (String checkedUuid : checkedUuids) {
                if (!previouslyCheckedUuids.contains(checkedUuid)) {
                    ReviewDTO reviewDTO = changeVisibilityByUuid(checkedUuid);
                    reviewDTOs.add(reviewDTO);
                }
            }
            for (String previouslyCheckedUuid : previouslyCheckedUuids) {
                if (!checkedUuids.contains(previouslyCheckedUuid)) {
                    ReviewDTO reviewDTO = changeVisibilityByUuid(previouslyCheckedUuid);
                    reviewDTOs.add(reviewDTO);
                }
            }
        } else if (Objects.isNull(checkedUuids) && !Objects.isNull(previouslyCheckedUuids)) {
            for (String previouslyCheckedUuid : previouslyCheckedUuids) {
                ReviewDTO reviewDTO = changeVisibilityByUuid(previouslyCheckedUuid);
                reviewDTOs.add(reviewDTO);
            }
        } else if (!Objects.isNull(checkedUuids)) {
            for (String checkedUuid : checkedUuids) {
                ReviewDTO reviewDTO = changeVisibilityByUuid(checkedUuid);
                reviewDTOs.add(reviewDTO);
            }
        }
        return reviewDTOs;
    }

    private ReviewDTO changeVisibilityByUuid(String uuid) {
        Optional<Review> optionalReview = reviewRepository.findByUuid(uuid);
        if (optionalReview.isPresent()) {
            Review review = optionalReview.get();
            boolean isVisible = review.getIsVisible();
            boolean newStatus = !isVisible;
            review.setIsVisible(newStatus);
            reviewRepository.merge(review);
            return reviewConverter.convertReviewToReviewDTO(review);
        } else {
            throw new ReviewNotFoundException(String.format("Review with uuid %s was not found in the database", uuid));
        }
    }
}
