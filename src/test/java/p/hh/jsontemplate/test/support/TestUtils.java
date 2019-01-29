package p.hh.jsontemplate.test.support;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import p.hh.jsontemplate.main.JsonTemplate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class TestUtils {

    public static DocumentContext parse(String template) {
        System.out.println("===== Template =====");
        System.out.println(template);
        String json = JsonTemplate.toJson(template);
        System.out.println("===== Generated Json =====");
        System.out.println(json);
        return JsonPath.parse(json);
    }

    public static void exist(DocumentContext document, String path, Class type) {
        assertThat(document.read(path, type), is(notNullValue()));
    }
}
