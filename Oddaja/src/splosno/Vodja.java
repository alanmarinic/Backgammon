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
				vrziKocki();
				break;
			case R:
				igrajRacunalnikovoPotezo();
				break;
			}
		}
	}
	
	
	
	public static int[] vrziKocki() {
		prviMet = Igra.metKocke();
		drugiMet = Igra.metKocke();
		if (prviMet == drugiMet) {
			meti = new int[] {prviMet, prviMet, prviMet, prviMet};
		}
		else {meti = new int[] {prviMet, drugiMet};}
		return meti;
	}
	
	public static void igrajRacunalnikovoPotezo() {
		Igra tempIgra = new Igra(igra);
		SwingWorker<List<Poteza>, Void> worker = new SwingWorker<List<Poteza>, Void> () {
			// V ozadju izbere in odigra računalnikovo potezo
			@Override
			protected List<Poteza> doInBackground() {
				System.out.println("pred montecarlo");
				List<Poteza> poteze = MonteCarlo.izberiPotezo(tempIgra, vrziKocki(), 100);

				System.out.println("po montecarlo");
				//izpisiSeznamPotez(poteze);
				/*for (Poteza p : poteze) {
					igra.odigraj(p);
					okno.osveziGUI();
					try {TimeUnit.SECONDS.sleep(1);} catch (Exception e) {};
				}*/
				if (poteze == null) 		System.out.println("poteze v vodji1 so null");
				return poteze;
			}
			@Override
			protected void done () {
				List<Poteza> poteze = null;
				try {poteze = get();} catch (Exception e) {System.out.println("exception");};
				System.out.println("done");
				Vodja.izpisiSeznamPotez(poteze);
				if (poteze == null) 		System.out.println("poteze v vodji so null");
				else {
					for (Poteza p : poteze) {
						igra.odigraj(p);
						System.out.println("odigran p");
						///////ne osvezi
						okno.osveziGUI();
						//try {TimeUnit.SECONDS.sleep(2);} catch (Exception e) {};
					}
				igra.naPotezi = igra.naPotezi().nasprotnik();
				try {TimeUnit.SECONDS.sleep(2);} catch (Exception e) {};

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
			igra.naPotezi = igra.naPotezi().nasprotnik();
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
