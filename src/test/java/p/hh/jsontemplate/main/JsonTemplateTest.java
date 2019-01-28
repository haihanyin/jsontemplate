package p.hh.jsontemplate.main;


import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class JsonTemplateTest {

    @Test
    public void test1() {
        String template = "{aField : %s}";
        String json = JsonTemplate.toJson(template);
        System.out.println(json);
        DocumentContext document = JsonPath.parse(json);
        document.read("$.aField", String.class);
    }

    @Test
    public void test2() {
        String template = "{aField : %s(myValue)}";
        String json = JsonTemplate.toJson(template);
        System.out.println(json);
    }

    @Test
    public void test3() {
        String template = "{aField : %s(A, B, C, D)}";
        String json = JsonTemplate.toJson(template);
        System.out.println(json);
    }

    @Test
    public void test4() {
        String template = "{aStringField : %s(min=3)}";
        String json = JsonTemplate.toJson(template);
        System.out.println(json);
    }

    @Test
    public void test5() {
        String template = "{aStringField : %s(min=3, max=5)}";
        String json = JsonTemplate.toJson(template);
        System.out.println(json);
    }

}


