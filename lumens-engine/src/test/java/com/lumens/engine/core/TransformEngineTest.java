/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.engine.core;

import com.lumens.connector.Connector;
import com.lumens.connector.ConnectorFactory;
import com.lumens.connector.Direction;
import com.lumens.engine.ConnectorFactoryHolder;
import com.lumens.engine.TransformEngineContext;
import com.lumens.engine.TransformComponent;
import com.lumens.engine.TransformEngine;
import com.lumens.engine.TransformProject;
import com.lumens.engine.component.FormatEntry;
import com.lumens.engine.component.instrument.DataTransformer;
import com.lumens.engine.component.resource.DataSource;
import com.lumens.engine.connector.ChameleonConnector;
import com.lumens.engine.connector.Mock;
import com.lumens.engine.handler.DataSourceResultHandler;
import com.lumens.engine.handler.ResultHandler;
import com.lumens.engine.run.SequenceTransformExecuteJob;
import com.lumens.engine.serializer.ProjectJsonParser;
import com.lumens.engine.serializer.ProjectSerializer;
import com.lumens.model.Element;
import com.lumens.processor.transform.TransformForeach;
import com.lumens.processor.transform.TransformRule;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public class TransformEngineTest {

    private TransformEngine transformEngine;

    public static String generateID() {
        for (int i = 0; i < 100; ++i)
            System.currentTimeMillis();
        return Long.toString(System.currentTimeMillis() + Math.round(Math.random()));
    }

    public TransformEngineTest() {
        transformEngine = new TransformEngine();
    }

    private void mockEngeinContext() {

        TransformEngineContext.start(new ConnectorFactoryHolder() {

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
        mockEngeinContext();
        DataSource personDs = new DataSource(Mock.PERSON.name(), generateID());
        personDs.open();
        System.out.println(String.format("Person DS id '%s'", personDs.getId()));
        DataSource warehouseDs = new DataSource(Mock.WAREHOUSE.name(), generateID());
        warehouseDs.open();
        System.out.println(String.format("WareHouse DS id '%s'", warehouseDs.getId()));

        DataSource finalDs = new DataSource(Mock.FINAL.name(), generateID());
        finalDs.open();
        System.out.println(String.format("Final DS id '%s'", finalDs.getId()));
        // FormatEntry p11 = personDs.registerFormat("GetPerson", personDs.getFormatList(Direction.IN).get("Person"), Direction.IN);

        FormatEntry p10 = personDs.registerFormat("GetPerson0", personDs.getFormatList(Direction.OUT).get("Person"), Direction.OUT);
        FormatEntry p12 = personDs.registerFormat("GetPerson1", personDs.getFormatList(Direction.OUT).get("Person"), Direction.OUT);
        FormatEntry w21 = warehouseDs.registerFormat("PutWareHouse", warehouseDs.getFormatList(Direction.IN).get("WareHouse"), Direction.IN);
        FormatEntry w22 = warehouseDs.registerFormat("PutWareHouse", warehouseDs.getFormatList(Direction.OUT).get("WareHouse"), Direction.OUT);
        FormatEntry f31 = finalDs.registerFormat("LogFinal", finalDs.getFormatList(Direction.IN).get("Final"), Direction.IN);

        DataTransformer person_warehouse_transformator = new DataTransformer(generateID());
        DataTransformer final_transformator = new DataTransformer(generateID());

        // Person --> transformator
        personDs.getTargetList().put(person_warehouse_transformator.getId(), person_warehouse_transformator);
        // transformator ---> warehouse
        person_warehouse_transformator.getTargetList().put(warehouseDs.getId(), warehouseDs);
        // warehouse ---> transformator
        warehouseDs.getTargetList().put(final_transformator.getId(), final_transformator);
        // transformator --> finalout
        final_transformator.getTargetList().put(finalDs.getId(), finalDs);

        TransformRule rule_person0_warehouse = person_warehouse_transformator.registerRule(p10, w21);
        rule_person0_warehouse.getRuleItem("WareHouse.name").setScript("@Person.name");
        rule_person0_warehouse.getRuleItem("WareHouse.asset").addTransformForeach(new TransformForeach("Person.Asset", "Asset", "index"));
        rule_person0_warehouse.getRuleItem("WareHouse.asset.id").setScript("'ASSET_' + index");
        rule_person0_warehouse.getRuleItem("WareHouse.asset.name").setScript("@Person.Asset[index].name");
        rule_person0_warehouse.getRuleItem("WareHouse.asset.price").setScript("@Person.Asset[index].price");
        rule_person0_warehouse.getRuleItem("WareHouse.asset.vendor.name").setScript("@Person.Asset[index].Vendor.name");

        rule_person0_warehouse.getRuleItem("WareHouse.asset.part").addTransformForeach(new TransformForeach("Person.Asset.Part", "Part", "partIndex"));
        rule_person0_warehouse.getRuleItem("WareHouse.asset.part.id").setScript("'PART_' + index + '_' + partIndex");
        rule_person0_warehouse.getRuleItem("WareHouse.asset.part.name").setScript("@Person.Asset[index].Part[partIndex].name");

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
        rule_warehouse_final.getRuleItem("Final.value").setScript("var id = @WareHouse.asset[index].id; \n //logInfo('assetId of final:' + index + '-' + id); \n return id;");

        //**********************************************************************
        TransformProject project = new TransformProject();
        project.getDatasourceList().add(personDs);
        project.getDatasourceList().add(warehouseDs);
        project.getDatasourceList().add(finalDs);
        project.getDataTransformerList().add(person_warehouse_transformator);
        project.getDataTransformerList().add(final_transformator);

        long start = System.currentTimeMillis();
        ResultHandler log = new DataSourceResultHandler() {

            @Override
            public void processOutput(TransformComponent src, String targetName, List<Element> output) {
                System.out.println(String.format("### %s size is '%d'", targetName, output.size()));
            }

            @Override
            public void processInput(TransformComponent src, String targetName, List<Element> input) {
            }
        };
        new SequenceTransformExecuteJob(project, Arrays.asList(log)).run();
        //**********************************************************************
        assertTrue(ChameleonConnector.countFinal == 150);

        System.out.println("Final size: " + ChameleonConnector.countFinal);
        System.out.println("Cost: " + (System.currentTimeMillis() - start));

        ProjectSerializer pwriter = new ProjectSerializer(project);
        ByteArrayOutputStream baosJson = new ByteArrayOutputStream();
        pwriter.writeToJson(baosJson);
        System.out.println(baosJson.toString());

        TransformProject projectRead = new TransformProject();
        ProjectJsonParser preader = new ProjectJsonParser(projectRead);
        preader.parse(getResourceAsByteArrayInputStream("/json/chameleon_project.json"));
        ChameleonConnector.countFinal = 1;
        new SequenceTransformExecuteJob(projectRead, Arrays.asList(log)).run();
        assertTrue(ChameleonConnector.countFinal == 151);
        System.out.println("Run readed project completed");
    }

    public void testLoadDBProject() throws Exception {
        TransformProject projectRead = new TransformProject();
        ProjectJsonParser preader = new ProjectJsonParser(projectRead);
        preader.parse(getResourceAsByteArrayInputStream("/json/db_project.json"));
        System.out.println("Project Loaded");
    }

    @Test
    public void testEngine() throws Exception {
        transformEngine.start("../dist/lumens/addin");
        TransformProject project = loadProjectFromJson();
        assertEquals(3, project.getDatasourceList().size());
        assertEquals(4, project.getDataTransformerList().size());
        String projectJson = projectSerialize2Json(project);
        assertEquals(projectJson, IOUtils.toString(getResourceAsByteArrayInputStream("/json/soap_db_project.json")));
    }

    public TransformProject loadProjectFromJson() throws Exception {
        TransformProject newProject = new TransformProject();
        ProjectSerializer projSerializer = new ProjectSerializer(newProject);
        projSerializer.readFromJson(getResourceAsByteArrayInputStream("/json/soap_db_project.json"));
        return newProject;
    }

    private String projectSerialize2Json(TransformProject project) throws Exception {
        ByteArrayOutputStream baosXML = new ByteArrayOutputStream();
        ByteArrayOutputStream baosJson = new ByteArrayOutputStream();
        new ProjectSerializer(project).writeToXml(baosXML);
        new ProjectSerializer(project).writeToJson(baosJson);
        System.out.println(baosXML.toString());
        System.out.println(baosJson.toString());
        System.out.println();
        return baosJson.toString();
    }

    public static InputStream getResourceAsByteArrayInputStream(String url) throws IOException {
        try (InputStream in = TransformEngineTest.class.getResourceAsStream(url)) {
            return new ByteArrayInputStream(IOUtils.toByteArray(in));
        }
    }
}
