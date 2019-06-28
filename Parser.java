//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "exemploSem.y"
import java.io.*;
import java.util.Stack;
//#line 20 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short IDENT=257;
public final static short INT=258;
public final static short DOUBLE=259;
public final static short BOOL=260;
public final static short NUM=261;
public final static short STRING=262;
public final static short LITERAL=263;
public final static short AND=264;
public final static short VOID=265;
public final static short MAIN=266;
public final static short IF=267;
public final static short DECSTRUCT=268;
public final static short STRUCT=269;
public final static short FUNC=270;
public final static short RETURN=271;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    5,    0,    4,    4,    8,    7,    9,   11,    7,   12,
   14,   16,   17,   18,    7,   19,   20,   21,   22,    7,
   13,   13,   13,   25,   23,   15,   15,   15,   28,   10,
   29,   10,   27,   24,    1,    2,    2,    2,    6,   30,
   26,   26,   31,   31,    3,    3,    3,    3,    3,    3,
    3,    3,   32,    3,   33,    3,   35,    3,   34,   34,
};
final static short yylen[] = {                            2,
    0,    3,    2,    0,    0,    4,    0,    0,    8,    0,
    0,    0,    0,    0,   17,    0,    0,    0,    0,   13,
    3,    1,    0,    0,    3,    2,    2,    0,    0,    5,
    0,    4,    1,    1,    1,    1,    1,    1,    5,    3,
    2,    0,    2,    5,    3,    3,    3,    1,    3,    1,
    3,    4,    0,    4,    0,    4,    0,    5,    3,    1,
};
final static short yydefred[] = {                         1,
    0,    0,   36,   37,   38,    7,    0,    5,    0,    0,
    0,   16,   10,    0,    0,    2,    3,   35,    0,    0,
    0,    0,    0,    8,    0,    0,    6,    0,    0,   17,
   11,    0,   31,    0,    0,    0,   42,   39,    0,    0,
   29,   24,    0,   22,    0,    0,   33,    0,    9,    0,
    0,    0,    0,    0,    0,   48,    0,    0,   40,    0,
    0,   41,   32,    0,   34,   25,   18,   21,   12,   57,
    0,    0,   53,    0,    0,    0,    0,    0,    0,   43,
   30,   28,   28,    0,    0,    0,    0,   49,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   56,    0,   54,
   52,    0,    0,    0,    0,   58,    0,   44,   20,    0,
    0,    0,   14,    0,   15,
};
final static short yydgoto[] = {                          1,
   19,   33,   61,    9,    2,   16,   10,   14,   11,  102,
   29,   21,   43,   36,   94,   83,  112,  114,   20,   35,
   82,  103,   44,   66,   51,  104,   48,   50,   39,   38,
   62,   87,   71,   97,   84,
};
final static short yysindex[] = {                         0,
    0, -131,    0,    0,    0,    0, -154,    0, -251, -131,
 -238,    0,    0, -238, -246,    0,    0,    0, -101, -238,
 -238,  -33,   -6,    0,    2,   12,    0,   -3, -124,    0,
    0,  -83,    0, -115, -124, -124,    0,    0, -201,    5,
    0,    0,  -32,    0,    3,  -39,    0,    9,    0, -201,
 -184,  -44, -124,  -15,   44,    0,   61, -150,    0,  -22,
  -26,    0,    0,   57,    0,    0,    0,    0,    0,    0,
   77,  -22,    0,  -30,  -22,  -22,  -22,  -22,  -22,    0,
    0,    0,    0,  -22, -126,   -4, -125,    0,   55,   -2,
  -41,  -87,  -13, -124, -161,   -2,   47,    0,  -24,    0,
    0, -124,   13,  -24,  -22,    0,  -22,    0,    0,   -2,
   -2,   91,    0,   23,    0,
};
final static short yyrindex[] = {                         0,
    0, -114,    0,    0,    0,    0,    0,    0,    0, -114,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   68,   68,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -38,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  -16,   22,
   10,   31,    0,  -25,  -11,   81,    0,    0,    0,    0,
    0,  -40,    0, -118,    0,    0,    0,    0,    0,   93,
   89,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   62,   60,   42,  144,    0,    0,    0,    0,    0,  126,
    0,    0,  120,    0,   74,    0,    0,    0,    0,    0,
    0,    0,  105,    0,    0,  122,  110,    0,    0,    0,
   64,    0,    0,    0,    0,
};
final static int YYTABLESIZE=262;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         27,
   60,   78,   50,   79,   50,   50,   26,   55,   52,   40,
   88,   53,   78,   15,   42,   60,   78,   60,   18,   23,
   50,   24,   50,   50,   47,   27,   47,   47,   42,   78,
   76,   77,   80,   28,   76,   77,   99,   32,   78,   37,
   78,   30,   47,   54,   47,   47,   53,   76,   77,   79,
   46,   31,   50,   46,   50,   47,   76,   77,   76,   77,
   79,    8,   51,   49,   79,   51,   13,   63,   46,    8,
   46,   45,   65,   45,   45,   22,   47,   79,   67,  101,
   51,   25,   26,   70,   27,   59,   79,  106,   79,   45,
  107,   45,   45,   41,   42,   42,    3,    4,    5,   19,
   72,   74,   46,    3,    4,    5,   73,   69,   23,  105,
   12,   23,   42,   86,   51,   81,   89,   90,   91,   92,
   93,   60,   85,   45,   60,   96,    3,    4,    5,   59,
   98,  100,   59,    3,    4,    5,    6,  109,    7,   26,
   26,   26,    3,    4,    5,   79,  110,  115,  111,  113,
    4,   13,   26,   17,   34,   45,   95,   68,   46,   64,
    0,   41,  108,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   75,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   27,   55,    0,    0,
   27,   56,   75,    0,    0,   50,   27,   57,   27,   58,
   27,   42,   55,   75,   55,   42,   56,   75,   56,    0,
    0,   42,   57,   42,   58,   42,   58,   47,    0,   42,
   75,    0,    0,    0,    0,   42,    0,   42,    0,   75,
    0,   75,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   40,   43,   41,   91,   43,   44,  125,   46,   41,  125,
   41,   44,   43,  265,   40,   40,   43,   40,  257,  266,
   59,  123,   61,   62,   41,   59,   43,   44,   40,   43,
   61,   62,   59,   40,   61,   62,   41,   41,   43,  123,
   43,   40,   59,   41,   61,   62,   44,   61,   62,   91,
   41,   40,   91,   44,   93,  257,   61,   62,   61,   62,
   91,    2,   41,   59,   91,   44,    7,   59,   59,   10,
   61,   41,  257,   43,   44,   14,   93,   91,  123,   93,
   59,   20,   21,   40,  125,  125,   91,   41,   91,   59,
   44,   61,   62,   34,   35,   36,  258,  259,  260,  125,
   40,   60,   93,  258,  259,  260,  257,  123,   41,  271,
  265,   44,   53,   72,   93,   59,   75,   76,   77,   78,
   79,   41,   46,   93,   44,   84,  258,  259,  260,   41,
  257,  257,   44,  258,  259,  260,  268,  125,  270,  258,
  259,  260,  258,  259,  260,   91,  105,  125,  107,   59,
  265,   59,  271,   10,   29,   36,   83,   53,   37,   50,
   -1,  102,   99,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  264,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  257,  257,   -1,   -1,
  261,  261,  264,   -1,   -1,  264,  267,  267,  269,  269,
  271,  257,  257,  264,  257,  261,  261,  264,  261,   -1,
   -1,  267,  267,  269,  269,  257,  269,  264,   -1,  261,
  264,   -1,   -1,   -1,   -1,  267,   -1,  269,   -1,  264,
   -1,  264,
};
}
final static short YYFINAL=1;
final static short YYMAXTOKEN=271;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'",null,"'+'","','",
null,"'.'",null,null,null,null,null,null,null,null,null,null,null,null,"';'",
null,"'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'['",null,"']'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,"IDENT","INT","DOUBLE","BOOL","NUM",
"STRING","LITERAL","AND","VOID","MAIN","IF","DECSTRUCT","STRUCT","FUNC",
"RETURN",
};
final static String yyrule[] = {
"$accept : prog",
"$$1 :",
"prog : $$1 dList main",
"dList : decl dList",
"dList :",
"$$2 :",
"decl : type $$2 id ';'",
"$$3 :",
"$$4 :",
"decl : DECSTRUCT $$3 id '{' $$4 dListStruct '}' ';'",
"$$5 :",
"$$6 :",
"$$7 :",
"$$8 :",
"$$9 :",
"decl : FUNC type $$5 id '(' $$6 lparam ')' '{' $$7 corpoF RETURN exp $$8 ';' $$9 '}'",
"$$10 :",
"$$11 :",
"$$12 :",
"$$13 :",
"decl : FUNC VOID $$10 id '(' $$11 lparam ')' '{' $$12 corpoF $$13 '}'",
"lparam : lparam ',' param",
"lparam : param",
"lparam :",
"$$14 :",
"param : type $$14 idP",
"corpoF : corpoF listacmd",
"corpoF : corpoF dListStruct",
"corpoF :",
"$$15 :",
"dListStruct : dListStruct type $$15 idL ';'",
"$$16 :",
"dListStruct : type $$16 idL ';'",
"idL : IDENT",
"idP : IDENT",
"id : IDENT",
"type : INT",
"type : DOUBLE",
"type : BOOL",
"main : VOID MAIN '(' ')' bloco",
"bloco : '{' listacmd '}'",
"listacmd : listacmd cmd",
"listacmd :",
"cmd : exp ';'",
"cmd : IF '(' exp ')' cmd",
"exp : exp '+' exp",
"exp : exp '>' exp",
"exp : exp AND exp",
"exp : NUM",
"exp : '(' exp ')'",
"exp : IDENT",
"exp : exp '=' exp",
"exp : exp '[' exp ']'",
"$$17 :",
"exp : STRUCT IDENT $$17 IDENT",
"$$18 :",
"exp : IDENT $$18 '.' IDENT",
"$$19 :",
"exp : IDENT '(' $$19 pexp ')'",
"pexp : pexp ',' exp",
"pexp : exp",
};

