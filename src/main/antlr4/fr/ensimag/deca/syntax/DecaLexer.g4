lexer grammar DecaLexer;

options {
   language=Java;
   // Tell ANTLR to make the generated lexer class extend the
   // the named class, which is where any supporting code and
   // variables will be placed.
   superClass = AbstractDecaLexer;
}

@members {
} 

fragment COMMENT : ('//' .*? ('\n'| EOF ) | '/*' .*? '*/');
fragment ESPACE : ' ';
fragment TAB : '\t';
fragment EOL : '\n'; 
SEPARATEUR : (COMMENT | ESPACE |EOL | TAB |'\r'){ skip(); };



fragment LETTER : ( 'a'  ..  'z' | 'A'  ..  'Z');
fragment DIGIT : '0' .. '9';
fragment NOF : (LETTER | '.' | DIGIT | '-' | '_')+;
INCLUDE : '#include'  (ESPACE)*  '"'  NOF  '"'{
   doInclude(getText());
}; 


ASM : 'asm' ;
CLASS : 'class' ;
ELSEIF: 'elseif';
ELSE : 'else';
IF :'if';
NEW :'new' ;
NULL :'null' ;
READINT :'readInt' ;
READFLOAT :'readFloat' ;
PROTECTED :'protected' ;
RETURN :'return' ;
THIS :'this' ;
INSTANCEOF :'instanceof';
EXTENDS :'extends' ;
PRINTLN :  'println';
PRINTLNX :  'printlnx';
PRINT :  'print';
PRINTX :  'printx';
FALSE : 'false';
TRUE : 'true' ; 
WHILE : 'while';
 





CPARENT : ')';
OPARENT : '(';
SEMI : ';' ;
OBRACE : '{' ;
CBRACE :'}' ;
COMMA : ',' ;
EQUALS : '=' ;
AND : '&&';
DOT : '.' ;
EXCLAM : '!';
OR : '||';
NEQ : '!=';
EQEQ : '==';
fragment POSITIVE_DIGIT : '1' .. '9';

PLUS : '+';
MINUS : '-';
GEQ : '>=';
LEQ : '<=';
GT : '>';
LT : '<';
TIMES : '*';
SLASH :  '/';
PERCENT : '%';


fragment NUM : DIGIT+ ;
fragment SIGN : ('+' | '-' | ) ;// Meme chose
fragment EXP : ('E' | 'e') SIGN NUM;
fragment DEC : NUM '.' NUM;
fragment FLOATDEC : (DEC | DEC EXP) ('F' | 'f'| ); // Normalement sans espace 
fragment DIGITHEX : '0' .. '9' | 'A' .. 'F' | 'a' .. 'f';
fragment NUMHEX : DIGITHEX+;
fragment FLOATHEX : ('0x' | '0X') NUMHEX '.' NUMHEX ('P' | 'p') SIGN NUM ('F' | 'f'| ); // Meme chose 
FLOAT : FLOATDEC | FLOATHEX;
INT : '0' | POSITIVE_DIGIT DIGIT* ;
IDENT : (LETTER | '$' | '_')(LETTER | DIGIT | '$' | '_')*;
STRING : '"' ( '\\"' | '\\\\' | ~('"'|'\n'|'\\'))* '"' ;
MULTI_LINE_STRING : '"' ('\\"' |'\\\\' | ~('"'|'\\'))* '"';

