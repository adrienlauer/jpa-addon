/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.business.jpa.samples.domain.base;

import org.seedstack.business.domain.BaseFactory;

public class SampleBaseJpaFactoryDefault extends BaseFactory<SampleBaseJpaAggregateRoot> implements SampleBaseJpaFactory{
    @Override
    public SampleBaseJpaAggregateRoot create(String id) {
        SampleBaseJpaAggregateRoot sampleBaseJpaAggregateRoot = new SampleBaseJpaAggregateRoot();
        sampleBaseJpaAggregateRoot.setEntityId(id);
        return sampleBaseJpaAggregateRoot;
    }
}
