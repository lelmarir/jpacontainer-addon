package com.vaadin.demo.jpaaddressbook;

import org.vaadin.addon.customfield.CustomField;

import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.filter.Filters;
import com.vaadin.data.Property;
import com.vaadin.demo.jpaaddressbook.domain.Department;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;

/**
 * A custom field that allows selection of a department.
 */
public class DepartmentSelector extends CustomField {
    private ComboBox geographicalDepartment = new ComboBox();
    private ComboBox department = new ComboBox();

    private JPAContainer<Department> container;
    private JPAContainer<Department> geoContainer;

    public DepartmentSelector() {
        ContainerFactory cf = JpaAddressbookApplication.getInstance()
                .getContainerFactory();
        container = cf.getDepartmentReadOnlyContainer();
        geoContainer = cf.getDepartmentReadOnlyContainer();
        setCaption("Department");
        // Only list "roots" which are in our example geographical super
        // departments
        geoContainer.addPredicate(Filters.isNull("parent"));
        geographicalDepartment.setContainerDataSource(geoContainer);
        geographicalDepartment.setItemCaptionPropertyId("name");
        geographicalDepartment.setImmediate(true);

        container.setApplyPredicatesImmediately(false);
        filterDepartments(null);
        department.setContainerDataSource(container);
        department.setItemCaptionPropertyId("name");

        geographicalDepartment.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(
                    com.vaadin.data.Property.ValueChangeEvent event) {
                /*
                 * Modify filtering of the department combobox
                 */
                EntityItem<Department> item = geoContainer
                        .getItem(geographicalDepartment.getValue());
                Department entity = item.getEntity();
                filterDepartments(entity);
            }
        });
        department.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(
                    com.vaadin.data.Property.ValueChangeEvent event) {
                /*
                 * Modify the actual value of the custom field.
                 */
                if (department.getValue() == null) {
                    setValue(null, false);
                } else {
                    Department entity = container
                            .getItem(department.getValue()).getEntity();
                    setValue(entity, false);
                }
            }
        });

        CssLayout cssLayout = new CssLayout();
        cssLayout.addComponent(geographicalDepartment);
        cssLayout.addComponent(department);
        setCompositionRoot(cssLayout);
    }

    /**
     * Modify available options based on the "geo deparment" select.
     * 
     * @param currentGeoDepartment
     */
    private void filterDepartments(Department currentGeoDepartment) {
        if (currentGeoDepartment == null) {
            department.setValue(null);
            department.setEnabled(false);
        } else {
            container.removeAllContainerFilters();
            container.addPredicate(Filters.eq("parent", currentGeoDepartment));
            container.applyPredicates();
            department.setValue(null);
            department.setEnabled(true);
        }
    }

    @Override
    public void setPropertyDataSource(Property newDataSource) {
        super.setPropertyDataSource(newDataSource);
        setDepartment(newDataSource.getValue());
    }

    @Override
    public void setValue(Object newValue) throws ReadOnlyException,
            ConversionException {
        setDepartment(newValue);
    }

    private void setDepartment(Object newValue) {
        Department value = (Department) newValue;
        geographicalDepartment.setValue(value != null ? value.getParent()
                .getId() : null);
        department.setValue(value != null ? value.getId() : null);
    }

    @Override
    public Class<?> getType() {
        return Department.class;
    }

}
