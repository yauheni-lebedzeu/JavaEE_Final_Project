package com.gmail.yauheniylebedzeu.service.util;

import com.gmail.yauheniylebedzeu.repository.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.checkPageNumber;
import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.formatDateTime;
import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.getCountOfPages;
import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.getFirstAndLastNameString;
import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.getFullName;
import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.getStartPosition;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServiceUtilTest {

    @Test
    void shouldGetCountOfPagesIfCountOfEntitiesGreaterThan0() {
        long countOfEntities = 5;
        int pageSize = 10;
        int expectedCountOfPages = (int) Math.ceil(countOfEntities / (double) pageSize);
        int countOfPages = getCountOfPages(countOfEntities, pageSize);
        assertEquals(expectedCountOfPages, countOfPages);
    }

    @Test
    void shouldGetCountOfPagesIfCountOfEntitiesIs0() {
        int countOfEntities = 0;
        int pageSize = 10;
        int expectedCountOfPages = 1;
        int actualCountOfPages = getCountOfPages(countOfEntities, pageSize);
        assertEquals(expectedCountOfPages, actualCountOfPages);
    }

    @Test
    void shouldGetStartPosition() {
        int pageNumber = 3;
        int pageSize = 10;
        int expectedStartPosition = (pageNumber - 1) * pageSize;
        int actualStartPosition = getStartPosition(3, 10);
        assertEquals(expectedStartPosition, actualStartPosition);
    }

    @Test
    void shouldCheckPageNumberIfPageNumberLessThanOrEqualToCountOfPages() {
        int pageNumber = 3;
        int countOfPages = 4;
        int checkedPageNumber = checkPageNumber(pageNumber, countOfPages);
        assertEquals(pageNumber, checkedPageNumber);
    }

    @Test
    void shouldCheckPageNumberIfPageNumberGreaterThanCountOfPages() {
        int pageNumber = 5;
        int countOfPages = 4;
        int checkedPageNumber = checkPageNumber(pageNumber, countOfPages);
        assertEquals(countOfPages, checkedPageNumber);
    }

    @Test
    void shouldGetFirstAndLastNameString() {
        User user = getTestUser();
        String expectedFirstAndLastNameString = new StringBuilder()
                .append(user.getFirstName())
                .append(" ")
                .append(user.getLastName())
                .toString();
        String actualFirstAndLastNameString = getFirstAndLastNameString(user);
        assertEquals(expectedFirstAndLastNameString, actualFirstAndLastNameString);
    }

    @Test
    void shouldGetFullName() {
        User user = getTestUser();
        String expectedFullName = new StringBuilder()
                .append(user.getLastName())
                .append(" ")
                .append(user.getFirstName())
                .append(" ")
                .append(user.getPatronymic())
                .toString();
        String actualFullName = getFullName(user);
        assertEquals(expectedFullName, actualFullName);
    }

    @Test
    void shouldFormatDateTime() {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm", Locale.ENGLISH);
        String expectedFormattedDateTime = dateTime.format(formatter);
        String actualFormattedDateTime = formatDateTime(dateTime);
        assertEquals(expectedFormattedDateTime, actualFormattedDateTime);
    }

    private User getTestUser() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Smith");
        user.setPatronymic("Ivanovich");
        return user;
    }
}