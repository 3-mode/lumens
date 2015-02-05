/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.component.resource;

import com.lumens.connector.Connector;
import com.lumens.connector.ConnectorFactory;
import com.lumens.connector.Direction;
import com.lumens.connector.Operation;
import com.lumens.connector.OperationResult;
import com.lumens.connector.ElementChunk;
import com.lumens.engine.TransformEngineContext;
import com.lumens.engine.TransformExecuteContext;
import com.lumens.engine.component.AbstractTransformComponent;
import com.lumens.engine.component.FormatEntry;
import com.lumens.engine.component.RegisterFormatComponent;
import com.lumens.engine.Resource;
import com.lumens.engine.TransformComponent;
import com.lumens.engine.ExecuteContext;
import com.lumens.engine.handler.DataSourceResultHandler;
import com.lumens.engine.handler.ResultHandler;
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
        if (TransformEngineContext.getContext() != null) {
            ConnectorFactory factory = TransformEngineContext.getContext().getConnectorFactory(getComponentType());
            if (factory == null)
                throw new RuntimeException("The '" + getComponentType() + "' is not supported");
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
    public void open() {
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
            List<ExecuteContext> exList = new ArrayList<>();
            List<Element> results = new ArrayList<>();
            DataContext dataCtx = null;
            OperationResult opRet = null;
            if (context instanceof DataContext) {
                if (this != context.getTargetComponent())
                    throw new RuntimeException(String.format("Fatal logical error with target component '%s'", context.getTargetComponent().getName()));
                opRet = ((DataContext) context).getResult();
                context = context.getParentContext();
            } else {
                Format targetFormat = entry != null ? entry.getFormat() : null;
                ElementChunk inputChunk = context.getInput();
                // Log input data
                handleInputLogging(context.getResultHandlers(), targetFmtName, inputChunk.getData());
                // use ElementChunk.isLast
                Operation operation = connector.getOperation();
                opRet = operation.execute(inputChunk, targetFormat);
            }

            if (opRet != null && opRet.hasData())
                results.addAll(opRet.getData());

            // Cache the executeNext chunk of current data source
            if (opRet != null && opRet.hasNext())
                dataCtx = new DataContext(context, opRet.executeNext());
            else {
                // If dataCtx is null then need to return to parent node not return to sibling 
                // because datasource can be link to multiple destination
                dataCtx = context.getParentDataContext();
            }

            if (opRet != null && !results.isEmpty() && this.hasTarget()) {
                for (TransformComponent target : this.getTargetList().values()) {
                    if (!results.isEmpty() && entry != null && target.accept(entry.getName()))
                        exList.add(new TransformExecuteContext(dataCtx, new ElementChunk(!opRet.hasNext(), results), target, entry.getName(), context.getResultHandlers()));
                }
            }

            if (exList.isEmpty()) {
                // If dataCtx is not null, continue to handle current data source return data chunks
                if (dataCtx != null)
                    exList.add(dataCtx);
            }
            // Log output data
            handleOutputLogging(context.getResultHandlers(), targetFmtName, results);

            return exList;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void handleInputLogging(List<ResultHandler> handlers, String targetName, List<Element> input) {
        for (ResultHandler handler : handlers)
            if (handler instanceof DataSourceResultHandler)
                handler.processInput(this, targetName, input);
    }

    private void handleOutputLogging(List<ResultHandler> handlers, String targetName, List<Element> input) {
        for (ResultHandler handler : handlers)
            if (handler instanceof DataSourceResultHandler)
                handler.processOutput(this, targetName, input);
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
    public boolean accept(String name) {
        return registerINFormatList.containsKey(name);
    }

    @Override
    public boolean isSingleTarget() {
        return false;
    }

    @Override
    public void start() {
        this.connector.start();
    }

    @Override
    public void stop() {
        this.connector.stop();
    }

}
