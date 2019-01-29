package p.hh.jsontemplate.supplier;

import p.hh.jsontemplate.valueproducer.IValueProducer;

import java.util.function.Supplier;

public class SingleParamSupplier<T> implements Supplier<T> {

    private IValueProducer<T> valueProducer;
    private String singleParam;

    public SingleParamSupplier(IValueProducer<T> valueProducer, String singleParam) {
        this.valueProducer = valueProducer;
        this.singleParam = singleParam;
    }

    @Override
    public T get() {
        return valueProducer.produce(singleParam);
    }
}
