package p.hh.jsontemplate.jsoncomposer;

public class JsonWrapperNode implements JsonNode {

    private JsonNode jsonNode;

    public void setJsonNode(JsonNode jsonNode) {
        this.jsonNode = jsonNode;
    }

    public JsonNode getJsonNode() {
        return jsonNode;
    }

    @Override
    public String print() {
        return jsonNode.print();
    }

    @Override
    public String prettyPrint(int identation) {
        return jsonNode.prettyPrint(identation);
    }
}