//#line 214 "exemploSem.y"

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
System.out.println("\n\nVerificador semantico simples\n");


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
//#line 471 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 29 "exemploSem.y"
{ currClass = ClasseID.VarGlobal; }
break;
case 5:
//#line 33 "exemploSem.y"
{ currentType = (TS_entry)val_peek(0).obj; }
break;
case 7:
//#line 34 "exemploSem.y"
{ currentType = Tp_DECSTRUCT; }
break;
case 8:
//#line 34 "exemploSem.y"
{ escopoLocal = new TabSimb(); }
break;
case 9:
//#line 34 "exemploSem.y"
{ TS_entry nodo = ts.pesquisa(val_peek(5).sval);
                                                                                                            nodo.insereLocais(escopoLocal); }
break;
case 10:
//#line 36 "exemploSem.y"
{ currentType = (TS_entry)val_peek(0).obj; }
break;
case 11:
//#line 36 "exemploSem.y"
{ escopoLocal = new TabSimb(); }
break;
case 12:
//#line 37 "exemploSem.y"
{ ts = escopoLocal; }
break;
case 13:
//#line 38 "exemploSem.y"
{ if ((TS_entry)val_peek(0).obj != currentType)   yyerror("(sem) retorno do tipo errado"); }
break;
case 14:
//#line 38 "exemploSem.y"
{ ts = aux; }
break;
case 15:
//#line 39 "exemploSem.y"
{ TS_entry nodo = ts.pesquisa(val_peek(13).sval); nodo.insereLocais(escopoLocal); }
break;
case 16:
//#line 40 "exemploSem.y"
{ currentType = Tp_VOID; }
break;
case 17:
//#line 40 "exemploSem.y"
{ escopoLocal = new TabSimb(); }
break;
case 18:
//#line 41 "exemploSem.y"
{ ts = escopoLocal; }
break;
case 19:
//#line 41 "exemploSem.y"
{ ts = aux; }
break;
case 20:
//#line 41 "exemploSem.y"
{ TS_entry nodo = ts.pesquisa(val_peek(9).sval);
                                                                                                            nodo.insereLocais(escopoLocal); escopoLocal.listar(); }
