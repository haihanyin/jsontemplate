package p.hh.jsontemplate.valueproducer;

import java.util.List;
import java.util.Map;

public interface IArrayProducer<T> {
    T produce(Map<String, String> arrayMapParam, IValueProducer<T> elementProducer, String value);
    T produce(Map<String, String> arrayMapParam, IValueProducer<T> elementProducer, List<String> valueList);
    T produce(Map<String, String> arrayMapParam, IValueProducer<T> elementProducer, Map<String, String> paramMap);
}
