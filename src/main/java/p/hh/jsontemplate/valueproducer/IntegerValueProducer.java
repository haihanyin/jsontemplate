package p.hh.jsontemplate.valueproducer;

import java.util.Map;
import java.util.Random;

public class IntegerValueProducer extends AbstractValueProducer<Integer> {

    @Override
    public Class<Integer> getValueType() {
        return Integer.class;
    }

    @Override
    public Integer produce(String value) {
        return Integer.parseInt(value);
    }

    @Override
    public Integer produce(Map<String, String> paramMap) {
        Integer min = getIntegerParam(paramMap, "min");
        Integer max = getIntegerParam(paramMap, "max");
        Random random = new Random();
        if (min != null && max != null && min < max) {
            return createIntegerInRange(min, max);
        } else if (min != null && max == null) {
            return createIntegerInRange(min, 2*min);
        } else if (min == null && max != null) {
            return createIntegerInRange(0, max);
        } else {
            return createIntegerInRange(0, 100);
        }
    }

    protected Integer createIntegerInRange(int min, int max) {
        int bound = max - min;
        return new Random().nextInt(bound) + min;
    }
}
