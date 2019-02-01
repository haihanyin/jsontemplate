package p.hh.jsontemplate.valueproducer;

import p.hh.jsontemplate.jsoncomposer.JsonStringNode;

import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import java.util.stream.Collectors;

public class Base64Producer extends AbstractNodeProducer<JsonStringNode> {

    @Override
    public JsonStringNode produce() {
        return new JsonStringNode(this::produceBase64);
    }

    private String produceBase64() {
        byte[] bytes = Long.toString(System.currentTimeMillis()).getBytes();
        return Base64.getEncoder().encodeToString(bytes);
    }
}
