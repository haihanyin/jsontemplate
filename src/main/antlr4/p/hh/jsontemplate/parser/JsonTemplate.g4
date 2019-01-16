grammar JsonTemplate;

root : jsonObject | jsonArray;
jsonObject : type? '{' properties '}';
properties : property (',' property)*;
property : simpleProperty | pairProperty;
simpleProperty : IDENTIFIER;
pairProperty : propertyNameSpec ':' propertyValueSpec;
propertyNameSpec : propertyName | typeName;
propertyName : IDENTIFIER;
typeName : TYPE_NAME;
propertyValueSpec : VALUE | type | type typeParamSpec | jsonArray | jsonObject;
type : STRING_TYPE | CHAR_TYPE | INTEGER_TYPE | FLOAT_TYPE | BOOLEAN_TYPE;
STRING_TYPE : '%s';
CHAR_TYPE : '%c';
INTEGER_TYPE : '%d';
FLOAT_TYPE : '%f';
BOOLEAN_TYPE : '%b';
typeParamSpec : '(' VALUE | typeParam (',' typeParam)* ')';
typeParam : typeParamName '=' typeParamValue;
typeParamName : IDENTIFIER;
typeParamValue : VALUE;

jsonArray : '%a' typeParamSpec? propertyValueSpec?;

IDENTIFIER : [a-zA-Z][a-zA-Z0-9]*;
VALUE : [a-zA-Z][a-zA-Z0-9]*;
TYPE_NAME : '%'IDENTIFIER;
STRING : .*?;
WS : [ \t\n\r]+ -> skip ;