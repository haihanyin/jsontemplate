package p.hh.jsontemplate.jsoncomposer;

import java.util.function.Supplier;

public class JsonBooleanNode extends AbstractJsonValueNode<Boolean> {

    public JsonBooleanNode(Supplier<Boolean> supplier) {
        super(supplier);
    }

    @Override
    public String print() {
        return Boolean.toString(supplier.get());
    }

}
