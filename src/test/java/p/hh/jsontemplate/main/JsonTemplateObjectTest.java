package p.hh.jsontemplate.main;

import com.jayway.jsonpath.DocumentContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static p.hh.jsontemplate.test.support.TestUtils.exist;
import static p.hh.jsontemplate.test.support.TestUtils.parse;

@RunWith(JUnit4.class)
public class JsonTemplateObjectTest {

    @Test
    public void test_twoLevelObject() {
        DocumentContext document = parse("{anObject:{aField:%s}}");
        exist(document, "$.anObject.aField", String.class);
    }

    @Test
    public void test_anObjectWithTwoFields() {
        DocumentContext document = parse("{anObject:{aField:%s, bField:%s}}");
        exist(document, "$.anObject.aField", String.class);
        exist(document, "$.anObject.bField", String.class);
    }

    @Test
    public void test_multipleLevelObject() {
        DocumentContext document = parse("{objA:{objB:{objC:{objD:{aField:%s}}}}}");
        exist(document, "$.objA.objB.objC.objD.aField", String.class);
    }

    @Test
    public void test_twoObjects() {
        DocumentContext document = parse("{objA:{fieldA:%s},objB:{fieldB:%i}}");
        exist(document, "$.objA.fieldA", String.class);
        exist(document, "$.objB.fieldB", Integer.class);
    }

}
