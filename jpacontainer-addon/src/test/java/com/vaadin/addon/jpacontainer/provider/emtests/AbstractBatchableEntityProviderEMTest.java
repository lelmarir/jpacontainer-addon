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

package com.vaadin.addon.jpacontainer.provider.emtests;

import com.vaadin.addon.jpacontainer.BatchableEntityProvider;
import com.vaadin.addon.jpacontainer.MutableEntityProvider;
import com.vaadin.addon.jpacontainer.testdata.Address;
import com.vaadin.addon.jpacontainer.testdata.Person;
import com.vaadin.addon.jpacontainer.testdata.DataGenerator;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Abstract test case for {@link BatchableEntityProvider} that should work with
 * any entity manager that follows the specifications. Subclasses should provide
 * a concrete entity manager implementation to test. If the test passes, the
 * entity manager implementation should work with JPAContainer.
 * <p>
 * Note, that the test data used will not contain circular references that might
 * cause problems when committing buffered changes.
 * 
 * @author Petter Holmström (Vaadin Ltd)
 * @since 1.0
 */
public abstract class AbstractBatchableEntityProviderEMTest extends
		AbstractMutableEntityProviderEMTest {

	@Test
	public void testBatchUpdate() {

		/*
		 * This is a very simple test, but better than nothing.
		 */

		final Person addedPerson = new Person();
		addedPerson.setFirstName("Hello");
		addedPerson.setLastName("World");
		addedPerson.setDateOfBirth(java.sql.Date.valueOf("2000-06-02"));
		addedPerson.setAddress(new Address());
		addedPerson.getAddress().setStreet("Street");
		addedPerson.getAddress().setPostalCode("Postal Code");
		addedPerson.getAddress().setPostOffice("Post Office");
		
		final Person updatedPerson = DataGenerator.getTestDataSortedByName().get(1).clone();
		updatedPerson.setFirstName("Another changed first name");
		addedPerson.setManager(updatedPerson);


		final Person removedPerson = DataGenerator.getTestDataSortedByName().get(2).clone();
		final Object[] addedPersonId = new Object[1];
		
		entityProvider.setEntitiesDetached(false);

		BatchableEntityProvider.BatchUpdateCallback<Person> callback = new BatchableEntityProvider.BatchUpdateCallback<Person>() {

			public void batchUpdate(MutableEntityProvider<Person> batchEnabledEntityProvider) {
				addedPersonId[0] = batchEnabledEntityProvider.addEntity(addedPerson).getId();
				batchEnabledEntityProvider.updateEntity(updatedPerson);
				batchEnabledEntityProvider.removeEntity(removedPerson.getId());
			}
			
		};
		((BatchableEntityProvider<Person>) entityProvider).batchUpdate(callback);

		assertEquals(updatedPerson, entityProvider.getEntity(container, updatedPerson.getId()));
		assertFalse(entityProvider.containsEntity(container, removedPerson.getId(), null));
		
		Person entityFromProvider = entityProvider.getEntity(container, addedPersonId[0]);
		assertEquals(addedPerson.getFirstName(), entityFromProvider.getFirstName());
                assertEquals(addedPerson.getLastName(), entityFromProvider.getLastName());
                assertEquals(addedPerson.getDateOfBirth(), entityFromProvider.getDateOfBirth());
                assertEquals(addedPerson.getAddress().getStreet(), entityFromProvider.getAddress().getStreet());
                assertEquals(addedPerson.getAddress().getPostalCode(), entityFromProvider.getAddress().getPostalCode());
                assertEquals(addedPerson.getAddress().getPostOffice(), entityFromProvider.getAddress().getPostOffice());
		
		assertEquals(addedPerson.getManager(), updatedPerson);
	}
}
