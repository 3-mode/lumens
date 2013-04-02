/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.connector;

import com.lumens.model.Element;

/**
 *
 * @author washaofe
 */
public interface Operation
{
    public OperationResult execute(Element input) throws Exception;
}
