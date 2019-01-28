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

    private TypeDefinition curTypeDef;
    private Stack<ValueDeclaration> valueDeclarationStack = new Stack<>();

    public JsonTemplateTreeListener() {
        setupValueProducerMap();
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
            }
        }
    }

    @Override
    public void exitPairProperty(JsonTemplateParser.PairPropertyContext ctx) {
        if (curTypeDef != null && curTypeDef.getTypeDefContext() == ctx) {
            JsonNode jsonNode = jsonBuilder.build();
            String typeDefName = curTypeDef.getTypeName();
            typeMap.put(typeDefName, jsonNode);

            List<JsonWrapperNode> missTypeNodes = typeMissMap.get(typeDefName);
            if (missTypeNodes != null) {
                missTypeNodes.stream().forEach(wrapperNode -> wrapperNode.setJsonNode(jsonNode));
                typeMissMap.remove(typeDefName);
            }
            typeMap.put(typeDefName, jsonNode);
            curTypeDef = null;
        }
    }

    @Override
    public void enterMapParam(JsonTemplateParser.MapParamContext ctx) {
        String key = ctx.getChild(0).getText();
        String value = ctx.getChild(2).getText();
        valueDeclarationStack.peek().putMapParam(key, value);
    }

    @Override
    public void enterListParams(JsonTemplateParser.ListParamsContext ctx) {
        IntStream.of(ctx.getChildCount())
                .mapToObj(ctx::getChild)
                .map(ParseTree::getText)
                .filter(text -> !",".equals(text))
                .forEach(valueDeclarationStack.peek()::addListParam);
    }

    @Override
    public void enterTypeName(JsonTemplateParser.TypeNameContext ctx) {
        if (curTypeDef != null) {
            curTypeDef.setTypeName(ctx.getText());
        } else {
            currentPropertyName = ctx.getText();
        }
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
    public void exitPropertyValueSpec(JsonTemplateParser.PropertyValueSpecContext ctx) {
        String typeName = valueDeclarationStack.getTypeName();
        IValueProducer valueProducer = valueProducerMap.get(typeName);
        if (valueProducer == null) {
            buildJsonUnlinkedNode(typeName);
        } else {
            buildJsonValueNode(valueProducer);
        }
    }

    @Override
    public void exitTypeName(JsonTemplateParser.TypeNameContext ctx) {
        String typeName = valueDeclarationStack.getTypeName();
        IValueProducer valueProducer = valueProducerMap.get(typeName);
        if (valueProducer == null) {
            buildJsonUnlinkedNode(typeName);
        } else {
            buildJsonValueNode(valueProducer);
        }
    }

    private void buildJsonValueNode(IValueProducer valueProducer) {
        Class valueType = valueProducer.getValueType();
        if (valueType.equals(Integer.class)) {
            buildJsonValue(createSupplier(valueProducer), jsonBuilder::putInteger, jsonBuilder::addInteger);

        } else if (valueType.equals(Boolean.class)) {
            buildJsonValue(createSupplier(valueProducer), jsonBuilder::putBoolean, jsonBuilder::addBoolean);

        } else if (valueType.equals(String.class)) {
            buildJsonValue(createSupplier(valueProducer), jsonBuilder::putString, jsonBuilder::addString);
        }
    }

    private void buildJsonUnlinkedNode(String typeName) {
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
    }

    private <T> Supplier<T> createSupplier(IValueProducer<T> valueProducer) {
        Supplier<T> supplier  = () -> (T) valueProducer.produce(Collections.emptyMap());
        if (valueDeclarationStack.getSingleParam() != null) {
            supplier = () -> (T) valueProducer.produce(valueDeclarationStack.getSingleParam());
        } else if (valueDeclarationStack.getListParam() != null) {
            supplier = () -> (T) valueProducer.produce(valueDeclarationStack.getListParam());
        } else if (valueDeclarationStack.getMapParam() != null) {
            supplier = () -> (T) valueProducer.produce(valueDeclarationStack.getMapParam());
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
