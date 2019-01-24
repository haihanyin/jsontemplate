package p.hh.jsontemplate.jsoncomposer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonObjectNode implements JsonNode {

    private Map<String, JsonNode> children = new LinkedHashMap<>();

    public void put(String key, int value) {
        children.put(key, new JsonIntegerNode(value));
    }

    public void put(String key, float value) {
        children.put(key, new JsonFloatNode(value));
    }

    public void put(String key, boolean value) {
        children.put(key, new JsonBooleanNode(value));
    }

    public void put(String key, String value) {
        children.put(key, new JsonStringNode(value));
    }

    public void put(String key, JsonObjectNode value) {
        children.put(key, value);
    }

    public void put(String key, JsonArrayNode value) {
        children.put(key, value);
    }

    public void putNull(String key) {
        children.put(key, new JsonNullNode());
    }

    @Override
    public String print() {
        String joinedChildren = children.entrySet().stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue().print())
                .collect(Collectors.joining(","));
        return "{" + joinedChildren + "}";
    }

    @Override
    public String prettyPrint(int identation) {
        String childSpaces = JsonUtils.makeIdentation(identation + 1);
        String joinedIdentChildren = children.entrySet().stream()
                .map(entry -> childSpaces + "\"" + entry.getKey() + "\" : " + entry.getValue().prettyPrint(identation+1))
                .collect(Collectors.joining(",\n"));
        String spaces = JsonUtils.makeIdentation(identation);
        return  "{\n" +
                joinedIdentChildren +
                "\n" + spaces + "}";
    }
}
