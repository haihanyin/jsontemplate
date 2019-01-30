package p.hh.jsontemplate.jsoncomposer;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class JsonArrayNode implements JsonNode {

    private List<JsonNode> children = new LinkedList<>();

    private Supplier<List<JsonNode>> supplier;

    public JsonArrayNode(Supplier<List<JsonNode>> supplier) {
        this.supplier = supplier;
    }


    public void add(JsonNode jsonNode) {
        children.add(jsonNode);
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
        String joinedIdentChildren = children.stream()
                .map(child -> childrenSpaces + child.prettyPrint(identation + 1))
                .collect(Collectors.joining(",\n"));
        String spaces = JsonUtils.makeIdentation(identation);
        return  "[\n" +
                joinedIdentChildren +
                "\n" + spaces + "]";
    }

}
