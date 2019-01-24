package p.hh.jsontemplate.valueproducer;

import java.util.Map;

public class BooleanValueProducer extends AbstractValueProducer<Boolean> {

    @Override
    public Class<Boolean> getValueType() {
        return Boolean.class;
    }

    @Override
    public Boolean produce(String value) {
        return Boolean.parseBoolean(value);
    }

}
