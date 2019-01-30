package p.hh.jsontemplate.valueproducer;

import p.hh.jsontemplate.jsoncomposer.JsonArrayNode;
import p.hh.jsontemplate.jsoncomposer.JsonNode;

import java.util.*;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ArrayNodeProducer  {

    public JsonArrayNode produce(JsonNode defaultNode) {
        return new JsonArrayNode(() -> createNodeList(defaultNode, 2));
    }

    public JsonArrayNode produce(JsonNode defaultNode, Map<String, String> arrayMapParam) {
        String size = arrayMapParam.get("size");
        int parsedSize = Integer.parseInt(size);

        return new JsonArrayNode(() -> createNodeList(defaultNode, parsedSize));
    }

    public JsonArrayNode produce(JsonNode defaultNode, Map<String, String> arrayMapParam, List<JsonNode> elements) {
        String size = arrayMapParam.get("size");
        int parsedSize = Integer.parseInt(size);

        if (parsedSize > elements.size()) {
            return new JsonArrayNode(() -> createNodeList(elements, defaultNode, parsedSize - elements.size()));
        } else {
            return new JsonArrayNode(() -> elements);
        }
    }

    public JsonArrayNode produce(Map<String, String> arrayMapParam, List<JsonNode> elements) {
        return null;
    }

    private List<JsonNode> createNodeList(List<JsonNode> elements, JsonNode defaultNode, int size) {
        ArrayList<JsonNode> nodeList = new ArrayList<>(elements.size() + size);
        nodeList.addAll(elements);
        IntStream.range(0, size).forEach(i -> nodeList.add(defaultNode));
        return nodeList;
    }

    private List<JsonNode> createNodeList(JsonNode node, int size) {
        JsonNode[] nodes = new JsonNode[size];
        Arrays.fill(nodes, node);
        return Arrays.asList(nodes);
    }

}
