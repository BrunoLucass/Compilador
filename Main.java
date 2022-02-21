import exceptions.LexicoException;
import exceptions.SemanticoException;
import exceptions.SintaticoException;

public class Main {
	public static void main(String[] args) {
		try {
            Lexico sc = new Lexico("C:\\Users\\j-vit\\input.txt");
            Sintatico  pa = new Sintatico(sc);

			pa.programa();

			
        } catch (LexicoException ex) {
			System.out.println("Lexical Error "+ex.getMessage());
		}
		catch (SintaticoException ex) {
			System.out.println("Syntax Error "+ex.getMessage());
        } 
		catch(SemanticoException ex) {
			System.out.println("Semantic Error " + ex.getMessage());
		}
		catch(Exception ex) {
			System.out.println("Generic error! / End of file");
			
		}
		
          
    }
}

