import java.util.LinkedList;
import java.util.List;

import exceptions.SemanticoException;
import exceptions.SintaticoException;

public class Sintatico {
	private Lexico scanner;
	private Token token;
	
	List<String> tabela = new LinkedList<String>();
	List<String> tabelaVariavel = new LinkedList<String>();
	List<String> tabelaTipo = new LinkedList<String>();
	
	String textoId;
	int index;
	

	public Sintatico(Lexico scanner) {
		this.scanner = scanner;
	}

	public void programa() {
		token = scanner.nextToken();
		if(token.getText().compareTo("int") != 0) {
			throw new SintaticoException("INT FOR MAIN EXPECTED, found " + token.getText() + " WATCH NEAR LINE " + token.getLine());
		}
		token = scanner.nextToken();
		if(token.getText().compareTo("main") != 0) {
			throw new SintaticoException("RESERVED WORD MAIN EXPECTED, found " + token.getText() + " WATCH NEAR LINE " + token.getLine());
		}
		token = scanner.nextToken();
		if(token.getType() != Token.TK_CARACTER_especial_abre_parenteses) {
			throw new SintaticoException("( EXPECTED, found " + token.getText() + " WATCH NEAR LINE " + token.getLine());
		}
		token = scanner.nextToken();
		if(token.getType() != Token.TK_CARACTER_especial_fecha_parenteses) {
			throw new SintaticoException(") EXPECTED, found " + token.getText() + " WATCH NEAR LINE " + token.getLine());
		}
		token = scanner.nextToken();
		bloco();
	}
	
	public void bloco() {
		if(token.getType() != Token.TK_CARACTER_especial_abre_chave) {
		throw new SintaticoException("{ EXPECTED, found " + token.getText() + " WATCH NEAR LINE " + token.getLine());
		}	
		token = scanner.nextToken();
		
		do {
			decl_var();
			comando();
			token = scanner.nextToken();
		} while(token.getType() != Token.TK_CARACTER_especial_fecha_chave);
	}
	
	public void decl_var() {
		tipo();
		id();
		token = scanner.nextToken();
		if(token.getType() != Token.TK_CARACTER_especial_pontovirgula) {
			throw new SintaticoException("; EXPECTED, found " + token.getText() + " WATCH NEAR LINE " + token.getLine());
		}
	}
	
	public void tipo() {
		if(token.getText().compareTo("int") != 0 && token.getText().compareTo("float") != 0 && token.getText().compareTo("char") != 0) {
			throw new SintaticoException("VARIABLE TYPE EXPECTED, found " + token.getText() + " WATCH NEAR LINE " + token.getLine());
		}
		
		tabela.add(token.getText());
	}
	
	public void id() {
		token = scanner.nextToken();
		if(token.getType() != Token.TK_IDENTIFIER) {
			throw new SintaticoException("ID EXPECTED, found " + token.getText() + " WATCH NEAR LINE " + token.getLine());
		}
		
		if(tabelaVariavel.contains(token.getText()) == true) {
			throw new SemanticoException("Variavel '" + token.getText() + "' repetida! WATCH NEAR LINE " + token.getLine());
		}
		
		tabelaVariavel.add(token.getText());
	}
	
	public void comando() {
		token = scanner.nextToken();
		if(token.getText().compareTo("while") == 0 || token.getText().compareTo("for") == 0) {
			iteracao();
		}
		else if(token.getText().compareTo("if") == 0) {
			token = scanner.nextToken();
			if(token.getType() != Token.TK_CARACTER_especial_abre_parenteses) {
				throw new SintaticoException("( EXPECTED, found " + token.getText() + " WATCH NEAR LINE " + token.getLine());
			}
			expr_relacional();
			if(token.getType() != Token.TK_CARACTER_especial_fecha_parenteses) {
				throw new SintaticoException(") EXPECTED, found " + token.getText() + " WATCH NEAR LINE " + token.getLine());
			}
			token = scanner.nextToken();
			if(token.getType() != Token.TK_CARACTER_especial_abre_chave) {
				throw new SintaticoException("{ EXPECTED, found " + token.getText() + " WATCH NEAR LINE " + token.getLine());
			}
			comando();
			token = scanner.nextToken();
			if(token.getType() != Token.TK_CARACTER_especial_fecha_chave) {
				throw new SintaticoException("} EXPECTED, found " + token.getText() + " WATCH NEAR LINE " + token.getLine());
			}
			token = scanner.nextToken();
			if(token.getText().compareTo("else") != 0) {
				throw new SintaticoException("RESERVED WORD MAIN EXPECTED, found " + token.getText() + " WATCH NEAR LINE " + token.getLine());
			}
			token = scanner.nextToken();
			if(token.getType() != Token.TK_CARACTER_especial_abre_chave) {
				throw new SintaticoException("{ EXPECTED, found " + token.getText() + " WATCH NEAR LINE " + token.getLine());
			}
			comando();
			token = scanner.nextToken();
			if(token.getType() != Token.TK_CARACTER_especial_fecha_chave) {
				throw new SintaticoException("} EXPECTED, found " + token.getText() + " WATCH NEAR LINE " + token.getLine());
			}
		}
		else {
			comando_basico();
		}
	}
	
