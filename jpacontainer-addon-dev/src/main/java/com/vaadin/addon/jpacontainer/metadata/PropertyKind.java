package com.vaadin.addon.jpacontainer.metadata;

import javax.persistence.Embeddable;

/**
 * Enumeration defining the property kind.
 * 
 * @author Petter Holmström (Vaadin Ltd)
 */
public enum PropertyKind {

    /**
     * The property is embedded.
     * 
     * @see javax.persistence.Embeddable
     * @see javax.persistence.Embedded
     */
    EMBEDDED,
    /**
     * The property is a reference.
     * 
     * @see javax.persistence.OneToOne
     * @see javax.persistence.ManyToOne
     */
    MANY_TO_ONE,
    /**
     * The property is a reference.
     * 
     * @see javax.persistence.ManyToOne
     */
    ONE_TO_ONE,
     /**
     * The property is a collection.
     * 
     * @see javax.persistence.OneToMany
     */
    ONE_TO_MANY,
    /**
     * The property is a reference.
     * 
     * @see javax.persistence.ManyToMany
     */
    MANY_TO_MANY,
    /**
     * The property is a collection {@link Embeddable}s or basic data types.
     * 
     * @see javax.persistence.ElementCollection
     */
    ELEMENT_COLLECTION,
    /**
     * The property is of a simple datatype.
     */
    SIMPLE,
    /**
     * The property is not persistent property.
     */
    NONPERSISTENT
}