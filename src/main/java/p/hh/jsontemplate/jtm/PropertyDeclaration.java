package p.hh.jsontemplate.jtm;

import p.hh.jsontemplate.jsoncomposer.JsonBuilder;
import p.hh.jsontemplate.jsoncomposer.JsonNode;
import p.hh.jsontemplate.jsoncomposer.JsonWrapperNode;
import p.hh.jsontemplate.valueproducer.INodeProducer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertyDeclaration {
    protected String valueName;
    protected String typeName;

    protected String singleParam;
    protected List<String> listParam;
    protected Map<String, String> mapParam;
    protected String variableName;
    protected boolean isObject;
    protected List<PropertyDeclaration> properties = new ArrayList<>();
    protected PropertyDeclaration parent;
    protected boolean isArray;
    protected Map<String, String> arrayMapParam;

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        if (typeName != null) {
            this.typeName = typeName;
        }
    }

    public String getSingleParam() {
        return singleParam;
    }

    public void setSingleParam(String singleParam) {
        this.singleParam = singleParam;
    }

    public List<String> getListParam() {
        if (listParam == null) {
            listParam = new ArrayList<>();
        }
        return listParam;
    }

    public void setListParam(List<String> listParam) {
        this.listParam = listParam;
    }

    public Map<String, String> getMapParam() {
        if (mapParam == null) {
            mapParam = new HashMap<>();
        }
        return mapParam;
    }

    public void setMapParam(Map<String, String> mapParam) {
        this.mapParam = mapParam;
    }

    public void addProperty(PropertyDeclaration propertyDeclaration) {
        this.properties.add(propertyDeclaration);
        propertyDeclaration.setParent(this);
    }

    public void removeProperty(PropertyDeclaration propertyDeclaration) {
        this.properties.remove(propertyDeclaration);
        propertyDeclaration.setParent(null);
    }

    public PropertyDeclaration getParent() {
        return parent;
    }

    public void setParent(PropertyDeclaration parent) {
        this.parent = parent;
    }

    public boolean isArray() {
        return isArray;
    }

    public void setAsArray(boolean array) {
        isArray = array;
    }

    public Map<String, String> getArrayMapParam() {
        return arrayMapParam;
    }

    public void setArrayMapParam(Map<String, String> arrayMapParam) {
        this.arrayMapParam = arrayMapParam;
    }

    public boolean isObject() {
        return isObject;
    }

    public void setAsObject(boolean object) {
        isObject = object;
    }

    void visitTypeDeclaration(Map<String, PropertyDeclaration> typeMap) {
        if (isTypeDeclaration()) {
            typeMap.put(this.getTypeName().substring(1), this);
            this.getParent().removeProperty(this);
            this.parent = null;
        }
        for (PropertyDeclaration declaration : properties) {
            declaration.visitTypeDeclaration(typeMap);
        }
    }

    public void buildType(JsonBuilder builder, Map<String, INodeProducer> producerMap, Map<String, JsonNode> typeMap, Map<String, List<JsonWrapperNode>> missTypeMap) {
        if (!isObject && !isArray) { // plain value
            String valueTypeName = findValueType();
            JsonNode jsonNode = buildNodeFromProducer(producerMap, valueTypeName);

            if(jsonNode == null) {
                jsonNode = typeMap.get(valueTypeName);
                if (jsonNode == null) {
                    jsonNode = new JsonWrapperNode();
                    List<JsonWrapperNode> jsonWrapperNodes = missTypeMap.get(valueTypeName);
                    if (jsonWrapperNodes == null) {
                        missTypeMap.put(valueTypeName, Arrays.asList((JsonWrapperNode) jsonNode));
                    } else {
                        jsonWrapperNodes.add((JsonWrapperNode) jsonNode);
                    }
                }
            }

            putOrAddNode(builder, jsonNode);
        } else {
            handleComposite(builder, producerMap, typeMap);
        }
    }

    public void buildJson(JsonBuilder builder, Map<String, INodeProducer> producerMap, Map<String, JsonNode> typeMap) {
        if (!isObject && !isArray) { // plain value
            String valueTypeName = findValueType();
            JsonNode jsonNode = buildNodeFromProducer(producerMap, valueTypeName);

            if(jsonNode == null) {
                jsonNode = typeMap.get(valueTypeName);
                if (jsonNode == null) {
                    throw new IllegalArgumentException("unknown valueTypeName");
                }
            }

            putOrAddNode(builder, jsonNode);
        } else {
            handleComposite(builder, producerMap, typeMap);
        }
    }

    private void putOrAddNode(JsonBuilder builder, JsonNode jsonNode) {
        if (builder.inObject()) {
            builder.putNode(valueName, jsonNode);
        } else if (builder.inArray()) {
            builder.addNode(jsonNode);
        }
    }
    private String findValueType() {
        String valueTypeName = typeName;
        PropertyDeclaration declParent = this.getParent();
        while (valueTypeName == null && declParent != null) {
            valueTypeName = declParent.getTypeName();
            declParent = declParent.getParent();
        }
        if (valueTypeName == null) {
            throw new IllegalArgumentException("type name is null");
        }
        return valueTypeName;
    }

    private JsonNode buildNodeFromProducer(Map<String, INodeProducer> producerMap, String valueTypeName) {
        JsonNode jsonNode = null;
        INodeProducer producer = producerMap.get(valueTypeName);
        if (producer != null) {
            if (singleParam != null) {
                jsonNode = producer.produce(singleParam);
            } else if (listParam != null) {
                jsonNode = producer.produce(listParam);
            } else if (mapParam != null) {
                jsonNode = producer.produce(mapParam);
            } else {
                jsonNode = producer.produce();
            }
        }
        return jsonNode;
    }

    private void handleComposite(JsonBuilder builder, Map<String, INodeProducer> producerMap, Map<String, JsonNode> typeMap) {
        if (parent == null) {
            if (isObject) {
                builder.createObject();
            } else if (isArray) {
                builder.createArray();
            }
        } else {
            if (isObject) {
                if (builder.inObject()) {
                    builder.putObject(valueName);
                } else if (builder.inArray()) {
                    builder.addObject();
                }

            } else if (isArray) {
                if (builder.inObject()) {
                    builder.putArray(valueName);
                } else if (builder.inArray()) {
                    builder.addArray();
                }
            }
        }
        buildChildrenJson(builder, producerMap, typeMap);
        builder.end();
    }

    private void buildChildrenJson(JsonBuilder builder, Map<String, INodeProducer> producerMap, Map<String, JsonNode> typeMap) {
        for (PropertyDeclaration declaration : properties) {
            declaration.buildJson(builder, producerMap, typeMap);
        }
    }

    boolean isTypeDeclaration() {
        return this.getTypeName().startsWith("%");
    }
}
