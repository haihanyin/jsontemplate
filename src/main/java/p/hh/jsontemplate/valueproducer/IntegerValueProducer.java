package p.hh.jsontemplate.valueproducer;

import java.util.Map;
import java.util.Random;

public class IntegerValueProducer extends AbstractValueProducer<Integer> {

    @Override
    public Class<Integer> getValueType() {
        return Integer.class;
    }

    @Override
    public Integer produce() {
        return randomInRange(0, 100);
    }

    @Override
    public Integer produce(String value) {
        return Integer.parseInt(value);
    }

    @Override
    public Integer produce(Map<String, String> paramMap) {
        Integer min = pickIntegerParam(paramMap, "min");
        Integer max = pickIntegerParam(paramMap, "max");

        validateParamMap(paramMap);

        if (min != null && max != null && min < max) {
            return randomInRange(min, max);
        } else if (min != null && max == null) {
            return randomInRange(min, 2*min);
        } else if (min == null && max != null) {
            return randomInRange(0, max);
        } else {
            return produce();
        }
    }

}
