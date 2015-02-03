/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.sysdb.dao;

import com.lumens.sysdb.entity.JobProjectRelation;
import java.util.List;

/**
 *
 * @author Xiaoxin(whiskeyfly@163.com)
 */
public class JobProjectRelationDAO extends BaseDAO {

    public void create(final long jobId, final long projectId) {
        simplePrepareStatTransactionExecute(sqlManager.getSQL("RelationDAO/CreateRelation"), jobId, projectId);
    }

    public List<JobProjectRelation> getAllRelation(long jobId) {
        return simpleQuery(sqlManager.getSQL("RelationDAO/AllRelation", jobId), JobProjectRelation.class);
    }

    public void deleteAllRelation(long jobId) {
        simpleTransactionExecute(sqlManager.getSQL("RelationDAO/DeleteAllRelation", jobId));
    }

    public void delete(final long jobId, final long projectId) {
        simplePrepareStatTransactionExecute(sqlManager.getSQL("RelationDAO/DeleteRelation"), jobId, projectId);
    }
}
