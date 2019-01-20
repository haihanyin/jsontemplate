package p.hh.jsontemplate.valueproducer;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ArrayProducer extends AbstractValueProducer implements IArrayProducer {

    private final static int SIZE = 3;

    ArrayProducer() {

    }

    public <T> List<T> produce(Map<String, String> arrayParamMap,
                               IValueProducer<T> valueProducer,
                               String elementValue) {
        Integer size = getIntegerParam(arrayParamMap, "size", SIZE);
        return produceValueArray(size, valueProducer::produce, elementValue);
    }

    public <T> List<T> produce(Map<String, String> arrayParamMap,
                               IValueProducer<T> valueProducer,
                               List<String> elementOptions) {
        Integer size = getIntegerParam(arrayParamMap, "size", SIZE);
        return produceValueArray(size, valueProducer::produce, elementOptions);
    }

    public <T> List<T> produce(Map<String, String> arrayParamMap,
                               IValueProducer<T> valueProducer,
                               Map<String, String> elementParamMap) {
        Integer size = getIntegerParam(arrayParamMap, "size", SIZE);
        return produceValueArray(size, valueProducer::produce, elementParamMap);
    }

    private <E, T> List<T> produceValueArray(int size, Function<E, T> function, E expr) {
        return IntStream.range(0, size).mapToObj(i -> {
            return function.apply(expr);
        }).collect(Collectors.toList());
    }
}
