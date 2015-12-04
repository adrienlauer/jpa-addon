/**
 * Copyright (c) 2013-2015, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.jpa;

import org.seedstack.business.finder.Range;
import org.seedstack.business.finder.RangeFinder;
import org.seedstack.business.finder.Result;

import javax.persistence.Query;
import java.util.List;
import java.util.Map;

/**
 * A base finder providing a simple pagination mechanism.
 *
 * @param <Item> the dto to paginate
 * @author epo.jemba@ext.mpsa.com
 * @deprecated This range finder will be replace by {@code org.seedstack.business.finder.BaseRangeFinder}
 */
@Deprecated
public abstract class BaseJpaRangeFinder<Item> implements RangeFinder<Item, Map<String, Object>> {

    @Override
    public Result<Item> find(Range range, Map<String, Object> criteria) {
        // Count
        long resultSize = computeFullRequestSize(criteria);

        // list
        List<Item> list = computeResultList(range, criteria);

        return new Result<Item>(list, range.getOffset(), resultSize);
    }

    /**
     * Returns a sub list of items corresponding to the required range and criteria.
     *
     * @param range    the range
     * @param criteria the criteria
     * @return the sub list of item
     */
    protected abstract List<Item> computeResultList(Range range, Map<String, Object> criteria);

    /**
     * Returns the total number of items available.
     *
     * @param criteria the request criteria
     * @return the total number of item
     */
    protected abstract long computeFullRequestSize(Map<String, Object> criteria);

    protected void updateQuery(Query query, Map<String, Object> criteria) {
        for (String key : criteria.keySet()) {
            query.setParameter(key, criteria.get(key));
        }
    }

}
