/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine;

import com.lumens.connector.Direction;
import com.lumens.engine.component.FormatEntry;
import com.lumens.engine.component.TransformRuleEntry;
import com.lumens.engine.component.resource.DataSource;
import com.lumens.engine.component.instrument.DataTransformer;
import com.lumens.logsys.LogSysFactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class TransformProject {

    private final Logger log = LogSysFactory.getLogger(TransformProject.class);
    private List<DataSource> datasourceList = new ArrayList<>();
    private List<DataTransformer> transformerList = new ArrayList<>();
    private List<StartEntry> startList = new ArrayList<>();
    private long ID;
    private String name;
    private String description;
    private boolean isOpen;

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

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

    public void setStartEntryList(List<StartEntry> startList) {
        this.startList = startList;
    }

    public List<StartEntry> discoverStartEntryList() {
        List<StartEntry> discoverStartList = new ArrayList<>();
        // By default the order is on created order
        for (DataTransformer dt : transformerList) {
            for (TransformRuleEntry tr : dt.getTransformRuleList())
                if (tr.getSourceId() == null || tr.getSourceId().isEmpty() || tr.getSourceId().equals(dt.getId()))
                    discoverStartList.add(new StartEntry(tr.getName(), dt));
        }
        for (DataSource ds : this.datasourceList) {
            Map<String, FormatEntry> inFmtList = ds.getRegisteredFormatList(Direction.IN);
            for (FormatEntry entry : ds.getRegisteredFormatList(Direction.OUT).values()) {
                if (!inFmtList.containsKey(entry.getName()))
                    discoverStartList.add(new StartEntry(entry.getName(), ds));
            }
        }

        removeInvalidStartEntry(discoverStartList);
        for (StartEntry startEntry : discoverStartList)
            this.addStartEntry(startEntry);

        if (log.isDebugEnabled()) {
            log.debug("Get Start entry total '" + startList.size() + "'");
            for (StartEntry startEntry : startList)
                log.debug(startEntry.getStartComponent().getName() + "[" + startEntry.getStartFormatName() + "]");
        }

        return startList;
    }

    private void removeInvalidStartEntry(List<StartEntry> discoverStartList) {
        Iterator<StartEntry> it = startList.iterator();
        while (it.hasNext()) {
            StartEntry next = it.next();
            if (!discoverStartList.contains(next))
                it.remove();
        }
    }

    public void addStartEntry(StartEntry startEntry) {
        if (startList.contains(startEntry))
            return;
        startList.add(startEntry);
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void open() {
        if (!isOpen()) {
            for (DataSource ds : datasourceList) {
                if (!ds.isOpen())
                    ds.open();
            }
            for (DataTransformer dt : transformerList) {
                if (!dt.isOpen())
                    dt.open();
            }
            isOpen = true;
        }
    }

    public void close() {
        if (isOpen()) {
            for (DataSource ds : datasourceList)
                ds.close();
            for (DataTransformer dt : transformerList)
                dt.close();
        }
        isOpen = false;
    }
}