	public void comando_basico() {
		if(token.getType() == Token.TK_CARACTER_especial_abre_chave) {
			bloco();
		}
		else {
			atribuicao();
		}
	}
	
	public void atribuicao() {
		if(token.getType() != Token.TK_IDENTIFIER) {
			throw new SintaticoException("ID EXPECTED, found " + token.getText() + " WATCH NEAR LINE " + token.getLine());
		}
		textoId = token.getText();
		if(tabelaVariavel.contains(token.getText()) == false) {
			throw new SemanticoException("VARIABLE '" + token.getText() + "' NOT DECLARED WATCH NEAR LINE " + token.getLine());
		}
		token = scanner.nextToken();
		if(token.getType() != Token.TK_OPERATOR_atribuidor) {
			throw new SintaticoException("= FOR VARIABLE DECLARATION EXPECTED, found " + token.getText()+ " WATCH NEAR LINE " + token.getLine() );
		}
		expr_arit();
		if(tabelaTipo.contains(Token.TK_CHAR) && tabelaTipo.contains(Token.TK_FLOAT)) {
			throw new SemanticoException("CHAR TYPE INCOMPATIBLE WITH FLOAT TYPE, found " + token.getText() + " WATCH NEAR LINE " + token.getLine());
		}
		else if(tabelaTipo.contains(Token.TK_CHAR) && tabelaTipo.contains(Token.TK_INTEIRO)) {
			throw new SemanticoException("CHAR TYPE INCOMPATIBLE WITH INT TYPE, found " + token.getText() + " WATCH NEAR LINE " + token.getLine());
		}
		else if(tabelaTipo.contains(Token.TK_FLOAT) && tabelaTipo.contains(Token.TK_INTEIRO) && tabelaTipo.contains(Token.TK_CHAR) == false) {
			index = tabelaVariavel.indexOf(textoId);
			if(tabela.get(index).compareTo("float") != 0) {
				throw new SemanticoException("FLOAT DECLARATION EXPECTED, found " + token.getText() + " WATCH NEAR LINE " + token.getLine());
			}
		}
		else if(tabelaTipo.contains(Token.TK_FLOAT) && tabelaTipo.contains(Token.TK_CHAR) == false) {
			index = tabelaVariavel.indexOf(textoId);
			if(tabela.get(index).compareTo("float") != 0) {
				throw new SemanticoException("FLOAT DECLARATION EXPECTED, found " + token.getText() + " WATCH NEAR LINE " + token.getLine());
			}
		}
		else if(tabelaTipo.contains(Token.TK_INTEIRO) && tabelaTipo.contains(Token.TK_CHAR) == false) {
			index = tabelaVariavel.indexOf(textoId);
			if(tabela.get(index).compareTo("char") == 0) {
				throw new SemanticoException("FLOAT OR INT DECLARATION EXPECTED, found " + token.getText() + " WATCH NEAR LINE " + token.getLine());
			}
		}
		else {
			index = tabelaTipo.indexOf(textoId);
			if(tabelaVariavel.get(index).compareTo("char") != 0) {
				throw new SemanticoException("CHAR DECLARATION EXPECTED, found " + token.getText() + " WATCH NEAR LINE " + token.getLine());
			}
		}
		tabelaTipo.clear();
		if(token.getType() != Token.TK_CARACTER_especial_pontovirgula) {
			throw new SintaticoException("; EXPECTED, found " + token.getText() + " WATCH NEAR LINE " + token.getLine());
		}
	}
	
