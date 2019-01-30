package p.hh.jsontemplate.parserimpl;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import p.hh.jsontemplate.jsoncomposer.JsonBuilder;
import p.hh.jsontemplate.jsoncomposer.JsonNode;
import p.hh.jsontemplate.jsoncomposer.JsonWrapperNode;
import p.hh.jsontemplate.jtm.PropertyDeclaration;
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



    private Stack<PropertyDeclaration> stack = new Stack<>();

    private boolean debug = true;

    public PropertyDeclaration getRoot() {
        System.out.println("stack size " + stack.size());
        return stack.peek();
    }


    @Override
    public void enterPairProperty(JsonTemplateParser.PairPropertyContext ctx) {
        debug("enterPairProperty ", ctx);
        stack.push(new PropertyDeclaration());
    }

    @Override
    public void exitPairProperty(JsonTemplateParser.PairPropertyContext ctx) {
        debug("exitPairProperty ", ctx);
        PropertyDeclaration pop = stack.pop();
        stack.peek().addProperty(pop);
    }

    @Override
    public void enterMapParam(JsonTemplateParser.MapParamContext ctx) {
        debug("enterMapParam", ctx);
        String key = ctx.getChild(0).getText();
        String value = ctx.getChild(2).getText();
        stack.peek().getMapParam().put(key, value);
    }

    @Override
    public void enterListParams(JsonTemplateParser.ListParamsContext ctx) {
        debug("enterListParams", ctx);
        IntStream.range(0, ctx.getChildCount())
                .mapToObj(ctx::getChild)
                .map(ParseTree::getText)
                .filter(text -> !",".equals(text))
                .forEach(param -> stack.peek().getListParam().add(param));
    }

    @Override
    public void enterSingleParam(JsonTemplateParser.SingleParamContext ctx) {
        debug("enterSingleParam", ctx);
        stack.peek().setSingleParam(ctx.getText());
    }

    @Override
    public void enterPropertyNameSpec(JsonTemplateParser.PropertyNameSpecContext ctx) {
        stack.peek().setValueName(ctx.getText());
    }

    @Override
    public void enterTypeName(JsonTemplateParser.TypeNameContext ctx) {
        debug("enterTypeName", ctx);
        if ( !(ctx.getParent() instanceof JsonTemplateParser.TypeDefContext) ) {
            stack.peek().setTypeName(ctx.getText());
        }
    }

    @Override
    public void enterJsonObject(JsonTemplateParser.JsonObjectContext ctx) {
        debug("enterJsonObject", ctx);
        if (stack.isEmpty()) {
            stack.push(new PropertyDeclaration());
        }
        stack.peek().setAsObject(true);
    }

    @Override
    public void enterJsonArray(JsonTemplateParser.JsonArrayContext ctx) {
        if (stack.isEmpty()) {
            stack.push(new PropertyDeclaration());
        }
        stack.peek().setAsArray(true);
    }

    @Override
    public void enterItem(JsonTemplateParser.ItemContext ctx) {
        stack.push(new PropertyDeclaration());
    }

    @Override
    public void exitItem(JsonTemplateParser.ItemContext ctx) {
        PropertyDeclaration pop = stack.pop();
        stack.pop().addProperty(pop);
    }

    @Override
    public void enterValue(JsonTemplateParser.ValueContext ctx) {
        stack.peek().setSingleParam(ctx.getText());
    }

    @Override
    public void enterVariableName(JsonTemplateParser.VariableNameContext ctx) {
        stack.peek().setVariableName(ctx.getText());
    }

    private void debug(String message, ParserRuleContext ctx) {
        if (debug) {
            System.out.println(message + " " + ctx.getText());
        }
    }
}
