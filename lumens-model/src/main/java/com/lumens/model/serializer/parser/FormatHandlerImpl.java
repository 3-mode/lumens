package com.lumens.model.serializer.parser;

import com.lumens.model.DataFormat;
import com.lumens.model.Format;
import com.lumens.model.Format.Form;
import com.lumens.model.Type;
import com.lumens.model.Value;
import java.util.LinkedList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 *
 * @author shaofeng wang (shaofeng.cjpw@gmail.com)
 */
public class FormatHandlerImpl implements FormatHandler {

    private final List<Format> formatList;
    private final LinkedList<Format> formatStack = new LinkedList<>();
    private Format currentFormat;

    public FormatHandlerImpl(List<Format> formatList) {
        this.formatList = formatList;
    }

    @Override
    public void start_format_list(final Attributes meta) throws SAXException {
    }

    @Override
    public void end_format_list() throws SAXException {
    }

    @Override
    public void handle_property(final String data, final Attributes meta) throws SAXException {
        Type type = Type.parseString(meta.getValue("type"));
        currentFormat.setProperty(meta.getValue("name"), new Value(type, data));
    }

    @Override
    public void start_format(final Attributes meta) throws SAXException {
        Form form = Form.parseString(meta.getValue("form"));
        Type type = Type.parseString(meta.getValue("type"));
        currentFormat = new DataFormat(meta.getValue("name"), form, type);
        if (!formatStack.isEmpty()) {
            formatStack.getLast().addChild(currentFormat);
        }
        formatStack.add(currentFormat);
    }

    @Override
    public void end_format() throws SAXException {
        currentFormat = formatStack.removeLast();
        if (formatStack.isEmpty()) {
            formatList.add(currentFormat);
        }
        currentFormat = null;
    }
}
