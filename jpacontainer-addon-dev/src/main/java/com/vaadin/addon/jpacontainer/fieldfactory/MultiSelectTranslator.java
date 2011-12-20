package com.vaadin.addon.jpacontainer.fieldfactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vaadin.addon.jpacontainer.EntityContainer;
import com.vaadin.ui.AbstractSelect;

/**
 * Wrapper property that translates collection of entities to collection of
 * identifiers and visa versa. Expects that the translator is used in a select
 * backed by a jpacontainer.
 */
public class MultiSelectTranslator extends PropertyTranslator {

    private final AbstractSelect select;

    public MultiSelectTranslator(AbstractSelect select) {
        this.select = select;
    }

    @SuppressWarnings({ "rawtypes" })
    private EntityContainer getContainer() {
        return (EntityContainer) select.getContainerDataSource();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object translateFromDatasource(Object value) {
        // Value here is a collection of entities, should be transformed to a
        // collection (set) of identifier
        // TODO, consider creating a cached value
        HashSet<Object> identifiers = new HashSet<Object>();
        Collection<Object> entities = (Collection<Object>) value;
        for (Object entity : entities) {
            Object identifier = getContainer().getEntityProvider()
                    .getIdentifier(entity);
            identifiers.add(identifier);
        }
        return identifiers;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Object translateToDatasource(Object formattedValue) throws Exception {

        // NOTE, this currently works properly only if equals and hashcode
        // methods have been implemented correctly (both depending on identifier
        // of the entity)
        // TODO create a filter that has a workaround for invalid
        // equals/hashCode

        // formattedValue here is a set of identifiers.
        // We will modify the existing collection of entities to contain
        // corresponding entities
        Collection idset = (Collection) formattedValue;

        Collection value = (Collection) getPropertyDataSource().getValue();

        if (value == null) {
            value = createNewCollectionForType(getPropertyDataSource().getType());
        }

        HashSet orphaned = new HashSet(value);

        // Add those that did not exist do not exist already + remove them from
        // orphaned collection
        for (Object id : idset) {
            Object entity = getContainer().getEntityProvider().getEntity(id);
            if (!value.contains(entity)) {
                value.add(entity);
            }
            orphaned.remove(entity);
        }

        // remove orphanded
        for (Object object : orphaned) {
            value.remove(object);
        }

        return value;
    }

    static Collection createNewCollectionForType(Class<?> type)
            throws InstantiationException, IllegalAccessException {
        if (type.isInterface()) {
            if(type == Set.class) {
                return new HashSet();
            } else if (type == List.class) {
                return new ArrayList();
            } else {
                throw new RuntimeException("Couldn't instantiate a collection for property.");
            }
        } else {
            return (Collection) type.newInstance();
        }
    }

}
