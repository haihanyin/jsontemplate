package p.hh.jsontemplate.test.support;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import p.hh.jsontemplate.main.JsonTemplate;

public class TestUtils {

    public static DocumentContext parse(String template) {
        System.out.println("===== Template =====");
        System.out.println(template);
        String json = JsonTemplate.toJson(template);
        System.out.println("===== Generated Json =====");
        System.out.println(json);
        return JsonPath.parse(json);
    }
}
