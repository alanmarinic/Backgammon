package inteligenca;

import splosno.Igra;

// Tip definiramo za lažje delovanje AI
public class Postavitev {
	
	Igra igra;
	double stZmag;
	double stZmagNasprotnik;
	double odigraneIgre;
	
	public Postavitev(Igra igra, double stZmag, double stZmagNasprotnik, double odigraneIgre) {
		this.igra = igra;
		this.stZmag = stZmag;
		this.stZmagNasprotnik = stZmagNasprotnik;
		this.odigraneIgre = odigraneIgre;
	}
	
}
