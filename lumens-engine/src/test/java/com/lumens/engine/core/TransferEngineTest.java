/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.core;

import com.lumens.connector.Connector;
import com.lumens.connector.ConnectorFactory;
import com.lumens.connector.Direction;
import com.lumens.engine.ConnectorFactoryHolder;
import com.lumens.engine.EngineContext;
import com.lumens.engine.TransformProject;
import com.lumens.engine.component.FormatEntry;
import com.lumens.engine.component.instrument.DataTransformator;
import com.lumens.engine.component.resource.DataSource;
import com.lumens.engine.connector.ChameleonConnector;
import com.lumens.engine.connector.Mock;
import com.lumens.engine.run.SingleThreadTransformExecuteJob;
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
                } else if (Mock.FINAL.name().equals(componentType)) {
                    return new ConnectorFactory() {

                        @Override
                        public String getComponentType() {
                            return Mock.FINAL.name();
                        }

                        @Override
                        public Connector createConnector() {
                            return new ChameleonConnector(Mock.FINAL);
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
        DataSource finalDs = new DataSource(Mock.FINAL.name(), Long.toHexString(System.currentTimeMillis()));
        finalDs.open();
        System.out.println(String.format("WareHouse DS id '%s'", warehouseDs.getId()));
        // FormatEntry p11 = personDs.registerFormat("GetPerson", personDs.getFormatList(Direction.IN).get("Person"), Direction.IN);
        FormatEntry p12 = personDs.registerFormat("GetPerson", personDs.getFormatList(Direction.OUT).get("Person"), Direction.OUT);
        FormatEntry w21 = warehouseDs.registerFormat("PutWareHouse", warehouseDs.getFormatList(Direction.IN).get("WareHouse"), Direction.IN);
        FormatEntry w22 = warehouseDs.registerFormat("PutWareHouse", warehouseDs.getFormatList(Direction.OUT).get("WareHouse"), Direction.OUT);
        FormatEntry f31 = finalDs.registerFormat("LogFinal", finalDs.getFormatList(Direction.IN).get("Final"), Direction.IN);

        DataTransformator person_warehouse_transformator = new DataTransformator(Long.toHexString(System.currentTimeMillis()));
        DataTransformator final_transformator = new DataTransformator(Long.toHexString(System.currentTimeMillis()));

        // Person --> transformator
        personDs.getTargetList().put(person_warehouse_transformator.getId(), person_warehouse_transformator);
        // transformator ---> warehouse
        person_warehouse_transformator.getTargetList().put(warehouseDs.getId(), warehouseDs);
        // warehouse ---> transformator
        warehouseDs.getTargetList().put(final_transformator.getId(), final_transformator);
        // transformator --> finalout
        final_transformator.getTargetList().put(finalDs.getId(), finalDs);

        TransformRule rule_person_warehouse = person_warehouse_transformator.registerRule(p12, w21);
        rule_person_warehouse.getRuleItem("WareHouse.name").setScript("@Person.name");
        rule_person_warehouse.getRuleItem("WareHouse.asset").addTransformForeach(new TransformForeach("Person.Asset", "Asset", "index"));
        rule_person_warehouse.getRuleItem("WareHouse.asset.id").setScript("'ASSET_' + index");
        rule_person_warehouse.getRuleItem("WareHouse.asset.name").setScript("@Person.Asset[index].name");
        rule_person_warehouse.getRuleItem("WareHouse.asset.price").setScript("@Person.Asset[index].price");
        rule_person_warehouse.getRuleItem("WareHouse.asset.vendor.name").setScript("@Person.Asset[index].Vendor.name");

        rule_person_warehouse.getRuleItem("WareHouse.asset.part").addTransformForeach(new TransformForeach("Person.Asset.Part", "Part", "partIndex"));
        rule_person_warehouse.getRuleItem("WareHouse.asset.part.id").setScript("'PART_' + index + '_' + partIndex");
        rule_person_warehouse.getRuleItem("WareHouse.asset.part.name").setScript("@Person.Asset[index].Part[partIndex].name");

        TransformRule rule_warehouse_final = final_transformator.registerRule(w22, f31);
        rule_warehouse_final.getRuleItem("Final.name").setScript("@WareHouse.name");
        rule_warehouse_final.getRuleItem("Final.value").addTransformForeach(new TransformForeach("WareHouse.asset", "Asset", "index"));
        rule_warehouse_final.getRuleItem("Final.value").setScript("@WareHouse.asset[index].id");

        //**********************************************************************
        TransformProject project = new TransformProject();
        project.getDatasourceList().add(personDs);
        project.getDatasourceList().add(warehouseDs);
        project.getDatasourceList().add(finalDs);
        project.getDataTransformatorList().add(person_warehouse_transformator);
        project.getDataTransformatorList().add(final_transformator);

        long start = System.currentTimeMillis();
        new SingleThreadTransformExecuteJob(project).run();
        //**********************************************************************
        assertTrue(true);
        System.out.println("Cost: " + (System.currentTimeMillis() - start));
    }
}
