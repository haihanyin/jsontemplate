package p.hh.jsontemplate;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;

import p.hh.jsontemplate.valueproducer.StringValueProducer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) {
        System.out.println("-----");
        StringValueProducer svProducer1 = new StringValueProducer("helloworld");
        IntStream.range(0, 10).forEach(i -> { System.out.println(svProducer1.produce()); });

        System.out.println("-----");
        StringValueProducer svProducer2 = new StringValueProducer(Arrays.asList("A1", "B1", "C1"));
        IntStream.range(0, 10).forEach(i -> { System.out.println(svProducer2.produce());});

        System.out.println("-----");
        Map<String, String> map = new HashMap<>();
        StringValueProducer svProducer3 = new StringValueProducer(map);
        IntStream.range(0, 10).forEach(i -> { System.out.println(svProducer3.produce());});

        System.out.println("-----");
        Map<String, String> map2 = new HashMap<>();
        map2.put("length", "10");
        StringValueProducer svProducer4 = new StringValueProducer(map2);
        IntStream.range(0, 10).forEach(i -> { System.out.println(svProducer4.produce());});
    }
}
