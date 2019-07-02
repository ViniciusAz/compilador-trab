%{
import java.io.*;
import java.util.Stack;
%}


%token IDENT, INT, DOUBLE, BOOL, NUM, STRING
%token LITERAL, AND, VOID, MAIN, IF
%token DECSTRUCT, STRUCT
%token FUNC, VOID, RETURN

%right '='
%nonassoc '>'
%left '+'
%left AND
%left '['



%type <sval> IDENT, id
%type <ival> NUM
%type <obj> DECSTRUCT
%type <obj> type
%type <obj> exp
%type <obj> FUNC

%%

prog : { currClass = ClasseID.VarGlobal; } dList main ;

dList : decl dList | ;

decl : type  { currentType = (TS_entry)$1; } Lid ';'
     | DECSTRUCT { currentType = Tp_DECSTRUCT; } id '{' { escopoLocal = new TabSimb(); } dListStruct '}' ';' { TS_entry nodo = ts.pesquisa($3);
                                                                                                            nodo.insereLocais(escopoLocal); }
	 | FUNC type { currentType = (TS_entry)$2; } id '(' { escopoLocal = new TabSimb(); } lparam ')' 
		'{' { ts = escopoLocal; } corpoF 
			RETURN exp { if ((TS_entry)$13 != currentType)   yyerror("(sem) retorno do tipo errado"); } ';' { ts = aux; } 
		'}' { TS_entry nodo = ts.pesquisa($4); nodo.insereLocais(escopoLocal); }
	 | FUNC VOID { currentType = Tp_VOID; } id '(' { escopoLocal = new TabSimb(); } lparam ')' 
		'{' { ts = escopoLocal; } corpoF { ts = aux; } '}' { TS_entry nodo = ts.pesquisa($4); nodo.insereLocais(escopoLocal); /*escopoLocal.listar();*/ }
                                                                                                            
     ;

Lid : Lid  ',' id 
    | id  
    ;

lparam  : lparam ',' param
		| param
		|
		;

param : type { currentType = (TS_entry)$1; } idP 
		;
		
corpoF : corpoF listacmd
	| corpoF dListStruct
	|
	;


dListStruct : dListStruct type { currentType = (TS_entry)$2; } LidL ';'
            | type { currentType = (TS_entry)$1; } LidL ';'
            ;

LidL : LidL  ',' idL 
     | idL  
     ;

idL : IDENT   { TS_entry nodo = escopoLocal.pesquisa($1);
                            if (nodo != null) {
                              yyerror("(sem) variavel >" + $1 + "< jah declarada");
                } else {
                  escopoLocal.insert(new TS_entry($1, currentType, ClasseID.VarLocal));
                }
         }
;
idP : IDENT   { TS_entry nodo = escopoLocal.pesquisa($1);
                            if (nodo != null) {
                              yyerror("(sem) parametro >" + $1 + "< jah declarado");
                } else {
                  escopoLocal.insert(new TS_entry($1, currentType, ClasseID.NomeParam));
                }
         }
;

id : IDENT   { TS_entry nodo = ts.pesquisa($1);
                            if (nodo != null) {
                              yyerror("(sem) variavel >" + $1 + "< jah declarada");
                } else {
                  ts.insert(new TS_entry($1, currentType, currClass));
                }
         }

;

type : INT    { $$ = Tp_INT; }
 | DOUBLE  { $$ = Tp_DOUBLE; }
 | BOOL   { $$ = Tp_BOOL; }
 ;

main :  VOID MAIN '(' ')' { System.out.println("\n\nVerificador semantico simples\n"); } bloco ;

bloco : '{' listacmd '}';

listacmd : listacmd cmd
    |
     ;

cmd :  exp ';'
  | IF '(' exp ')' cmd   {  if ( ((TS_entry)$3) != Tp_BOOL)
                                 yyerror("(sem) expressão (if) deve ser lógica "+((TS_entry)$3).getTipo());
                         }
   ;


