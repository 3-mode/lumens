/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.rapsync;

import static com.lumens.connector.database.DBConstants.SQLPARAMS;
import static com.lumens.connector.database.DBConstants.WHERE;
import com.lumens.logsys.SysLogFactory;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.ModelUtils;
import com.lumens.model.Value;
import org.apache.commons.lang.StringUtils;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class RapSyncQuerySQLBuilder implements RapSyncConstants {

    private final Format output;
    private final Logger log = SysLogFactory.getLogger(RapSyncQuerySQLBuilder.class);
    private int pageSize = 1000;

    public RapSyncQuerySQLBuilder(Format output) {
        this.output = output;
    }

    public RapSyncQuerySQLBuilder setPageSize(int size) {
        this.pageSize = size;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public Format getFormat() {
        return output;
    }

    public String generateSelectSQL(Element input) {
        StringBuilder queryFields = new StringBuilder();
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
        } else {
            throw new RuntimeException("Error no output format");
        }

        String strWhere = "", strOrderBy = null, strGroupBy = null;
        if (input != null) {
            for (Element condition : input.getChildren()) {
                String formatName = condition.getFormat().getName();
                if (SQLPARAMS.equals(formatName)) {
                    Element whereElem = condition.getChild(WHERE);
                    if (ModelUtils.isNotNullValue(whereElem)) {
                        if (!strWhere.isEmpty()) {
                            strWhere += " AND ";
                        }
                        strWhere += whereElem.getValue().getString();
                    }
                    Element tableListElem = condition.getChild(TABLE_LIST);
                    if (ModelUtils.isNotNullValue(tableListElem)) {
                        String tableList = "";
                        for (String table : tableListElem.getValue().getString().split(",")) {
                            if (!tableList.isEmpty()) {
                                tableList += " OR ";
                            }
                            String name = table.trim().replaceAll("\"", "'");
                            if (!name.startsWith("'")) {
                                name = "'" + name;
                            }
                            if (!name.endsWith("'")) {
                                name = name + "'";
                            }
                            tableList += String.format("TABLE_NAME=%s", name);
                        }
                        if (!tableList.isEmpty()) {
                            if (!strWhere.isEmpty()) {
                                strWhere += " AND ";
                            }
                            strWhere += String.format("(%s)", tableList);
                        }
                    }
                } else {
                    Value value = condition.getValue();
                    if (value != null) {
                        String valueString = value.toString();
                        Matcher matcher = Pattern.compile("(=|>=|<=|<|>)(.*)").matcher(valueString);
                        String oper;
                        if (matcher.matches()) {
                            oper = matcher.group(1);
                            valueString = matcher.group(2);
                        } else {
                            log.warn("Missing oper in value: %s. Suggested usage:[<|<=|=|>|>=]'value'. Changed to default format: =%s. ", valueString, valueString);
                            oper = "=";
                        }

                        if (!strWhere.isEmpty()) {
                            strWhere += " AND ";
                        }

                        strWhere += String.format(" %s %s %s", formatName, oper, valueString);
                    }
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

        innerQuerySQL.append(" ,ROWNUM AS rowno FROM V_$LOGMNR_CONTENTS");
        innerQuerySQL.append(" WHERE ( ROWNUM < %d AND seg_type_name='TABLE' AND operation !='SELECT_FOR_UPDATE') ");
        if (StringUtils.isNotEmpty(strWhere) && StringUtils.isNotBlank(strWhere)) {
            innerQuerySQL.append(" AND ( ").append(strWhere.trim()).append(") ");
        }

        innerQuerySQL.append(" ORDER BY SCN ASC");  // Enforce to order by SCN as Oracle does not order by default        

        // Build final paging query SQL
        StringBuilder finalSQL = new StringBuilder();
        finalSQL.append("SELECT * FROM (").append(innerQuerySQL.toString()).append(") ").append(PAGEQUERY_TABLEALIAS)
                .append(" WHERE ").append(PAGEQUERY_TABLEALIAS).append(".rowno >= %d");

        return finalSQL.toString();
    }

    public String generatePageSQL(String SQL, int start, int page) {
        return String.format(SQL, start + page, start);
    }
}
