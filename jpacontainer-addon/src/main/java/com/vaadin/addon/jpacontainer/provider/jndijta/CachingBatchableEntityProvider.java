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
package com.vaadin.addon.jpacontainer.provider.jndijta;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import com.vaadin.addon.jpacontainer.provider.CachingBatchableLocalEntityProvider;

/**
 * An entity provider implementation that uses JTA transactions and JPA context
 * provided by the application server. To provide a generic non EJB provider
 * this class gets references to both {@link UserTransaction} and
 * {@link EntityManager} via JNDI lookup.
 * 
 * @param <T>
 */
public class CachingBatchableEntityProvider<T> extends
        CachingBatchableLocalEntityProvider<T> implements JndiJtaProvider<T> {

    private JndiAddresses jndiAddresses;

    public CachingBatchableEntityProvider(Class<T> entityClass) {
        super(entityClass);
        setTransactionsHandledByProvider(false);
    }

    public CachingBatchableEntityProvider(Class<T> entityClass,
            JndiAddresses addresses) {
        this(entityClass);
        setJndiAddresses(addresses);
    }

    @Override
    public boolean isEntitiesDetached() {
        return false;
    }

    @Override
    protected void runInTransaction(Runnable operation) {
        Util.runInJTATransaction(getJndiAddresses(), operation);
    }

    @Override
    public EntityManager getEntityManager() {
        return Util.getEntityManager(getJndiAddresses());
    }

    @Override
    public void setJndiAddresses(JndiAddresses addresses) {
        this.jndiAddresses = addresses;
    }

    @Override
    public JndiAddresses getJndiAddresses() {
        if (jndiAddresses == null) {
            return JndiAddresses.DEFAULTS;
        }
        return jndiAddresses;
    }
}
