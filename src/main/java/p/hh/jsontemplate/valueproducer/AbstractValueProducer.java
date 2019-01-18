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
import java.util.stream.Collectors;

public abstract class AbstractValueProducer<T> implements IValueProducer<T> {

    protected T fixedValue;
    protected List valueChoiceList;

    AbstractValueProducer(String value) {
        try {
            Field field = this.getClass().getSuperclass().getDeclaredField("fixedValue");
            Type fieldType = getTypeArgument();
            if (Integer.class.equals(fieldType) || int.class.equals(fieldType)) {
                field.setInt(this, Integer.parseInt(value));
            } else if (Float.class.equals(fieldType) || float.class.equals(fieldType)) {
                field.setFloat(this, Float.parseFloat(value));
            } else if (Boolean.class.equals(fieldType) || boolean.class.equals(fieldType)) {
                field.setBoolean(this, Boolean.parseBoolean(value));
            } else if (String.class.equals(fieldType)) {
                field.set(this, value);
            } else {
                throw new UnsupportedOperationException("Param value " + value + " cannot be parsed to type " + fieldType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    AbstractValueProducer(List<String> valueChoices) {
        try {
            Type fieldType = getTypeArgument();
            if (Integer.class.equals(fieldType) || int.class.equals(fieldType)) {
                this.valueChoiceList = valueChoices.stream().map(Integer::parseInt).collect(Collectors.toList());
            } else if (Float.class.equals(fieldType) || float.class.equals(fieldType)) {
                this.valueChoiceList = valueChoices.stream().map(Float::parseFloat).collect(Collectors.toList());
            } else if (Boolean.class.equals(fieldType) || boolean.class.equals(fieldType)) {
                this.valueChoiceList = valueChoices.stream().map(Boolean::parseBoolean).collect(Collectors.toList());
            } else if (String.class.equals(fieldType)) {
                this.valueChoiceList = valueChoices;
            } else {
                throw new UnsupportedOperationException("Unsupported type " + fieldType.getTypeName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    AbstractValueProducer(Map<String, String> parameterMap) {

    }

    @Override
    public T produce() {
        if (this.fixedValue != null) {
            return fixedValue;
        } else if (this.valueChoiceList != null) {
            Random random = new Random();
            int index = random.nextInt(this.valueChoiceList.size());
            return (T) this.valueChoiceList.get(index);
        } else {
            return produceWithParameters();
        }
    }

    protected abstract T produceWithParameters();

    protected void mapToFields(Map<String, String> parameterMap) {
        List<Field> fields = Arrays.stream(this.getClass().getDeclaredFields())
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .collect(Collectors.toList());
        for(Field field : fields) {
            field.setAccessible(true);
            String paramValue = parameterMap.get(field.getName());
            if (paramValue != null) {
                Class fieldType = field.getType();
                try {
                    if (Integer.class.equals(fieldType) || int.class.equals(fieldType)) {
                        field.setInt(this, Integer.parseInt(paramValue));
                    } else if (Float.class.equals(fieldType) || float.class.equals(fieldType)) {
                        field.setFloat(this, Float.parseFloat(paramValue));
                    } else if (Boolean.class.equals(fieldType) || boolean.class.equals(fieldType)) {
                        field.setBoolean(this, Boolean.parseBoolean(paramValue));
                    } else if (String.class.equals(fieldType)) {
                        field.set(this, paramValue);
                    } else {
                        throw new UnsupportedOperationException("Param value " + paramValue + " cannot be parsed to type " + fieldType);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private Type getTypeArgument() {
        return ((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
