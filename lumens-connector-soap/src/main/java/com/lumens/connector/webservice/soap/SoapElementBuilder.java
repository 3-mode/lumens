/*
 * Copyright Lumens Team, Inc. All Rights Reserved.
 */
package com.lumens.connector.webservice.soap;

import com.lumens.model.DataElement;
import com.lumens.model.Element;
import com.lumens.model.Format;
import com.lumens.model.Type;
import com.lumens.model.Value;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPBody;
import org.apache.axiom.soap.SOAPEnvelope;

/**
 *
 * @author shaofeng wang
 */
public class SoapElementBuilder implements SoapConstants {

    public Element buildElement(Format format, SOAPEnvelope envelope) {
        // TODO need to handle binary attachment
        if (format == null || format.getChildren() == null) {
            return null;
        }
        Element element = new DataElement(format);
        List<Format> children = format.getChildren();
        for (Format message : children) {
            Value isMessage = message.getProperty(SOAPMESSAGE);
            if (isMessage != null && isMessage.getInt() == SOAPMESSAGE_OUT) {
                Element messageElement = element.addChild(message.getName());
                String namespace = message.getProperty(TARGETNAMESPACE).
                getString();
                SOAPBody body = envelope.getBody();
                OMElement omElem = body.getFirstElement();
                if (omElem != null) {
                    QName qName = omElem.getQName();
                    if (qName.getLocalPart().equals(message.getName())
                        && qName.getNamespaceURI().equals(namespace)) {
                        buildElementFromOMElement(messageElement, omElem);
                    }
                }
                break;
            }
        }
        return element;
    }

    private void buildElementFromOMElement(Element element, OMElement omElem) {
        List<Format> children = element.getFormat().getChildren();
        if (children != null) {
            for (Format child : children) {
                String namespace = child.getProperty(TARGETNAMESPACE).getString();
                Iterator<OMElement> it = omElem.getChildrenWithName(new QName(namespace, child.
                getName()));
                if (it != null && it.hasNext()) {
                    Element childElement = element.newChild(child);
                    while (it.hasNext()) {
                        OMElement omChild = it.next();
                        if (child.isArray()) {
                            Element arrayItem = childElement.newArrayItem();
                            if (child.getType() != Type.NONE && arrayItem.isArrayItem()) {
                                arrayItem.setValue(omChild.getText());
                            }
                            buildElementFromOMElement(arrayItem, omChild);
                            if (arrayItem.getChildren() != null || arrayItem.isField()) {
                                childElement.addArrayItem(arrayItem);
                            }
                        } else {
                            if (child.getType() != Type.NONE) {
                                childElement.setValue(omChild.getText());
                            }
                            buildElementFromOMElement(childElement, omChild);
                        }
                    }
                    if (childElement.getChildren() != null || childElement.isField()) {
                        element.addChild(childElement);
                    }
                }
            }
        }
    }
}
