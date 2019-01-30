package p.hh.jsontemplate.jtm;

import p.hh.jsontemplate.jsoncomposer.JsonBuilder;
import p.hh.jsontemplate.valueproducer.IValueProducer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropertyDeclaration {
    protected String valueName;
    protected String typeName;

    protected String singleParam;
    protected List<String> listParam;
    protected Map<String, String> mapParam;

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    protected String variableName;

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

    protected boolean isObject;
    protected List<PropertyDeclaration> properties = new ArrayList<>();

    public void addProperty(PropertyDeclaration propertyDeclaration) {
        this.properties.add(propertyDeclaration);
        propertyDeclaration.setParent(this);
    }

    public void removeProperty(PropertyDeclaration propertyDeclaration) {
        this.properties.remove(propertyDeclaration);
        propertyDeclaration.setParent(null);
    }

    protected PropertyDeclaration parent;

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

    protected boolean isArray;
    protected Map<String, String> arrayMapParam;

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
        }
        for (PropertyDeclaration declaration : properties) {
            declaration.visitTypeDeclaration(typeMap);
        }
    }

    void buildJson(JsonBuilder builder, Map<String, IValueProducer> producerMap, Map<String, PropertyDeclaration> typeMap) {
        if (parent == null) {
            if (isObject) {
                builder.createObject();
                buildChildrenJson(builder);
                builder.end();
            } else if (isArray) {
                builder.createArray();
                buildChildrenJson(builder);
                builder.end();
            }
        } else {
            if (isObject) {
                if (builder.inObject()) {
                    builder.putObject(valueName);
                    buildChildrenJson(builder);
                    builder.end();
                } else if (builder.inArray()) {
                    builder.addObject();
                    buildChildrenJson(builder);
                    builder.end();
                }
            } else if (isArray) {
                if (builder.inObject()) {
                    builder.putArray(valueName);
                    buildChildrenJson(builder);
                    builder.end();
                } else if (builder.inArray()) {
                    builder.addArray();
                    buildChildrenJson(builder);
                    builder.end();
                }
            } else { // plain value
                builder.pu
            }
        }

    }

    private void buildChildrenJson(JsonBuilder builder) {
        for (PropertyDeclaration declaration : properties) {
            declaration.buildJson(builder);
        }
    }

    boolean isTypeDeclaration() {
        return this.getTypeName().startsWith("%");
    }
}
