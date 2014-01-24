/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine;

import com.lumens.engine.component.DataSource;
import com.lumens.engine.component.DataTransformation;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class TransformProject {

    private List<DataSource> datasourceList = new ArrayList<>();
    private List<DataTransformation> transformationList = new ArrayList<>();
    private List<StartEntry> startList = new ArrayList<>();
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

    public void setDataTransformationList(List<DataTransformation> transformationList) {
        this.transformationList = transformationList;
    }

    public List<DataTransformation> getDataTransformationList() {
        return transformationList;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<StartEntry> getStartEntryList() {
        return startList;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void open() throws Exception {
        for (DataSource ds : datasourceList) {
            try {
                ds.open();
            } catch (Exception ex) {
                throw new Exception(ex);
            }
        }
        for (DataTransformation dt : transformationList) {
            try {
                dt.open();
            } catch (Exception ex) {
                throw new Exception(ex);
            }
        }
        isOpen = true;
    }

    public void close() throws Exception {

        if (isOpen()) {
            for (DataSource ds : datasourceList)
                ds.close();
            for (DataTransformation dt : transformationList)
                dt.close();
        }
        isOpen = false;
    }
}
