/*
 * JPAContainer
 * Copyright (C) 2010-2011 Oy Vaadin Ltd
 *
 * This program is available both under Commercial Vaadin Add-On
 * License 2.0 (CVALv2) and under GNU Affero General Public License (version
 * 3 or later) at your option.
 *
 * See the file licensing.txt distributed with this software for more
 * information about licensing.
 *
 * You should have received a copy of the GNU Affero General Public License
 * and CVALv2 along with this program.  If not, see
 * <http://www.gnu.org/licenses/> and <http://vaadin.com/license/cval-2.0>.
 */
package com.vaadin.addon.jpacontainer.provider.emtests.hibernate;

import com.vaadin.addon.jpacontainer.provider.emtests.AbstractLocalEntityProviderEMTest;
import com.vaadin.addon.jpacontainer.provider.LocalEntityProvider;
import com.vaadin.addon.jpacontainer.testdata.Address;
import com.vaadin.addon.jpacontainer.testdata.EmbeddedIdPerson;
import com.vaadin.addon.jpacontainer.testdata.Name;
import com.vaadin.addon.jpacontainer.testdata.Person;
import com.vaadin.addon.jpacontainer.testdata.PersonSkill;
import com.vaadin.addon.jpacontainer.testdata.Skill;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.hibernate.ejb.Ejb3Configuration;

/**
 * Entity Manager test for {@link LocalEntityProvider} that uses Hibernate as
 * the entity manager implementation.
 * 
 * @author Petter Holmström (IT Mill)
 * @since 1.0
 */
public class LocalEntityProviderHibernateTest extends
		AbstractLocalEntityProviderEMTest {

	protected EntityManager createEntityManager() throws Exception {
		Ejb3Configuration cfg = new Ejb3Configuration().setProperty(
				"hibernate.dialect", "org.hibernate.dialect.HSQLDialect")
				.setProperty("hibernate.connection.driver_class",
						"org.hsqldb.jdbcDriver").setProperty(
						"hibernate.connection.url",
						getDatabaseUrl()).setProperty(
						"hibernate.connection.username", "sa").setProperty(
						"hibernate.connection.password", "").setProperty(
						"hibernate.connection.pool_size", "1").setProperty(
						"hibernate.connection.autocommit", "true").setProperty(
						"hibernate.cache.provider_class",
						"org.hibernate.cache.HashtableCacheProvider")
				.setProperty("hibernate.hbm2ddl.auto", "create-drop")
				.setProperty("hibernate.show_sql", "false")
				.addAnnotatedClass(Person.class)
				.addAnnotatedClass(Address.class)
				.addAnnotatedClass(EmbeddedIdPerson.class)
				.addAnnotatedClass(Name.class)
				.addAnnotatedClass(PersonSkill.class)
				.addAnnotatedClass(Skill.class);
		EntityManagerFactory emf = cfg.buildEntityManagerFactory();
		return emf.createEntityManager();
	}
}
