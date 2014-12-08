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
    public OperationResult execute(List<Element> elementList, Format fmt) throws Exception {
        List<Element> result = new ArrayList<>();
        if( elementList != null && !elementList.isEmpty() ){
            for(Element elem: elementList){
                Element oper = elem.getChild(TextConstants.OPERATION);                
                if( oper == null && oper.getValue() == null)
                    throw new Exception("'operation' is mandatory");
                
                String operation = oper.getValue().toString();
                if( TextConstants.OPERATION_READ.equalsIgnoreCase(operation)){
                    result.addAll(client.read(elem));
                }else if (TextConstants.OPERATION_APPEND.equalsIgnoreCase(operation)){
                    client.write(elem, true);
                }if(TextConstants.OPERATION_OVERWRITE.equalsIgnoreCase(operation)){
                    client.write(elem, false);
                }else{
                    return null;
                }                    
            }            
        }
        return new TextOperationResult(result);
    }

    @Override
    public void commit() {
    }
}
