package p.hh.jsontemplate.jsoncomposer;

import java.util.Stack;

public class JsonBuilder {

    private Stack<JsonNode> nodeStack = new Stack<>();
    private JsonNode lastPopNode;

    public JsonBuilder createArray() {
        nodeStack.push(new JsonArrayNode());
        return this;
    }

    public JsonBuilder createObject() {
        nodeStack.push(new JsonObjectNode());
        return this;
    }

    public JsonBuilder end() {
        lastPopNode = nodeStack.pop();
        return this;
    }

    public JsonBuilder putArray(String key) {
        JsonArrayNode jsonArrayNode = new JsonArrayNode();
        ((JsonObjectNode) nodeStack.peek()).put(key, jsonArrayNode);
        nodeStack.push(jsonArrayNode);
        return this;
    }

    public JsonBuilder putObject(String key) {
        JsonObjectNode jsonObjectNode = new JsonObjectNode();
        ((JsonObjectNode) nodeStack.peek()).put(key, jsonObjectNode);
        nodeStack.push(jsonObjectNode);
        return this;
    }

    public JsonBuilder putInteger(String key, int value) {
        ((JsonObjectNode) nodeStack.peek()).put(key, value);
        return this;
    }

    public JsonBuilder putFloat(String key, float value) {
        ((JsonObjectNode) nodeStack.peek()).put(key, value);
        return this;
    }

    public JsonBuilder putBoolean(String key, boolean value) {
        ((JsonObjectNode) nodeStack.peek()).put(key, value);
        return this;
    }

    public JsonBuilder putString(String key, String value) {
        ((JsonObjectNode) nodeStack.peek()).put(key, value);
        return this;
    }

    public JsonBuilder putNull(String key) {
        ((JsonObjectNode) nodeStack.peek()).putNull(key);
        return this;
    }

    public JsonBuilder addArray() {
        JsonArrayNode jsonArrayNode = new JsonArrayNode();
        ((JsonArrayNode) nodeStack.peek()).add(jsonArrayNode);
        nodeStack.push(jsonArrayNode);
        return this;
    }

    public JsonBuilder addObject() {
        JsonObjectNode jsonObjectNode = new JsonObjectNode();
        ((JsonArrayNode) nodeStack.peek()).add(jsonObjectNode);
        nodeStack.push(jsonObjectNode);
        return this;
    }

    public JsonBuilder addInteger(int value) {
        ((JsonArrayNode) nodeStack.peek()).add(value);
        return this;
    }

    public JsonBuilder addFloat(float value) {
        ((JsonArrayNode) nodeStack.peek()).add(value);
        return this;
    }

    public JsonBuilder addBoolean(boolean value) {
        ((JsonArrayNode) nodeStack.peek()).add(value);
        return this;
    }

    public JsonBuilder addString(String value) {
        ((JsonArrayNode) nodeStack.peek()).add(value);
        return this;
    }

    public JsonBuilder addNull() {
        ((JsonArrayNode) nodeStack.peek()).addNull();
        return this;
    }

    public JsonNode build() {
        if (!nodeStack.empty()) {
            throw new IllegalStateException("Json is not build on root node");
        } else {
            return lastPopNode;
        }
    }
}
