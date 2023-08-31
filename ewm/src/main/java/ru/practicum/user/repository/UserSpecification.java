package ru.practicum.user.repository;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.user.criteria.UserCriteria;
import ru.practicum.user.model.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class UserSpecification implements Specification<User> {
    private final UserCriteria criteria;
    private final List<Predicate> predicateList;

    public UserSpecification(UserCriteria criteria) {
        this.criteria = criteria;
        this.predicateList = new ArrayList<>();
    }

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (criteria.getIds() != null) {
            Predicate category = root.get("id").in(criteria.getIds());
            this.predicateList.add(category);
        }
        return query.where(criteriaBuilder.and(predicateList.toArray(new Predicate[0])))
                .getRestriction();
    }
}