/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.jpa.internal.orm;

import org.seedstack.business.domain.AggregateRoot;
import org.seedstack.business.domain.Repository;
import org.seedstack.business.domain.SortOption;
import org.seedstack.business.specification.FalseSpecification;
import org.seedstack.business.specification.IdentitySpecification;
import org.seedstack.business.specification.Specification;
import org.seedstack.business.specification.TrueSpecification;
import org.seedstack.business.spi.specification.SpecificationTranslator;
import org.seedstack.jpa.internal.specification.JpaTranslationContext;
import org.seedstack.jpa.spi.JpaRepositoryFactoryPriority;
import org.seedstack.shed.reflect.Classes;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.stream.Stream;

@Priority(JpaRepositoryFactoryPriority.JPA_20_PRIORITY)
public class Jpa20RepositoryFactory extends BaseJpaRepositoryFactory {
    private static final String JAVAX_PERSISTENCE_CRITERIA_CRITERIA_QUERY = "javax.persistence.criteria.CriteriaQuery";
    private static final boolean jpa20Available = isJpa20Available();

    /**
     * <p>
     * Extending {@link Jpa10RepositoryFactory.Jpa10Repository}, this implementation of business framework repository
     * takes advantage of JPA 2.0 features. Specifications are fully supported on {@link #get(Specification, Option...)}
     * and {@link #count(Specification)} methods.
     * </p>
     *
     * <p>
     * Only {@link TrueSpecification}, {@link FalseSpecification} and {@link IdentitySpecification} are supported by the
     * {@link #remove(Specification)} method.
     * </p>
     *
     * @param <A>  the aggregate root class.
     * @param <ID> the aggregate root identifier class.
     */
    public static class Jpa20Repository<A extends AggregateRoot<ID>, ID> extends Jpa10RepositoryFactory.Jpa10Repository<A, ID> {
        @Inject
        protected SpecificationTranslator<JpaTranslationContext, Predicate> specificationTranslator;

        protected Jpa20Repository(Class<A> aggregateRootClass, Class<ID> idClass) {
            super(aggregateRootClass, idClass);
        }

        @Override
        public Stream<A> get(Specification<A> specification, Option... options) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            Class<A> entityClass = getAggregateRootClass();
            CriteriaQuery<A> cq = cb.createQuery(entityClass);
            Root<A> root = cq.from(entityClass);

            cq.where(specificationTranslator.translate(specification, new JpaTranslationContext<>(cb, root)));
            if (!root.getJoins().isEmpty()) {
                // When we have joins, we need to deduplicate the results
                cq.distinct(true);
            }

            for (Option option : options) {
                if (option instanceof SortOption) {
                    applySort(cb, root, cq, (SortOption) option);
                }
            }

            return buildStream(applyOffsetAndLimit(entityManager.createQuery(cq), options));
        }

        @Override
        public long count(Specification<A> specification) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            Class<A> entityClass = getAggregateRootClass();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<A> root = cq.from(entityClass);

            cq.select(cb.count(root));
            cq.where(specificationTranslator.translate(specification, new JpaTranslationContext<>(cb, root)));
            if (!root.getJoins().isEmpty()) {
                // When we have joins, we need to deduplicate the results
                cq.distinct(true);
            }

            return entityManager.createQuery(cq).getSingleResult();
        }

        private void applySort(CriteriaBuilder cb, Root<A> root, CriteriaQuery<A> cq, SortOption sortOption) {
            for (SortOption.SortedAttribute sortedAttribute : sortOption.getSortedAttributes()) {
                switch (sortedAttribute.getDirection()) {
                    case ASCENDING:
                        cq.orderBy(cb.asc(root.get(sortedAttribute.getAttribute())));
                        break;
                    case DESCENDING:
                        cq.orderBy(cb.desc(root.get(sortedAttribute.getAttribute())));
                        break;
                    default:
                        throw new IllegalArgumentException("Unsupported sort direction " + sortedAttribute.getDirection());
                }
            }
        }
    }

    @Override
    public boolean isSupporting(EntityManager entityManager) {
        return jpa20Available;
    }

    @Override
    public <A extends AggregateRoot<ID>, ID> Repository<A, ID> doCreateRepository(Class<A> aggregateRootClass, Class<ID> identifierClass) {
        return new Jpa20Repository<>(aggregateRootClass, identifierClass);
    }

    private static boolean isJpa20Available() {
        return Classes.optional(JAVAX_PERSISTENCE_CRITERIA_CRITERIA_QUERY).isPresent();
    }
}
