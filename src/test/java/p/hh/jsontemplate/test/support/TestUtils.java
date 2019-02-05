package p.hh.jsontemplate.test.support;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import p.hh.jsontemplate.jsoncomposer.JsonNode;
import p.hh.jsontemplate.jtm.PropertyDeclaration;
import p.hh.jsontemplate.main.JsonTemplate;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class TestUtils {

    public static DocumentContext parse(String template) {
        System.out.println("===== Template =====");
        System.out.println(template);
        JsonNode jsonNode = new JsonTemplate().parse(template);
        String json = jsonNode.prettyPrint(0);
        System.out.println("===== Generated Json =====");
        System.out.println(json);
        return JsonPath.parse(json);
    }

    public static DocumentContext parse(String template, Map<String, Object> variableMap) {
        System.out.println("===== Template =====");
        System.out.println(template);
        JsonNode jsonNode = new JsonTemplate().withVariables(variableMap).parse(template);
        String json = jsonNode.prettyPrint(0);
        System.out.println("===== Generated Json =====");
        System.out.println(json);
        return JsonPath.parse(json);
    }
}
