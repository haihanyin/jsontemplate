package p.hh.jsontemplate.jsoncomposer;

import p.hh.jsontemplate.valueproducer.INodeProducer;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class JsonArrayNode implements JsonNode {

    private List<JsonNode> children = new LinkedList<>();

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
        this.defaultNode = jsonNode;
    }

    private Integer size;
    private Integer max;
    private Integer min;

    public void setParameters(String singleParam) {
        size = Integer.parseInt(singleParam);
    }

    public void setParameters(List<String> singleParam) {
        min = Integer.parseInt(singleParam.get(0));
        max = Integer.parseInt(singleParam.get(1));
    }

    public void setParameters(Map<String, String> mapParam) {
        size = readParam(mapParam, "size");
        max = readParam(mapParam, "max");
        min = readParam(mapParam, "min");

        if (size == null) {
            if (min != null && max == null) {
                max = 2*min;
            } else if (min == null && max != null) {
                min = 0;
            }
        }
    }

    private List<JsonNode> prepareAdditionalNodeList() {
        if (size != null) {
            return addtionalNodeList(size);
        } else if (max != null && children.size() < max) {
            int randomSize = new Random().nextInt(max - min) + min;
            return addtionalNodeList(randomSize);
        } else {
            return Collections.emptyList();
        }
    }

    private List<JsonNode> addtionalNodeList(int size) {
        if (size > children.size()) {
            int amount = size - children.size();
            List<JsonNode> list = new ArrayList<>(amount);
            if (defaultNode != null) {
                for (int i=0; i<amount; i++) {
                    list.add(defaultNode);
                }
            } else if (defaultNodeProducer != null) {
                for (int i=0; i<amount; i++) {
                    list.add(defaultNodeProducer.produce());
                }
            }
            return list;
        } else {
            return Collections.emptyList();
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
        List<JsonNode> additionalNodeList = prepareAdditionalNodeList();
        printChildren.addAll(additionalNodeList);

        String joinedIdentChildren = printChildren.stream()
                .map(child -> childrenSpaces + child.prettyPrint(identation + 1))
                .collect(Collectors.joining(",\n"));

        String spaces = JsonUtils.makeIdentation(identation);
        return  "[\n" +
                joinedIdentChildren +
                "\n" + spaces + "]";
    }

}
