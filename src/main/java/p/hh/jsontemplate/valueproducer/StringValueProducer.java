package p.hh.jsontemplate.valueproducer;

import java.util.IllegalFormatException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class StringValueProducer extends AbstractValueProducer<String> {

    private final static String ALPHABETIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private final static int DEFAULT_LENGTH = 5;

    private Integer size;
    private Integer min;
    private Integer max;

    @Override
    public Class<String> getValueType() {
        return String.class;
    }

    @Override
    public String produce() {
        return produceString(DEFAULT_LENGTH);
    }

    @Override
    public String produce(String value) {
        return value;
    }

    @Override
    public String produce(List<String> valueList) {
        return super.produce(valueList);
    }

    @Override
    public String produce(Map<String, String> paramMap) {
        Integer size = getIntegerParam(paramMap, "size");
        Integer min = getIntegerParam(paramMap, "min");
        Integer max = getIntegerParam(paramMap, "max");

        if (size != null) {
            return produceString(size);
        } else if (min != null && max != null) {
            return produceString(randomInRange(min, max));
        } else if (min != null) { // max == null
            return produceString(randomInRange(min, 2*min));
        } else if (max != null) { // min == null
            return produceString(randomInRange(0, max));
        } else { // no expected parameters
            return produce();
        }
    }


    private String produceString(int length) {
        char[] chars = new char[length];
        Random random = new Random();
        for (int i=0; i<length; i++) {
            int index = random.nextInt(ALPHABETIC.length());
            chars[i] = ALPHABETIC.charAt(index);
        }
        return new String(chars);
    }
}
