package splosno;

import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import gui.Okno;

public class Vodja {

	public static Map<Igralec, VrstaIgralca> vrstaIgralca;
	//public static Map<Igralec, KdoIgra> kdoIgra;
	
	public static Okno okno;

	public static Igra igra = null;
	
	public static boolean clovekNaVrsti = false;
	public static KdoIgra racunalnikovaInteligenca;
	
	private static final Random RANDOM = new Random();
	
	public static int prviMet;
	public static int drugiMet;
	public static int[] meti;

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
				System.out.println("clovek");
				clovekNaVrsti = true;
				prviMet = Igra.metKocke();
				drugiMet = Igra.metKocke();
				if (prviMet == drugiMet) {
					meti = new int[] {prviMet, prviMet, prviMet, prviMet};
				}
				else {meti = new int[] {prviMet, drugiMet};}
				izpisiSeznam(meti);
				break;
			case R:
				igrajRacunalnikovoPotezo();
				break;
			}
		}
	}
	
	public static void igrajRacunalnikovoPotezo() {
		prviMet = Igra.metKocke();
		drugiMet = Igra.metKocke();
		if (prviMet == drugiMet) {
			meti = new int[] {prviMet, prviMet, prviMet, prviMet};
		}
		else {meti = new int[] {prviMet, drugiMet};}
		int dolzina = meti.length;
		for (int j = 0; j < dolzina; j ++) {
			List<Poteza> moznePoteze = Igra.moznePoteze(meti);
			if (moznePoteze.size() == 0) {
				break;
			}
			int i = RANDOM.nextInt(moznePoteze.size());	
			Poteza poteza = moznePoteze.get(i);
			igra.odigraj(poteza);
		}
		Igra.naPotezi = Igra.naPotezi.nasprotnik();
		igramo ();
	}
	
	public static void igrajClovekovoPotezo(Poteza poteza) {
		igra.odigraj(poteza);
		okno.osveziGUI();
		if (meti.length == 0) {
			clovekNaVrsti = false;
			Igra.naPotezi = Igra.naPotezi.nasprotnik();
			igramo ();
		}
		
	}	
	
	public static void izpisiSeznam(int[] seznam) {
		for (int i : seznam) System.out.print(i + " ");
		System.out.println();
	}
	
	public static void izpisiSeznamPotez(List<Poteza> seznam) {
		for (Poteza i : seznam) System.out.print(i.zacetnoPolje + "-" + i.koncnoPolje + " ");
		System.out.println();
	}
}
