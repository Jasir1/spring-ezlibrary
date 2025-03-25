package com.mhdjasir.ezLibrary.domain.book;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class BookSpecification {
    public static Specification<Book> filterByAttributes(
            String title,
            Status status,
            Boolean delete,
            Long category,
            String condition,
            String language,
            Double priceGreaterThan,
            Double priceLessThan) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (title != null && !title.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }

            if (category != null) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), category));
            }

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if (delete != null) {
                predicates.add(criteriaBuilder.equal(root.get("delete"), delete));
            }

            if (condition != null && !condition.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("condition"), condition));
            }

            if (language != null && !language.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("language"), language));
            }

            if (priceGreaterThan != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), priceGreaterThan));
            }

            if (priceLessThan != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), priceLessThan));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
