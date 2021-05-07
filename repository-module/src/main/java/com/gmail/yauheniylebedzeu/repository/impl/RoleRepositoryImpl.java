package com.gmail.yauheniylebedzeu.repository.impl;

import com.gmail.yauheniylebedzeu.repository.RoleRepository;
import com.gmail.yauheniylebedzeu.repository.enums.RoleEnum;
import com.gmail.yauheniylebedzeu.repository.model.Role;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Repository
public class RoleRepositoryImpl extends GenericRepositoryImpl<Role, Long> implements RoleRepository {

    @Override
    public Optional<Role> findByName(RoleEnum name) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Role> query = criteriaBuilder.createQuery(Role.class);
        Root<Role> root = query.from(Role.class);
        ParameterExpression<RoleEnum> parameterExpression = criteriaBuilder.parameter(RoleEnum.class);
        query.select(root)
                .where(criteriaBuilder.equal(root.get("name"), parameterExpression));
        TypedQuery<Role> typedQuery = entityManager.createQuery(query);
        typedQuery.setParameter(parameterExpression, name);
        try {
            Role role = typedQuery.getSingleResult();
            return Optional.ofNullable(role);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
