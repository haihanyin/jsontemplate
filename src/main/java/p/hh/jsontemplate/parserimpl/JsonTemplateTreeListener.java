package p.hh.jsontemplate.parserimpl;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import p.hh.jsontemplate.jsoncomposer.JsonBuilder;
import p.hh.jsontemplate.jsoncomposer.JsonNode;
import p.hh.jsontemplate.jsoncomposer.JsonWrapperNode;
import p.hh.jsontemplate.parser.JsonTemplateBaseListener;
import p.hh.jsontemplate.parser.JsonTemplateParser;
import p.hh.jsontemplate.valueproducer.BooleanNodeProducer;
import p.hh.jsontemplate.valueproducer.INodeProducer;
import p.hh.jsontemplate.valueproducer.IntegerNodeProducer;
import p.hh.jsontemplate.valueproducer.StringNodeProducer;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class JsonTemplateTreeListener extends JsonTemplateBaseListener {


    private Map<String, JsonNode> typeMap = new HashMap<>();
    private Map<String, List<JsonWrapperNode>> typeMissMap = new HashMap<>();
    private Map<String, INodeProducer> valueProducerMap = new HashMap<>();
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
        valueProducerMap.put("s", new StringNodeProducer());
        valueProducerMap.put("i", new IntegerNodeProducer());
        valueProducerMap.put("b", new BooleanNodeProducer());
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
    public void enterMapParams(JsonTemplateParser.MapParamsContext ctx) {
        if (inItemsArray) {
            curValueDecl = new ValueDeclaration();
        }
    }

    @Override
    public void exitMapParams(JsonTemplateParser.MapParamsContext ctx) {
        if (inItemsArray) {
            chooseBuilder().peekArrayNode().setParameters(curValueDecl.getMapParam());
        }
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
        debug("enterJsonArray ", ctx);
        JsonBuilder builder = chooseBuilder();
        if (builder.isEmpty()) {
            builder.createArray();
        } else {
            builder.putArray(curValueDecl.getValueName());
        }
    }

    @Override
    public void exitJsonArray(JsonTemplateParser.JsonArrayContext ctx) {
        debug("exitJsonArray", ctx);
        chooseBuilder().end();
    }

    private boolean inArrayTypeInfo;

    @Override
    public void enterArrayTypeInfo(JsonTemplateParser.ArrayTypeInfoContext ctx) {
        inArrayTypeInfo = true;
    }

    @Override
    public void exitArrayTypeInfo(JsonTemplateParser.ArrayTypeInfoContext ctx) {
        inArrayTypeInfo = false;
    }

    @Override
    public void exitJsonValue(JsonTemplateParser.JsonValueContext ctx) {
        String typeName = curValueDecl.getTypeName();
        INodeProducer valueProducer = valueProducerMap.get(typeName);
        if (valueProducer == null) {
            buildJsonWrapperNode(typeName);
        } else {
            buildJsonValueNode(valueProducer);
        }
    }

    @Override
    public void enterValue(JsonTemplateParser.ValueContext ctx) {
        JsonBuilder builder = chooseBuilder();
        INodeProducer nodeProducer = builder.peekArrayNode().getDefaultType();
        JsonNode node = nodeProducer.produce(ctx.getText());
        builder.addNode(node);
    }


    private boolean inItemsArray;

    @Override
    public void enterItemsArray(JsonTemplateParser.ItemsArrayContext ctx) {
        inItemsArray = true;
    }

    @Override
    public void exitItemsArray(JsonTemplateParser.ItemsArrayContext ctx) {
        inItemsArray = false;
    }

    private boolean inTypeDefContext() {
        return  curTypeDef != null;
    }

    private JsonBuilder chooseBuilder() {
        return inTypeDefContext() ? typeBuilder : jsonBuilder;
    }

    private void buildJsonValueNode(INodeProducer valueProducer) {
        JsonNode jsonNode = createJsonValueNode(valueProducer);
        buildJsonValue(jsonNode);
    }

    private JsonNode createJsonValueNode(INodeProducer valueProducer) {
        JsonNode jsonNode = null;
        if (curValueDecl.getSingleParam() != null) {
            jsonNode = valueProducer.produce(curValueDecl.getSingleParam());
        } else if (curValueDecl.getListParam() != null) {
            jsonNode = valueProducer.produce(curValueDecl.getListParam());
        } else if (curValueDecl.getMapParam() != null) {
            jsonNode = valueProducer.produce(curValueDecl.getMapParam());
        } else {
            jsonNode = valueProducer.produce();
            if (inArrayTypeInfo) {
                chooseBuilder().peekArrayNode().setNodeProducer(valueProducer);
            }
        }
        return jsonNode;
    }

    private void buildJsonWrapperNode(String typeName) {
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
        JsonBuilder builder = chooseBuilder();
        if (builder.inObject()) {
            builder.putWrapper(curValueDecl.getValueName(), jsonWrapperNode);
        } else if (inArrayTypeInfo) {
            builder.peekArrayNode().setDefaultNode(jsonWrapperNode);
        } else {
            builder.addWrapper(jsonWrapperNode);
        }
    }

    private void buildJsonValue(JsonNode jsonNode) {
        JsonBuilder builder = chooseBuilder();
        if (builder.inObject()) {
            builder.putNode(curValueDecl.getValueName(), jsonNode);
        } else if (inArrayTypeInfo) {
            builder.peekArrayNode().setDefaultNode(jsonNode);
        } else {
            builder.addNode(jsonNode);
        }
    }

    private void debug(String message, ParserRuleContext ctx) {
        if (debug) {
            System.out.println(message + " " + ctx.getText());
        }
    }
}
