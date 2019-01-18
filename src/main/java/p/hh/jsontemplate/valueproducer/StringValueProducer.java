package p.hh.jsontemplate.valueproducer;

import java.util.IllegalFormatException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class StringValueProducer extends AbstractValueProducer<String> {

    private final static String ALPHABETIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private int length = 5;

    public StringValueProducer(String value) {
        super(value);
    }

    public StringValueProducer(List<String> valueChoices) {
        super(valueChoices);
    }

    public StringValueProducer(Map<String, String> parameterMap) {
        super(parameterMap);
        mapToFields(parameterMap);
    }

    @Override
    protected String produceWithParameters() {
        char[] chars = new char[this.length];
        Random random = new Random();
        for (int i=0; i<length; i++) {
            int index = random.nextInt(ALPHABETIC.length());
            chars[i] = ALPHABETIC.charAt(index);
        }
        return new String(chars);
    }
}
