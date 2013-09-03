package com.hrcms.server.dao;

/**
 *
 * @author shaofeng.wang@outlook.com
 */
public class PersonSummary {
    private String employeeID;
    private String employeeName;

    PersonSummary(int employeeID, String employeeName) {
        this.employeeID = Integer.toHexString(employeeID);
        this.employeeName = employeeName;
    }

    /**
     * @return the employeeID
     */
    public String getEmployeeID() {
        return employeeID;
    }

    /**
     * @param employeeID the employeeID to set
     */
    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    /**
     * @return the employeeName
     */
    public String getEmployeeName() {
        return employeeName;
    }

    /**
     * @param employeeName the employeeName to set
     */
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
}
