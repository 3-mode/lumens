/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client;

import com.lumens.connector.database.DBConstants;
import com.lumens.connector.database.DBUtils;
import com.lumens.logsys.SysLogFactory;
import com.lumens.model.DataElement;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.Value;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author shaofeng.wang@outlook.com
 */
public class DBElementBuilder {

    private final Logger log = SysLogFactory.getLogger(DBElementBuilder.class);

    public List<Element> buildElement(Format output, ResultSet ret) throws Exception {
        List<Element> result = new ArrayList<>();
        if (output != null && !ret.isClosed()) {
            while (ret.next()) {
                Element data = new DataElement(output);
                buildFieldList(data, ret);
                result.add(data);
            }
        }
        return result;
    }

    private void buildFieldList(Element record, ResultSet ret) throws Exception {
        int fieldIndex = 1;
        for (Format field : record.getFormat().getChildren()) {
            if (DBConstants.SQLPARAMS.equals(field.getName())) {
                continue;
            }
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
                return new Value(DBUtils.toByteArray(ret.getBinaryStream(fieldIndex)));
            case DATE:
                java.sql.Date date = ret.getDate(fieldIndex);
                return new Value(date != null ? new Date(date.getTime()) : (Date) null);
            default:
                String message = String.format("Not supported data type:%s for format:%s", format.getType().toString(), format.getName());
                log.error(message);
                throw new Exception(message);
        }
    }
}
