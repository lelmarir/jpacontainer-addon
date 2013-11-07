/**
 * Copyright 2009-2013 Oy Vaadin Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vaadin.addon.jpacontainer.provider;

import javax.persistence.EntityManager;

import com.vaadin.addon.jpacontainer.BatchableEntityProvider;

/**
 * A very simple implementation of {@link BatchableEntityProvider} with caching
 * support that simply passes itself to the {@link BatchUpdateCallback}. No data
 * consistency checks are performed.
 * 
 * @see CachingMutableLocalEntityProvider
 * @see BatchableLocalEntityProvider
 * 
 * @author Petter Holmström (Vaadin Ltd)
 * @since 1.0
 */
public class CachingBatchableLocalEntityProvider<T> extends
        CachingMutableLocalEntityProvider<T> implements
        BatchableEntityProvider<T> {

    private static final long serialVersionUID = 9174163487778140520L;

    /**
     * Creates a new <code>CachingBatchableLocalEntityProvider</code>. The
     * entity manager must be set using
     * {@link #setEntityManager(javax.persistence.EntityManager) }.
     * 
     * @param entityClass
     *            the entity class (must not be null).
     */
    public CachingBatchableLocalEntityProvider(Class<T> entityClass) {
        super(entityClass);
    }

    /**
     * Creates a new <code>CachingBatchableLocalEntityProvider</code>.
     * 
     * @param entityClass
     *            the entity class (must not be null).
     * @param entityManager
     *            the entity manager to use (must not be null).
     */
    public CachingBatchableLocalEntityProvider(Class<T> entityClass,
            EntityManager entityManager) {
        super(entityClass, entityManager);
    }

    @Override
    public void batchUpdate(final BatchUpdateCallback<T> callback)
            throws UnsupportedOperationException {
        assert callback != null : "callback must not be null";
        setFireEntityProviderChangeEvents(false);
        try {
            runInTransaction(new Runnable() {

                @Override
                public void run() {
                    callback.batchUpdate(CachingBatchableLocalEntityProvider.this);
                }
            });
        } finally {
            setFireEntityProviderChangeEvents(true);
        }
        fireEntityProviderChangeEvent(new BatchUpdatePerformedEvent<T>(this));
    }
}
