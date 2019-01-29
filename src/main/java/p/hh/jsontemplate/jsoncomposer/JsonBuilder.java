package p.hh.jsontemplate.jsoncomposer;

import java.util.Stack;
import java.util.function.Supplier;

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
        ((JsonObjectNode) nodeStack.peek()).putArray(key, jsonArrayNode);
        nodeStack.push(jsonArrayNode);
        return this;
    }

    public JsonBuilder putObject(String key) {
        JsonObjectNode jsonObjectNode = new JsonObjectNode();
        ((JsonObjectNode) nodeStack.peek()).putObject(key, jsonObjectNode);
        nodeStack.push(jsonObjectNode);
        return this;
    }

    public JsonBuilder putInteger(String key, Supplier<Integer> supplier) {
        ((JsonObjectNode) nodeStack.peek()).putInteger(key, supplier);
        return this;
    }

    public JsonBuilder putFloat(String key, Supplier<Float> supplier) {
        ((JsonObjectNode) nodeStack.peek()).putFloat(key, supplier);
        return this;
    }

    public JsonBuilder putBoolean(String key, Supplier<Boolean> supplier) {
        ((JsonObjectNode) nodeStack.peek()).putBoolean(key, supplier);
        return this;
    }

    public JsonBuilder putString(String key, Supplier<String> supplier) {
        ((JsonObjectNode) nodeStack.peek()).putString(key, supplier);
        return this;
    }

    public JsonBuilder putWrapper(String key, JsonWrapperNode wrapperNode) {
        ((JsonObjectNode) nodeStack.peek()).putWrapper(key, wrapperNode);
        return this;
    }

    public JsonBuilder putNull(String key) {
        ((JsonObjectNode) nodeStack.peek()).putNull(key);
        return this;
    }

    public JsonBuilder addArray() {
        JsonArrayNode jsonArrayNode = new JsonArrayNode();
        ((JsonArrayNode) nodeStack.peek()).addArray(jsonArrayNode);
        nodeStack.push(jsonArrayNode);
        return this;
    }

    public JsonBuilder addObject() {
        JsonObjectNode jsonObjectNode = new JsonObjectNode();
        ((JsonArrayNode) nodeStack.peek()).addObject(jsonObjectNode);
        nodeStack.push(jsonObjectNode);
        return this;
    }

    public JsonBuilder addInteger(Supplier<Integer> supplier) {
        ((JsonArrayNode) nodeStack.peek()).addInteger(supplier);
        return this;
    }

    public JsonBuilder addFloat(Supplier<Float> supplier) {
        ((JsonArrayNode) nodeStack.peek()).addFloat(supplier);
        return this;
    }

    public JsonBuilder addBoolean(Supplier<Boolean> supplier) {
        ((JsonArrayNode) nodeStack.peek()).addBoolean(supplier);
        return this;
    }

    public JsonBuilder addString(Supplier<String> supplier) {
        ((JsonArrayNode) nodeStack.peek()).addString(supplier);
        return this;
    }

    public JsonBuilder addWrapper(JsonWrapperNode wrapperNode) {
        ((JsonArrayNode) nodeStack.peek()).addWrapper(wrapperNode);
        return this;
    }

    public JsonBuilder addNull() {
        ((JsonArrayNode) nodeStack.peek()).addNull();
        return this;
    }

    public boolean isEmpty() {
        return nodeStack.isEmpty();
    }

    public boolean inObject() {
        return nodeStack.peek() instanceof JsonObjectNode;
    }

    public boolean inArray() {
        return nodeStack.peek() instanceof JsonArrayNode;
    }

    public JsonNode build() {
        if (!nodeStack.empty()) {
            throw new IllegalStateException("Json is not build on root node");
        } else {
            return lastPopNode;
        }
    }
}
