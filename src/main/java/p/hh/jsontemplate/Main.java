package p.hh.jsontemplate;

import org.antlr.v4.runtime.*;

import p.hh.jsontemplate.parser.JsonTemplateLexer;
import p.hh.jsontemplate.parser.JsonTemplateParser;
import p.hh.jsontemplate.parserimpl.JsonTemplateTreeListener;

public class Main {
    public static void main(String[] args) {
        String jsonTemplate = "{myfield: %s}";

        JsonTemplateLexer jsonTemplateLexer = new JsonTemplateLexer(new ANTLRInputStream(jsonTemplate));
        CommonTokenStream commonTokenStream = new CommonTokenStream(jsonTemplateLexer);
        JsonTemplateParser parser = new JsonTemplateParser(commonTokenStream);

        JsonTemplateTreeListener jsonTemplateTreeVisitor = new JsonTemplateTreeListener();
//        jsonTemplateTreeVisitor.visit(parser.root());


    }
}
