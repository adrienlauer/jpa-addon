/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.jpa.samples.domain.tinyaggregate;

import org.seedstack.business.domain.BaseAggregateRoot;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author pierre.thirouin@ext.mpsa.com
 */
@Entity
public class TinyAggRoot extends BaseAggregateRoot<String> {

    @Id
    private String id;

    private String name;

    public TinyAggRoot() {
    }

    TinyAggRoot(String id) {
        this.id = id;
    }

    @Override
    public String getEntityId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
