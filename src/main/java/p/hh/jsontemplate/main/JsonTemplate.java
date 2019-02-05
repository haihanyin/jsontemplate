package p.hh.jsontemplate.main;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import p.hh.jsontemplate.jsoncomposer.JsonBooleanNode;
import p.hh.jsontemplate.jsoncomposer.JsonBuilder;
import p.hh.jsontemplate.jsoncomposer.JsonIntegerNode;
import p.hh.jsontemplate.jsoncomposer.JsonNode;
import p.hh.jsontemplate.jsoncomposer.JsonStringNode;
import p.hh.jsontemplate.jsoncomposer.JsonWrapperNode;
import p.hh.jsontemplate.jtm.JsonTemplateTreeListener;
import p.hh.jsontemplate.jtm.PropertyDeclaration;
import p.hh.jsontemplate.parser.JsonTemplateLexer;
import p.hh.jsontemplate.parser.JsonTemplateParser;
import p.hh.jsontemplate.valueproducer.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonTemplate {

    private String template;
    private Map<String, Object> variableMap = new HashMap<>();
    private Map<String, JsonNode> variableNodeMap = new HashMap<>();
    private Map<String, INodeProducer> producerMap = new HashMap<>();
    private JsonNode builtJsonNode;

    public JsonTemplate() {
        producerMap.put("s", new StringNodeProducer());
        producerMap.put("i", new IntegerNodeProducer());
        producerMap.put("b", new BooleanNodeProducer());
        producerMap.put("ip", new IpNodeProducer());
        producerMap.put("ipv6", new Ipv6NodeProducer());
        producerMap.put("base64", new Base64NodeProducer());
        producerMap.put("raw", new RawJsonNodeProducer());
    }

    public JsonTemplate withTempalte(String template) {
        this.template = template;
        return this;
    }

    public JsonTemplate withVariable(String variableName, Object variable) {
        this.variableMap.put(variableName, variable);
        return this;
    }

    public JsonTemplate withVariables(Map<String, Object> variables) {
        variables.forEach(this::withVariable);
        return this;
    }

    public JsonTemplate withNodeProducer(String name, INodeProducer nodeProducer) {
        this.producerMap.put(name, nodeProducer);
        return this;
    }

    public JsonNode parse(String template) {
        this.template = template;
        return parse();
    }

    public JsonNode parse() {
        if (template == null) {
            throw new IllegalArgumentException("Template is not set.");
        }
        if (builtJsonNode == null) {
            builtJsonNode = buildJsonNode(template);
        }
        return builtJsonNode;
    }

    private JsonNode buildJsonNode(String template) {
        buildVariableNodeMap();

        PropertyDeclaration rootDeclaration = stringToJsonTemplateModel(template);
        Map<String, JsonNode> typeMap = buildTypeMap(rootDeclaration);

        JsonBuilder builder = new JsonBuilder();
        rootDeclaration.buildJson(builder, producerMap, typeMap, Collections.emptyMap(), variableNodeMap);

        return builder.build();
    }

    private void buildVariableNodeMap() {
        variableMap.forEach((key, value) -> this.variableNodeMap.put(key, JsonNode.of(value)));
    }

    private PropertyDeclaration stringToJsonTemplateModel(String template) {
        JsonTemplateLexer jsonTemplateLexer = new JsonTemplateLexer(new ANTLRInputStream(template));
        CommonTokenStream commonTokenStream = new CommonTokenStream(jsonTemplateLexer);
        JsonTemplateParser parser = new JsonTemplateParser(commonTokenStream);

        JsonTemplateTreeListener listener = new JsonTemplateTreeListener();

        ParseTreeWalker parseTreeWalker = new ParseTreeWalker();
        parseTreeWalker.walk(listener, parser.root());
        PropertyDeclaration rootDeclaration = listener.getRoot();
        return rootDeclaration;
    }

    private Map<String, JsonNode> buildTypeMap(PropertyDeclaration rootDeclaration) {
        List<PropertyDeclaration> typeDeclList = new ArrayList<>();
        rootDeclaration.collectTypeDeclaration(typeDeclList);

        for (PropertyDeclaration typeDecl : typeDeclList) {
            typeDecl.getParent().removeProperty(typeDecl);
            typeDecl.setParent(null);
        }

        Map<String, JsonNode> typeMap = buildTypeMap(producerMap, typeDeclList);
        return typeMap;
    }

    private Map<String, JsonNode> buildTypeMap(Map<String, INodeProducer> producerMap, List<PropertyDeclaration> typeDeclarations) {
        Map<String, JsonNode> typeMap = new HashMap<>();
        Map<String, List<JsonWrapperNode>> missTypeMap = new HashMap<>();
        for (PropertyDeclaration typeDecl : typeDeclarations) {
            JsonBuilder jsonBuilder = new JsonBuilder();
            typeDecl.buildType(jsonBuilder, producerMap, typeMap, missTypeMap, variableNodeMap);
            JsonNode typeNode = jsonBuilder.build();
            typeMap.put(typeDecl.getValueName().substring(1), typeNode);
        }
        for (Map.Entry<String, List<JsonWrapperNode>> entry : missTypeMap.entrySet()) {
            JsonNode jsonNode = typeMap.get(entry.getKey());
            for (JsonWrapperNode jsonWrapperNode : entry.getValue()) {
                jsonWrapperNode.setJsonNode(jsonNode);
            }
        }
        return typeMap;
    }

}
