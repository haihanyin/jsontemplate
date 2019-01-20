package p.hh.jsontemplate;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;

import org.apache.commons.lang3.ObjectUtils;
import p.hh.jsontemplate.valueproducer.StringValueProducer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        StringValueProducer stringValueProducer = new StringValueProducer();
        System.out.println("-----");
        IntStream.range(0, 10).forEach(i -> {
            System.out.println(stringValueProducer.produce("helloworld"));
        });

        System.out.println("-----");
        IntStream.range(0, 10).forEach(i -> {
            System.out.println(stringValueProducer.produce(Arrays.asList("A1", "B1", "C1")));
        });

        System.out.println("-----");
        Map<String, String> map = new HashMap<>();
        IntStream.range(0, 10).forEach(i -> {
            System.out.println(stringValueProducer.produce(map));
        });

        System.out.println("-----");
        Map<String, String> map2 = new HashMap<>();
        map2.put("length", "10");
        IntStream.range(0, 10).forEach(i -> {
            System.out.println(stringValueProducer.produce(map2));
        });
    }
}
