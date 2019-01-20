package p.hh.jsontemplate.valueproducer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class AbstractValueProducer<T> implements IValueProducer<T> {

    @Override
    public T produce(String value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T produce(List<String> valueList) {
        int index = new Random().nextInt(valueList.size());
        return this.produce(valueList.get(index));
    }

    @Override
    public T produce(Map<String, String> paramMap) {
        throw new UnsupportedOperationException();
    }

    protected Integer getIntegerParam(Map<String, String> paramMap, String paramName) {
        return parseParamValue(paramMap, paramName, Integer::parseInt);
    }

    protected Integer getIntegerParam(Map<String, String> paramMap, String paramName, int defaultValue) {
        return defaultIfNull(getIntegerParam(paramMap, paramName), defaultValue);
    }

    protected Float getFloatParam(Map<String, String> paramMap, String paramName) {
        return parseParamValue(paramMap, paramName, Float::parseFloat);
    }

    protected Float getFloatParam(Map<String, String> paramMap, String paramName, float defaultValue) {
        return defaultIfNull(getFloatParam(paramMap, paramName), defaultValue);
    }

    protected Boolean getBooleanParam(Map<String, String> paramMap, String paramName) {
        return parseParamValue(paramMap, paramName, Boolean::parseBoolean);
    }

    protected Boolean getBooleanParam(Map<String, String> paramMap, String paramName, boolean defaultValue) {
        return defaultIfNull(getBooleanParam(paramMap, paramName), defaultValue);
    }

    protected String getStringParam(Map<String, String> paramMap, String paramName) {
        return paramMap.get(paramName);
    }

    protected String getStringParam(Map<String, String> paramMap, String paramName, String defaultValue) {
        return defaultIfNull(getStringParam(paramMap, paramName), defaultValue);
    }

    protected Type getTypeArgument() {
        return ((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected  <R> R parseParamValue(Map<String, String> paramMap, String paramName, Function<String, R> parser) {
        String paramValue = paramMap.get(paramName);
        if (paramValue != null) {
            try {
                return parser.apply(paramValue);
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }

    protected  <R> R defaultIfNull(R object, R defaultValue) {
        return object != null ? object : defaultValue;
    }
}
