/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.core;

import com.lumens.connector.Connector;
import com.lumens.connector.ConnectorFactory;
import com.lumens.connector.Direction;
import com.lumens.engine.ConnectorFactoryHolder;
import com.lumens.engine.EngineContext;
import com.lumens.engine.component.FormatEntry;
import com.lumens.engine.component.instrument.DataTransformator;
import com.lumens.engine.component.resource.DataSource;
import com.lumens.engine.connector.ChameleonConnector;
import com.lumens.engine.connector.Mock;
import com.lumens.processor.transform.TransformForeach;
import com.lumens.processor.transform.TransformRule;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class TransferEngineTest {

    public TransferEngineTest() {
        EngineContext.start(new ConnectorFactoryHolder() {

            @Override
            public ConnectorFactory getFactory(String componentType) {
                if (Mock.PERSON.name().equals(componentType)) {
                    return new ConnectorFactory() {

                        @Override
                        public String getComponentType() {
                            return Mock.PERSON.name();
                        }

                        @Override
                        public Connector createConnector() {
                            return new ChameleonConnector(Mock.PERSON);
                        }
                    };
                } else if (Mock.WAREHOUSE.name().equals(componentType)) {
                    return new ConnectorFactory() {

                        @Override
                        public String getComponentType() {
                            return Mock.WAREHOUSE.name();
                        }

                        @Override
                        public Connector createConnector() {
                            return new ChameleonConnector(Mock.WAREHOUSE);
                        }
                    };
                }
                return null;
            }
        });
    }

    @Test
    public void runSimpleTransformTask() throws Exception {
        DataSource personDs = new DataSource(Mock.PERSON.name(), Long.toHexString(System.currentTimeMillis()));
        personDs.open();
        System.out.println(String.format("Person DS id '%s'", personDs.getId()));
        DataSource warehouseDs = new DataSource(Mock.WAREHOUSE.name(), Long.toHexString(System.currentTimeMillis()));
        warehouseDs.open();
        System.out.println(String.format("WareHouse DS id '%s'", warehouseDs.getId()));
        FormatEntry p11 = personDs.registerFormat("GetPerson", personDs.getFormatList(Direction.IN).get("Person"), Direction.IN);
        FormatEntry p12 = personDs.registerFormat("GetPerson", personDs.getFormatList(Direction.OUT).get("Person"), Direction.OUT);
        FormatEntry w21 = warehouseDs.registerFormat("PutWareHouse", personDs.getFormatList(Direction.IN).get("WareHouse"), Direction.IN);
        FormatEntry w22 = warehouseDs.registerFormat("PutWareHouse", personDs.getFormatList(Direction.OUT).get("WareHouse"), Direction.OUT);
        DataTransformator dtf = new DataTransformator(Long.toHexString(System.currentTimeMillis()));
        personDs.getTargetList().put(dtf.getId(), dtf);
        dtf.getTargetList().put(warehouseDs.getId(), warehouseDs);
        TransformRule rule = dtf.registerRule(p12, w21);
        rule.getRuleItem("WareHouse.name").setScript("@Person.name");
        rule.getRuleItem("WareHouse.asset").addTransformForeach(new TransformForeach("Person.Asset", "Asset", "index"));
        rule.getRuleItem("WareHouse.asset.id").setScript("'ASSET_' + index");
        rule.getRuleItem("WareHouse.asset.name").setScript("@Person.Asset[index].name");
        rule.getRuleItem("WareHouse.asset.price").setScript("@Person.Asset[index].price");
        rule.getRuleItem("WareHouse.asset.vendor.name").setScript("@Person.Asset[index].Vendor.name");

        rule.getRuleItem("WareHouse.asset.part").addTransformForeach(new TransformForeach("Person.Asset.Part", "Part", "partIndex"));
        rule.getRuleItem("WareHouse.asset.part.id").setScript("'PART_' + index + '_' + partIndex");
        rule.getRuleItem("WareHouse.asset.part.name").setScript("@Person.Asset[index].Part[partIndex].name");

        long start = System.currentTimeMillis();
        //**********************************************************************
        TransferEngine te = new TransferEngine();
        te.execute();
        //**********************************************************************
        assertTrue(true);
        System.out.println("Cost: " + (System.currentTimeMillis() - start));
    }
}
