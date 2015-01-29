/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine;

import com.lumens.connector.Direction;
import com.lumens.engine.component.FormatEntry;
import com.lumens.engine.component.TransformRuleEntry;
import com.lumens.engine.component.resource.DataSource;
import com.lumens.engine.component.instrument.DataTransformer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class TransformProject {

    private List<DataSource> datasourceList = new ArrayList<>();
    private List<DataTransformer> transformerList = new ArrayList<>();
    private String name;
    private String description;
    private boolean isOpen;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DataSource> getDatasourceList() {
        return datasourceList;
    }

    public void setDatasourceList(List<DataSource> datasourceList) {
        this.datasourceList = datasourceList;
    }

    public void setTransformatorList(List<DataTransformer> transformerList) {
        this.transformerList = transformerList;
    }

    public List<DataTransformer> getDataTransformerList() {
        return transformerList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<StartEntry> getStartEntryList() {
        List<StartEntry> startList = new ArrayList<>();
        for (DataTransformer dt : transformerList) {
            // build start point list
            for (TransformRuleEntry tr : dt.getTransformRuleList())
                if (tr.getSourceId() == null || tr.getSourceId().isEmpty() || tr.getSourceId().equals(dt.getId()))
                    startList.add(new StartEntry(tr.getName(), dt));
        }
        for (DataSource ds : this.datasourceList) {
            Map<String, FormatEntry> inFmtList = ds.getRegisteredFormatList(Direction.IN);
            for (FormatEntry entry : ds.getRegisteredFormatList(Direction.OUT).values()) {
                if (!inFmtList.containsKey(entry.getName()))
                    startList.add(new StartEntry(entry.getName(), ds));
            }
        }
        return startList;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void open() throws Exception {
        if (!isOpen()) {
            try {
                for (DataSource ds : datasourceList)
                    ds.open();
                for (DataTransformer dt : transformerList)
                    dt.open();
                isOpen = true;
            } catch (Exception ex) {
                throw new Exception(ex);
            }
        }
    }

    public void close() throws Exception {
        if (isOpen()) {
            for (DataSource ds : datasourceList)
                ds.close();
            for (DataTransformer dt : transformerList)
                dt.close();
        }
        isOpen = false;
    }
}
