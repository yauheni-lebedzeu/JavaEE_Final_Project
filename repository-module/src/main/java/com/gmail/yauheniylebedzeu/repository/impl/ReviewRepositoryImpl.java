package com.gmail.yauheniylebedzeu.repository.impl;

import com.gmail.yauheniylebedzeu.repository.ReviewRepository;
import com.gmail.yauheniylebedzeu.repository.model.Review;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class ReviewRepositoryImpl extends GenericRepositoryImpl<Review> implements ReviewRepository {

    @Override
    @SuppressWarnings("unchecked")
    public List<Review> findVisibleReviews(int startPosition, int maxResult, String sortParameter) {
        String queryString = "select c from Review as c where c.isVisible = true order by c." + sortParameter;
        Query query = entityManager.createQuery(queryString);
        query.setFirstResult(startPosition);
        query.setMaxResults(maxResult);
        return query.getResultList();
    }

    @Override
    public Long getCountOfVisible() {
        String queryString = "select count(c.id) from Review as c where c.isVisible = true ";
        Query query = entityManager.createQuery(queryString);
        return (Long) query.getSingleResult();

    }
}
