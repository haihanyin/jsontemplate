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
    private ValueDeclaration curValueDecl;

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
        String typeName = curValueDecl.getTypeName();
        IValueProducer valueProducer = valueProducerMap.get(typeName);
        if (valueProducer == null) {
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
            Class valueType = valueProducer.getValueType();
            if (valueType.equals(Integer.class)) {
                buildJsonValue(createSupplier(valueProducer), jsonBuilder::putInteger, jsonBuilder::addInteger);

            } else if (valueType.equals(Boolean.class)) {
                buildJsonValue(createSupplier(valueProducer), jsonBuilder::putBoolean, jsonBuilder::addBoolean);

            } else if (valueType.equals(String.class)) {
                buildJsonValue(createSupplier(valueProducer), jsonBuilder::putString, jsonBuilder::addString);
            }
        }
    }

    private <T> Supplier<T> createSupplier(IValueProducer<T> valueProducer) {
        Supplier<T> supplier  = () -> (T) valueProducer.produce(Collections.emptyMap());
        if (curValueDecl.getSingleParam() != null) {
            supplier = () -> (T) valueProducer.produce(curValueDecl.getSingleParam());
        } else if (curValueDecl.getListParam() != null) {
            supplier = () -> (T) valueProducer.produce(curValueDecl.getListParam());
        } else if (curValueDecl.getMapParam() != null) {
            supplier = () -> (T) valueProducer.produce(curValueDecl.getMapParam());
        }
        return supplier;
    }

    private <T> void buildJsonValue(Supplier<T> supplier, BiConsumer<String, Supplier<T>> putInObject, Consumer<Supplier<T>> addInArray) {
        if (jsonBuilder.inObject()) {
            putInObject.accept(currentPropertyName, supplier);
        } else {
            addInArray.accept(supplier);
        }
    }
}
