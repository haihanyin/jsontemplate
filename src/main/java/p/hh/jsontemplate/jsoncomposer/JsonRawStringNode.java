package p.hh.jsontemplate.jsoncomposer;

import java.util.function.Supplier;

public class JsonRawStringNode extends JsonStringNode {

    public JsonRawStringNode(Supplier<String> supplier) {
        super(supplier);
    }

    @Override
    public String print() {
        return supplier.get();
    }
}
