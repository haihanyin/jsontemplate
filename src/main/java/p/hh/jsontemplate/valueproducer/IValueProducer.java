package p.hh.jsontemplate.valueproducer;

public interface IValueProducer<T> {

    String getName();
    T produce();
}
