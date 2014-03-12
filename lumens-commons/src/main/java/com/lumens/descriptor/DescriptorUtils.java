/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.descriptor;

import com.lumens.LumensException;
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
public class DescriptorUtils {

    public static String DESCRIPTOR = "descriptor";
    public static String TYPE_PROPERTY = "type";
    public static String ID_PROPERTY = "id";
    public static String NAME_PROPERTY = "name";
    public static String CLASS_NAME_PROPERTY = "class_name";
    public static String INSTANCE_ICON_PROPERTY = "instance_icon";
    public static String ITEM_ICON_PROPERTY = "item_icon";
    public static String PROPS_PROPERTY = "property";
    public static String I18N_PROPERTY = "i18n";
    public static String HTML_PROPERTY = "html";

    private static InputStream getResource(Class clazz, String configRoot, String path) {
        return clazz.getClassLoader().getResourceAsStream(configRoot + "/" + path);
    }

    public static JsonNode discoverDescriptor(Class clazz, String configRoot) {
        try (InputStream in = clazz.getClassLoader().getResourceAsStream(configRoot + "/descriptor.json")) {
            return JsonUtility.createJson(IOUtils.toString(in));
        } catch (IOException ex) {
            throw new LumensException(ex);
        }
    }

    public static Map<String, Object> processDescriptor(Class clazz, String configRoot, String resourceID) {
        Map<String, Object> props = new HashMap<>();
        JsonUtility utility = JsonUtility.createJsonUtility();
        JsonGenerator generator = utility.getGenerator();
        JsonNode descJson = null;
        try {
            generator.writeStartObject();
            {
                // TODO need to check if the property exists
                descJson = discoverDescriptor(clazz, configRoot);
                descJson = descJson.get(resourceID);
                if (JsonUtility.isNull(descJson))
                    throw new LumensException("Descriptor file is invalid, the resource id is not matched!");
                generator.writeStringField(ID_PROPERTY, resourceID);
                JsonNode i18nJson = null;
                try (InputStream in = getResource(clazz, configRoot, descJson.get(I18N_PROPERTY).asText())) {
                    i18nJson = JsonUtility.createJson(IOUtils.toString(in));
                } catch (IOException ex) {
                    throw new LumensException(ex);
                }
                generator.writeStringField(NAME_PROPERTY, i18nJson.get(descJson.get(NAME_PROPERTY).asText()).asText());
                generator.writeStringField(TYPE_PROPERTY, descJson.get(TYPE_PROPERTY).asText());
                generator.writeObjectField(PROPS_PROPERTY, descJson.get(PROPS_PROPERTY));
                generator.writeObjectField(I18N_PROPERTY, i18nJson);
                try (InputStream in = getResource(clazz, configRoot, descJson.get(HTML_PROPERTY).asText())) {
                    generator.writeStringField(HTML_PROPERTY, IOUtils.toString(in));
                } catch (IOException ex) {
                    throw new LumensException(ex);
                }
                try (InputStream in = getResource(clazz, configRoot, descJson.get(ITEM_ICON_PROPERTY).asText())) {
                    generator.writeBinaryField(ITEM_ICON_PROPERTY, IOUtils.toByteArray(in));
                } catch (IOException ex) {
                    throw new LumensException(ex);
                }
                try (InputStream in = getResource(clazz, configRoot, descJson.get(INSTANCE_ICON_PROPERTY).asText())) {
                    generator.writeBinaryField(INSTANCE_ICON_PROPERTY, IOUtils.toByteArray(in));
                } catch (IOException ex) {
                    throw new LumensException(ex);
                }
            }
            generator.writeEndObject();
            props.put(DESCRIPTOR, JsonUtility.createJson(utility.toUTF8String()));
        } catch (IOException ex) {
            throw new LumensException(ex);
        }
        return props;
    }
}
