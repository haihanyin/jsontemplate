package p.hh.jsontemplate.test.support;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import p.hh.jsontemplate.jtm.PropertyDeclaration;
import p.hh.jsontemplate.main.JsonTemplate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class TestUtils {

    public static DocumentContext parse(String template) {
        System.out.println("===== Template =====");
        System.out.println(template);
        PropertyDeclaration propertyDeclaration = JsonTemplate.toJson(template);
//        String json = propertyDeclaration;
//        System.out.println("===== Generated Json =====");
//        System.out.println(json);
        return JsonPath.parse("");
    }
}
