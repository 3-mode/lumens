/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.rapsync;

import static com.lumens.connector.database.DBConstants.SQLPARAMS;
import static com.lumens.connector.database.DBConstants.WHERE;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.ModelUtils;
import com.lumens.model.Value;
import org.apache.commons.lang.StringUtils;
import java.util.regex.Pattern;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class RapSyncQuerySQLBuilder implements RapSyncConstants {

    private final Format output;

    public RapSyncQuerySQLBuilder(Format output) {
        this.output = output;
    }

    public String generateSelectSQL(Element input) {
        StringBuilder queryFields = new StringBuilder();
        String strWhere = "", strOrderBy = null, strGroupBy = null;
        if (output != null) {
            for (Format child : output.getChildren()) {
                if (SQLPARAMS.equals(child.getName())) {
                    continue;
                }
                if (queryFields.length() > 0) {
                    queryFields.append(", ");
                }
                queryFields.append(child.getName());
            }
             for (Element condition : input.getChildren()) {
                if (SQLPARAMS.equals(condition.getFormat().getName())) {
                    continue;
                }
                Value value = condition.getValue();
                if (value != null) {
                    if (!strWhere.isEmpty()) {
                        strWhere += ",";
                    }
                    strWhere += value.toString();
                }
            }
        } else {
            throw new RuntimeException("Error no output format");
        }

        if (input != null) {
            Element sqlParams = input.getChild(SQLPARAMS);
            if (sqlParams != null) {
                Element whereElem = sqlParams.getChild(WHERE);
                if (ModelUtils.isNotNullValue(whereElem)) {
                    if (!strWhere.isEmpty()) {
                        strWhere += ",";
                    }
                    strWhere += whereElem.getValue().getString();
                }
            }
        }
        return generatePageSQL(output, queryFields.toString(), strWhere, strOrderBy, strGroupBy);
    }

    protected String generatePageSQL(Format table, String fields, String strWhere, String strOrderBy, String strGroupBy) {
        StringBuilder innerQuerySQL = new StringBuilder();
        innerQuerySQL.append("SELECT ").append(fields);

        // add enforce fields                
        for (String field : ENFORCE_FIELDS.split(",")) {
            Pattern pattern = Pattern.compile(String.format("%s,|%s$", field, field));           
            if (!pattern.matcher(fields).find()) {
                if (innerQuerySQL.length() > 0) {
                    innerQuerySQL.append(", ");
                }
                innerQuerySQL.append(field);
            }
        }

        innerQuerySQL.append(" FROM V_$LOGMNR_CONTENTS");
        innerQuerySQL.append(" WHERE ( seg_type_name='TABLE' AND operation !='SELECT_FOR_UPDATE') ");
        if (StringUtils.isNotEmpty(strWhere) && StringUtils.isNotBlank(strWhere)) {
            innerQuerySQL.append(" AND ( ").append(strWhere.trim()).append(") ");
        }

        innerQuerySQL.append(" ORDER BY SCN ASC");  // Enforce to order by SCN as Oracle does not order by default        

        return innerQuerySQL.toString();
    }

    public String generatePageSQL(String SQL, int start, int page) {
        return String.format(SQL, start + page, start);
    }
}
