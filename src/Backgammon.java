import gui.Okno;
import splosno.Vodja;

// S to kodo zaženemo program 
public class Backgammon {

	public static void main(String[] args) {
		Okno glavno_okno = new Okno();
		glavno_okno.pack();
		glavno_okno.setVisible(true);
		Vodja.okno = glavno_okno;
	}
	
}
