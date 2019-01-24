package p.hh.jsontemplate.jsoncomposer;

public class JsonFloatNode extends AbstractJsonValueNode<Float> {

    public JsonFloatNode(Float value) {
        super(value);
    }

    @Override
    public String print() {
        return Float.toString(value);
    }
}
