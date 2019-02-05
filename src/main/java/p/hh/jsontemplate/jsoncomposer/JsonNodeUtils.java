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
}
