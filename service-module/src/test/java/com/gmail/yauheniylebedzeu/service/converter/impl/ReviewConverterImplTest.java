package com.gmail.yauheniylebedzeu.service.converter.impl;

import com.gmail.yauheniylebedzeu.repository.model.Review;
import com.gmail.yauheniylebedzeu.repository.model.User;
import com.gmail.yauheniylebedzeu.service.converter.ReviewConverter;
import com.gmail.yauheniylebedzeu.service.model.ReviewDTO;
import com.gmail.yauheniylebedzeu.service.util.ServiceUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.getFullName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReviewConverterImplTest {

    private final ReviewConverter reviewConverter;

    ReviewConverterImplTest() {
        reviewConverter = new ReviewConverterImpl();
    }

    @Test
    void shouldConvertReviewDTOToReviewAndGetNotNullObject() {
        ReviewDTO reviewDTO = new ReviewDTO();
        Review review = reviewConverter.convertReviewDTOToReview(reviewDTO);
        assertNotNull(review);
    }

    @Test
    void shouldConvertReviewDTOToReviewAndGetRightAdditionDateTime() {
        ReviewDTO reviewDTO = new ReviewDTO();
        LocalDateTime dateTime = LocalDateTime.now();
        Review review = reviewConverter.convertReviewDTOToReview(reviewDTO);
        assertEquals(dateTime, review.getAdditionDateTime());
    }

    @Test
    void shouldConvertReviewDTOToReviewAndGetRightContent() {
        ReviewDTO reviewDTO = new ReviewDTO();
        String content = "test content";
        reviewDTO.setContent(content);
        Review review = reviewConverter.convertReviewDTOToReview(reviewDTO);
        assertEquals(content, review.getContent());
    }

    @Test
    void shouldConvertReviewToReviewDTOAndGetNotNullObject() {
        Review review = getTestReviewWithUser();
        ReviewDTO reviewDTO = reviewConverter.convertReviewToReviewDTO(review);
        assertNotNull(reviewDTO);
    }

    @Test
    void shouldConvertReviewToReviewDTOAndGetRightId() {
        Review review = getTestReviewWithUser();
        Long id = 1L;
        review.setId(id);
        ReviewDTO reviewDTO = reviewConverter.convertReviewToReviewDTO(review);
        assertEquals(id, reviewDTO.getId());
    }

    @Test
    void shouldConvertReviewToReviewDTOAndGetRightUuid() {
        Review review = getTestReviewWithUser();
        String uuid = "6d4883c7-aa9c-11eb-a3ca-0242ac130002";
        review.setUuid(uuid);
        ReviewDTO reviewDTO = reviewConverter.convertReviewToReviewDTO(review);
        assertEquals(uuid, reviewDTO.getUuid());
    }

    @Test
    void shouldConvertReviewToReviewDTOAndGetRightContent() {
        Review review = getTestReviewWithUser();
        String content = "test content";
        review.setContent(content);
        ReviewDTO reviewDTO = reviewConverter.convertReviewToReviewDTO(review);
        assertEquals(content, reviewDTO.getContent());
    }

    @Test
    void shouldConvertReviewToReviewDTOAndGetRightAdditionDate() {
        Review review = getTestReviewWithUser();
        String formattedDateTime = ServiceUtil.formatDateTime(review.getAdditionDateTime());
        ReviewDTO reviewDTO = reviewConverter.convertReviewToReviewDTO(review);
        assertEquals(formattedDateTime, reviewDTO.getAdditionDateTime());
    }

    @Test
    void shouldConvertReviewToReviewDTOAndGetRightIsVisible() {
        Review review = getTestReviewWithUser();
        review.setIsVisible(true);
        ReviewDTO reviewDTO = reviewConverter.convertReviewToReviewDTO(review);
        assertEquals(true, reviewDTO.getIsVisible());
    }

    @Test
    void shouldConvertReviewToReviewDTOAndGetRightUserFullName() {
        Review review = getTestReviewWithUser();
        User user = review.getUser();
        String fullName = getFullName(user);
        ReviewDTO reviewDTO = reviewConverter.convertReviewToReviewDTO(review);
        assertEquals(fullName, reviewDTO.getFullName());
    }

    @Test
    void shouldConvertReviewToReviewDTOAndGetRightIsAuthorDeleted() {
        Review review = getTestReviewWithUser();
        User user = review.getUser();
        user.setIsDeleted(true);
        ReviewDTO reviewDTO = reviewConverter.convertReviewToReviewDTO(review);
        assertEquals(true, reviewDTO.getIsAuthorDeleted());
    }

    @Test
    void shouldConvertEmptyReviewListToReviewDTOList() {
        List<Review> reviews = Collections.emptyList();
        List<ReviewDTO> reviewDTOs = reviewConverter.convertReviewListToReviewDTOList(reviews);
        assertTrue(reviewDTOs.isEmpty());
    }

    @Test
    void shouldConvertNotEmptyReviewListToReviewDTOList() {
        Review review = getTestReviewWithUser();
        List<Review> reviews = Collections.singletonList(review);
        List<ReviewDTO> reviewDTOs = reviewConverter.convertReviewListToReviewDTOList(reviews);
        assertEquals(reviews.size(), reviewDTOs.size());
    }

    private Review getTestReviewWithUser() {
        User user = new User();
        String firstName = "test first name";
        user.setFirstName(firstName);
        String lastName = "test last name";
        user.setLastName(lastName);
        String patronymic = "test patronymic";
        user.setPatronymic(patronymic);
        Review review = new Review();
        review.setAdditionDateTime(LocalDateTime.now());
        review.setUser(user);
        return review;
    }
}