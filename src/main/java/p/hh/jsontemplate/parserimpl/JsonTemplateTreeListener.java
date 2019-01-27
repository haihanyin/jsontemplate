package p.hh.jsontemplate.parserimpl;

import p.hh.jsontemplate.jsoncomposer.JsonBuilder;
import p.hh.jsontemplate.parser.JsonTemplateBaseListener;
import p.hh.jsontemplate.parser.JsonTemplateBaseVisitor;
import p.hh.jsontemplate.parser.JsonTemplateParser;
import p.hh.jsontemplate.valueproducer.BooleanValueProducer;
import p.hh.jsontemplate.valueproducer.FloatValueProducer;
import p.hh.jsontemplate.valueproducer.IValueProducer;
import p.hh.jsontemplate.valueproducer.IntegerValueProducer;
import p.hh.jsontemplate.valueproducer.StringValueProducer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class JsonTemplateTreeListener extends JsonTemplateBaseListener {

    private Map<String, IValueProducer> valueProducerMap = new HashMap<>();
    private String currentPropertyName;
    private IValueProducer currentValueProducer;

    public JsonBuilder getJsonBuilder() {
        return jsonBuilder;
    }

    private JsonBuilder jsonBuilder;


    public JsonTemplateTreeListener() {
        valueProducerMap.put("s", new StringValueProducer());
        valueProducerMap.put("i", new IntegerValueProducer());
        valueProducerMap.put("b", new BooleanValueProducer());
    }

    @Override
    public void enterJsonObject(JsonTemplateParser.JsonObjectContext ctx) {
        if (jsonBuilder == null) {
            jsonBuilder = new JsonBuilder();
            jsonBuilder.createObject();
        } else {
            jsonBuilder.putObject(currentPropertyName);
        }
    }

    @Override
    public void exitJsonObject(JsonTemplateParser.JsonObjectContext ctx) {
        jsonBuilder.end();
    }

    @Override
    public void enterJsonArray(JsonTemplateParser.JsonArrayContext ctx) {
        if (jsonBuilder == null) {
            jsonBuilder = new JsonBuilder();
            jsonBuilder.createArray();
        } else {
            jsonBuilder.putArray(currentPropertyName);
        }
    }

    @Override
    public void exitPropertyName(JsonTemplateParser.PropertyNameContext ctx) {
        this.currentPropertyName = ctx.getText();
        System.out.println("propertyName=" + this.currentPropertyName);
    }

    @Override
    public void exitTypeName(JsonTemplateParser.TypeNameContext ctx) {
        String typeName = ctx.getText();
        currentValueProducer = valueProducerMap.get(typeName);
        Class valueType = currentValueProducer.getValueType();
        if (valueType.equals(Integer.class)) {
            Supplier<Integer> supplier = () -> (Integer) currentValueProducer.produce(Collections.emptyMap());
            if (jsonBuilder.inObject()) {
                jsonBuilder.putInteger(currentPropertyName, supplier);
            } else {
                jsonBuilder.addInteger(supplier);
            }
        } else if (valueType.equals(Boolean.class)) {
            Supplier<Boolean> supplier = () -> {
                return (Boolean) currentValueProducer.produce(Collections.emptyMap());
            };
            if (jsonBuilder.inObject()) {
                jsonBuilder.putBoolean(currentPropertyName, supplier);
            } else {
                jsonBuilder.addBoolean(supplier);
            }
        } else if (valueType.equals(String.class)) {
            Supplier<String> supplier = () -> (String) currentValueProducer.produce(Collections.emptyMap());
            if (jsonBuilder.inObject()) {
                jsonBuilder.putString(currentPropertyName, supplier);
            } else {
                jsonBuilder.addString(supplier);
            }
        }
    }

}
