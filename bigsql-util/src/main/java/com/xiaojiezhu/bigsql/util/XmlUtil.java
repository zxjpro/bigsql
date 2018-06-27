package com.xiaojiezhu.bigsql.util;


import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.management.modelmbean.XMLParseException;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaojie.zhu
 */
public class XmlUtil {
    public static final String PROPERTY_TAG_NAME = "property";
    public static final String KEY_TAG_NAME = "key";
    public static final String VALUE_TAG_NAME = "value";

    public static Node findSingleNodeByTagName(Document document,String tagName,String errMsg) throws XMLParseException {
        NodeList nodeList = document.getElementsByTagName(tagName);
        if(nodeList.getLength() != 1){
            throw new XMLParseException(errMsg);
        }else{
            return nodeList.item(0);
        }
    }

    public static Node findSingleChildNodeByTagName(Node node,String tagName,String errMsg)throws XMLParseException{
        NodeList childNodes = node.getChildNodes();
        if(childNodes.getLength() < 1){
            throw new XMLParseException(errMsg);
        }else{
            for(int i = 0 ; i < childNodes.getLength() ; i++){
                Node childNode = childNodes.item(i);
                if(childNode.getNodeName().equals(tagName)){
                    return childNode;
                }
            }
            throw new XMLParseException(errMsg + "\n" + tagName + "not found");
        }
    }

    public static Map<String,Object> readMap(Node propertiesNode, File file) throws XMLParseException {
        Map<String,Object> properties = new HashMap<>();
        NodeList propertyNodes = propertiesNode.getChildNodes();
        for(int i = 0 ; i < propertyNodes.getLength() ; i ++){
            Node propertyNode = propertyNodes.item(i);
            if(PROPERTY_TAG_NAME.equals(propertyNode.getNodeName())){
                Node keyNode = XmlUtil.findSingleChildNodeByTagName(propertyNode, KEY_TAG_NAME, KEY_TAG_NAME + " not found : " + file.getAbsolutePath());
                Node valueNode = XmlUtil.findSingleChildNodeByTagName(propertyNode, VALUE_TAG_NAME, VALUE_TAG_NAME + " not found : " + file.getAbsolutePath());
                properties.put(keyNode.getTextContent(),valueNode.getTextContent());
            }
        }
        return properties;
    }
}
