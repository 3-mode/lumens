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

    private final Map<String, FormatEntry> registerOUTFormatList;
    private final Map<String, FormatEntry> registerINFormatList;
    private Connector connector;
    private Map<String, Value> propertyList = new HashMap<>();
    private Map<String, Format> inFormatList;
    private Map<String, Format> outFormatList;

    public DataSource(String componentType, String id) {
        super(componentType, id);
        registerOUTFormatList = new HashMap<>();
        registerINFormatList = new HashMap<>();
        // Try to search the OSGI bundle if exist else try to instance it directly
        if (EngineContext.getContext() != null) {
            ConnectorFactory factory = EngineContext.getContext().getConnectorFactory(getComponentType());
            if (factory != null)
                connector = factory.createConnector();
            else
                throw new RuntimeException("The '" + getComponentType() + "' is not supported");
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
            isOpen = connector.isOpen();
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
            String targetFmtName = context.getTargetFormatName();
            FormatEntry entry = registerOUTFormatList.get(targetFmtName);
            Format targetFormat = entry != null ? entry.getFormat() : null;
            List<Element> result = new ArrayList<>();
            List<Element> inputDataList = context.getInput();
            Operation operation = connector.getOperation();
            // TODO Support chunk
            OperationResult opRet = operation.execute(inputDataList, targetFormat);
            while (opRet != null && opRet.hasResult()) {
                result.addAll(opRet.getResult());
            }
            for (ResultHandler handler : context.getResultHandlers())
                if (!(handler instanceof LastResultHandler))
                    handler.process(this, targetFmtName, result);
            List<ExecuteContext> exList = new ArrayList<>();
            if (!result.isEmpty() && entry != null)
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
    public FormatEntry registerFormat(String formatEntryName, Format format, Direction direction) {
        if (direction == Direction.IN) {
            FormatEntry inFmtEntry = new FormatEntry(this.getId(), formatEntryName, format, Direction.IN);
            registerINFormatList.put(formatEntryName, inFmtEntry);
            return inFmtEntry;
        } else {
            FormatEntry outFmtEntry = new FormatEntry(this.getId(), formatEntryName, format, Direction.OUT);
            registerOUTFormatList.put(formatEntryName, outFmtEntry);
            return outFmtEntry;
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
        return registerINFormatList.containsKey(ctx.getTargetFormatName());
    }

    @Override
    public boolean isSingleTarget() {
        return false;
    }
}
