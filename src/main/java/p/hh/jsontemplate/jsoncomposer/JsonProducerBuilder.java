//package p.hh.jsontemplate.jsoncomposer;
//
//import p.hh.jsontemplate.valueproducer.IValueProducer;
//
//import java.util.Stack;
//import java.util.function.Supplier;
//
//public class JsonProducerBuilder {
//
//    private Stack<JsonNode> nodeStack = new Stack<>();
//    private JsonNode lastPopNode;
//
//    public JsonProducerBuilder createArray() {
//        nodeStack.push(new JsonArrayNode());
//        return this;
//    }
//
//    public JsonProducerBuilder createObject() {
//        nodeStack.push(new JsonObjectNode());
//        return this;
//    }
//
//    public JsonProducerBuilder end() {
//        lastPopNode = nodeStack.pop();
//        return this;
//    }
//
//    public JsonProducerBuilder putArray(String key) {
//        JsonArrayNode jsonArrayNode = new JsonArrayNode();
//        ((JsonObjectNode) nodeStack.peek()).putArray(key, jsonArrayNode);
//        nodeStack.push(jsonArrayNode);
//        return this;
//    }
//
//    public JsonProducerBuilder putObject(String key) {
//        JsonObjectNode jsonObjectNode = new JsonObjectNode();
//        ((JsonObjectNode) nodeStack.peek()).putObject(key, jsonObjectNode);
//        nodeStack.push(jsonObjectNode);
//        return this;
//    }
//
//    public JsonProducerBuilder putInteger(String key, Supplier<Integer> producer) {
//        ((JsonObjectNode) nodeStack.peek()).putInteger(key, producer.get());
//        return this;
//    }
//
//    public JsonProducerBuilder putFloat(String key, Supplier<Float> producer) {
//        ((JsonObjectNode) nodeStack.peek()).putFloat(key, producer.get());
//        return this;
//    }
//
//    public JsonProducerBuilder putBoolean(String key, Supplier<Boolean> producer) {
//        ((JsonObjectNode) nodeStack.peek()).putBoolean(key, producer.get());
//        return this;
//    }
//
//    public JsonProducerBuilder putString(String key, Supplier<String> producer) {
//        ((JsonObjectNode) nodeStack.peek()).put(key, producer.get());
//        return this;
//    }
//
//    public JsonProducerBuilder putNull(String key) {
//        ((JsonObjectNode) nodeStack.peek()).putNull(key);
//        return this;
//    }
//
//    public JsonProducerBuilder addArray() {
//        JsonArrayNode jsonArrayNode = new JsonArrayNode();
//        ((JsonArrayNode) nodeStack.peek()).add(jsonArrayNode);
//        nodeStack.push(jsonArrayNode);
//        return this;
//    }
//
//    public JsonProducerBuilder addObject() {
//        JsonObjectNode jsonObjectNode = new JsonObjectNode();
//        ((JsonArrayNode) nodeStack.peek()).add(jsonObjectNode);
//        nodeStack.push(jsonObjectNode);
//        return this;
//    }
//
//    public JsonProducerBuilder addInteger(int value) {
//        ((JsonArrayNode) nodeStack.peek()).add(value);
//        return this;
//    }
//
//    public JsonProducerBuilder addFloat(float value) {
//        ((JsonArrayNode) nodeStack.peek()).add(value);
//        return this;
//    }
//
//    public JsonProducerBuilder addBoolean(boolean value) {
//        ((JsonArrayNode) nodeStack.peek()).add(value);
//        return this;
//    }
//
//    public JsonProducerBuilder addString(String value) {
//        ((JsonArrayNode) nodeStack.peek()).add(value);
//        return this;
//    }
//
//    public JsonProducerBuilder addNull() {
//        ((JsonArrayNode) nodeStack.peek()).addNull();
//        return this;
//    }
//
//    public boolean inObject() {
//        return nodeStack.peek() instanceof JsonObjectNode;
//    }
//
//    public boolean inArray() {
//        return nodeStack.peek() instanceof JsonArrayNode;
//    }
//
//    public JsonNode build() {
//        if (!nodeStack.empty()) {
//            throw new IllegalStateException("Json is not build on root node");
//        } else {
//            return lastPopNode;
//        }
//    }
//}