break;
case 24:
//#line 51 "exemploSem.y"
{ currentType = (TS_entry)val_peek(0).obj; }
break;
case 29:
//#line 60 "exemploSem.y"
{ currentType = (TS_entry)val_peek(0).obj; }
break;
case 31:
//#line 61 "exemploSem.y"
{ currentType = (TS_entry)val_peek(0).obj; }
break;
case 33:
//#line 64 "exemploSem.y"
{ TS_entry nodo = escopoLocal.pesquisa(val_peek(0).sval);
                            if (nodo != null) {
                              yyerror("(sem) variavel >" + val_peek(0).sval + "< jah declarada");
                } else {
                  escopoLocal.insert(new TS_entry(val_peek(0).sval, currentType, ClasseID.VarLocal));
                }
         }
break;
case 34:
//#line 72 "exemploSem.y"
{ TS_entry nodo = escopoLocal.pesquisa(val_peek(0).sval);
                            if (nodo != null) {
                              yyerror("(sem) parametro >" + val_peek(0).sval + "< jah declarada");
                } else {
                  escopoLocal.insert(new TS_entry(val_peek(0).sval, currentType, ClasseID.NomeParam));
                }
         }
break;
case 35:
//#line 81 "exemploSem.y"
{ TS_entry nodo = ts.pesquisa(val_peek(0).sval);
                            if (nodo != null) {
                              yyerror("(sem) variavel >" + val_peek(0).sval + "< jah declarada");
                } else {
                  ts.insert(new TS_entry(val_peek(0).sval, currentType, currClass));
                }
         }