exp : exp '+' exp { $$ = validaTipo('+', (TS_entry)$1, (TS_entry)$3); }
| exp '>' exp { $$ = validaTipo('>', (TS_entry)$1, (TS_entry)$3); }
| exp AND exp { $$ = validaTipo(AND, (TS_entry)$1, (TS_entry)$3); }
| NUM         { $$ = Tp_INT; }
| '(' exp ')' { $$ = $2; }
| IDENT       { TS_entry nodo = ts.pesquisa($1);
                if (nodo == null) {
					nodo = aux.pesquisa($1);
					if (nodo == null) {
                   yyerror("(sem) var <" + $1 + "> nao declarada");
                   $$ = Tp_ERRO;
				} else
                    $$ = nodo.getTipo();
			   } 
                else
                    $$ = nodo.getTipo();
              }
 | exp '=' exp  {  $$ = validaTipo(ATRIB, (TS_entry)$1, (TS_entry)$3);  }
 | exp '[' exp ']'  {  if ((TS_entry)$3 != Tp_INT)
                          yyerror("(sem) indexador não é numérico ");
                       else
                           if (((TS_entry)$1).getTipo() != Tp_ARRAY)
                              yyerror("(sem) elemento não indexado ");
                           else
                              $$ = ((TS_entry)$1).getTipoBase();
                     }
 | STRUCT IDENT { TS_entry nodo = ts.pesquisa($2);
                  if (nodo == null) {
                     yyerror("(sem) struct <" + $2 + "> nao declarada");
                     $$ = Tp_ERRO;
                  }
                  else
                      $$ = nodo.getTipo();
                  }
    IDENT { TS_entry nodo_new = ts.pesquisa($4);
					if (nodo_new != null) {
					  yyerror("(sem) variavel >" + $4 + "< jah declarada");
				} else {
				  TS_entry aux = new TS_entry($4, Tp_STRUCT, currClass);
				  TS_entry nodoDecl = ts.pesquisa($2);
				  // pesquisar o nodoDecl.getLocais() e fazer uma copia e inserir no aux
				  
				  aux.insereLocais(nodoDecl.getLocais());
				  ts.insert(aux);
				}
           }

 | IDENT { TS_entry nodo = ts.pesquisa($1);
              if (nodo == null) {
                 yyerror("(sem) variavel <" + $1 + "> nao declarada");
                 $$ = Tp_ERRO;
              }
              else {
                  if(nodo.getTipo() != Tp_STRUCT) {
                      yyerror("(sem) variavel <" + $1 + "> nao é uma STRUCT");
                      $$ = Tp_ERRO;
                  }
              }
         }
  '.' IDENT { TS_entry nodo = ts.pesquisa($1);
              escopoLocal = nodo.getLocais();
              if (escopoLocal == null) {
                  yyerror("(sem) nenhuma variavel local declarada");
                  $$ = Tp_ERRO;
              }
              TS_entry nodoLocal = escopoLocal.pesquisa($4);
               if (nodoLocal == null) {
                  yyerror("(sem) variavel local <" + $4 + "> nao declarada");
                  $$ = Tp_ERRO;
               } else $$ = nodoLocal.getTipo();
          }
   | IDENT '(' { TS_entry nodo = aux.pesquisa($1);	   
                 System.out.println("Tam1 " );
				 if(nodo == null) {
                 System.out.println("Tam2 " );
					 pilhaPosicao.push(0);
					 pilhaEscopo.push(null);
                     yyerror("(sem) Funcao <" + $1 + "> nao declarada");
					 $$ = Tp_ERRO;
                 } else /* achou funcao */ {
					                  System.out.println("Tam1d " );
					 if(nodo.getLocais() == null ) {
						pilhaPosicao.push(0);
						pilhaEscopo.push(null);
					 } else /* tem locais */ {
						 pilhaPosicao.push(nodo.getLocais().contaParam());
						 pilhaEscopo.push(nodo.getLocais());
					 }
                 }
                                  System.out.println("Tam1x " );
			   } pexp ')' { if (pilhaPosicao.peek() != 0)
								yyerror("(sem) numero de parametros diferente da chamada da função");
							pilhaEscopo.pop(); pilhaPosicao.pop(); }
	;

pexp : pexp ',' exp { //System.out.println("ordem posicao: " + pilhaPosicao.peek() + " , id : " + pilhaEscopo.peek().get(pilhaPosicao.peek()-1).getId() ); 
					  if ( pilhaEscopo.peek() != null ) {	
					    if ( pilhaEscopo.peek().get(pilhaPosicao.peek()-1).getTipo() != (TS_entry)$3 ) {
							yyerror("(sem) tipo do parametro <" + $3 + "> errado");
						} pilhaPosicao.push(pilhaPosicao.pop() - 1);
					  } 
				   }
	 | exp { if (pilhaEscopo.peek() != null) {
				//System.out.println("ordem posicao: " + pilhaPosicao.peek() + " , id : " + pilhaEscopo.peek().get(pilhaPosicao.peek()-1).getId() ); 
				 if (pilhaEscopo.peek().get(pilhaPosicao.peek()-1).getTipo() != (TS_entry)$1) {
					yyerror("(sem) tipo do parametro <" + $1 + "> errado");
				 } pilhaPosicao.push(pilhaPosicao.pop() - 1); 
			  }
		   }
	 ;

