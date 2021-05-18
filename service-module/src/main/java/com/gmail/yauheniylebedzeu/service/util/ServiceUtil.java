package com.gmail.yauheniylebedzeu.service.util;

public class ServiceUtil {

    public static int getCountOfPages(long countOfEntities, int pageSize) {
        if (countOfEntities == 0) {
            return 1;
        } else {
            return  (int) Math.ceil(countOfEntities / (double) pageSize);
        }
    }

    public static int getStartPosition(int pageNumber, int pageSize) {
        return (pageNumber - 1) * pageSize;
    }
}