break;
case 36:
//#line 91 "exemploSem.y"
{ yyval.obj = Tp_INT; }
break;
case 37:
//#line 92 "exemploSem.y"
{ yyval.obj = Tp_DOUBLE; }
break;
case 38:
//#line 93 "exemploSem.y"
{ yyval.obj = Tp_BOOL; }
break;
case 44:
//#line 108 "exemploSem.y"
{  if ( ((TS_entry)val_peek(2).obj) != Tp_BOOL)
                                 yyerror("(sem) expressão (if) deve ser lógica "+((TS_entry)val_peek(2).obj).getTipo());
                         }
break;
case 45:
//#line 114 "exemploSem.y"
{ yyval.obj = validaTipo('+', (TS_entry)val_peek(2).obj, (TS_entry)val_peek(0).obj); }
break;
case 46:
//#line 115 "exemploSem.y"
{ yyval.obj = validaTipo('>', (TS_entry)val_peek(2).obj, (TS_entry)val_peek(0).obj); }
break;
case 47:
//#line 116 "exemploSem.y"
{ yyval.obj = validaTipo(AND, (TS_entry)val_peek(2).obj, (TS_entry)val_peek(0).obj); }
break;
case 48:
//#line 117 "exemploSem.y"
{ yyval.obj = Tp_INT; }
break;
case 49:
//#line 118 "exemploSem.y"
{ yyval.obj = val_peek(1).obj; }
break;
case 50:
//#line 119 "exemploSem.y"
{ TS_entry nodo = ts.pesquisa(val_peek(0).sval);
                if (nodo == null) {
					nodo = aux.pesquisa(val_peek(0).sval);
					if (nodo == null) {
                   yyerror("(sem) var <" + val_peek(0).sval + "> nao declarada");
                   yyval.obj = Tp_ERRO;
				} else
                    yyval.obj = nodo.getTipo();
			   } 
                else
                    yyval.obj = nodo.getTipo();
              }
break;
case 51:
//#line 131 "exemploSem.y"
{  yyval.obj = validaTipo(ATRIB, (TS_entry)val_peek(2).obj, (TS_entry)val_peek(0).obj);  }
break;
case 52:
//#line 132 "exemploSem.y"
{  if ((TS_entry)val_peek(1).obj != Tp_INT)
                          yyerror("(sem) indexador não é numérico ");
                       else
                           if (((TS_entry)val_peek(3).obj).getTipo() != Tp_ARRAY)
                              yyerror("(sem) elemento não indexado ");
                           else
                              yyval.obj = ((TS_entry)val_peek(3).obj).getTipoBase();
                     }