%%

private Yylex lexer;

private TabSimb ts, escopoLocal, aux;

private Stack<TabSimb> pilhaEscopo = new Stack<TabSimb>();
private Stack<Integer> pilhaPosicao = new Stack<Integer>();


private int position;

public static TS_entry Tp_INT =  new TS_entry("int", null, ClasseID.TipoBase);
public static TS_entry Tp_DOUBLE = new TS_entry("double", null,  ClasseID.TipoBase);
public static TS_entry Tp_BOOL = new TS_entry("bool", null,  ClasseID.TipoBase);
public static TS_entry Tp_DECSTRUCT = new TS_entry("decStruct", null,  ClasseID.TipoBase);
public static TS_entry Tp_STRUCT = new TS_entry("struct", null,  ClasseID.TipoBase);

public static TS_entry Tp_ARRAY = new TS_entry("array", null,  ClasseID.TipoBase);
public static TS_entry Tp_VOID = new TS_entry("void", null,  ClasseID.TipoBase);

public static TS_entry Tp_ERRO = new TS_entry("_erro_", null,  ClasseID.TipoBase);

public static final int ARRAY = 1500;
public static final int ATRIB = 1600;

private String currEscopo;
private ClasseID currClass;

private TS_entry currentType;

private int yylex () {
int yyl_return = -1;
try {
  yylval = new ParserVal(0);
  yyl_return = lexer.yylex();
}
catch (IOException e) {
  System.err.println("IO error :"+e);
}
return yyl_return;
}


public void yyerror (String error) {
//System.err.println("Erro (linha: "+ lexer.getLine() + ")\tMensagem: "+error);
System.err.printf("Erro (linha: %2d) \tMensagem: %s\n", lexer.getLine(), error);
}


public Parser(Reader r) {
lexer = new Yylex(r, this);

ts = new TabSimb();
aux = ts;

//
// não me parece que necessitem estar na TS
// já que criei todas como public static...
//
ts.insert(Tp_ERRO);
ts.insert(Tp_INT);
ts.insert(Tp_DOUBLE);
ts.insert(Tp_BOOL);
ts.insert(Tp_ARRAY);
ts.insert(Tp_STRUCT);
ts.insert(Tp_DECSTRUCT);
ts.insert(Tp_VOID);

}

public void setDebug(boolean debug) {
yydebug = debug;
}

public void listarTS() { ts.listar();}

public static void main(String args[]) throws IOException {
//System.out.println("\n\nVerificador semantico simples\n");


Parser yyparser;
if ( args.length > 0 ) {
  // parse a file
  yyparser = new Parser(new FileReader(args[0]));
}
else {
  // interactive mode
  System.out.println("[Quit with CTRL-D]");
  System.out.print("Programa de entrada:\n");
    yyparser = new Parser(new InputStreamReader(System.in));
}

yyparser.yyparse();

  yyparser.listarTS();

  System.out.print("\n\nFeito!\n");

}


TS_entry validaTipo(int operador, TS_entry A, TS_entry B) {

     switch ( operador ) {
          case ATRIB:
                if ( (A == Tp_INT && B == Tp_INT)                        ||
                     ((A == Tp_DOUBLE && (B == Tp_INT || B == Tp_DOUBLE))) ||
                     (A == B) )
                     return A;
                 else
                     yyerror("(sem) tipos incomp. para atribuicao: "+ A.getTipoStr() + " = "+B.getTipoStr());
                break;

          case '+' :
                if ( A == Tp_INT && B == Tp_INT)
                      return Tp_INT;
                else if ( (A == Tp_DOUBLE && (B == Tp_INT || B == Tp_DOUBLE)) ||
                                        (B == Tp_DOUBLE && (A == Tp_INT || A == Tp_DOUBLE)) )
                     return Tp_DOUBLE;
                else
                    yyerror("(sem) tipos incomp. para soma: "+ A.getTipoStr() + " + "+B.getTipoStr());
                break;

         case '>' :
                 if ((A == Tp_INT || A == Tp_DOUBLE) && (B == Tp_INT || B == Tp_DOUBLE))
                     return Tp_BOOL;
                  else
                    yyerror("(sem) tipos incomp. para op relacional: "+ A.getTipoStr() + " > "+B.getTipoStr());
                  break;

         case AND:
                 if (A == Tp_BOOL && B == Tp_BOOL)
                     return Tp_BOOL;
                  else
                    yyerror("(sem) tipos incomp. para op lógica: "+ A.getTipoStr() + " && "+B.getTipoStr());
             break;
        }

        return Tp_ERRO;

 }
