package p.hh.jsontemplate.main;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import p.hh.jsontemplate.jsoncomposer.JsonNode;
import p.hh.jsontemplate.jtm.PropertyDeclaration;
import p.hh.jsontemplate.parser.JsonTemplateLexer;
import p.hh.jsontemplate.parser.JsonTemplateParser;
import p.hh.jsontemplate.parserimpl.JsonTemplateTreeListener;

public class JsonTemplate {

    public static PropertyDeclaration toJson(String template) {
        JsonTemplateLexer jsonTemplateLexer = new JsonTemplateLexer(new ANTLRInputStream(template));
        CommonTokenStream commonTokenStream = new CommonTokenStream(jsonTemplateLexer);
        JsonTemplateParser parser = new JsonTemplateParser(commonTokenStream);

        JsonTemplateTreeListener listener = new JsonTemplateTreeListener();

        ParseTreeWalker parseTreeWalker = new ParseTreeWalker();
        parseTreeWalker.walk(listener, parser.root());
        return listener.getRoot();
    }
}