	public void iteracao() {
		if(token.getText().compareTo("while") == 0) {
			token = scanner.nextToken();
			if(token.getType() != Token.TK_CARACTER_especial_abre_parenteses) {
				throw new SintaticoException("( EXPECTED, found " + token.getText() + " WATCH NEAR LINE " + token.getLine());
			}
			expr_relacional();
			if(token.getType() != Token.TK_CARACTER_especial_fecha_parenteses) {
				throw new SintaticoException(") EXPECTED, found " + token.getText() + " WATCH NEAR LINE " + token.getLine());
			}
			token = scanner.nextToken();
			if(token.getType() != Token.TK_CARACTER_especial_abre_chave) {
				throw new SintaticoException("{ EXPECTED, found " + token.getText() + " WATCH NEAR LINE " + token.getLine());
			}
			comando();
			token = scanner.nextToken();
			if(token.getType() != Token.TK_CARACTER_especial_fecha_chave) {
				throw new SintaticoException("} EXPECTED, found " + token.getText() + " WATCH NEAR LINE " + token.getLine());
			}
		}
		
		else {
			String idAnterior;
			
			token = scanner.nextToken();
			if(token.getType() != Token.TK_CARACTER_especial_abre_parenteses) {
				throw new SintaticoException("( EXPECTED, found " + token.getText() + " WATCH NEAR LINE " + token.getLine());
			}
			token = scanner.nextToken();
			if(token.getType() != Token.TK_IDENTIFIER) {
				throw new SintaticoException("ID EXPECTED, found " + token.getText() + " WATCH NEAR LINE " + token.getLine());
			}
			if(tabelaVariavel.contains(token.getText()) == false) {
				throw new SemanticoException("VARIABLE" + token.getText() + " NOT DECLARED WATCH NEAR LINE " + token.getLine() );
			}
			textoId = token.getText();
			token = scanner.nextToken();
			if(token.getType() != Token.TK_OPERATOR_atribuidor) {
				throw new SintaticoException("Atribuidor Expected!");
			}
			token = scanner.nextToken();
			if(token.getType() != Token.TK_FLOAT && token.getType() != Token.TK_INTEIRO && token.getType() != Token.TK_CHAR) {
				throw new SintaticoException("Terminal Expected!");
			}
			index = tabelaVariavel.indexOf(textoId);
			if(token.getType() != tabela.get(index)) {
				if(token.getType().compareToIgnoreCase("character") == 0 && tabela.get(index).compareTo("float") == 0) {
					throw new SemanticoException("Tipo 'float' incompativel com tipo 'char'!");
				}
				else if(token.getType().compareToIgnoreCase("character") == 0 && tabela.get(index).compareTo("int") == 0) {
					throw new SemanticoException("Tipo 'int' incompativel com tipo 'char'!");
				}
				else if(token.getType().compareToIgnoreCase("inteiro") == 0 && tabela.get(index).compareTo("char") == 0) {
					throw new SemanticoException("Tipo 'char' incompativel com tipo 'int'!");
				}
				else if(token.getType().compareToIgnoreCase("float") == 0 && tabela.get(index).compareTo("int") == 0) {
					throw new SemanticoException("Tipo 'int' incompativel com tipo 'float'!");
				}
			}
			token = scanner.nextToken();
			if(token.getType() != Token.TK_CARACTER_especial_pontovirgula) {
				throw new SintaticoException("Ponto e virgula Expected!");
			}
			expr_relacional();
			if(token.getType() != Token.TK_CARACTER_especial_pontovirgula) {
				throw new SintaticoException("Ponto e virgula Expected!");
			}
			token = scanner.nextToken();
			if(token.getType() != Token.TK_IDENTIFIER) {
				throw new SintaticoException("Identifier Expected!");
			}
			if(tabelaVariavel.contains(token.getText()) == false) {
				throw new SemanticoException("Variavel '" + token.getText() + "' nao declarada!");
			}
			idAnterior = token.getText();
			index = tabelaVariavel.indexOf(idAnterior);
			if(tabela.get(index).compareTo("char") == 0) {
				throw new SemanticoException("Tipo 'char' incompativel com tipo 'int'!");
			}
			token = scanner.nextToken();
			if(token.getType() != Token.TK_OPERATOR_atribuidor) {
				throw new SintaticoException("Atribuidor Expected!");
			}
			token = scanner.nextToken();
			if(token.getType() != Token.TK_IDENTIFIER) {
				throw new SintaticoException("Identifier Expected!");
			}
			if(idAnterior.compareTo(token.getText()) != 0) {
				throw new SemanticoException("Previous different identifier!");
			}
			token = scanner.nextToken();
			if(token.getType() != Token.TK_OPERATOR_aritmetrico_divisao && token.getType() != Token.TK_OPERATOR_aritmetrico_mais && token.getType() != Token.TK_OPERATOR_aritmetrico_menos && token.getType() != Token.TK_OPERATOR_aritmetrico_multiplicacao) {
				throw new SintaticoException("Operador aritmetrico Expected!");
			}
			token = scanner.nextToken();
			if(token.getType() != Token.TK_INTEIRO) {
				throw new SintaticoException("Inteiro Expected!");
			}
			token = scanner.nextToken();
			if(token.getType() != Token.TK_CARACTER_especial_fecha_parenteses) {
				throw new SintaticoException("Fecha parenteses Expected!");
			}
			token = scanner.nextToken();
			if(token.getType() != Token.TK_CARACTER_especial_abre_chave) {
				throw new SintaticoException("Abre chave Expected!");
			}
			comando();
			token = scanner.nextToken();
			if(token.getType() != Token.TK_CARACTER_especial_fecha_chave) {
				throw new SintaticoException("Fecha chave Expected!");
			}
		}
	}
	
