package p.hh.jsontemplate.main;

import com.jayway.jsonpath.DocumentContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static p.hh.jsontemplate.test.support.TestUtils.*;

@RunWith(JUnit4.class)
public class JsonTemplateTypeTest {


    @Test
    public void test_twoLevelObject() {
        DocumentContext document = parse("{" +
                    "%address:{city:%s,street:%s(A),number:%i},%address2:{city:%s(B)}," +
                    "office:%address, home:%address2" +
                "}");
        exist(document, "$.office.city", String.class);
        exist(document, "$.office.street", String.class);
        exist(document, "$.office.number", Integer.class);
        exist(document, "$.home.city", String.class);
        exist(document, "$.home.street", String.class);
        exist(document, "$.home.number", Integer.class);
    }



}
