package splosno;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import gui.Okno;

public class Vodja {

	public static Map<Igralec, VrstaIgralca> vrstaIgralca;
	public static Map<Igralec, KdoIgra> kdoIgra;
	
	public static Okno okno;

	public static Igra igra = null;
	
	public static boolean clovekNaVrsti = false;
	
	private static final Random RANDOM = new Random();

	public static void igramoNovoIgro () {
		igra = new Igra ();
		igramo ();
	}
	
	public static void igramo () {
		okno.osveziGUI();
		switch (igra.stanje()) {
		case ZMAGA_CRNA: break;
		case ZMAGA_BELA: 
		case NEODLOCENO: 
			return; // odhajamo iz metode igramo
		case V_TEKU: 
			Igralec igralec = igra.naPotezi();
			VrstaIgralca vrstaNaPotezi = vrstaIgralca.get(igralec);
			switch (vrstaNaPotezi) {
			case C: 
				clovekNaVrsti = true;
				break;
			case R:
				igrajRacunalnikovoPotezo();
				break;
			}
		}
	}
	
	public static void igrajRacunalnikovoPotezo() {
		int prviMet = Igra.metKocke();
		int drugiMet = Igra.metKocke();
		int[] meti;
		if (prviMet == drugiMet) {
			meti = new int[] {prviMet, prviMet, prviMet, prviMet};
		}
		else {meti = new int[] {prviMet, drugiMet};}
		List<Poteza> moznePoteze = Igra.moznePoteze(meti);
		int i = RANDOM.nextInt(moznePoteze.size());	
		Poteza poteza = moznePoteze.get(i);
		igra.odigraj(poteza);
		igramo ();
		

	}
	
	public static void igrajClovekovoPotezo(Poteza poteza) {
		if (igra.odigraj(poteza)) clovekNaVrsti = false;
		igramo ();
	}	
}
