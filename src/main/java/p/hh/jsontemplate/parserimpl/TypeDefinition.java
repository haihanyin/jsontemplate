package p.hh.jsontemplate.parserimpl;

import p.hh.jsontemplate.parser.JsonTemplateParser;

public class TypeDefinition {
    private String typeName;
    private JsonTemplateParser.PairPropertyContext typeDefContext;

    public TypeDefinition(JsonTemplateParser.PairPropertyContext typeDefContext) {
        this.typeDefContext = typeDefContext;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public JsonTemplateParser.PairPropertyContext getTypeDefContext() {
        return typeDefContext;
    }

}
