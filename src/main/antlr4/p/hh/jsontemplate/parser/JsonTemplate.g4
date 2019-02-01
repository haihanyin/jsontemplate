grammar JsonTemplate;

root : jsonObject | jsonArray;
jsonObject : '{' properties '}';
properties : property (',' property)*;
property : pairProperty;
pairProperty : propertyNameSpec ':' propertyValueSpec;
propertyNameSpec : propertyName | typeDef ;
propertyName : IDENTIFIER;
typeDef : '%'typeName;
typeName : IDENTIFIER;

propertyValueSpec : jsonValue | jsonArray | jsonObject | variable;
jsonValue : typeInfo typeParamSpec?;
typeInfo : '%'typeName;

typeParamSpec : '(' singleParam ')' | '(' listParams ')' | '(' mapParams ')';
singleParam : IDENTIFIER;
listParams : IDENTIFIER (',' IDENTIFIER)+;
mapParams : mapParam (',' mapParam)*;
mapParam : IDENTIFIER '=' IDENTIFIER;

jsonArray : arrayTypeInfo? itemsArray ;
arrayTypeInfo : jsonValue;
itemsArray : '[' items? ']' arrayParamSpec?;
arrayParamSpec : typeParamSpec;
items : item (',' item)*;
item : jsonValue | variable | value;
value : IDENTIFIER;

variable : '$'variableName;
variableName : IDENTIFIER;
IDENTIFIER : [a-zA-Z0-9-]+;
WS : [ \t\n\r]+ -> skip ;