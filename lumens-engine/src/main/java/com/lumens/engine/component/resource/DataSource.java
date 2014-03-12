/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.component.resource;

import com.lumens.connector.Connector;
import com.lumens.connector.ConnectorFactory;
import com.lumens.connector.Direction;
import com.lumens.connector.Operation;
import com.lumens.connector.OperationResult;
import com.lumens.engine.EngineContext;
import com.lumens.engine.TransformExecuteContext;
import com.lumens.engine.component.AbstractTransformComponent;
import com.lumens.engine.component.FormatEntry;
import com.lumens.engine.component.RegisterFormatComponent;
import com.lumens.engine.Resource;
import com.lumens.engine.run.ExecuteContext;
import com.lumens.engine.run.LastResultHandler;
import com.lumens.engine.run.ResultHandler;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.Value;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class DataSource extends AbstractTransformComponent implements RegisterFormatComponent, Resource {

    private Connector connector;
    private Map<String, FormatEntry> registerOUTFormatList;
    private Map<String, FormatEntry> registerINFormatList;
    private Map<String, Value> propertyList = new HashMap<>();
    private Map<String, Format> inFormatList;
    private Map<String, Format> outFormatList;

    public DataSource(String identifier) {
        super(identifier);
        registerOUTFormatList = new HashMap<>();
        registerINFormatList = new HashMap<>();
        // Try to search the OSGI bundle if exist else try to instance it directly
        if (EngineContext.getContext() != null) {
            ConnectorFactory factory = EngineContext.getContext().getConnectorFactory(getIdentifier());
            if (factory != null)
                connector = factory.createConnector();
        }
    }

    public void setPropertyList(Map<String, Value> propertyList) {
        this.propertyList = propertyList;
    }

    public Map<String, Value> getPropertyList() {
        return propertyList;
    }

    @Override
    public void open() throws Exception {
        if (!isOpen()) {
            connector.setPropertyList(propertyList);
            connector.open();
            isOpen = true;
        }
    }

    public Map<String, Format> getFormatList(Direction direction) {
        if (isOpen()) {
            if (direction == Direction.IN && inFormatList == null)
                inFormatList = connector.getFormatList(Direction.IN);
            else if (direction == Direction.OUT && outFormatList == null)
                outFormatList = connector.getFormatList(Direction.OUT);
        }
        return direction == Direction.IN ? inFormatList : outFormatList;
    }

    @Override
    public void close() {
        if (isOpen()) {
            connector.close();
            inFormatList = null;
            outFormatList = null;
            isOpen = false;
        }
    }

    @Override
    public List<ExecuteContext> execute(ExecuteContext context) {
        try {
            String targetName = context.getTargetName();
            FormatEntry entry = registerOUTFormatList.get(targetName);
            Format targetFormat = entry.getFormat();
            List<Element> result = new ArrayList<>();
            Object input = context.getInput();
            Operation operation = connector.getOperation();
            if (input instanceof List) {
                List list = (List) input;
                if (!list.isEmpty() && list.get(0) instanceof Element) {
                    List<Element> inputDataList = (List<Element>) list;
                    for (Element data : inputDataList) {
                        OperationResult opRet = operation.execute(data, targetFormat);
                        result.addAll(opRet.getResult());
                    }
                }
            } else if (input instanceof Element) {
                OperationResult opRet = operation.execute((Element) input, targetFormat);
                result.addAll(opRet.getResult());
            }
            for (ResultHandler handler : context.getResultHandlers())
                if (!(handler instanceof LastResultHandler))
                    handler.process(this, targetName, result);
            List<ExecuteContext> exList = new ArrayList<>();
            exList.add(new TransformExecuteContext(result, entry.getName(), context.getResultHandlers()));
            return exList;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public Connector getConnector() {
        return connector;
    }

    @Override
    public void registerFormat(String formatEntryName, Format format, Direction direction) {
        if (direction == Direction.IN) {
            registerINFormatList.put(formatEntryName, new FormatEntry(formatEntryName, format, Direction.IN));
        } else {
            registerOUTFormatList.put(formatEntryName, new FormatEntry(formatEntryName, format, Direction.OUT));
        }
    }

    @Override
    public FormatEntry removeFormat(String formatEntryName, Direction direction) {
        if (direction == Direction.IN) {
            return registerINFormatList.remove(formatEntryName);
        } else {
            return registerOUTFormatList.remove(formatEntryName);
        }
    }

    @Override
    public Map<String, FormatEntry> getRegisteredFormatList(Direction direction) {
        if (direction == Direction.IN) {
            return registerINFormatList;
        } else {
            return registerOUTFormatList;
        }
    }

    @Override
    public boolean accept(ExecuteContext ctx) {
        return registerINFormatList.containsKey(ctx.getTargetName());
    }

    @Override
    public boolean isSingleTarget() {
        return false;
    }
}
