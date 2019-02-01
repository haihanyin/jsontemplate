package p.hh.jsontemplate.jsoncomposer;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class JsonNodeUtils {

    public static String makeIdentation(int count) {
        if (count <= 0) {
            return "";
        }
        char[] spaces = new char[count * 2];
        Arrays.fill(spaces, ' ');
        return new String(spaces);
    }

    public static JsonNode objectToNode(Object obj) {
        JsonNode jsonNode;
        if (obj == null) {
            jsonNode = new JsonNullNode();
        } else if (obj instanceof Integer) {
            jsonNode = JsonIntegerNode.of((Integer) obj);
        } else if (obj instanceof Boolean) {
            jsonNode = JsonBooleanNode.of((Boolean) obj);
        } else if (obj instanceof String) {
            jsonNode = JsonStringNode.of((String) obj);
        } else if (obj instanceof Collection) {
            jsonNode = JsonArrayNode.of((Collection<?>) obj);
        } else if (obj instanceof Map) {
            jsonNode = JsonObjectNode.of((Map) obj);
        } else {
            jsonNode = JsonStringNode.of(obj.toString());
        }
        return jsonNode;
    }
}
