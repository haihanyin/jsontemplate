package p.hh.jsontemplate.main;

import com.jayway.jsonpath.DocumentContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static p.hh.jsontemplate.test.support.TestUtils.parse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.Matchers.notNullValue;
import static p.hh.jsontemplate.test.support.TestUtils.*;

@RunWith(JUnit4.class)
public class JsonTemplateVariableTest {

    @Test
    public void test_stringVariable() {
        Map<String, Object> varMap = new HashMap<>();
        varMap.put("name", "John");
        DocumentContext document = parse("{name : $name}", varMap);
        assertThat(document.read("$.name", String.class), is("John"));
    }

    @Test
    public void test_integerVariable() {
        Map<String, Object> varMap = new HashMap<>();
        varMap.put("age", 20);
        DocumentContext document = parse("{age : $age}", varMap);
        assertThat(document.read("$.age", Integer.class), is(20));
    }

    @Test
    public void test_booleanVariable() {
        Map<String, Object> varMap = new HashMap<>();
        varMap.put("male", true);
        DocumentContext document = parse("{male : $male}", varMap);
        assertThat(document.read("$.male", Boolean.class), is(true));
    }

    @Test
    public void test_arrayVariable() {
        Map<String, Object> varMap = new HashMap<>();
        varMap.put("letters", new String[] {"A", "B", "C"});
        DocumentContext document = parse("{letters : $letters}", varMap);
        assertThat(document.read("$.letters[0]", String.class), is("A"));
        assertThat(document.read("$.letters[1]", String.class), is("B"));
        assertThat(document.read("$.letters[2]", String.class), is("C"));
    }

    @Test
    public void test_listVariable() {
        Map<String, Object> varMap = new HashMap<>();
        varMap.put("letters", Arrays.asList("A", "B", "C"));
        DocumentContext document = parse("{letters : $letters}", varMap);
        assertThat(document.read("$.letters[0]", String.class), is("A"));
        assertThat(document.read("$.letters[1]", String.class), is("B"));
        assertThat(document.read("$.letters[2]", String.class), is("C"));
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
