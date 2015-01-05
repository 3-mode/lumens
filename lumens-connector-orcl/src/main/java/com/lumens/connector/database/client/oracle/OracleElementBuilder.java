/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client.oracle;

import com.lumens.connector.database.DbUtils;
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
public class OracleElementBuilder {

    public List<Element> buildElement(Format output, ResultSet ret) throws Exception {
        List<Element> result = new ArrayList<>();
        if (output != null && !ret.isClosed()) {
            while (ret.next()) {
                DataElement data = new DataElement(output);
                buildFieldList(data, ret);
                result.add(data);
            }
        }
        return result;
    }

    private void buildFieldList(Element record, ResultSet ret) throws Exception {
        int fieldIndex = 1;
        for (Format field : record.getFormat().getChildren()) {
            if (OracleConstants.SQLPARAMS.equals(field.getName()))
                continue;
            record.addChild(field.getName()).setValue(getValue(field, ret, fieldIndex));
            ++fieldIndex;
        }
    }

    private Value getValue(Format format, ResultSet ret, int fieldIndex) throws Exception {
        switch (format.getType()) {
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
