package p.hh.jsontemplate.supplier;

import p.hh.jsontemplate.valueproducer.IValueProducer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class MapParamSupplier<T> implements Supplier<T> {

    private IValueProducer<T> valueProducer;
    private Map<String, String> mapParam;

    public MapParamSupplier(IValueProducer<T> valueProducer, Map<String, String> mapParam) {
        this.valueProducer = valueProducer;
        this.mapParam = mapParam;
    }

    @Override
    public T get() {
        return valueProducer.produce(new HashMap<>(mapParam));
    }
}
