/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.jpa.fixtures.business.utils;

import org.seedstack.jpa.fixtures.business.domain.simple.SampleSimpleFactory;
import org.seedstack.jpa.fixtures.business.domain.simple.SampleSimpleJpaAggregateRoot;
import org.seedstack.seed.transaction.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Date;

public class LoaderImpl implements Loader {
    @Inject
    private
    EntityManager entityManager;
    @Inject
    private
    SampleSimpleFactory sampleSimpleFactory;

    @Override
    @Transactional
    public void init(Scenario scenario) {
        switch (scenario) {
            case ONE:
                for (int i = 0; i < (10000 - 9); i++) {
                    String f2 = i % 2 == 0 ? "odd" : "even";
                    String f4 = (f2.equals("odd") ? "" + (i / 2) : "");
                    SampleSimpleJpaAggregateRoot aRoot = sampleSimpleFactory
                            .createSampleSimpleJpaAggregateRoot(i, "f1-" + (i % 2 == 0 ? i : i - 1), f2, new Date(), f4);
                    entityManager.persist(aRoot);
                }
                break;

            case TWO:

                break;

            default:
                break;
        }


    }

}
