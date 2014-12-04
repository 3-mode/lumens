/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.connector;

import com.lumens.connector.Connector;
import com.lumens.connector.Direction;
import com.lumens.connector.Operation;
import com.lumens.connector.OperationResult;
import com.lumens.model.DataElement;
import com.lumens.model.DataFormat;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.Value;
import com.lumens.model.serializer.FormatSerializer;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static junit.framework.TestCase.assertEquals;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class ChameleonConnector implements Connector {
    private final Mock typeName;

    public ChameleonConnector(Mock typeName) {
        this.typeName = typeName;
    }

    @Override
    public boolean isOpen() {
        return true;
    }

    @Override
    public void open() {
    }

    @Override
    public void close() {
    }

    @Override
    public Operation getOperation() {
        return new Operation() {

            @Override
            public void begin() {
            }

            @Override
            public void end() {
            }

            @Override
            public OperationResult execute(List<Element> input, Format output) throws Exception {

                final List<Element> inputList = input;
                final Format format = output;

                return new OperationResult() {
                    private int chunkSize = 5;

                    @Override
                    public boolean isLastChunk() {
                        return --chunkSize == 0;
                    }

                    @Override
                    public List<Element> getResult() {
                        List<Element> resultList = new ArrayList<>();
                        if (Mock.PERSON == typeName) {
                            DataElement personData = new DataElement(format);
                            buildPersonData(personData);
                            resultList.add(personData);
                            personData = new DataElement(format);
                            buildPersonData(personData);
                            resultList.add(personData);
                            personData = new DataElement(format);
                            buildPersonData(personData);
                            resultList.add(personData);
                        } else if (Mock.WAREHOUSE == typeName) {
                            // the same data for warehouse input and output
                            resultList.addAll(inputList);
                        }
                        return resultList;
                    }

                    private void buildPersonData(Element personData) {
                        Element nameData = personData.addChild("name");
                        String mills = Long.toHexString(System.currentTimeMillis());
                        nameData.setValue("James wang_" + mills);
                        assertEquals("James wang_" + mills, nameData.getValue().getString());
                        Element assetData = personData.addChild("Asset");
                        // personAsset item 1
                        Element assetDataItem = assetData.addArrayItem();
                        assetDataItem.addChild("name").setValue("Mac air book " + mills);
                        assetDataItem.addChild("price").setValue(12000.05f);
                        assetDataItem.addChild("Vendor").addChild("name").setValue("Apple");
                        Element assetItemPart = assetDataItem.addChild("Part");
                        assetItemPart.addArrayItem().addChild("name").setValue("Mac mouse");
                        assetItemPart.addArrayItem().addChild("name").setValue("Mac keyboard");
                        assetItemPart.addArrayItem().addChild("name").setValue("Mac voice box");
                        // personAsset item 2
                        assetDataItem = assetData.addArrayItem();
                        assetDataItem.addChild("name").setValue("HP computer " + mills);
                        assetDataItem.addChild("price").setValue(15000.05f);
                        assetDataItem.addChild("Vendor").addChild("name").setValue("HP");
                        assetItemPart = assetDataItem.addChild("Part");
                        assetItemPart.addArrayItem().addChild("name").setValue("HP mouse");
                        assetItemPart.addArrayItem().addChild("name").setValue("HP keyboard");
                    }
                };
            }

            @Override
            public void commit() {
            }

        };
    }

    @Override
    public Map<String, Format> getFormatList(Direction direction) {
        Map<String, Format> formatList = new HashMap<>();
        Format root = new DataFormat("Root");
        if (Mock.PERSON == typeName) {
            try (InputStream in = ChameleonConnector.class.getResourceAsStream("/xml/Person_Format.xml")) {
                FormatSerializer xmlSerializer = new FormatSerializer(root);
                xmlSerializer.readFromXml(in);
            } catch (Exception ex) {
            }
            Format person = root.getChild("Person");
            person.setParent(null);
            formatList.put(person.getName(), person);
        } else if (Mock.WAREHOUSE == typeName) {
            try (InputStream in = ChameleonConnector.class.getResourceAsStream("/xml/Warehouse_Format.xml")) {
                FormatSerializer xmlSerializer = new FormatSerializer(root);
                xmlSerializer.readFromXml(in);
            } catch (Exception ex) {
            }
            Format wareHouse = root.getChild("WareHouse");
            wareHouse.setParent(null);
            formatList.put(wareHouse.getName(), wareHouse);
        } else if (Mock.FINAL == typeName) {
            try (InputStream in = ChameleonConnector.class.getResourceAsStream("/xml/Final_Format.xml")) {
                FormatSerializer xmlSerializer = new FormatSerializer(root);
                xmlSerializer.readFromXml(in);
            } catch (Exception ex) {
            }
            Format finalFmt = root.getChild("Final");
            finalFmt.setParent(null);
            formatList.put(finalFmt.getName(), finalFmt);
        }
        return formatList;
    }

    @Override
    public Format getFormat(Format format, String path, Direction direction) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setPropertyList(Map<String, Value> parameters) {
    }

}
