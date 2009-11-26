/*
 * JPAContainer
 * Copyright (C) 2009 Oy IT Mill Ltd
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.vaadin.addons.jpacontainer.filter;

/**
 * Filter that includes all items for which the filtered (String)-property matches the filter value. The precent-sign (%)
 * may be used as wildcard.
 *
 * @author Petter Holmström (IT Mill)
 */
public class StringLikeFilter extends AbstractStringFilter {

    public StringLikeFilter(Object propertyId, Object value,
            boolean caseSensitive) {
        super(propertyId, value, caseSensitive);
    }

    @Override
    public String toQLString(PropertyIdPreprocessor propertyIdPreprocessor) {
        String s;
        if (isCaseSensitive()) {
            s = "(%s like :%s)";
        } else {
            s = "(upper(%s) like upper(:%s))";
        }
        return String.format(s, propertyIdPreprocessor.process(
                getPropertyId()), getQLParameterName());
    }
}
