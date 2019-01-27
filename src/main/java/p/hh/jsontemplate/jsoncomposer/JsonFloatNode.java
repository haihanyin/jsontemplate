package p.hh.jsontemplate.jsoncomposer;

import java.util.function.Supplier;

public class JsonFloatNode extends AbstractJsonValueNode<Float> {

    public JsonFloatNode(Supplier<Float> supplier) {
        super(supplier);
    }

    @Override
    public String print() {
        return Float.toString(supplier.get());
    }
}
