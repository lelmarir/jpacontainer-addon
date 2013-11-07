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

package com.vaadin.addon.jpacontainer;

/**
 * Interface to be implemented by all <code>EntityProvider</code>s that perform
 * some kind of internal caching.
 * 
 * @author Petter Holmström (Vaadin Ltd)
 * @since 1.0
 */
public interface CachingEntityProvider<T> extends EntityProvider<T> {

    /**
     * Gets the maximum number of entity instances to store in the cache. The
     * default value is implementation specific.
     * 
     * @return the max size, or -1 for unlimited size.
     */
    public int getEntityCacheMaxSize();

    /**
     * Sets the maximum number of entity instances to store in the cache. The
     * implementation may decide what to do when the cache is full, but a full
     * cache may never cause an exception. This feature is optional.
     * 
     * @param maxSize
     *            the new maximum size, or -1 for unlimited size.
     * @throws UnsupportedOperationException
     *             if this implementation does not support configuring the
     *             maximum cache size.
     */
    public void setEntityCacheMaxSize(int maxSize)
            throws UnsupportedOperationException;

    /**
     * Flushes the cache, forcing all entities to be loaded from the persistence
     * storage upon next request. This feature should be implemented by all
     * caching entity providers.
     */
    public void flush();

    /**
     * Returns whether the entity provider currently has the internal cache
     * enabled. By default, caching should be enabled.<br/>
     * <br/>
     * 
     * <b>NOTE!</b> If a {@link QueryModifierDelegate} is in use and it modifies
     * the filters through the
     * {@link QueryModifierDelegate#filtersWillBeAdded(javax.persistence.criteria.CriteriaBuilder, javax.persistence.criteria.CriteriaQuery, java.util.List)}
     * method, caching will <em>NOT</em> be enabled.
     * 
     * @return true if the cache is in use, false otherwise.
     */
    public boolean isCacheEnabled();

    /**
     * Turns the cache on or off. When the cache is turned off, it is
     * automatically flushed. <br/>
     * <br/>
     * 
     * <b>NOTE!</b> If a {@link QueryModifierDelegate} is in use and it modifies
     * the filters through the
     * {@link QueryModifierDelegate#filtersWillBeAdded(javax.persistence.criteria.CriteriaBuilder, javax.persistence.criteria.CriteriaQuery, java.util.List)}
     * method, caching will <em>NOT</em> be enabled.
     * 
     * @param cacheEnabled
     *            true to turn the cache on, false to turn it off.
     * @throws UnsupportedOperationException
     *             if the cache cannot be turned on or off programmatically.
     */
    public void setCacheEnabled(boolean cacheEnabled)
            throws UnsupportedOperationException;

    /**
     * Returns whether the entity provider is currently using the internal
     * cache, which will be the case if both the caching is enabled (
     * {@link #setCacheEnabled(boolean)} and there is no filter modifiying
     * {@link QueryModifierDelegate} in use.
     * 
     * @return true if the cache is actually in use, false otherwise.
     */
    public boolean usesCache();

    /**
     * If the cache is in use, all entities are automatically detached
     * regardless of the state of this flag.
     * <p>
     * {@inheritDoc }
     * 
     * @see #isCacheEnabled()
     */
    @Override
    public boolean isEntitiesDetached();

    /**
     * If the cache is in use, all entities are automatically detached
     * regardless of the state of this flag.
     * <p>
     * {@inheritDoc }
     * 
     * @see #isCacheEnabled()
     */
    @Override
    public void setEntitiesDetached(boolean detached)
            throws UnsupportedOperationException;

    /**
     * Returns whether entities found in the cache should be cloned before they
     * are returned or not. If this flag is false, two subsequent calls to
     * {@link #getEntity(java.lang.Object) } with the same entity ID and without
     * flushing the cache in between may return the same entity instance. This
     * could be a problem if the instance is modified, as the cache would then
     * contain the locally modified entity instance and not the one that was
     * fetched from the persistence storage.
     * <p>
     * If the entity instances are serialized and deserialized before they reach
     * the container, or the container is read-only, entities need not be
     * cloned.
     * <p>
     * It is undefined what happens if this flag is true and the entities are
     * not cloneable.
     * <p>
     * The default value of this flag is implementation dependent.
     * 
     * @see #setCloneCachedEntities(boolean)
     * @return true if cached entities should be cloned before they are
     *         returned, false to return them directly.
     */
    public boolean isCloneCachedEntities();

    /**
     * Changes the value of the {@link #isCloneCachedEntities() } flag.
     * 
     * @param clone
     *            true to clone cached entities before returning them, false to
     *            return them directly.
     * @throws UnsupportedOperationException
     *             if the implementation does not support changing the state of
     *             this flag.
     */
    public void setCloneCachedEntities(boolean clone)
            throws UnsupportedOperationException;
}
