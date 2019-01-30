package p.hh.jsontemplate.valueproducer;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ArrayProducer implements IArrayProducer {

    @Override
    public List produce(Map<String, String> mapParam, Supplier supplier) {
        String sizeStr = mapParam.remove("size");
        if (sizeStr != null) {
            int size = Integer.parseInt(sizeStr);
        }
        return null;
    }

    @Override
    public List produce(Map<String, String> mapParam, List<Supplier> valueList) {
        return null;
    }
}
