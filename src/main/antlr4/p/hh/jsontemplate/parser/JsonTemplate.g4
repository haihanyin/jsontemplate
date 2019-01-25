grammar JsonTemplate;

root : jsonObject | jsonArray;
jsonObject : '{' properties '}';
properties : property (',' property)*;
property : pairProperty;
pairProperty : propertyNameSpec ':' propertyValueSpec;
propertyNameSpec : propertyName | typeInfo ;
propertyName : IDENTIFIER;
typeInfo : '%'typeName;
typeName : IDENTIFIER;

propertyValueSpec : jsonValue | jsonArray | jsonObject | variable;
jsonValue : typeInfo typeParamSpec?;

typeParamSpec : '(' singleParam ')' | '(' listParams ')' | '(' mapParams ')';
singleParam : IDENTIFIER;
listParams : IDENTIFIER (',' IDENTIFIER)+;
mapParams : mapParam (',' mapParam)*;
mapParam : IDENTIFIER '=' IDENTIFIER;

jsonArray : itemsArray | genericArray;
itemsArray : '[' items ']' jsonValue?;
items : item (',' item)*;
item : jsonValue | variable | value;
value : IDENTIFIER;
genericArray : '[]' typeParamSpec? propertyValueSpec;

variable : '$'variableName;
variableName : IDENTIFIER;
IDENTIFIER : [a-zA-Z0-9-]+;
WS : [ \t\n\r]+ -> skip ;