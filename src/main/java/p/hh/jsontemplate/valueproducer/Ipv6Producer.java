package p.hh.jsontemplate.valueproducer;

import p.hh.jsontemplate.jsoncomposer.JsonStringNode;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Ipv6Producer extends AbstractNodeProducer<JsonStringNode> {

    private static final String letters = "0123456789abcdef";

    @Override
    public JsonStringNode produce() {
        return new JsonStringNode(this::produceIp);
    }

    private String produceIp() {
        Random random = new Random();

        return IntStream.range(0, 8)
                .mapToObj( i -> produceGroup())
                .collect(Collectors.joining(":"));
    }

    private String produceGroup() {
        Random random = new Random();
        int length = letters.length();
        char[] group = new char[] {
                letters.charAt(random.nextInt(length)),
                letters.charAt(random.nextInt(length)),
                letters.charAt(random.nextInt(length)),
                letters.charAt(random.nextInt(length))
        };
        return new String(group);
    }
}
