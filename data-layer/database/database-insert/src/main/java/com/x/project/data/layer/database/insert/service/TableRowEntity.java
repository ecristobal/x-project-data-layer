package com.x.project.data.layer.database.insert.service;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * JPA entity class.
 * 
 * @author Esteban Cristóbal
 */
@Entity
@Table(name = "PROPERTIES")
public class TableRowEntity {

    @Id
    @Column(name = "NAME")
    public String name;

    @Column(name = "VALUE")
    public String value;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

}
