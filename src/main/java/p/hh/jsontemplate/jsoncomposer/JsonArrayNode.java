package p.hh.jsontemplate.jsoncomposer;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class JsonArrayNode implements JsonNode {

    private List<JsonNode> children = new LinkedList<>();

    public void add(int value) {
        children.add(new JsonIntegerNode(value));
    }

    public void add(float value) {
        children.add(new JsonFloatNode(value));
    }

    public void add(boolean value) {
        children.add(new JsonBooleanNode(value));
    }

    public void add(String value) {
        children.add(new JsonStringNode(value));
    }

    public void add(JsonObjectNode value) {
        children.add(value);
    }

    public void add(JsonArrayNode value) {
        children.add(value);
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
