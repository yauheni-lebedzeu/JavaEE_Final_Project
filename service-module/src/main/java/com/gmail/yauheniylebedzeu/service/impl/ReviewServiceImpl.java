package com.gmail.yauheniylebedzeu.service.impl;

import com.gmail.yauheniylebedzeu.repository.ReviewRepository;
import com.gmail.yauheniylebedzeu.repository.model.Review;
import com.gmail.yauheniylebedzeu.repository.model.User;
import com.gmail.yauheniylebedzeu.service.ReviewService;
import com.gmail.yauheniylebedzeu.service.UserService;
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

import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.checkPageNumber;
import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.getCountOfPages;
import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.getStartPosition;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewConverter reviewConverter;
    private final UserService userService;

    @Override
    @Transactional
    public PageDTO<ReviewDTO> getReviewPage(int pageNumber, int pageSize, String sortParameter) {
        PageDTO<ReviewDTO> page = new PageDTO<>();
        Long countOfReviews = reviewRepository.getCountOfEntities();
        int countOfPages = getCountOfPages(countOfReviews, pageSize);
        page.setCountOfPages(countOfPages);
        pageNumber = checkPageNumber(pageNumber, countOfPages);
        page.setPageNumber(pageNumber);
        int startPosition = getStartPosition(pageNumber, pageSize);
        List<Review> reviews = reviewRepository.findEntitiesWithLimits(startPosition, pageSize, sortParameter);
        List<ReviewDTO> reviewsDTOs = reviewConverter.convertReviewListToReviewDTOList(reviews);
        page.addObjects(reviewsDTOs);
        return page;
    }

    @Override
    @Transactional
    public PageDTO<ReviewDTO> getVisibleReviewsPage(int pageNumber, int pageSize, String sortParameter) {
        PageDTO<ReviewDTO> page = new PageDTO<>();
        Long countOfReviews = reviewRepository.getCountOfVisible();
        int countOfPages = getCountOfPages(countOfReviews, pageSize);
        page.setCountOfPages(countOfPages);
        pageNumber = checkPageNumber(pageNumber, countOfPages);
        page.setPageNumber(pageNumber);
        int startPosition = getStartPosition(pageNumber, pageSize);
        List<Review> reviews = reviewRepository.findVisibleReviews(startPosition, pageSize, sortParameter);
        List<ReviewDTO> reviewsDTOs = reviewConverter.convertReviewListToReviewDTOList(reviews);
        List<ReviewDTO> reviewsOnPage = page.getObjects();
        reviewsOnPage.addAll(reviewsDTOs);
        return page;
    }

    @Override
    @Transactional
    public void removeByUuid(String reviewUuid) {
        Review review = getSafeReview(reviewUuid);
        reviewRepository.remove(review);
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

    @Override
    @Transactional
    public Review getSafeReview(String reviewUuid) {
        Optional<Review> optionalReview = reviewRepository.findByUuid(reviewUuid);
        if (optionalReview.isPresent()) {
            return optionalReview.get();
        } else {
            throw new ReviewNotFoundException(String.format("Review with uuid %s was not found", reviewUuid));
        }
    }

    @Override
    @Transactional
    public ReviewDTO addReview(String userUuid, ReviewDTO reviewDTO) {
        Review review = reviewConverter.convertReviewDTOToReview(reviewDTO);
        User user = userService.getSafeUser(userUuid);
        review.setUser(user);
        reviewRepository.persist(review);
        return reviewConverter.convertReviewToReviewDTO(review);
    }

    private ReviewDTO changeVisibilityByUuid(String reviewUuid) {
        Review review = getSafeReview(reviewUuid);
        boolean isVisible = review.getIsVisible();
        boolean newStatus = !isVisible;
        review.setIsVisible(newStatus);
        reviewRepository.merge(review);
        return reviewConverter.convertReviewToReviewDTO(review);
    }
}