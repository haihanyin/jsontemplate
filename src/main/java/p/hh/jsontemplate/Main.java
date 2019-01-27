package p.hh.jsontemplate;

import org.antlr.v4.runtime.*;

import org.antlr.v4.runtime.tree.ParseTreeWalker;
import p.hh.jsontemplate.parser.JsonTemplateLexer;
import p.hh.jsontemplate.parser.JsonTemplateParser;
import p.hh.jsontemplate.parserimpl.JsonTemplateTreeListener;

public class Main {
    public static void main(String[] args) {
        String jsonTemplate = "{myfield: %s}";

//        String jsonTemplate = "{%address: {city: %s, street: %s, number %i}, " +
//                "info : { name : %s, adress: %adress}}";

        JsonTemplateLexer jsonTemplateLexer = new JsonTemplateLexer(new ANTLRInputStream(jsonTemplate));
        CommonTokenStream commonTokenStream = new CommonTokenStream(jsonTemplateLexer);
        JsonTemplateParser parser = new JsonTemplateParser(commonTokenStream);

        JsonTemplateTreeListener listener = new JsonTemplateTreeListener();

        ParseTreeWalker parseTreeWalker = new ParseTreeWalker();
        parseTreeWalker.walk(listener, parser.root());

        for(int i=0; i<3; i++) {
            String s = listener.getJsonBuilder().build().prettyPrint(0);
            System.out.println(s);
        }
    }
}
