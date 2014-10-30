/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lumens.connector.database.client.sqlserver;

import com.lumens.connector.OperationResult;
import com.lumens.model.Element;
import java.util.List;

public class SqlServerOperationResult implements OperationResult{
     private List<Element> result;

    public SqlServerOperationResult(List<Element> result) {
        this.result = result;
    }

    @Override
    public List<Element> getResult() {
        return result;
    }   
}
