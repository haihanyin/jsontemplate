package p.hh.jsontemplate.valueproducer;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.TypeUtils;

import java.lang.reflect.Field;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractValueProducer<T> implements IValueProducer<T> {

    protected T fixedValue;
    protected List<T> valueChoiceList;
    protected Map<String, String> parameterMap;

    AbstractValueProducer(String value) {
        this.fixedValue = parseValue();
    }

    AbstractValueProducer(List<String> valueChoices) {
        this.valueChoiceList = valueChoices.stream().map(this::parseValue).collect(Collectors.toList());
    }

    AbstractValueProducer(Map<String, String> parameterMap) {
        this.parameterMap = parameterMap;
        mapToFields();
    }

    @Override
    public T produce() {
        if (this.fixedValue != null) {
            return fixedValue;
        } else if (this.valueChoiceList != null) {
            int i = RandomUtils.nextInt(0, this.valueChoiceList.size());
            return this.valueChoiceList.get(i);
        } else {
            produceWithParameters();
        }
    }

    abstract T parseValue(String value);
    abstract void mapToFields();
    abstract T produceWithParameters();

    protected Integer getInteger(String fieldName, Integer defaultValue) {
        String paramValue = this.parameterMap.get(fieldName);
        return paramValue != null ? Integer.parseInt(paramValue) : defaultValue;
    }

    protected Float getFloat(String fieldName, Float defaultValue) {
        String paramValue = this.parameterMap.get(fieldName);
        return paramValue != null ? Float.parseFloat(paramValue) : defaultValue;
    }

    protected Boolean getBoolean(String fieldName, Boolean defaultValue) {
        String paramValue = this.parameterMap.get(fieldName);
        return paramValue != null ? Boolean.parseBoolean(paramValue) : defaultValue;
    }

    protected Character getCharacter(String fieldName, Character defaultValue) {
        String paramValue = this.parameterMap.get(fieldName);
        return paramValue != null ? paramValue.charAt(0) : defaultValue;
    }

    protected String getString(String fieldName, String defaultValue) {
        String paramValue = this.parameterMap.get(fieldName);
        return paramValue != null ? paramValue : defaultValue;
    }

}
