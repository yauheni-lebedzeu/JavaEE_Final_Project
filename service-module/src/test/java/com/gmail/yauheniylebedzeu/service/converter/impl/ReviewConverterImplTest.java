package com.gmail.yauheniylebedzeu.service.converter.impl;

import com.gmail.yauheniylebedzeu.repository.model.Review;
import com.gmail.yauheniylebedzeu.repository.model.User;
import com.gmail.yauheniylebedzeu.service.exception.UserNotReceivedException;
import com.gmail.yauheniylebedzeu.service.model.ReviewDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ReviewConverterImplTest {

    private final ReviewConverterImpl reviewConverter = new ReviewConverterImpl();

    @Test
    void shouldConvertReviewDTOToReviewAndGetNotNullObject() {
        ReviewDTO reviewDTO = new ReviewDTO();
        Review review = reviewConverter.convertReviewDTOToReview(reviewDTO);
        assertNotNull(review);
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
    void shouldConvertReviewWithoutUserToReviewDTO() {
        Review review = new Review();
        Assertions.assertThrows(UserNotReceivedException.class, () -> reviewConverter.convertReviewToReviewDTO(review));
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
        LocalDate additionDate = LocalDate.now();
        review.setAdditionDate(additionDate);
        ReviewDTO reviewDTO = reviewConverter.convertReviewToReviewDTO(review);
        assertEquals(additionDate, reviewDTO.getAdditionDate());
    }

    @Test
    void shouldConvertReviewToReviewDTOAndGetRightIsVisible() {
        Review review = getTestReviewWithUser();
        Boolean isVisible = true;
        review.setIsVisible(isVisible);
        ReviewDTO reviewDTO = reviewConverter.convertReviewToReviewDTO(review);
        assertEquals(isVisible, reviewDTO.getIsVisible());
    }

    @Test
    void shouldConvertReviewToReviewDTOAndGetRightUserFullName() {
        Review review = getTestReviewWithUser();
        User user = review.getUser();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String patronymic = user.getPatronymic();
        String fullName = lastName + " " + firstName + " " + patronymic;
        ReviewDTO reviewDTO = reviewConverter.convertReviewToReviewDTO(review);
        assertEquals(fullName, reviewDTO.getFullName());
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
        review.setUser(user);
        return review;
    }
}