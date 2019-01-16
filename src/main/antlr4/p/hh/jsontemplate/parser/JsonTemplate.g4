grammar JsonTemplate;
IDENTIFIER : [a-zA-Z][a-zA-Z0-9]*;
ANNO_OTHER_TAG : '@'[a-zA-Z][a-zA-Z0-9]*;
DIGIT: [0-9]+;
STRING : [a-zA-Z0-9\-:]+;

jsonRoot : jsonObject | jsonArray;
jsonArray : annoSize '{' arrayElement (',' property)* '}';
arrayElement : 'e' ':' propValue;
jsonObject : '{' properties '}';
properties : property (',' property)*;
property : propName ':' propValue;
propName : IDENTIFIER;
propValue : type? plainValue | type? anno* | jsonRoot;
plainValue : STRING | DIGIT;
type : STRING_TYPE | CHAR_TYPE | INTEGER_TYPE | FLOAT_TYPE | BOOLEAN_TYPE;
STRING_TYPE : '%s';
CHAR_TYPE : '%c';
INTEGER_TYPE : '%d';
FLOAT_TYPE : '%f';
BOOLEAN_TYPE : '%b';
anno : annoSize | annoOther;
annoSize : '@Size' '(' annoParams ')';
annoOther : ANNO_OTHER_TAG annoParamPart?;
annoParamPart : '(' annoParams ')';
annoParams : listParam | listNamedParam;
listParam : plainValue (',' plainValue)*;
namedParam : paramName '=' paramValue;
listNamedParam : namedParam (',' namedParam)*;
paramName : IDENTIFIER;
paramValue : plainValue;


WS : [ \t\n\r]+ -> skip ;