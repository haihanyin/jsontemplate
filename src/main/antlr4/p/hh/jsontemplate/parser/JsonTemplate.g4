grammar JsonTemplate;
IDENTIFIER : [a-zA-Z][a-zA-Z0-9]*;
ANNO_OTHER_TAG : '@'[a-zA-Z][a-zA-Z0-9]*;
PLAIN_VALUE : [a-zA-Z0-9]+;

jsonRoot : annoSize? '{' properties '}';
properties : property (',' property)*;
property : propName ':' propValue;
propName : IDENTIFIER;
propValue : type? PLAIN_VALUE | type? anno* | jsonRoot;
type : STRING_TYPE | CHAR_TYPE | INTEGER_TYPE | FLOAT_TYPE | BOOLEAN_TYPE;
STRING_TYPE : '%s';
CHAR_TYPE : '%c';
INTEGER_TYPE : '%d';
FLOAT_TYPE : '%f';
BOOLEAN_TYPE : '%b';
anno : annoSize | annoOther;
annoSize : '@Size' '(' annoParams ')';
annoOther : ANNO_OTHER_TAG '(' annoParams ')';
annoParams : singleParam | listParam | namedParam;
singleParam : IDENTIFIER;
listParam : IDENTIFIER (',' IDENTIFIER)+;
namedParam : paramName '=' paramValue;
paramName : IDENTIFIER;
paramValue : PLAIN_VALUE;


WS : [ \t\n\r]+ -> skip ;