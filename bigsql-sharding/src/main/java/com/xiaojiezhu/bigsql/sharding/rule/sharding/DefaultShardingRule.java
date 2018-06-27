package com.xiaojiezhu.bigsql.sharding.rule.sharding;

import com.xiaojiezhu.bigsql.util.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaojie.zhu
 */
public class DefaultShardingRule implements ShardingRule {
    protected String strategyName;
    protected Map<String,Object> properties;

    public DefaultShardingRule(String strategyName, Map<String, Object> properties) {
        this.strategyName = strategyName;
        this.properties = properties;
    }

    @Override
    public Map<String, Object> getProperties() {
        return properties;
    }

    @Override
    public String getStrategyName() {
        return strategyName;
    }

    public static DefaultShardingRule create(File file) throws Exception {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(file);
        Node strategyNode = XmlUtil.findSingleNodeByTagName(document,STRATEGY_TAG_NAME,STRATEGY_TAG_NAME + " parse fail , " + file.getAbsolutePath());
        String strategyName = strategyNode.getTextContent();

        Node propertiesNode = XmlUtil.findSingleNodeByTagName(document,PROPERTIES_TAG_NAME,PROPERTIES_TAG_NAME + " parse fail , " + file.getAbsolutePath());

        Map<String,Object> properties = XmlUtil.readMap(propertiesNode , file);
        DefaultShardingRule shardingRule = new DefaultShardingRule(strategyName, properties);
        return shardingRule;
    }


    @Override
    public String toString() {
        return strategyName + " : " + properties;
    }
}
