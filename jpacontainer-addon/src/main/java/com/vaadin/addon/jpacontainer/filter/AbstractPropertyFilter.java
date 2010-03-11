/*
 * JPAContainer
 * Copyright (C) 2010 Oy IT Mill Ltd
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.vaadin.addon.jpacontainer.filter;

/**
 * Abstract base class for {@link PropertyFilter}s. Subclasses should implement
 * {@link #toQLString(com.vaadin.addon.jpacontainer.Filter.PropertyIdPreprocessor) }
 * .
 * 
 * @author Petter Holmström (IT Mill)
 * @since 1.0
 */
public abstract class AbstractPropertyFilter implements PropertyFilter {

	private static final long serialVersionUID = -9084459778211844263L;
	private Object propertyId;

	protected AbstractPropertyFilter(Object propertyId) {
		assert propertyId != null : "propertyId must not be null";
		this.propertyId = propertyId;
	}

	public Object getPropertyId() {
		return propertyId;
	}

	public String toQLString() {
		return toQLString(PropertyIdPreprocessor.DEFAULT);
	}

	@Override
	public boolean equals(Object obj) {
		return obj.getClass() == getClass()
				&& ((AbstractPropertyFilter) obj).propertyId.equals(propertyId);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode() + 7 * propertyId.hashCode();
	}
}
