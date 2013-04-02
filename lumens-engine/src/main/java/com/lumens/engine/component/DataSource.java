/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.component;

import com.lumens.connector.Connector;
import com.lumens.connector.Direction;
import com.lumens.connector.Operation;
import com.lumens.connector.OperationResult;
import com.lumens.engine.TransformExecuteContext;
import com.lumens.engine.run.ExecuteContext;
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
public class DataSource extends AbstractTransformComponent implements RegisterFormatComponent
{
    private String name;
    private String className;
    private String description;
    private Connector connector;
    private Map<String, FormatEntry> registerOUTFormatList = new HashMap<String, FormatEntry>();
    private Map<String, FormatEntry> registerINFormatList = new HashMap<String, FormatEntry>();
    private Map<String, Format> inFormatList;
    private Map<String, Format> outFormatList;
    private Map<String, Value> propertyList;

    public DataSource(String className)
    {
        this.className = className;
    }

    @Override
    public String getClassName()
    {
        return className;
    }

    public void setPropertyList(Map<String, Value> propertyList)
    {
        this.propertyList = propertyList;
    }

    public Map<String, Value> getPropertyList()
    {
        return propertyList;
    }

    @Override
    public void open() throws Exception
    {
        connector = (Connector) Class.forName(className).newInstance();
        connector.setPropertyList(propertyList);
        connector.open();
        inFormatList = connector.getFormatList(Direction.IN);
        outFormatList = connector.getFormatList(Direction.OUT);
    }

    public Map<String, Format> getFormatList(Direction direction)
    {
        return direction == Direction.IN ? inFormatList : outFormatList;
    }

    @Override
    public void close()
    {
        connector.close();
        registerOUTFormatList.clear();
        registerINFormatList.clear();
    }

    @Override
    public List<ExecuteContext> execute(ExecuteContext context)
    {
        try
        {
            String targetName = context.getTargetName();
            FormatEntry entry = registerOUTFormatList.get(targetName);
            Format targetFormat = entry.getFormat();
            List<Element> result = new ArrayList<Element>();
            Object input = context.getInput();
            Operation operation = connector.getOperation();
            if (input instanceof List)
            {
                List list = (List) input;
                if (!list.isEmpty() && list.get(0) instanceof Element)
                {
                    List<Element> inputDataList = (List<Element>) list;
                    for (Element data : inputDataList)
                    {
                        OperationResult opRet = operation.execute(data);
                        result.addAll(opRet.getResult(targetFormat));
                    }
                }
            } else if (input instanceof Element)
            {
                OperationResult opRet = operation.execute((Element) input);
                result.addAll(opRet.getResult(targetFormat));
            }
            List<ExecuteContext> exList = new ArrayList<ExecuteContext>();
            exList.add(new TransformExecuteContext(result, entry.getName()));
            return exList;
        } catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public void setName(String name)
    {
        this.name = name;
    }

    public Connector getConnector()
    {
        return connector;
    }

    @Override
    public void registerFormat(String formatEntryName, Format format, Direction direction)
    {
        if (direction == Direction.IN)
            registerINFormatList.put(formatEntryName, new FormatEntry(formatEntryName, format,
                                                                      Direction.IN));
        else
            registerOUTFormatList.
                    put(formatEntryName, new FormatEntry(formatEntryName, format, Direction.OUT));
    }

    @Override
    public FormatEntry removeFormat(String formatEntryName, Direction direction)
    {
        if (direction == Direction.IN)
        {
            return registerINFormatList.remove(formatEntryName);
        } else
            return registerOUTFormatList.remove(formatEntryName);
    }

    @Override
    public Map<String, FormatEntry> getRegisteredFormatList(Direction direction)
    {
        if (direction == Direction.IN)
            return registerINFormatList;
        else
            return registerOUTFormatList;
    }

    @Override
    public boolean accept(ExecuteContext ctx)
    {
        return registerINFormatList.containsKey(ctx.getTargetName());
    }

    @Override
    public boolean isSingleTarget()
    {
        return false;
    }
}
