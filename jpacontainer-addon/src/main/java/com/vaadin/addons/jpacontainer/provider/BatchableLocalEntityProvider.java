/*
 * JPAContainer
 * Copyright (C) 200 Oy IT Mill Ltd
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
package com.vaadin.addons.jpacontainer.provider;

import com.vaadin.addons.jpacontainer.BatchableEntityProvider;
import com.vaadin.addons.jpacontainer.BatchableEntityProvider.BatchUpdateCallback;
import com.vaadin.addons.jpacontainer.EntityProvider;
import com.vaadin.addons.jpacontainer.EntityProviderChangeEvent;
import java.util.Collection;
import java.util.Collections;
import javax.persistence.EntityManager;

/**
 * A very simple implementation of {@link BatchableEntityProvider} that simply
 * passes itself to the {@link BatchUpdateCallback}. No data consistency checks
 * are performed.
 * 
 * @author Petter Holmström (IT Mill)
 * @since 1.0
 */
public class BatchableLocalEntityProvider<T> extends
		MutableLocalEntityProvider<T> implements BatchableEntityProvider<T> {

	private static final long serialVersionUID = 9174163487778140520L;

	/**
	 * Creates a new <code>BatchableLocalEntityProvider</code>. The entity
	 * manager must be set using
	 * {@link #setEntityManager(javax.persistence.EntityManager) }.
	 * 
	 * @param entityClass
	 *            the entity class (must not be null).
	 */
	public BatchableLocalEntityProvider(Class<T> entityClass) {
		super(entityClass);
	}

	/**
	 * Creates a new <code>BatchableLocalEntityProvider</code>.
	 * 
	 * @param entityClass
	 *            the entity class (must not be null).
	 * @param entityManager
	 *            the entity manager to use (must not be null).
	 */
	public BatchableLocalEntityProvider(Class<T> entityClass,
			EntityManager entityManager) {
		super(entityClass, entityManager);
	}

	public void batchUpdate(final BatchUpdateCallback<T> callback)
			throws UnsupportedOperationException {
		assert callback != null : "callback must not be null";
		setFireEntityProviderChangeEvents(false);
		try {
			runInTransaction(new Runnable() {

				public void run() {
					callback.batchUpdate(BatchableLocalEntityProvider.this);
				}
			});
		} finally {
			setFireEntityProviderChangeEvents(true);
		}
		fireEntityProviderChangeEvent(new BatchUpdatePerformedEvent());
	}

	protected class BatchUpdatePerformedEvent implements
			EntityProviderChangeEvent<T> {

		private static final long serialVersionUID = -4080306860560561433L;

		public Collection<T> getAffectedEntities() {
			return Collections.emptyList();
		}

		public EntityProvider<T> getEntityProvider() {
			return (EntityProvider<T>) BatchableLocalEntityProvider.this;
		}
	};
}
