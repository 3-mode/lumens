/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lumens.connector.txt;

import com.lumens.connector.OperationResult;
import com.lumens.model.Element;
import java.util.List;
/**
 *
 * @author xiaoxiao
 */
public class TextOperationResult implements OperationResult{
     private List<Element> result;
     
    public TextOperationResult(List<Element> result) {
        this.result = result;
    }

    @Override
    public List<Element> getResult() {
        return result;
    }        
}
