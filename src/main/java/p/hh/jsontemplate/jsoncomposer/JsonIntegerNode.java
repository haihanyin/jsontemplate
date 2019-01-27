package p.hh.jsontemplate.jsoncomposer;

import java.util.function.Supplier;

public class JsonIntegerNode extends AbstractJsonValueNode<Integer> {

    public JsonIntegerNode(Supplier<Integer> supplier) {
        super(supplier);
    }

    @Override
    public String print() {
        return Integer.toString(supplier.get());
    }
}
