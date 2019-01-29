package p.hh.jsontemplate.supplier;

import p.hh.jsontemplate.valueproducer.IValueProducer;

import java.util.List;
import java.util.function.Supplier;

public class ListParamSupplier<T> implements Supplier<T> {

    private IValueProducer<T> valueProducer;
    private List<String> listParam;

    public ListParamSupplier(IValueProducer<T> valueProducer, List<String> listParam) {
        this.valueProducer = valueProducer;
        this.listParam = listParam;
    }

    @Override
    public T get() {
        return valueProducer.produce(listParam);
    }
}
