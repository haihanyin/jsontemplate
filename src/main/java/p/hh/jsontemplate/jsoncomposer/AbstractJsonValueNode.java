package p.hh.jsontemplate.jsoncomposer;

public abstract class AbstractJsonValueNode<T> implements JsonValueNode {

    protected T value;

    public AbstractJsonValueNode(T value) {
        this.value = value;
    }

    @Override
    public String prettyPrint(int identation) {
        return print();
    }
}
