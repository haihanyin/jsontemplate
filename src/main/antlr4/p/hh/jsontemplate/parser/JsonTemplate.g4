grammar JsonTemplate;

root : jsonObject | jsonArray;
jsonObject : '{' properties '}';
properties : property (',' property)*;
property : pairProperty;
pairProperty : propertyNameSpec ':' propertyValueSpec;
propertyNameSpec : propertyName | CUSTOM_TYPE_NAME ;
propertyName : IDENTIFIER;
propertyValueSpec : type typeParamSpec? | jsonArray | jsonObject;
type : stringType | charType | integerType | floatType | booleanType | customType | variableName;
stringType : '%s';
charType : '%c';
integerType : '%d';
floatType : '%f';
booleanType : '%b';
customType : CUSTOM_TYPE_NAME;
variableName : VARIABLE_NAME;
typeParamSpec : '(' typeParams ')';
typeParams : typeParam (',' typeParam)*;
typeParam : defaultTypeValue | typeParamName '=' typeParamValue;
defaultTypeValue : IDENTIFIER;
typeParamName : IDENTIFIER;
typeParamValue : IDENTIFIER;

jsonArray : '%a' typeParamSpec? propertyValueSpec;

IDENTIFIER : [a-zA-Z0-9-]+;

CUSTOM_TYPE_NAME : '%'[A-Z]IDENTIFIER;
VARIABLE_NAME : '%'[a-z]IDENTIFIER;
WS : [ \t\n\r]+ -> skip ;