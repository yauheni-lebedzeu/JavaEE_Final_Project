package com.gmail.yauheniylebedzeu.service.util;

import com.gmail.yauheniylebedzeu.repository.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ServiceUtil {

    public static int getCountOfPages(long countOfEntities, int pageSize) {
        if (countOfEntities == 0) {
            return 1;
        } else {
            return (int) Math.ceil(countOfEntities / (double) pageSize);
        }
    }

    public static int getStartPosition(int pageNumber, int pageSize) {
        return (pageNumber - 1) * pageSize;
    }

    public static int checkPageNumber(int pageNumber, int countOfPages) {
        if (pageNumber > countOfPages) {
            pageNumber = countOfPages;
        }
        return pageNumber;
    }

    public static String getFirstAndLastNameString(User user) {
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        return new StringBuilder()
                .append(firstName)
                .append(" ")
                .append(lastName)
                .toString();
    }

    public static String getFullName(User user) {
        String lastName = user.getLastName();
        String firstName = user.getFirstName();
        String patronymic = user.getPatronymic();
        return new StringBuilder()
                .append(lastName)
                .append(" ")
                .append(firstName)
                .append(" ")
                .append(patronymic)
                .toString();
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm", Locale.ENGLISH);
        return dateTime.format(formatter);
    }
}