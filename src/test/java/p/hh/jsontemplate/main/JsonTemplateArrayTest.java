package p.hh.jsontemplate.main;

import com.jayway.jsonpath.DocumentContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static p.hh.jsontemplate.test.support.TestUtils.parse;

@RunWith(JUnit4.class)
public class JsonTemplateArrayTest {

    @Test
    public void test_emptyArray() {
        DocumentContext document = parse("{anArray : %s[]}");
        assertThat(document.read("$.anArray[0]", String.class), is(nullValue()));
    }

    @Test
    public void test_nonEmptyArray() {
        DocumentContext document = parse("{anArray : %s[](size=3)}");
        assertThat(document.read("$.anArray[0]", String.class), is(nullValue()));
    }

}
