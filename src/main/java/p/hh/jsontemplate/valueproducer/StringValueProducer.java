package p.hh.jsontemplate.valueproducer;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.IllegalFormatException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StringValueProducer extends AbstractValueProducer<String> {

    private Integer length;
    private String pattern;

    StringValueProducer(String value) {
        super(value);
    }

    StringValueProducer(List<String> valueChoices) {
        super(valueChoices);
    }

    StringValueProducer(Map<String, String> parameterMap) {
        super(parameterMap);
    }


    @Override
    String parseValue(String value) throws IllegalFormatException {
        return value;
    }

    @Override
    void mapToFields() {
        this.length = getInteger("length", 10);
        this.pattern = getString("pattern", null);
    }

    @Override
    public String getName() {
        return "s";
    }

    @Override
    public String produceWithParameters() {
        if (length != null) {
            return RandomStringUtils.randomAlphabetic(length);
        }
        if (pattern != null) {
            return "";
        }
        throw new UnsupportedOperationException("no suitable parameters to produce");
    }
}
