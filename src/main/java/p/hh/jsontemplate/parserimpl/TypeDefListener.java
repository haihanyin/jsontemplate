package p.hh.jsontemplate.parserimpl;

import jdk.nashorn.internal.ir.ObjectNode;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import p.hh.jsontemplate.jsoncomposer.JsonArrayNode;
import p.hh.jsontemplate.jsoncomposer.JsonBuilder;
import p.hh.jsontemplate.jsoncomposer.JsonNode;
import p.hh.jsontemplate.jsoncomposer.JsonObjectNode;
import p.hh.jsontemplate.parser.JsonTemplateBaseListener;
import p.hh.jsontemplate.parser.JsonTemplateParser;
import p.hh.jsontemplate.valueproducer.BooleanValueProducer;
import p.hh.jsontemplate.valueproducer.IValueProducer;
import p.hh.jsontemplate.valueproducer.IntegerValueProducer;
import p.hh.jsontemplate.valueproducer.StringValueProducer;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class TypeDefListener extends JsonTemplateTreeListener {

    private boolean inTypeDef;
    private String typeDefName;
    private Map<String, JsonNode> typeMap = new HashMap<>();
    private Map<String, List<JsonNode>> typeMissMap = new HashMap<>();
    private Map<String, IValueProducer> valueProducerMap = new HashMap<>();
    private JsonBuilder jsonBuilder;
    private String currentPropertyName;
    private IValueProducer currentValueProducer;
    
    @Override
    public void enterPropertyNameSpec(JsonTemplateParser.PropertyNameSpecContext ctx) {
        ParseTree child = ctx.getChild(0);
        if (child instanceof JsonTemplateParser.TypeInfoContext) {
            inTypeDef = true;
            jsonBuilder = new JsonBuilder();
        }
    }

    @Override
    public void enterTypeName(JsonTemplateParser.TypeNameContext ctx) {
        if (inTypeDef) {
            typeDefName = ctx.getText();
        }
    }
    
    @Override
    public void exitPropertyNameSpec(JsonTemplateParser.PropertyNameSpecContext ctx) {
        if (inTypeDef) {
            JsonNode jsonNode = jsonBuilder.end().build();
            typeMap.put(typeDefName, );

            List<JsonNode> missTypeNodes = typeMissMap.get(typeDefName);
            if (missTypeNodes != null && missTypeNodes.size() > 0) {
                for (JsonNode missTypeNode : missTypeNodes) {
                    if (missTypeNode instanceof JsonObjectNode) {
                        JsonObjectNode missTypeJsonObjectNode = (JsonObjectNode) missTypeNode;
                        if (jsonNode instanceof JsonObjectNode) {
                            missTypeJsonObjectNode.putObject(((JsonObjectNode) jsonNode));
                        } else if (jsonNode instanceof JsonArrayNode) {
                            missTypeJsonObjectNode.putArray(((JsonArrayNode) jsonNode));
                        }
                        
                    } else if (missTypeNode instanceof JsonArrayNode) {
                        if (jsonNode instanceof JsonObjectNode) {

                        } else if (jsonNode instanceof JsonArrayNode) {

                        }
                    }
                }
            }
            
            inTypeDef = false;
            typeDefName = null;
            jsonBuilder = null;
        }
    }

    @Override
    public void enterJsonObject(JsonTemplateParser.JsonObjectContext ctx) {
        if (inTypeDef) {
            if (jsonBuilder == null) {
                jsonBuilder = new JsonBuilder();
                jsonBuilder.createObject();
            } else {
                jsonBuilder.putObject(currentPropertyName);
            }
        }
    }

    @Override
    public void exitJsonObject(JsonTemplateParser.JsonObjectContext ctx) {
        if (inTypeDef) {
            jsonBuilder.end();
        }
    }

    @Override
    public void enterJsonArray(JsonTemplateParser.JsonArrayContext ctx) {
        if (inTypeDef) {
            if (jsonBuilder == null) {
                jsonBuilder = new JsonBuilder();
                jsonBuilder.createArray();
            } else {
                jsonBuilder.putArray(currentPropertyName);
            }
        }
    }

    @Override
    public void exitPropertyName(JsonTemplateParser.PropertyNameContext ctx) {
        if (inTypeDef) {
            this.currentPropertyName = ctx.getText();
            System.out.println("propertyName=" + this.currentPropertyName);
        }
    }

    @Override
    public void exitTypeName(JsonTemplateParser.TypeNameContext ctx) {
        if (inTypeDef) {
            String typeName = ctx.getText();
            currentValueProducer = valueProducerMap.get(typeName);
            Class valueType = currentValueProducer.getValueType();
            if (valueType.equals(Integer.class)) {
                Supplier<Integer> supplier = () -> (Integer) currentValueProducer.produce(Collections.emptyMap());
                if (jsonBuilder.inObject()) {
                    jsonBuilder.putInteger(currentPropertyName, supplier);
                } else {
                    jsonBuilder.addInteger(supplier);
                }
            } else if (valueType.equals(Boolean.class)) {
                Supplier<Boolean> supplier = () -> (Boolean) currentValueProducer.produce(Collections.emptyMap());
                if (jsonBuilder.inObject()) {
                    jsonBuilder.putBoolean(currentPropertyName, supplier);
                } else {
                    jsonBuilder.addBoolean(supplier);
                }
            } else if (valueType.equals(String.class)) {
                Supplier<String> supplier = () -> (String) currentValueProducer.produce(Collections.emptyMap());
                if (jsonBuilder.inObject()) {
                    jsonBuilder.putString(currentPropertyName, supplier);
                } else {
                    jsonBuilder.addString(supplier);
                }
            }
        }
    }

}
