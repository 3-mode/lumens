
package com.lumens.server.sql.config;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "databaseAccessObject"
})
@XmlRootElement(name = "database-access-objects")
public class DatabaseAccessObjects {

    @XmlElement(name = "database-access-object")
    protected List<DatabaseAccessObject> databaseAccessObject;

    /**
     * Gets the value of the databaseAccessObject property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the databaseAccessObject property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDatabaseAccessObject().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DatabaseAccessObject }
     * 
     * 
     */
    public List<DatabaseAccessObject> getDatabaseAccessObject() {
        if (databaseAccessObject == null) {
            databaseAccessObject = new ArrayList<DatabaseAccessObject>();
        }
        return this.databaseAccessObject;
    }

}
