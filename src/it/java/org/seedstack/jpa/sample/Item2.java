/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.jpa.sample;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@SuppressWarnings("serial")
@Entity
public class Item2 implements Serializable {
	
	@Id
	private long ID;
	
	private String name = "";

	public long getID() {
		return ID;
	}

    public void setID(long ID) {
        this.ID = ID;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (ID ^ (ID >>> 32));
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Item2 other = (Item2) obj;
              return name.equals(other.name) && ID == other.ID;
    }

    @Override
    public String toString()
    {
        return String.format("Item2 [ID=%s, name=%s]", ID, name);
    }

}
