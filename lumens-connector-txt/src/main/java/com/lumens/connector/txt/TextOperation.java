/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.txt;

import com.lumens.connector.Operation;
import com.lumens.connector.OperationResult;
import com.lumens.model.Element;
import com.lumens.model.Format;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class TextOperation implements Operation {
    private TextClient client;
        
    TextOperation(TextClient cli){
        client = cli;        
    }
    
    @Override
    public void begin() {
    }

    @Override
    public void end() {
    }

    @Override
    public OperationResult execute(List<Element> elem, Format fmt) throws Exception {
        List<Element> result = new ArrayList();
        if( elem != null ){
            
        }
        return new TextOperationResult(result);
    }

    @Override
    public void commit() {
    }
}
