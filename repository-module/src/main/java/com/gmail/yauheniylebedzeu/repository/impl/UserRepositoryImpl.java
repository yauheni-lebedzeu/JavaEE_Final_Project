package com.gmail.yauheniylebedzeu.repository.impl;

import com.gmail.yauheniylebedzeu.repository.UserRepository;
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
public class UserRepositoryImpl extends GenericRepositoryImpl<Long, User> implements UserRepository {

    @Override
    public Optional<User> findByEmail(String username) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        ParameterExpression<String> parameterExpression = criteriaBuilder.parameter(String.class);
        query.select(root)
                .where(criteriaBuilder.equal(root.get("email"), parameterExpression));
        TypedQuery<User> typedQuery = entityManager.createQuery(query);
        typedQuery.setParameter(parameterExpression, username);
        try {
            User user = typedQuery.getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
