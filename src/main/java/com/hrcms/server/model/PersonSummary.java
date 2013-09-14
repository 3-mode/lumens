package com.hrcms.server.model;

/**
 *
 * @author shaofeng.wang@outlook.com
 */
public class PersonSummary {
    public String employeeID;
    public String employeeName;

    public PersonSummary(int employeeID, String employeeName) {
        this.employeeID = Integer.toHexString(employeeID);
        this.employeeName = employeeName;
    }
}
