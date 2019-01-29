package p.hh.jsontemplate.parserimpl;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import p.hh.jsontemplate.jsoncomposer.JsonBuilder;
import p.hh.jsontemplate.jsoncomposer.JsonNode;
import p.hh.jsontemplate.jsoncomposer.JsonWrapperNode;
import p.hh.jsontemplate.parser.JsonTemplateBaseListener;
import p.hh.jsontemplate.parser.JsonTemplateParser;
import p.hh.jsontemplate.supplier.ListParamSupplier;
import p.hh.jsontemplate.supplier.MapParamSupplier;
import p.hh.jsontemplate.supplier.SingleParamSupplier;
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

    private boolean debug = true;

    public JsonTemplateTreeListener() {
        setupValueProducerMap();
        jsonBuilder = new JsonBuilder();
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
        debug("enterPairProperty ", ctx);
        ParseTree child = ctx.getChild(0).getChild(0);
        if (child instanceof JsonTemplateParser.TypeInfoContext) {
            if (curTypeDef != null) {
                throw new IllegalStateException("Nested type definition is not allowed [" + child.getText() + "]");
            } else {
                curTypeDef = new TypeDefinition(ctx);
                typeBuilder = new JsonBuilder();
            }
        }
        curValueDecl = new ValueDeclaration();
    }

    @Override
    public void exitPairProperty(JsonTemplateParser.PairPropertyContext ctx) {
        debug("exitPairProerty ", ctx);
        ParseTree child = ctx.getChild(0).getChild(0);
        if (inTypeDefContext() && child instanceof JsonTemplateParser.TypeInfoContext) {
            JsonNode typeNode = typeBuilder.build();
            String typeDefName = curTypeDef.getTypeName();
            typeMap.put(typeDefName, typeNode);

            List<JsonWrapperNode> missTypeNodes = typeMissMap.get(typeDefName);
            if (missTypeNodes != null) {
                missTypeNodes.stream().forEach(wrapperNode -> wrapperNode.setJsonNode(typeNode));
                typeMissMap.remove(typeDefName);
            }
            typeMap.put(typeDefName, typeNode);
            curTypeDef = null;
            typeBuilder = null;
        }
    }

    @Override
    public void enterMapParam(JsonTemplateParser.MapParamContext ctx) {
        debug("enterMapParam", ctx);
        String key = ctx.getChild(0).getText();
        String value = ctx.getChild(2).getText();
        curValueDecl.putMapParam(key, value);
    }

    @Override
    public void enterListParams(JsonTemplateParser.ListParamsContext ctx) {
        debug("enterListParams", ctx);
        IntStream.range(0, ctx.getChildCount())
                .mapToObj(ctx::getChild)
                .map(ParseTree::getText)
                .filter(text -> !",".equals(text))
                .forEach(curValueDecl::addListParam);
    }

    @Override
    public void enterSingleParam(JsonTemplateParser.SingleParamContext ctx) {
        debug("enterSingleParam", ctx);
        curValueDecl.setSingleParam(ctx.getText());
    }

    @Override
    public void enterPropertyName(JsonTemplateParser.PropertyNameContext ctx) {
        debug("enterPropertyName", ctx);
        curValueDecl.setValueName(ctx.getText());
    }

    @Override
    public void enterTypeName(JsonTemplateParser.TypeNameContext ctx) {
        debug("enterTypeName", ctx);
        if (inTypeDefContext() && curTypeDef.getTypeName() == null) {
            curTypeDef.setTypeName(ctx.getText());
        } else {
            curValueDecl.setTypeName(ctx.getText());
        }
    }

    @Override
    public void enterJsonObject(JsonTemplateParser.JsonObjectContext ctx) {
        debug("enterJsonObject", ctx);
        JsonBuilder builder = chooseBuilder();
        if (builder.isEmpty()) {
            builder.createObject();
        } else {
            builder.putObject(curValueDecl.getValueName());
        }
        System.out.println();
    }

    @Override
    public void exitJsonObject(JsonTemplateParser.JsonObjectContext ctx) {
        chooseBuilder().end();
    }

    @Override
    public void enterJsonArray(JsonTemplateParser.JsonArrayContext ctx) {
        JsonBuilder builder = chooseBuilder();
        if (builder.isEmpty()) {
            builder.createArray();
        } else {
            builder.putArray(curValueDecl.getValueName());
        }
    }

    @Override
    public void exitJsonValue(JsonTemplateParser.JsonValueContext ctx) {
        String typeName = curValueDecl.getTypeName();
        IValueProducer valueProducer = valueProducerMap.get(typeName);
        JsonBuilder builder = chooseBuilder();
        if (valueProducer == null) {
            buildJsonWrapperNode(builder, typeName);
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

    private void buildJsonWrapperNode(JsonBuilder builder, String typeName) {
        JsonWrapperNode jsonWrapperNode = new JsonWrapperNode();
        JsonNode type = typeMap.get(typeName);
        if (type != null) {
            jsonWrapperNode.setJsonNode(type);
        } else {
            List<JsonWrapperNode> typeMissNodes = this.typeMissMap.get(typeName);
            if (typeMissNodes == null) {
                this.typeMissMap.put(typeName, Arrays.asList(jsonWrapperNode));
            } else {
                typeMissNodes.add(jsonWrapperNode);
            }
        }

        if (builder.inObject()) {
            builder.putWrapper(curValueDecl.getValueName(), jsonWrapperNode);
        } else {
            builder.addWrapper(jsonWrapperNode);
        }
    }

    private <T> Supplier<T> createSupplier(IValueProducer<T> valueProducer) {
        Supplier<T> supplier  = () -> (T) valueProducer.produce();
        if (curValueDecl.getSingleParam() != null) {
            supplier = new SingleParamSupplier<>(valueProducer, curValueDecl.getSingleParam());
        } else if (curValueDecl.getListParam() != null) {
            supplier = new ListParamSupplier<>(valueProducer, new ArrayList<>(curValueDecl.getListParam()));
        } else if (curValueDecl.getMapParam() != null) {
            supplier = new MapParamSupplier<>(valueProducer, new HashMap<>(curValueDecl.getMapParam()));
        }
        return supplier;
    }

    private <T> void buildJsonValue(Supplier<T> supplier, BiConsumer<String, Supplier<T>> putInObject, Consumer<Supplier<T>> addInArray) {
        if (chooseBuilder().inObject()) {
            putInObject.accept(curValueDecl.getValueName(), supplier);
        } else {
            addInArray.accept(supplier);
        }
    }

    private void debug(String message, ParserRuleContext ctx) {
        if (debug) {
            System.out.println(message + " " + ctx.getText());
        }
    }
}
