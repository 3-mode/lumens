/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lumens.server;

import com.lumens.engine.TransformProject;
import com.lumens.engine.serializer.ProjectSerializer;
import com.lumens.server.sql.DAOFactory;
import com.lumens.server.sql.dao.ProjectDAO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author washaofe
 */
public class ServerTest {

    public ServerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    public InputStream getResourceAsByteArrayInputStream(String url) throws IOException {
        try (InputStream in = ServerTest.class.getResourceAsStream(url)) {
            return new ByteArrayInputStream(IOUtils.toByteArray(in));
        }
    }

    public void testDatabase() throws Exception {
        ProjectDAO pDAO = DAOFactory.getProjectDAO();
        System.out.println("size of short project: " + pDAO.getAllShortProject().size());
        System.out.println("size of project: " + pDAO.getAllProject().size());
        TransformProject project = new TransformProject();
        new ProjectSerializer(project).readFromJson(getResourceAsByteArrayInputStream("/json/oneline_project.json"));
        //pDAO.create(new Project(ServerUtils.generateID("P"), project.getName(), project.getDescription(), IOUtils.toString(getResourceAsByteArrayInputStream("/json/oneline_project.json"))));
        System.out.println("total of project: " + pDAO.getTotal());
        //Project p = pDAO.getProject("P-f061f65c-c017-466c-bb24-c421c31a00e9");
        //System.out.println(String.format("Found the project: id=%s, name=%s", p.Id, p.name));
        //System.out.println(p.data.replace("\"", "\\\""));
    }
}