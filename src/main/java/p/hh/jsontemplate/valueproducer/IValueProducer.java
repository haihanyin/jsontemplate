package p.hh.jsontemplate.valueproducer;

import java.util.List;
import java.util.Map;

public interface IValueProducer<T> {
    T produce(String value);
    T produce(List<String> valueList);
    T produce(Map<String, String> paramMap);
}
