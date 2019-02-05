package p.hh.jsontemplate.valueproducer;

import p.hh.jsontemplate.jsoncomposer.JsonRawStringNode;

public class RawJsonNodeProducer extends AbstractNodeProducer<JsonRawStringNode> {

    @Override
    public JsonRawStringNode produce(String value) {
        return new JsonRawStringNode(() -> value);
    }
}
