package p.hh.jsontemplate.jsoncomposer;

public class JsonBooleanNode extends AbstractJsonValueNode<Boolean> {

    public JsonBooleanNode(Boolean value) {
        super(value);
    }

    @Override
    public String print() {
        return Boolean.toString(value);
    }

}
