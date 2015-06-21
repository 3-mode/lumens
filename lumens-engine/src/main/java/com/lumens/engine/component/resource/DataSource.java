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
import com.lumens.connector.SupportAccessory;
import com.lumens.engine.TransformEngineContext;
import com.lumens.engine.TransformExecuteContext;
import com.lumens.engine.component.AbstractTransformComponent;
import com.lumens.engine.component.FormatEntry;
import com.lumens.engine.component.RegisterFormatComponent;
import com.lumens.engine.Resource;
import com.lumens.engine.TransformComponent;
import com.lumens.engine.ExecuteContext;
import com.lumens.engine.TransformException;
import com.lumens.engine.handler.InputOutputInspectionHandler;
import com.lumens.engine.handler.InspectionHandler;
import com.lumens.logsys.SysLogFactory;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.Value;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class DataSource extends AbstractTransformComponent implements RegisterFormatComponent, Resource {

    private final Logger log = SysLogFactory.getLogger(DataSource.class);
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
        if (log.isDebugEnabled()) {
            log.debug(String.format("'%s' properties is : '%s'", this.getName(), propertyList));
        }
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
            log.info(String.format("'%s' is opened", getName()));
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
        String targetFmtName = context.getTargetFormatName();
        FormatEntry entry = registerOUTFormatList.get(targetFmtName);
        List<Element> results = null;
        DataContext dataCtx = null;
        OperationResult opRet = null;

        if (log.isDebugEnabled())
            log.debug(String.format("Datasource '%s' is handling target '%s'", getName(), targetFmtName));

        if (context instanceof DataContext) {
            if (log.isDebugEnabled())
                log.debug("Get a next chunk result");

            if (this != context.getTargetComponent())
                throw new RuntimeException(String.format("Fatal logical error with target component '%s'",
                                                         context.getTargetComponent().getName()));

            opRet = ((DataContext) context).getResult();
            context = context.getParentContext();
        } else {
            if (log.isDebugEnabled())
                log.debug("Get first chunk result");
            Format targetFormat = entry != null ? entry.getFormat() : null;
            ElementChunk inputChunk = context.getInput();

            if (log.isDebugEnabled())
                log.debug(String.format("Datasource '%s' input chunk size '%d'.",
                                        getName(), inputChunk.getData() != null ? inputChunk.getData().size() : 0));

            // Log input data
            handleInputLogging(context.getInspectionHandlers(), targetFmtName, inputChunk.getData());
            Operation operation = connector.getOperation();
            try {
                opRet = operation.execute(inputChunk, targetFormat);
            } catch (Exception ex) {
                // TODO if ignore element exception
                throw new TransformException(this, ex);
            }
        }

        // Get the transform results
        results = (opRet != null && opRet.hasData()) ? opRet.getData() : new ArrayList<Element>();
        passAccessories(opRet, results);

        if (log.isDebugEnabled())
            log.debug(String.format("Datasource '%s' result chunk size '%d'.", getName(), results.size()));

        // Log output data
        handleOutputLogging(context.getInspectionHandlers(), targetFmtName, results);

        if (opRet != null && opRet.hasNext()) {
            // Cache the executeNext chunk of current data source
            dataCtx = new DataContext(context, opRet.executeNext());
        } else {
            // If dataCtx is null then need to return to parent node not return to sibling 
            // because datasource can be link to multiple destination
            dataCtx = context.getParentDataContext();
        }

        List<ExecuteContext> exList = new ArrayList<>();
        if (opRet != null && !results.isEmpty() && this.hasTarget()) {
            for (TransformComponent target : this.getTargetList().values()) {
                if (!results.isEmpty() && entry != null && target.accept(entry.getName()))
                    exList.add(new TransformExecuteContext(dataCtx,
                                                           new ElementChunk(!opRet.hasNext(), results),
                                                           target, entry.getName(), context.getInspectionHandlers()));
            }
        }

        if (exList.isEmpty()) {
            // If dataCtx is not null, continue to handle current data source return data chunks
            if (dataCtx != null)
                exList.add(dataCtx);
        }

        return exList;

    }

    private void passAccessories(OperationResult opRet, List<Element> results) {
        if (opRet != null && opRet instanceof SupportAccessory) {
            SupportAccessory sa = (SupportAccessory) opRet;
            ElementChunk inChunk = sa.getInput();
            if (sa.isQuery()) {
                if (inChunk != null && inChunk.getData() != null) {
                    Element inElem = inChunk.getData().get(inChunk.getStart());
                    for (Element outElem : results)
                        outElem.passAccessory(inElem);
                }
            } else {
                List<Element> inList = inChunk.getData();
                if (inList.size() != results.size())
                    throw new RuntimeException("Error logic, the input and output size is not matched for writing operation!");
                for (int i = 0; i < inList.size(); ++i)
                    results.get(i).passAccessory(inList.get(i));
            }
        }
    }

    private void handleInputLogging(List<InspectionHandler> handlers, String targetName, List<Element> input) {
        for (InspectionHandler handler : handlers)
            if (handler instanceof InputOutputInspectionHandler)
                ((InputOutputInspectionHandler) handler).processInput(this, targetName, input);
    }

    private void handleOutputLogging(List<InspectionHandler> handlers, String targetName, List<Element> input) {
        for (InspectionHandler handler : handlers)
            if (handler instanceof InputOutputInspectionHandler)
                ((InputOutputInspectionHandler) handler).processOutput(this, targetName, input);
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
