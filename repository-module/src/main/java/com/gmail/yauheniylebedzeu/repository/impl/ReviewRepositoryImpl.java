package com.gmail.yauheniylebedzeu.repository.impl;

import com.gmail.yauheniylebedzeu.repository.ReviewRepository;
import com.gmail.yauheniylebedzeu.repository.model.Review;
import com.gmail.yauheniylebedzeu.repository.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Repository
public class ReviewRepositoryImpl extends GenericRepositoryImpl<Long, Review> implements ReviewRepository {

    @Override
    public Optional<Review> findByUuid(String uuid) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Review> query = criteriaBuilder.createQuery(Review.class);
        Root<Review> root = query.from(Review.class);
        ParameterExpression<String> parameterExpression = criteriaBuilder.parameter(String.class);
        query.select(root)
                .where(criteriaBuilder.equal(root.get("uuid"), parameterExpression));
        TypedQuery<Review> typedQuery = entityManager.createQuery(query);
        typedQuery.setParameter(parameterExpression, uuid);
        try {
            Review review = typedQuery.getSingleResult();
            return Optional.ofNullable(review);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
