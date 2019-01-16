grammar JsonTemplate;

root : jsonObject | jsonArray;
jsonArray : '%a' constraints ('[' propValue ']')?;
constraints : constraint*;
constraint : ANNO_TAG annoParamPart?;

jsonObject : propValue? '{' pairs '}';
pairs : pair (',' pair)*;
pair : key ':' propValue;
key : typeDef | IDENTIFIER;
typeDef : TYPEDEF;
TYPEDEF : '%'IDENTIFIER;

propValue : type? TEXT | type? anno* | jsonRoot;
type : STRING_TYPE | CHAR_TYPE | INTEGER_TYPE | FLOAT_TYPE | BOOLEAN_TYPE;
STRING_TYPE : '%s';
CHAR_TYPE : '%c';
INTEGER_TYPE : '%d';
FLOAT_TYPE : '%f';
BOOLEAN_TYPE : '%b';
anno : annoSize | annoOther;
annoSize : '@Size' '(' annoParams ')';

annoParamPart : '(' annoParams ')';
annoParams : listParam | listNamedParam;
listParam : TEXT (',' TEXT)*;
namedParam : paramName '=' TEXT;
listNamedParam : namedParam (',' namedParam)*;
paramName : IDENTIFIER;

IDENTIFIER : [a-zA-Z][a-zA-Z0-9]*;
ANNO_TAG : '@'IDENTIFIER;
STRING : .*?;
WS : [ \t\n\r]+ -> skip ;