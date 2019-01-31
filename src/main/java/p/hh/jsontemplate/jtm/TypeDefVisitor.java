package p.hh.jsontemplate.jtm;

import p.hh.jsontemplate.jsoncomposer.JsonBuilder;
import p.hh.jsontemplate.jsoncomposer.JsonNode;
import p.hh.jsontemplate.jsoncomposer.JsonWrapperNode;
import p.hh.jsontemplate.valueproducer.INodeProducer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypeDefVisitor {

    Map<String, PropertyDeclaration> getTypeMap(PropertyDeclaration propertyDeclaration, Map<String, PropertyDeclaration> typeMap) {
        propertyDeclaration.visitTypeDeclaration(typeMap);
        return typeMap;
    }

    void buildTypeMap(Map<String, INodeProducer> producerMap, List<PropertyDeclaration> typeDeclarations) {
        Map<String, JsonNode> typeMap = new HashMap<>();
        Map<String, List<JsonWrapperNode>> missTypeMap = new HashMap<>();
        for (PropertyDeclaration typeDecl : typeDeclarations) {
            JsonBuilder jsonBuilder = new JsonBuilder();
            typeDecl.buildType(jsonBuilder, producerMap, typeMap, missTypeMap);
            JsonNode typeNode = jsonBuilder.build();
            typeMap.put(typeDecl.getTypeName().substring(1), typeNode);
        }
        for (Map.Entry<String, List<JsonWrapperNode>> entry : missTypeMap.entrySet()) {
            JsonNode jsonNode = typeMap.get(entry.getKey());
            for (JsonWrapperNode jsonWrapperNode : entry.getValue()) {
                jsonWrapperNode.setJsonNode(jsonNode);
            }
        }
    }
}
