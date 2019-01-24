package p.hh.jsontemplate.jsoncomposer;

public class JsonStringNode extends AbstractJsonValueNode<String> {

    public JsonStringNode(String value) {
        super(value);
    }

    @Override
    public String print() {
        return "\"" + value + "\"";
    }

}
