package p.hh.jsontemplate.jsoncomposer;

public class JsonIntegerNode extends AbstractJsonValueNode<Integer> {

    public JsonIntegerNode(Integer value) {
        super(value);
    }

    @Override
    public String print() {
        return Integer.toString(value);
    }
}
