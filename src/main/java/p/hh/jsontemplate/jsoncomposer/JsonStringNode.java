package p.hh.jsontemplate.jsoncomposer;

import java.util.function.Supplier;

public class JsonStringNode extends AbstractJsonValueNode<String> {

    public JsonStringNode(Supplier<String> supplier) {
        super(supplier);
    }

    @Override
    public String print() {
        return "\"" + supplier.get() + "\"";
    }

}