	public void expr_relacional() {
		expr_arit();
		if(token.getType() != Token.TK_OPERATOR_relacional_diferenca && token.getType() != Token.TK_OPERATOR_relacional_maior && token.getType() != Token.TK_OPERATOR_relacional_maior_igual && token.getType() != Token.TK_OPERATOR_relacional_menor && token.getType() != Token.TK_OPERATOR_relacional_menor_igual && token.getType() != Token.TK_OPERATOR_igual) {
			throw new SintaticoException("Operador relacional Expected!");
		}
		expr_arit();
	}
	
	public void expr_arit() {
		termo();
		expr_aritL();
	}
	
	public void expr_aritL() {
		if (token != null) {
			if(token.getType() != Token.TK_CARACTER_especial_pontovirgula && token.getType() != Token.TK_OPERATOR_relacional_diferenca && token.getType() != Token.TK_OPERATOR_relacional_maior && token.getType() != Token.TK_OPERATOR_relacional_maior_igual && token.getType() != Token.TK_OPERATOR_relacional_menor && token.getType() != Token.TK_OPERATOR_relacional_menor_igual && token.getType() != Token.TK_OPERATOR_igual && token.getType() != Token.TK_CARACTER_especial_fecha_parenteses) {
				if(token.getType() != Token.TK_OPERATOR_aritmetrico_mais && token.getType() != Token.TK_OPERATOR_aritmetrico_menos) {
					throw new SintaticoException("Operador aritmetrico Expected!");
				}
				termo();
				expr_aritL();
			}
		}
	}
	
	public void termo() {
		fator();
		termoL();
	}
	
	public void termoL() {
		token = scanner.nextToken();
		if (token != null) {
			if(token.getType() == Token.TK_OPERATOR_aritmetrico_multiplicacao || token.getType() == Token.TK_OPERATOR_aritmetrico_divisao) {
				if(token.getType() != Token.TK_OPERATOR_aritmetrico_multiplicacao && token.getType() != Token.TK_OPERATOR_aritmetrico_divisao) {
					throw new SintaticoException("Operador aritmetrico Expected!");
				}
				fator();
				termoL();
			}
		}
	}
	
	public void fator() {
		token = scanner.nextToken();
		if(token.getType() == Token.TK_CARACTER_especial_abre_parenteses) {
			if(token.getType() != Token.TK_CARACTER_especial_abre_parenteses) {
				throw new SintaticoException("Abre parentese Expected!");
			}
			expr_arit();
			token = scanner.nextToken();
			if(token.getType() != Token.TK_CARACTER_especial_fecha_parenteses) {
				throw new SintaticoException("Fecha parenteses Expected!");
			}
		}
		else {
			if(token.getType() != Token.TK_IDENTIFIER && token.getType() != Token.TK_FLOAT && token.getType() != Token.TK_INTEIRO && token.getType() != Token.TK_CHAR) {
				throw new SintaticoException("Terminal Expected!");
			}
			
			if(token.getType() == Token.TK_IDENTIFIER && tabelaVariavel.contains(token.getText()) == false) {
				throw new SemanticoException("Variavel '" + token.getText() + "' nao declarada!");
			}
			
			if(token.getType() == Token.TK_FLOAT || token.getType() == Token.TK_INTEIRO || token.getType() == Token.TK_CHAR) {
				tabelaTipo.add(token.getType());
			}
		}
	}
}
