
public class Token {
	public static final String TK_IDENTIFIER = "IDENTIFIER";
    public static final String TK_INTEIRO = "INT (INTEIRO) ";
    public static final String TK_FLOAT = "FLOAT (REAL) ";
    public static final String TK_CHAR = "CHAR";
    public static final String TK_OPERATOR_relacional_maior = ">";
    public static final String TK_OPERATOR_relacional_menor = "<";
    public static final String TK_OPERATOR_relacional_maior_igual = ">=";
    public static final String TK_OPERATOR_relacional_menor_igual = "<=";
    public static final String TK_OPERATOR_relacional_diferenca = "!=";
    public static final String TK_OPERATOR_aritmetrico_mais= "+";
    public static final String TK_OPERATOR_aritmetrico_menos= "-";
    public static final String TK_OPERATOR_aritmetrico_multiplicacao= "*";
    public static final String TK_OPERATOR_aritmetrico_divisao= "/";
    public static final String TK_OPERATOR_igual= "=";
    public static final String TK_OPERATOR_atribuidor= "= (ATRIBUICAO)";
    public static final String TK_CARACTER_especial_abre_parenteses = "(";
    public static final String TK_CARACTER_especial_fecha_parenteses = ")";
    public static final String TK_CARACTER_especial_abre_chave = "{";
    public static final String TK_CARACTER_especial_fecha_chave = "}";
    public static final String TK_CARACTER_especial_virgula = ",";
    public static final String TK_CARACTER_especial_pontovirgula = ";";
    public static final String TK_PALAVRA_reservada = "PALAVRA RESERVADA";
    public static final String TK_FIM_CODIGO = "$";

    private String type;
    private String text;
    private int line;
    private int column;
    public Token(String type, String text, int line, int column) {
		super();
		this.type = type;
		this.text = text;
		this.line = line;
		this.column = column;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public Token(String type, String text) {
        super();
        this.type = type;
        this.text = text;
    }

    public Token() {
        super();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

	@Override
	public String toString() {
		return "Token - > Tipo =" + type + ", Lexema = " + text ;
	}
    
    
    
    
}
