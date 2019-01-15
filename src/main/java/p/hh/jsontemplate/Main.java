package p.hh.jsontemplate;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import p.hh.jsontemplate.parser.JsonTemplateLexer;
import p.hh.jsontemplate.parser.JsonTemplateParser;

public class Main {
    public static void main(String[] args) {
        String example = "{\n" +
                "    aCharField : %c,\n" +
                "    aStringField : %s @Size(max=5, min=3),\n" +
                "    aIntegerField : %d @Max(100) @Min(10),\n" +
                "    aArrayField : @Size(max=6) {\n" +
                "        item : %s\n" +
                "    }\n" +
                "}\n";
        CodePointCharStream codePointCharStream = CharStreams.fromString(example);
        JsonTemplateLexer jsonTemplateLexer = new JsonTemplateLexer(codePointCharStream);
        CommonTokenStream commonTokenStream = new CommonTokenStream(jsonTemplateLexer);
        JsonTemplateParser parser = new JsonTemplateParser(commonTokenStream);
        System.out.println("done");
    }
}
