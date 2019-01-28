package p.hh.jsontemplate.parserimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValueDeclaration {

    private String valueName;

    private String typeName;
    private String singleParam;
    private List<String> listParam;
    private Map<String, String> mapParam;

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
        return this.listParam;
    }

    public void addListParam(String param) {
        if (listParam == null) {
            listParam = new ArrayList<>();
        }
        listParam.add(param);
    }

    public Map<String, String> getMapParam() {
        return this.mapParam;
    }

    public void putMapParam(String key, String value) {
        if (mapParam == null) {
            mapParam = new HashMap<>();
        }
        mapParam.put(key, value);
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public String getValueName() {
        return valueName;
    }
}
