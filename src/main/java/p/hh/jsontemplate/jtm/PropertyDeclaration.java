package p.hh.jsontemplate.jtm;

import p.hh.jsontemplate.jsoncomposer.JsonArrayNode;
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

    protected String arraySingleParam;
    protected List<String> arrayListParam;
    protected Map<String, String> arrayMapParam;

    public List<PropertyDeclaration> getProperties() {
        return properties;
    }

    public String getArraySingleParam() {
        return arraySingleParam;
    }

    public void setArraySingleParam(String arraySingleParam) {
        this.arraySingleParam = arraySingleParam;
    }

    public List<String> getArrayListParam() {
        if (arrayListParam == null) {
            arrayListParam = new ArrayList<>();
        }
        return arrayListParam;
    }

    public void setArrayListParam(List<String> arrayListParam) {
        this.arrayListParam = arrayListParam;
    }

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
        if (arrayMapParam == null) {
            arrayMapParam = new HashMap<>();
        }
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

    public void collectTypeDeclaration(List<PropertyDeclaration> typeList) {
        if (isTypeDeclaration()) {
            typeList.add(this);
        }
        for (PropertyDeclaration declaration : properties) {
            declaration.collectTypeDeclaration(typeList);
        }
    }

    public void buildType(JsonBuilder builder, Map<String, INodeProducer> producerMap, Map<String, JsonNode> typeMap, Map<String, List<JsonWrapperNode>> missTypeMap, Map<String, JsonNode> variableMap) {
        if (!isObject && !isArray) { // plain value
            JsonNode jsonNode = null;
            if (variableName != null) {
                jsonNode = variableMap.get(variableName);
            } else {
                String valueTypeName = findValueType();
                jsonNode = buildNodeFromProducer(producerMap, valueTypeName);

                if (jsonNode == null) {
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
            }

            putOrAddNode(builder, jsonNode);
        } else {
            handleComposite(builder, producerMap, typeMap, variableMap);
        }
    }

    public void buildJson(JsonBuilder builder, Map<String, INodeProducer> producerMap, Map<String, JsonNode> typeMap, Map<String, JsonNode> variableMap) {
        // TODO: build defaultTypeNode for every type
        if (!isObject && !isArray) { // plain value
            JsonNode jsonNode = null;
            if (variableName != null) {
                jsonNode = variableMap.get(variableName);
            } else {
                String valueTypeName = findValueType();
                jsonNode = buildNodeFromProducer(producerMap, valueTypeName);

                if (jsonNode == null) {
                    jsonNode = typeMap.get(valueTypeName);
                }
            }
            if (jsonNode == null) {
                throw new IllegalArgumentException("unknown valueTypeName");
            }
            putOrAddNode(builder, jsonNode);
        } else {
            handleComposite(builder, producerMap, typeMap, variableMap);
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
            valueTypeName = "s"; // todo improve, temporary solution for array default type
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

    private void handleComposite(JsonBuilder builder, Map<String, INodeProducer> producerMap, Map<String, JsonNode> typeMap, Map<String, JsonNode> variableMap) {
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
        buildChildrenJson(builder, producerMap, typeMap, variableMap);
        if (isArray) {
            String valueTypeName = findValueType();
            if (valueTypeName != null) {
                JsonNode jsonNode = buildNodeFromProducer(producerMap, valueTypeName);

                if (jsonNode == null) {
                    jsonNode = typeMap.get(valueTypeName);
                }
                setArrayInfo(builder.peekArrayNode(), jsonNode);
            }
        }
        builder.end();
    }

    private void setArrayInfo(JsonArrayNode jsonArrayNode, JsonNode defaultNode) {
        jsonArrayNode.setDefaultNode(defaultNode);
        if (this.arraySingleParam != null) {
            jsonArrayNode.setParameters(this.arraySingleParam);
        }
        if (this.arrayListParam != null) {
            jsonArrayNode.setParameters(this.arrayListParam);
        }
        if (this.arrayMapParam != null) {
            jsonArrayNode.setParameters(this.arrayMapParam);
        }
    }

    private void buildChildrenJson(JsonBuilder builder, Map<String, INodeProducer> producerMap, Map<String, JsonNode> typeMap, Map<String, JsonNode> variableMap) {
        for (PropertyDeclaration declaration : properties) {
            declaration.buildJson(builder, producerMap, typeMap, variableMap);
        }
    }

    boolean isTypeDeclaration() {
        return valueName != null && valueName.startsWith("%");
    }
}
