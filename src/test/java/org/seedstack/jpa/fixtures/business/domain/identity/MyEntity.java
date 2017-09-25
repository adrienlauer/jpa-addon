/*
 * Copyright © 2013-2017, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
/**
 *
 */
package org.seedstack.jpa.fixtures.business.domain.identity;

import java.util.UUID;
import org.seedstack.business.domain.BaseEntity;
import org.seedstack.business.domain.Identity;
import org.seedstack.business.domain.UuidGenerator;

public class MyEntity extends BaseEntity<UUID> {

  @Identity(generator = UuidGenerator.class)
  private UUID id;

  @Override
  public UUID getId() {
    return id;
  }

}