break;
case 53:
//#line 140 "exemploSem.y"
{ TS_entry nodo = ts.pesquisa(val_peek(0).sval);
                  if (nodo == null) {
                     yyerror("(sem) struct <" + val_peek(0).sval + "> nao declarada");
                     yyval.obj = Tp_ERRO;
                  }
                  else
                      yyval.obj = nodo.getTipo();
                  }
break;
case 54:
//#line 148 "exemploSem.y"
{ TS_entry nodo_new = ts.pesquisa(val_peek(0).sval);
					if (nodo_new != null) {
					  yyerror("(sem) variavel >" + val_peek(0).sval + "< jah declarada");
				} else {
				  TS_entry aux = new TS_entry(val_peek(0).sval, Tp_STRUCT, currClass);
				  TS_entry nodoDecl = ts.pesquisa(val_peek(2).sval);
				  /* pesquisar o nodoDecl.getLocais() e fazer uma copia e inserir no aux*/
				  
				  aux.insereLocais(nodoDecl.getLocais());
				  ts.insert(aux);
				}
           }
break;
case 55:
//#line 161 "exemploSem.y"
{ TS_entry nodo = ts.pesquisa(val_peek(0).sval);
              if (nodo == null) {
                 yyerror("(sem) variavel <" + val_peek(0).sval + "> nao declarada");
                 yyval.obj = Tp_ERRO;
              }
              else {
                  if(nodo.getTipo() != Tp_STRUCT) {
                      yyerror("(sem) variavel <" + val_peek(0).sval + "> nao é uma STRUCT");
                      yyval.obj = Tp_ERRO;
                  }
              }
         }
break;
case 56:
//#line 173 "exemploSem.y"
{ TS_entry nodo = ts.pesquisa(val_peek(3).sval);
              escopoLocal = nodo.getLocais();
              if (escopoLocal == null) {
                  yyerror("(sem) nenhuma variavel local declarada");
                  yyval.obj = Tp_ERRO;
              }
              TS_entry nodoLocal = escopoLocal.pesquisa(val_peek(0).sval);
               if (nodoLocal == null) {
                  yyerror("(sem) variavel local <" + val_peek(0).sval + "> nao declarada");
                  yyval.obj = Tp_ERRO;
               }
               else yyval.obj = nodoLocal.getTipo();
          }
break;
case 57:
//#line 186 "exemploSem.y"
{ TS_entry nodo = aux.pesquisa(val_peek(1).sval);
				 if(nodo == null) {
					 pilhaEscopo.push(null);
					 pilhaPosicao.push(0);
                     yyerror("(sem) Funcao <" + val_peek(1).sval + "> nao declarada");
					 yyval.obj = Tp_ERRO;
                 } else {
					 pilhaEscopo.push(nodo.getLocais());
					 pilhaPosicao.push(nodo.getLocais().contaParam());
				 }
			   }
break;
case 58:
//#line 196 "exemploSem.y"
{ pilhaEscopo.pop(); pilhaPosicao.pop(); }
break;
case 59:
//#line 199 "exemploSem.y"
{ if ( pilhaEscopo.peek().get(pilhaPosicao.peek()-1).getTipo() != (TS_entry)val_peek(0).obj ) {
						yyerror("(sem) tipo do parametro <" + val_peek(0).obj + "> errado");
					  } else if ( pilhaEscopo.peek().get(pilhaPosicao.peek()-1).getClasse() != ClasseID.NomeParam ) {
						yyerror("(sem) parametro errado");
					  } else { pilhaPosicao.push(pilhaPosicao.pop() - 1); }
				   }
break;
case 60:
//#line 205 "exemploSem.y"
{ if (pilhaPosicao.peek() != 1) {
				yyerror("(sem) numero errado de parametros");
			 } else if (pilhaEscopo.peek().get(pilhaPosicao.peek()-1).getTipo() != (TS_entry)val_peek(0).obj) {
				yyerror("(sem) tipo do parametro <" + val_peek(0).obj + "> errado, esperado: <"+ (TS_entry)val_peek(0).obj +">");
			 }
		   }
break;
//#line 889 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
