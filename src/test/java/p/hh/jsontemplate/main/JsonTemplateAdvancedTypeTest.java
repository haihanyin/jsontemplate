package p.hh.jsontemplate.main;

import com.jayway.jsonpath.DocumentContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static p.hh.jsontemplate.test.support.TestUtils.parse;

@RunWith(JUnit4.class)
public class JsonTemplateAdvancedTypeTest {

    @Test
    public void test_ip() {
        DocumentContext document = parse("{ipField : %ip}");
        assertThat(document.read("$.ipField", String.class), is(notNullValue()));
    }

    @Test
    public void test_ipv6() {
        DocumentContext document = parse("{ipv6Field : %ipv6}");
        assertThat(document.read("$.ipv6Field", String.class), is(notNullValue()));
    }

    @Test
    public void test_base64() {
        DocumentContext document = parse("{base64Field : %base64}");
        assertThat(document.read("$.base64Field", String.class), is(notNullValue()));
    }
}
