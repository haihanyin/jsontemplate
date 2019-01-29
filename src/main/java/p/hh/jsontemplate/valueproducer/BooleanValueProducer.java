package p.hh.jsontemplate.valueproducer;

import java.util.Map;
import java.util.Random;

public class BooleanValueProducer extends AbstractValueProducer<Boolean> {

    @Override
    public Class<Boolean> getValueType() {
        return Boolean.class;
    }

    @Override
    public Boolean produce() {
        return new Random().nextBoolean();
    }

    @Override
    public Boolean produce(String value) {
        return Boolean.parseBoolean(value);
    }

}
