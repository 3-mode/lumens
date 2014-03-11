/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.addin;

import com.lumens.LumensException;
import com.lumens.connector.ConnectorFactory;
import com.lumens.io.JsonUtility;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;

/**
 *
 * @author Shaofeng Wang <shaofeng.wang@outlook.com>
 */
public abstract class AbstractActivator implements AddinActivator {

    protected Map<String, Object> processDescriptor(Class clazz) {
        Map<String, Object> props = new HashMap<>();
        JsonUtility utility = JsonUtility.createJsonUtility();
        JsonGenerator generator = utility.getGenerator();
        JsonNode descJson = null;
        try {
            generator.writeStartObject();
            {
                // TODO need to check if the property exists
                try (InputStream in = clazz.getClassLoader().getResourceAsStream("conf/descriptor.json")) {
                    descJson = JsonUtility.createJson(IOUtils.toString(in));
                    generator.writeStringField(ConnectorFactory.ID_PROPERTY, descJson.get(ConnectorFactory.ID_PROPERTY).asText());
                    generator.writeStringField(ConnectorFactory.TYPE_PROPERTY, descJson.get(ConnectorFactory.TYPE_PROPERTY).asText());
                    generator.writeStringField(ConnectorFactory.CLASS_NAME_PROPERTY, descJson.get(ConnectorFactory.CLASS_NAME_PROPERTY).asText());
                } catch (IOException ex) {
                    throw new LumensException(ex);
                }
                JsonNode i18nJson = null;
                try (InputStream in = clazz.getClassLoader().getResourceAsStream("conf/" + descJson.get(ConnectorFactory.I18N_PROPERTY).asText())) {
                    String i18nJsonStr = IOUtils.toString(in);
                    i18nJson = JsonUtility.createJson(i18nJsonStr);
                    generator.writeStringField(ConnectorFactory.NAME_PROPERTY, i18nJson.get(descJson.get(ConnectorFactory.NAME_PROPERTY).asText()).asText());
                    generator.writeObjectField(ConnectorFactory.I18N_PROPERTY, i18nJson);
                } catch (IOException ex) {
                    throw new LumensException(ex);
                }
                try (InputStream in = clazz.getClassLoader().getResourceAsStream("conf/" + descJson.get(ConnectorFactory.ITEM_ICON_PROPERTY).asText())) {
                    generator.writeBinaryField(ConnectorFactory.ITEM_ICON_PROPERTY, IOUtils.toByteArray(in));
                } catch (IOException ex) {
                    throw new LumensException(ex);
                }
                try (InputStream in = clazz.getClassLoader().getResourceAsStream("conf/" + descJson.get(ConnectorFactory.INSTANCE_ICON_PROPERTY).asText())) {
                    generator.writeBinaryField(ConnectorFactory.INSTANCE_ICON_PROPERTY, IOUtils.toByteArray(in));
                } catch (IOException ex) {
                    throw new LumensException(ex);
                }
                try (InputStream in = clazz.getClassLoader().getResourceAsStream("conf/" + descJson.get(ConnectorFactory.PROPS_PROPERTY).asText())) {
                    generator.writeStringField(ConnectorFactory.PROPS_PROPERTY, IOUtils.toString(in));
                } catch (IOException ex) {
                    throw new LumensException(ex);
                }
            }
            generator.writeEndObject();
            props.put(ConnectorFactory.DESCRIPTOR, utility.toUTF8String());
        } catch (IOException ex) {
            throw new LumensException(ex);
        }
        return props;
    }
}
