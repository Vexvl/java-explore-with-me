package ru.practicum.compilation.repository;

import org.springframework.data.jpa.domain.Specification;
import ru.practicum.compilation.model.Compilation;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class CompilationSpecification implements Specification<Compilation> {
    private final Boolean pinned;

    public CompilationSpecification(Boolean pinned) {
        this.pinned = pinned;
    }

    @Override
    public Predicate toPredicate(Root<Compilation> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicateList = new ArrayList<>();

        if (pinned != null) {
            Predicate pinnedPredicate = criteriaBuilder.equal(root.get("pinned"), pinned);
            predicateList.add(pinnedPredicate);
        }

        return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
    }
}
