package com.xiaojiezhu.bigsql.sharding.rule.masterslave;

import com.xiaojiezhu.bigsql.util.Asserts;
import com.xiaojiezhu.bigsql.util.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaojie.zhu
 */
public class DefaultMasterSlaveRule implements MasterSlaveRule {
    protected String strategyName;
    protected MasterSlaveDatasource masterSlaveDatasource;

    public DefaultMasterSlaveRule() {
    }

    public DefaultMasterSlaveRule(String strategyName, MasterSlaveDatasource masterSlaveDatasource) {
        this.strategyName = strategyName;
        this.masterSlaveDatasource = masterSlaveDatasource;
    }

    public static DefaultMasterSlaveRule create(File file) throws Exception {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(file);


        Node strategyNode = XmlUtil.findSingleNodeByTagName(document,STRATEGY_TAG_NAME,STRATEGY_TAG_NAME + " parse fail , " + file.getAbsolutePath());
        Node masterSlaveNode = XmlUtil.findSingleNodeByTagName(document,MASTER_SLAVE_TAG_NAME,MASTER_SLAVE_TAG_NAME + " parse fail , " + file.getAbsolutePath());
        Node propertiesNode = XmlUtil.findSingleChildNodeByTagName(masterSlaveNode, PROPERTIES_TAG_NAME, PROPERTIES_TAG_NAME + " not found : " + file.getAbsolutePath());

        Map<String, Object> map = XmlUtil.readMap(propertiesNode, file);


        String master = (String)map.get("master");
        Asserts.notEmpty(master , "not found master node");
        String slave = (String)map.get("slave");
        Asserts.notEmpty(slave , "not found slave node");
        MasterSlaveDatasource masterSlaveDatasource = new MasterSlaveDatasource(Arrays.asList(master), Arrays.asList(slave));
        DefaultMasterSlaveRule rule = new DefaultMasterSlaveRule(strategyNode.getTextContent() , masterSlaveDatasource);
        return rule;
    }

    @Override
    public MasterSlaveDatasource getMasterSlaveDatasource() {
        return this.masterSlaveDatasource;
    }

    @Override
    public String getStrategyName() {
        return this.strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }

    public void setMasterSlaveDatasource(MasterSlaveDatasource masterSlaveDatasource) {
        this.masterSlaveDatasource = masterSlaveDatasource;
    }

    @Override
    public String toString() {
        return "DefaultMasterSlaveRule{" +
                "strategyName='" + strategyName + '\'' +
                ", masterSlaveDatasource=" + masterSlaveDatasource +
                '}';
    }
}
