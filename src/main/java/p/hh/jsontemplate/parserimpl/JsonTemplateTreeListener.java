package p.hh.jsontemplate.parserimpl;

import org.antlr.v4.runtime.tree.ParseTree;
import p.hh.jsontemplate.jsoncomposer.JsonBuilder;
import p.hh.jsontemplate.jsoncomposer.JsonNode;
import p.hh.jsontemplate.jsoncomposer.JsonWrapperNode;
import p.hh.jsontemplate.parser.JsonTemplateBaseListener;
import p.hh.jsontemplate.parser.JsonTemplateParser;
import p.hh.jsontemplate.valueproducer.BooleanValueProducer;
import p.hh.jsontemplate.valueproducer.IValueProducer;
import p.hh.jsontemplate.valueproducer.IntegerValueProducer;
import p.hh.jsontemplate.valueproducer.StringValueProducer;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class JsonTemplateTreeListener extends JsonTemplateBaseListener {


    private Map<String, JsonNode> typeMap = new HashMap<>();
    private Map<String, List<JsonWrapperNode>> typeMissMap = new HashMap<>();
    private Map<String, IValueProducer> valueProducerMap = new HashMap<>();
    private JsonBuilder jsonBuilder;
    private JsonBuilder typeBuilder;

    private TypeDefinition curTypeDef;
    private ValueDeclaration curValueDecl;

    public JsonTemplateTreeListener() {
        setupValueProducerMap();
    }

    public String writeJson() {
        return jsonBuilder.build().prettyPrint(0);
    }

    protected void setupValueProducerMap() {
        valueProducerMap.put("s", new StringValueProducer());
        valueProducerMap.put("i", new IntegerValueProducer());
        valueProducerMap.put("b", new BooleanValueProducer());
    }

    @Override
    public void enterPairProperty(JsonTemplateParser.PairPropertyContext ctx) {
        ParseTree child = ctx.getChild(0).getChild(0);
        if (child instanceof JsonTemplateParser.TypeInfoContext) {
            if (curTypeDef != null) {
                throw new IllegalStateException("Nested type definition is not allowed [" + child.getText() + "]");
            } else {
                curTypeDef = new TypeDefinition(ctx);
                typeBuilder.createObject();
            }
        } else {
            curValueDecl = new ValueDeclaration();
        }
    }

    @Override
    public void exitPairProperty(JsonTemplateParser.PairPropertyContext ctx) {
        if (inTypeDefContext() && curTypeDef.getTypeDefContext() == ctx) {
            JsonNode typeNode = typeBuilder.end().build();
            String typeDefName = curTypeDef.getTypeName();
            typeMap.put(typeDefName, typeNode);

            List<JsonWrapperNode> missTypeNodes = typeMissMap.get(typeDefName);
            if (missTypeNodes != null) {
                missTypeNodes.stream().forEach(wrapperNode -> wrapperNode.setJsonNode(typeNode));
                typeMissMap.remove(typeDefName);
            }
            typeMap.put(typeDefName, typeNode);
            curTypeDef = null;
        }
    }

    @Override
    public void enterMapParam(JsonTemplateParser.MapParamContext ctx) {
        String key = ctx.getChild(0).getText();
        String value = ctx.getChild(2).getText();
        curValueDecl.putMapParam(key, value);
    }

    @Override
    public void enterListParams(JsonTemplateParser.ListParamsContext ctx) {
        IntStream.range(0, ctx.getChildCount())
                .mapToObj(ctx::getChild)
                .map(ParseTree::getText)
                .filter(text -> !",".equals(text))
                .forEach(curValueDecl::addListParam);
    }

    @Override
    public void enterSingleParam(JsonTemplateParser.SingleParamContext ctx) {
        curValueDecl.setSingleParam(ctx.getText());
    }

    @Override
    public void enterPropertyName(JsonTemplateParser.PropertyNameContext ctx) {
        curValueDecl.setValueName(ctx.getText());
    }

    @Override
    public void enterTypeName(JsonTemplateParser.TypeNameContext ctx) {
        if (inTypeDefContext()) {
            curTypeDef.setTypeName(ctx.getText());
        } else {
            curValueDecl.setTypeName(ctx.getText());
        }
    }

    @Override
    public void enterJsonObject(JsonTemplateParser.JsonObjectContext ctx) {
        JsonBuilder builder = chooseBuilder();
        if (builder == null) {
            createBuilder().createObject();
        } else {
            builder.putObject(ctx.getText());
        }
    }

    private JsonBuilder createBuilder() {
        if (inTypeDefContext()) {
            typeBuilder = new JsonBuilder();
            return typeBuilder;
        } else {
            jsonBuilder = new JsonBuilder();
            return jsonBuilder;
        }
    }

    @Override
    public void exitJsonObject(JsonTemplateParser.JsonObjectContext ctx) {
        jsonBuilder.end();
    }

    @Override
    public void enterJsonArray(JsonTemplateParser.JsonArrayContext ctx) {
        JsonBuilder builder = chooseBuilder();
        if (builder == null) {
            createBuilder().createArray();
        } else {
            jsonBuilder.putArray(curValueDecl.getValueName());
        }
    }

    @Override
    public void exitJsonValue(JsonTemplateParser.JsonValueContext ctx) {
        String typeName = curValueDecl.getTypeName();
        IValueProducer valueProducer = valueProducerMap.get(typeName);
        JsonBuilder builder = chooseBuilder();
        if (valueProducer == null) {
            buildJsonUnlinkedNode(builder, typeName);
        } else {
            buildJsonValueNode(builder, valueProducer);
        }
    }

    private boolean inTypeDefContext() {
        return  curTypeDef != null;
    }

    private JsonBuilder chooseBuilder() {
        return inTypeDefContext() ? typeBuilder : jsonBuilder;
    }

    private void buildJsonValueNode(JsonBuilder builder, IValueProducer valueProducer) {
        Class valueType = valueProducer.getValueType();
        if (valueType.equals(Integer.class)) {
            buildJsonValue(createSupplier(valueProducer), builder::putInteger, builder::addInteger);

        } else if (valueType.equals(Boolean.class)) {
            buildJsonValue(createSupplier(valueProducer), builder::putBoolean, builder::addBoolean);

        } else if (valueType.equals(String.class)) {
            buildJsonValue(createSupplier(valueProducer), builder::putString, builder::addString);
        }
    }

    private void buildJsonUnlinkedNode(JsonBuilder builder, String typeName) {
        JsonWrapperNode jsonWrapperNode = new JsonWrapperNode();
        if (builder.inObject()) {
            builder.putWrapper(curValueDecl.getValueName(), jsonWrapperNode);
        } else {
            builder.addWrapper(jsonWrapperNode);
        }
        List<JsonWrapperNode> typeMissNodes = this.typeMissMap.get(typeName);
        if (typeMissNodes == null) {
            this.typeMissMap.put(typeName, Arrays.asList(jsonWrapperNode));
        } else {
            typeMissNodes.add(jsonWrapperNode);
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
            putInObject.accept(curValueDecl.getValueName(), supplier);
        } else {
            addInArray.accept(supplier);
        }
    }
}
