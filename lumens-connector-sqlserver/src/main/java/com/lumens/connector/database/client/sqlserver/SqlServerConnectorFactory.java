/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lumens.connector.database.client.sqlserver;

import com.lumens.connector.Connector;
import com.lumens.connector.ConnectorFactory;
/**
 *
 * @author testing
 */
public class SqlServerConnectorFactory implements ConnectorFactory{

    @Override
    public Connector createConnector() {
        return new SqlServerConnector();
    }

    @Override
    public String getIdentifier() {
        return "id-sqlserver-jdbc";
    }   
}
