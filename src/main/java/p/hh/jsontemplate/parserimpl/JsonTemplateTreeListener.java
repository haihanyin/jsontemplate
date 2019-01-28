package p.hh.jsontemplate.parserimpl;

import org.antlr.v4.runtime.tree.ParseTree;
import p.hh.jsontemplate.jsoncomposer.JsonBuilder;
import p.hh.jsontemplate.jsoncomposer.JsonNode;
import p.hh.jsontemplate.jsoncomposer.JsonWrapperNode;
import p.hh.jsontemplate.parser.JsonTemplateBaseListener;
import p.hh.jsontemplate.parser.JsonTemplateBaseVisitor;
import p.hh.jsontemplate.parser.JsonTemplateParser;
import p.hh.jsontemplate.valueproducer.BooleanValueProducer;
import p.hh.jsontemplate.valueproducer.FloatValueProducer;
import p.hh.jsontemplate.valueproducer.IValueProducer;
import p.hh.jsontemplate.valueproducer.IntegerValueProducer;
import p.hh.jsontemplate.valueproducer.StringValueProducer;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class JsonTemplateTreeListener extends JsonTemplateBaseListener {

    private boolean inTypeDef;
    private String typeDefName;
    private Map<String, JsonNode> typeMap = new HashMap<>();
    private Map<String, List<JsonWrapperNode>> typeMissMap = new HashMap<>();
    private Map<String, IValueProducer> valueProducerMap = new HashMap<>();
    private JsonBuilder jsonBuilder;
    private String currentPropertyName;
    private IValueProducer currentValueProducer;

    @Override
    public void enterPropertyNameSpec(JsonTemplateParser.PropertyNameSpecContext ctx) {
        ParseTree child = ctx.getChild(0);
        if (child instanceof JsonTemplateParser.TypeInfoContext) {
            if (inTypeDef) {
                throw new IllegalStateException("Nested type definition is not allowed [" + child.getText() + "]");
            } else {
                inTypeDef = true;
                jsonBuilder = new JsonBuilder();
            }
        }
    }

    @Override
    public void enterTypeName(JsonTemplateParser.TypeNameContext ctx) {
        if (inTypeDef) {
            typeDefName = ctx.getText();
        }
    }

    @Override
    public void exitPropertyNameSpec(JsonTemplateParser.PropertyNameSpecContext ctx) {
        if (inTypeDef) {
            JsonNode jsonNode = jsonBuilder.end().build();

            List<JsonWrapperNode> missTypeNodes = typeMissMap.get(typeDefName);
            if (missTypeNodes != null) {
                missTypeNodes.stream().forEach(wrapperNode -> wrapperNode.setJsonNode(jsonNode));
                typeMissMap.remove(typeDefName);
            }
            typeMap.put(typeDefName, jsonNode);
            inTypeDef = false;
            typeDefName = null;
            jsonBuilder = null;
        }
    }

    @Override
    public void enterJsonObject(JsonTemplateParser.JsonObjectContext ctx) {
        if (inTypeDef) {
            if (jsonBuilder == null) {
                jsonBuilder = new JsonBuilder();
                jsonBuilder.createObject();
            } else {
                jsonBuilder.putObject(currentPropertyName);
            }
        }
    }

    @Override
    public void exitJsonObject(JsonTemplateParser.JsonObjectContext ctx) {
        if (inTypeDef) {
            jsonBuilder.end();
        }
    }

    @Override
    public void enterJsonArray(JsonTemplateParser.JsonArrayContext ctx) {
        if (inTypeDef) {
            if (jsonBuilder == null) {
                jsonBuilder = new JsonBuilder();
                jsonBuilder.createArray();
            } else {
                jsonBuilder.putArray(currentPropertyName);
            }
        }
    }

    @Override
    public void exitPropertyName(JsonTemplateParser.PropertyNameContext ctx) {
        if (inTypeDef) {
            this.currentPropertyName = ctx.getText();
            System.out.println("propertyName=" + this.currentPropertyName);
        }
    }

    @Override
    public void exitTypeName(JsonTemplateParser.TypeNameContext ctx) {
        String typeName = ctx.getText();
        currentValueProducer = valueProducerMap.get(typeName);
        if (currentValueProducer == null) {
            JsonWrapperNode jsonWrapperNode = new JsonWrapperNode();
            if (jsonBuilder.inObject()) {
                jsonBuilder.putWrapper(currentPropertyName, jsonWrapperNode);
            } else {
                jsonBuilder.addWrapper(jsonWrapperNode);
            }
            List<JsonWrapperNode> typeMissNodes = this.typeMissMap.get(typeName);
            if (typeMissNodes == null) {
                this.typeMissMap.put(typeName, Arrays.asList(jsonWrapperNode));
            } else {
                typeMissNodes.add(jsonWrapperNode);
            }
        } else {
            Class valueType = currentValueProducer.getValueType();
            if (valueType.equals(Integer.class)) {
                Supplier<Integer> supplier = () -> (Integer) currentValueProducer.produce(Collections.emptyMap());
                buildJsonValue(supplier, jsonBuilder::putInteger, jsonBuilder::addInteger);

            } else if (valueType.equals(Boolean.class)) {
                Supplier<Boolean> supplier = () -> (Boolean) currentValueProducer.produce(Collections.emptyMap());
                buildJsonValue(supplier, jsonBuilder::putBoolean, jsonBuilder::addBoolean);

            } else if (valueType.equals(String.class)) {
                Supplier<String> supplier = () -> (String) currentValueProducer.produce(Collections.emptyMap());
                buildJsonValue(supplier, jsonBuilder::putString, jsonBuilder::addString);
            }
        }
    }

    private <T> void buildJsonValue(Supplier<T> supplier, BiConsumer<String, Supplier<T>> putInObject, Consumer<Supplier<T>> addInArray) {
        if (jsonBuilder.inObject()) {
            putInObject.accept(currentPropertyName, supplier);
        } else {
            addInArray.accept(supplier);
        }
    }
}
