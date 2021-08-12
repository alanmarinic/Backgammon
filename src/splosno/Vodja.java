package splosno;

import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingWorker;

import gui.Okno;
import inteligenca.MonteCarlo;

public class Vodja {

	public static Map<Igralec, VrstaIgralca> vrstaIgralca;
	
	public static Okno okno;

	public static Igra igra = null;
	
	public static boolean clovekNaVrsti = false;
	
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
		case ZMAGA_BELA: break;
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
	
	public static void igrajRacunalnikovoPotezo1() {
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
	
	public static void igrajRacunalnikovoPotezo() {
		Igra zacetnaIgra = igra;
		SwingWorker<List<Poteza>, Void> worker = new SwingWorker<List<Poteza>, Void> () {
			// V ozadju izbere in odigra računalnikovo potezo
			@Override
			protected List<Poteza> doInBackground() {
				prviMet = Igra.metKocke();
				drugiMet = Igra.metKocke();
				if (prviMet == drugiMet) {
					meti = new int[] {prviMet, prviMet, prviMet, prviMet};
				}
				else {meti = new int[] {prviMet, drugiMet};}
				List<Poteza> poteze = MonteCarlo.izberiPotezo(igra, meti, 1);
				izpisiSeznamPotez(poteze);
				/*for (Poteza p : poteze) {
					igra.odigraj(p);
					okno.osveziGUI();
					try {TimeUnit.SECONDS.sleep(1);} catch (Exception e) {};
				}*/
				return poteze;
			}
			@Override
			protected void done () {
				List<Poteza> poteze = null;
				try {poteze = get();} catch (Exception e) {};
				if (igra == zacetnaIgra) {
					for (Poteza p : poteze) {
						igra.odigraj(p);
						//okno.osveziGUI();
						//try {TimeUnit.SECONDS.sleep(1);} catch (Exception e) {};
					}
					Igra.naPotezi = Igra.naPotezi.nasprotnik();
					igramo ();
				}
			}
		};
		worker.execute();
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
		if (seznam == null) System.out.println("null je");
		for (Poteza i : seznam) System.out.print(i.zacetnoPolje + "-" + i.koncnoPolje + " ");
		System.out.println();
	}
}
