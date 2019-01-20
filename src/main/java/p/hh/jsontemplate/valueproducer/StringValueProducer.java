package p.hh.jsontemplate.valueproducer;

import java.util.IllegalFormatException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class StringValueProducer extends AbstractValueProducer<String> {

    private final static String ALPHABETIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private final static int DEFAULT_LENGTH = 5;

    @Override
    public String produce(String value) {
        return value;
    }

    @Override
    public String produce(Map<String, String> paramMap) {
        Integer length = getIntegerParam(paramMap, "length", DEFAULT_LENGTH);
        char[] chars = new char[length];
        Random random = new Random();
        for (int i=0; i<length; i++) {
            int index = random.nextInt(ALPHABETIC.length());
            chars[i] = ALPHABETIC.charAt(index);
        }
        return new String(chars);
    }

}
