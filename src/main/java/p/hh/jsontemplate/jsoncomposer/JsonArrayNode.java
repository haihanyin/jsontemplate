package p.hh.jsontemplate.jsoncomposer;

import p.hh.jsontemplate.valueproducer.INodeProducer;
import p.hh.jsontemplate.valueproducer.IValueNodeProducer;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class JsonArrayNode implements JsonNode {

    private List<JsonNode> children = new LinkedList<>();

    private Supplier<List<JsonNode>> supplier;

    private JsonNode defaultNode;
    private INodeProducer defaultNodeProducer;

    public void addNode(JsonNode jsonNode) {
        children.add(jsonNode);
    }

    public void setNodeProducer(INodeProducer producer) {
        this.defaultNodeProducer = producer;
    }

    public INodeProducer getDefaultType() {
        return defaultNodeProducer;
    }

    public void setDefaultNode(JsonNode jsonNode) {
        this.defaultNode = defaultNode;
    }

    private Integer size;
    private Integer max;
    private Integer min;

    private List<JsonNode> additionalNodes = new ArrayList<>();
    private int additionalSize = 0;

    public void setParameters(Map<String, String> mapParam) {
        size = readParam(mapParam, "size");
        max = readParam(mapParam, "max");
        min = readParam(mapParam, "min");

        if (size != null) {

            if (size > children.size()) {
                additionalSize = size - children.size();
                int amount = size - children.size();
                fillAddtionalNodeList(amount);
            }
        }
    }

    private void fillAddtionalNodeList(int amount) {
        if (defaultNode != null) {
            for (int i=0; i< amount; i++) {
                additionalNodes.add(defaultNode);
            }
        } else if (defaultNodeProducer != null) {
            for (int i=0; i< amount; i++) {
                additionalNodes.add(defaultNodeProducer.produce());
            }
        }
    }

    private Integer readParam(Map<String, String> mapParam, String key) {
        String value = mapParam.get(key);
        if (value != null) {
            return Integer.parseInt(value);
        }
        return null;
    }

    public void addInteger(Supplier<Integer> supplier) {
        children.add(new JsonIntegerNode(supplier));
    }

    public void addFloat(Supplier<Float> supplier) {
        children.add(new JsonFloatNode(supplier));
    }

    public void addBoolean(Supplier<Boolean> supplier) {
        children.add(new JsonBooleanNode(supplier));
    }

    public void addString(Supplier<String> supplier) {
        children.add(new JsonStringNode(supplier));
    }

    public void addObject(JsonObjectNode value) {
        children.add(value);
    }

    public void addArray(JsonArrayNode value) {
        children.add(value);
    }

    public void addWrapper(JsonWrapperNode wrapperNode) {
        children.add(wrapperNode);
    }

    public void addNull() {
        children.add(new JsonNullNode());
    }



    @Override
    public String print() {
        String joinedChildren = children.stream().map(JsonNode::print).collect(Collectors.joining(","));
        return "[" + joinedChildren + "]";
    }

    @Override
    public String prettyPrint(int identation) {
        String childrenSpaces = JsonUtils.makeIdentation(identation+1);
        ArrayList<JsonNode> printChildren = new ArrayList<>();

        printChildren.addAll(children);
        for (int i=0; i<additionalSize; i++) {
            printChildren.add(additionalNodes.get(i));
        }

        String joinedIdentChildren = printChildren.stream()
                .map(child -> childrenSpaces + child.prettyPrint(identation + 1))
                .collect(Collectors.joining(",\n"));

        String spaces = JsonUtils.makeIdentation(identation);
        return  "[\n" +
                joinedIdentChildren +
                "\n" + spaces + "]";
    }

}
