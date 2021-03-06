/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.jpa.automode;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.seedstack.business.domain.Factory;
import org.seedstack.business.domain.Repository;
import org.seedstack.jpa.samples.domain.tinyaggregate.TinyAggRoot;
import org.seedstack.seed.it.SeedITRunner;
import org.seedstack.jpa.Jpa;
import org.seedstack.jpa.JpaUnit;
import org.seedstack.seed.transaction.Transactional;

import javax.inject.Inject;

/**
 * @author pierre.thirouin@ext.mpsa.com
 */
@Transactional
@JpaUnit("seed-biz-support")
@RunWith(SeedITRunner.class)
public class AutoRepositoriesIT {
    @Inject @Jpa
    Repository<TinyAggRoot, String> repository;

    @Inject
    Factory<TinyAggRoot> factory;

    @Test
    public void retrieve_aggregate_from_repository () {
        repository.save(factory.create("hello"));
        TinyAggRoot tinyAggRoot = repository.load("hello");
        Assertions.assertThat(tinyAggRoot).isNotNull();
    }
}
