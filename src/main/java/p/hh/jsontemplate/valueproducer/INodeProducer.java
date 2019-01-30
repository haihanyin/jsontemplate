package p.hh.jsontemplate.valueproducer;

import p.hh.jsontemplate.jsoncomposer.JsonNode;

import java.util.List;
import java.util.Map;

public interface INodeProducer<T extends JsonNode> {

    T produce();
    T produce(String value);
    T produce(List<String> valueList);
    T produce(Map<String, String> paramMap);
}
