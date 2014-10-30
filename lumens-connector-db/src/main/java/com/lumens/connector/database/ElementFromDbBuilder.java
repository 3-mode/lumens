/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lumens.connector.database;

import com.lumens.connector.database.DbUtils;
import com.lumens.connector.database.DatabaseConstants;
import com.lumens.model.DataElement;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.Value;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author shaofeng.wang@outlook.com
 */
public class ElementFromDbBuilder {

    public List<Element> buildElement(Format output, ResultSet ret) throws Exception {
        List<Element> result = new ArrayList<>();
        if (!ret.isClosed()) {
            while (ret.next()) {
                DataElement data = new DataElement(output);
                Element fields = data.addChild(DatabaseConstants.CONST_CNTR_SQLSERVER_FIELDS);
                buildFieldList(fields, ret);
                result.add(data);
            }
        }
        return result;
    }

    private void buildFieldList(Element fields, ResultSet ret) throws Exception {
        int fieldIndex = 1;
        for (Format f : fields.getFormat().getChildren()) {
            fields.addChild(f.getName()).setValue(getValue(f, ret, fieldIndex));
            ++fieldIndex;
        }
    }

    private Value getValue(Format f, ResultSet ret, int fieldIndex) throws Exception {
        switch (f.getType()) {
            case BOOLEAN:
                return new Value(ret.getBoolean(fieldIndex));
            case BYTE:
            case SHORT:
            case INTEGER:
            case LONG:
                return new Value(ret.getInt(fieldIndex));
            case FLOAT:
            case DOUBLE:
                return new Value(ret.getFloat(fieldIndex));
            case STRING:
                return new Value(ret.getString(fieldIndex));
            case BINARY:
                return new Value(DbUtils.toByteArray(ret.getBinaryStream(fieldIndex)));
            case DATE:
                java.sql.Date date = ret.getDate(fieldIndex);
                return new Value(date != null ? new Date(date.getTime()) : (Date) null);
            default:
                throw new Exception("Not support data type");
        }
    }
}
    
