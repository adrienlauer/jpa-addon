/*
 * Copyright © 2013-2017, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.jpa;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import org.seedstack.business.domain.AggregateExistsException;
import org.seedstack.business.domain.AggregateNotFoundException;
import org.seedstack.business.domain.AggregateRoot;
import org.seedstack.business.domain.BaseRepository;
import org.seedstack.business.domain.Repository;
import org.seedstack.business.specification.Specification;
import org.seedstack.jpa.internal.JpaErrorCode;
import org.seedstack.jpa.spi.JpaRepositoryFactory;
import org.seedstack.seed.SeedException;

/**
 * This class can serve as a base class for the JPA repositories. It provides methods for common
 * CRUD operations as well as access to the entity manager through the {@link #getEntityManager()}
 * protected method.
 *
 * @param <AggregateRootT> Aggregate root class
 * @param <IdT>            Identifier class
 */
public abstract class BaseJpaRepository<AggregateRootT extends AggregateRoot<IdT>, IdT> extends
    BaseRepository<AggregateRootT, IdT> {

  @Inject
  private EntityManager entityManager;
  @Inject
  private Set<JpaRepositoryFactory> jpaRepositoryFactories;

  /**
   * Default constructor.
   */
  public BaseJpaRepository() {
  }

  /**
   * This protected constructor is intended to be used by JPA repositories that already know their
   * aggregate root and key classes. It is notably used internally by {@link
   * org.seedstack.jpa.internal.DefaultJpaRepository} for providing a default JPA repository for all
   * aggregates.
   *
   * @param aggregateRootClass the aggregate root class.
   * @param idClass            the id class.
   */
  protected BaseJpaRepository(Class<AggregateRootT> aggregateRootClass, Class<IdT> idClass) {
    super(aggregateRootClass, idClass);
  }

  /**
   * Provides access to the entity manager for implementing custom data access methods.
   *
   * @return the entity manager.
   */
  protected EntityManager getEntityManager() {
    return entityManager;
  }

  @Override
  public void add(AggregateRootT aggregate) throws AggregateExistsException {
    resolveImplementation().add(aggregate);
  }

  @Override
  public Stream<AggregateRootT> get(Specification<AggregateRootT> specification,
      Option... options) {
    return resolveImplementation().get(specification, options);
  }

  @Override
  public Optional<AggregateRootT> get(IdT id) {
    return resolveImplementation().get(id);
  }

  @Override
  public boolean contains(Specification<AggregateRootT> specification) {
    return resolveImplementation().contains(specification);
  }

  @Override
  public boolean contains(IdT id) {
    return resolveImplementation().contains(id);
  }

  @Override
  public boolean contains(AggregateRootT aggregate) {
    return resolveImplementation().contains(aggregate);
  }

  @Override
  public long count(Specification<AggregateRootT> specification) {
    return resolveImplementation().count(specification);
  }

  @Override
  public long size() {
    return resolveImplementation().size();
  }

  @Override
  public boolean isEmpty() {
    return resolveImplementation().isEmpty();
  }

  @Override
  public long remove(Specification<AggregateRootT> specification) {
    return resolveImplementation().remove(specification);
  }

  @Override
  public void remove(IdT id) throws AggregateNotFoundException {
    resolveImplementation().remove(id);
  }

  @Override
  public void remove(AggregateRootT aggregate) throws AggregateNotFoundException {
    resolveImplementation().remove(aggregate);
  }

  @Override
  public void update(AggregateRootT aggregate) throws AggregateNotFoundException {
    resolveImplementation().update(aggregate);
  }

  @Override
  public void clear() {
    resolveImplementation().clear();
  }

  @SuppressWarnings("unchecked")
  private Repository<AggregateRootT, IdT> resolveImplementation() {
    for (JpaRepositoryFactory jpaRepositoryFactory : jpaRepositoryFactories) {
      if (jpaRepositoryFactory.isSupporting(entityManager)) {
        return jpaRepositoryFactory.createRepository(getAggregateRootClass(), getIdentifierClass());
      }
    }
    throw SeedException
        .createNew(JpaErrorCode.UNABLE_TO_FIND_A_SUITABLE_JPA_REPOSITORY_IMPLEMENTATION);
  }
}
