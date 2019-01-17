grammar JsonTemplate;

root : jsonObject | jsonArray;
jsonObject : '{' properties '}';
properties : property (',' property)*;
property : pairProperty;
pairProperty : propertyNameSpec ':' propertyValueSpec;
propertyNameSpec : propertyName | CUSTOM_TYPE_NAME ;
propertyName : IDENTIFIER;
propertyValueSpec : type typeParamSpec? | jsonArray | jsonObject;
type : STRING_TYPE | CHAR_TYPE | INTEGER_TYPE | FLOAT_TYPE | BOOLEAN_TYPE | CUSTOM_TYPE_NAME | VARIABLE_NAME;
STRING_TYPE : '%s';
CHAR_TYPE : '%c';
INTEGER_TYPE : '%d';
FLOAT_TYPE : '%f';
BOOLEAN_TYPE : '%b';
typeParamSpec : '(' typeParams ')';
typeParams : typeParam (',' typeParam)*;
typeParam : typeParamName ('=' typeParamValue)?;
typeParamName : IDENTIFIER;
typeParamValue : IDENTIFIER;

jsonArray : '%a' typeParamSpec? propertyValueSpec;

IDENTIFIER : [a-zA-Z0-9-]+;

CUSTOM_TYPE_NAME : '%'[A-Z]IDENTIFIER;
VARIABLE_NAME : '%'[a-z]IDENTIFIER;
WS : [ \t\n\r]+ -> skip ;