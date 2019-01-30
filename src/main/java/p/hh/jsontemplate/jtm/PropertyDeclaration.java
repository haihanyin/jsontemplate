package p.hh.jsontemplate.jtm;

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
        this.typeName = typeName;
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
}
