package p.hh.jsontemplate.main;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static p.hh.jsontemplate.test.support.TestUtils.parse;

@RunWith(JUnit4.class)
public class JsonTemplateVariableTest {

    @Test
    public void test_stringVariable() {
        Map<String, Object> varMap = new HashMap<>();
        varMap.put("name", "John");
        parse("{name : $name}", varMap);
    }

    @Test
    public void test_integerVariable() {
        Map<String, Object> varMap = new HashMap<>();
        varMap.put("age", 20);
        parse("{age : $age}", varMap);
    }

    @Test
    public void test_booleanVariable() {
        Map<String, Object> varMap = new HashMap<>();
        varMap.put("male", true);
        parse("{male : $male}", varMap);
    }

    @Test
    public void test_arrayVariable() {
        Map<String, Object> varMap = new HashMap<>();
        varMap.put("letters", new String[] {"A", "B", "C"});
        parse("{letters : $letters}", varMap);
    }

    @Test
    public void test_listVariable() {
        Map<String, Object> varMap = new HashMap<>();
        varMap.put("letters", Arrays.asList("A", "B", "C"));
        parse("{letters : $letters}", varMap);
    }

    @Test
    public void test_mapVariable() {

        Map<String, Object> john = new HashMap<>();
        john.put("name", "John");
        john.put("age", 20);
        john.put("male", true);
        john.put("roles", Arrays.asList("Admin", "Finance", "HR"));

        Map<String, Object> varMap = new HashMap<>();
        varMap.put("person", john);

        parse("{person : $person}", varMap);
    }

    @Test
    public void test_temp() {
        Map<String, Object> varMap = new HashMap<>();
        String json = "{\n" +
                "  \"string\": \"Hello World\"\n" +
                "}";
        varMap.put("json", json);
        parse("{json : %raw($json)}", varMap);
    }
}
