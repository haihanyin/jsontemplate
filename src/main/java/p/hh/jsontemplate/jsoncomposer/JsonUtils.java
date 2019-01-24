package p.hh.jsontemplate.jsoncomposer;

import java.util.Arrays;

public class JsonUtils {

    public static String makeIdentation(int count) {
        if (count <= 0) {
            return "";
        }
        char[] spaces = new char[count * 2];
        Arrays.fill(spaces, ' ');
        return new String(spaces);
    }
}
