package p.hh.jsontemplate.valueproducer2;

import java.util.function.Function;
import java.util.function.Supplier;

public class MyMain {

    public static void main(String[] args) {
        Supplier<String> stringSupplier = new Supplier<String>() {
            @Override
            public String get() {
                return "hello";
            }
        };

        Function<Integer, String> stringFunction = integer -> "hello".substring(integer);
    }
}
