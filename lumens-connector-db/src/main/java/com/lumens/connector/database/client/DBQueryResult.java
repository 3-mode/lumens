/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.database.client;

import com.lumens.connector.OperationResult;
import com.lumens.connector.database.Client;
import com.lumens.model.Element;
import com.lumens.model.Format;
import java.util.List;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class DBQueryResult implements OperationResult {

    private List<Element> result;
    private final Client client;
    private final String SQL;
    private final Format output;
    private int start;

    DBQueryResult(Client client, String SQL, Format output, List<Element> result) {
        this.client = client;
        this.SQL = SQL;
        this.output = output;
        this.result = result;
        this.start = client.getPageSize() + 1;
    }

    @Override
    public List<Element> get() {
        List<Element> prevResult = result;
        if (hasMore()) {
            result = client.executeQuery(DBQuerySQLBuilder.generatePageSQL(SQL, start, client.getPageSize()), output);
            start += client.getPageSize();
        } else {
            result = null;
        }
        return prevResult;
    }

    @Override
    public boolean has() {
        return result != null && !result.isEmpty();
    }

    @Override
    public boolean hasMore() {
        return result != null && result.size() == client.getPageSize();
    }
}
