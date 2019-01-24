package p.hh.jsontemplate.parserimpl;

import p.hh.jsontemplate.parser.JsonTemplateBaseListener;
import p.hh.jsontemplate.parser.JsonTemplateBaseVisitor;
import p.hh.jsontemplate.parser.JsonTemplateParser;
import p.hh.jsontemplate.valueproducer.IValueProducer;
import p.hh.jsontemplate.valueproducer.StringValueProducer;

import java.util.Map;

public class JsonTemplateTreeListener extends JsonTemplateBaseListener {

    private Map<String, IValueProducer> valueProducerMap;
    private IValueProducer currentValueProducer;



    public JsonTemplateTreeListener() {
        valueProducerMap.put("s", new StringValueProducer());
    }

    @Override
    public void exitType(JsonTemplateParser.TypeContext ctx) {
        String typeName = ctx.getText().substring(1);
        currentValueProducer = valueProducerMap.get(typeName);
    }


}
