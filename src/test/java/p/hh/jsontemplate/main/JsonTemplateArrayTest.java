package p.hh.jsontemplate.main;

import com.jayway.jsonpath.DocumentContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static p.hh.jsontemplate.test.support.TestUtils.parse;

@RunWith(JUnit4.class)
public class JsonTemplateArrayTest {

    @Test
    public void test1() {
        DocumentContext document = parse("{anArray:%s[1, 2, 3, 4]}");
    }

    @Test
    public void test2() {
        DocumentContext document = parse("{anArray:%s[1, 2](size=5)}");
    }

    @Test
    public void test3() {
        DocumentContext document = parse("{anArray:%s[1, 2, %i(3), 4]}");
    }
}
