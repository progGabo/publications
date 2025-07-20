package org.publications.service.specification;

import jakarta.persistence.criteria.*;
import lombok.experimental.UtilityClass;
import org.publications.domain.Author;
import org.publications.domain.Publication;
import org.publications.service.dto.publications.PublicationFilterDTO;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

@UtilityClass
public class PublicationSpecification {

    private static void fetchAuthorsIfNeeded(Root<Publication> root, CriteriaQuery<?> query) {
        if (Publication.class.equals(query.getResultType())) {
            root.fetch("authors", JoinType.LEFT);
            query.distinct(true);
        }
    }

    public static Specification<Publication> hasPublisherName(String publisherName) {
        return (root, query, cb) -> {
            fetchAuthorsIfNeeded(root, query);
            Join<?, ?> joinPub = root.join("publisher", JoinType.LEFT);
            return cb.like(cb.lower(joinPub.get("name")), "%" + publisherName.toLowerCase() + "%");
        };
    }

    public static Specification<Publication> hasCategoryName(String categoryName) {
        return (root, query, cb) -> {
            fetchAuthorsIfNeeded(root, query);
            Join<?, ?> joinCat = root.join("category", JoinType.LEFT);
            return cb.equal(cb.lower(joinCat.get("name")), categoryName.toLowerCase());
        };
    }

    public static Specification<Publication> hasTypeName(String typeName) {
        return (root, query, cb) -> {
            fetchAuthorsIfNeeded(root, query);
            Join<?, ?> joinType = root.join("type", JoinType.LEFT);
            return cb.equal(cb.lower(joinType.get("name")), typeName.toLowerCase());
        };
    }

    public static Specification<Publication> hasLanguageName(String languageName) {
        return (root, query, cb) -> {
            fetchAuthorsIfNeeded(root, query);
            Join<?, ?> joinLang = root.join("language", JoinType.LEFT);
            return cb.equal(cb.lower(joinLang.get("name")), languageName.toLowerCase());
        };
    }

    public static Specification<Publication> titleOrAuthorContains(String term) {
        return (root, query, cb) -> {
            fetchAuthorsIfNeeded(root, query);

            String pattern = "%" + term.toLowerCase() + "%";
            Predicate titleLike = cb.like(cb.lower(root.get("title")), pattern);

            Predicate isbnLike = cb.like(cb.lower(root.get("isbnIssn")), pattern);

            Join<Publication, Author> authorJoin = root.join("authors", JoinType.LEFT);
            Predicate firstNameLike = cb.like(cb.lower(authorJoin.get("firstName")), pattern);
            Predicate lastNameLike  = cb.like(cb.lower(authorJoin.get("lastName")),  pattern);
            Predicate authorLike    = cb.or(firstNameLike, lastNameLike);

            return cb.or(titleLike, isbnLike, authorLike);
        };
    }

    public static Specification<Publication> byFilter(PublicationFilterDTO filter) {
        Specification<Publication> spec = Specification.where(null);

        if (StringUtils.hasText(filter.getPublisher())) {
            spec = spec.and(hasPublisherName(filter.getPublisher()));
        }
        if (StringUtils.hasText(filter.getCategory())) {
            spec = spec.and(hasCategoryName(filter.getCategory()));
        }
        if (StringUtils.hasText(filter.getType())) {
            spec = spec.and(hasTypeName(filter.getType()));
        }
        if (StringUtils.hasText(filter.getLanguage())) {
            spec = spec.and(hasLanguageName(filter.getLanguage()));
        }
        if (StringUtils.hasText(filter.getSearchTerm())) {
            spec = spec.and(titleOrAuthorContains(filter.getSearchTerm()));
        }
        if (filter.getAuthorNames() != null) {
            for (String name : filter.getAuthorNames()) {
                spec = spec.and(titleOrAuthorContains(name));
            }
        }
        return spec;
    }
}