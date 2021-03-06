/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
/**
 *
 */
package org.seedstack.jpa.internal;

import org.seedstack.seed.ErrorCode;

/**
 * @author redouane.loulou@ext.mpsa.com
 */
enum JpaErrorCode implements ErrorCode {
    DATA_SOURCE_NOT_FOUND,
    NO_PERSISTED_CLASSES_IN_UNIT,
    ACCESSING_ENTITY_MANAGER_OUTSIDE_TRANSACTION
}