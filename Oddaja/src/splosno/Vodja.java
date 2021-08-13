package splosno;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingWorker;

import gui.Okno;
import inteligenca.MonteCarlo;

public class Vodja {

	public static Map<Igralec, VrstaIgralca> vrstaIgralca;
	
	public static Okno okno;

	public static Igra igra = null;
	
	public static boolean clovekNaVrsti = false;
		
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
				clovekNaVrsti = true;
				vrziKocki();
				break;
			case R:
				igrajRacunalnikovoPotezo();
				break;
			}
		}
	}
	
	//Vrne seznam metov kock, podvojen, ce sta meta enaka
	public static int[] vrziKocki() {
		prviMet = Igra.metKocke();
		drugiMet = Igra.metKocke();
		if (prviMet == drugiMet) {
			meti = new int[] {prviMet, prviMet, prviMet, prviMet};
		}
		else {meti = new int[] {prviMet, drugiMet};}
		return meti;
	}
	
	//Racunalnik odigra svojo potezo z uporabo Monte Carlo Tree Search metodo
	public static void igrajRacunalnikovoPotezo() {
		Igra tempIgra = new Igra(igra);
		SwingWorker<List<Poteza>, Void> worker = new SwingWorker<List<Poteza>, Void> () {
			// V ozadju izbere in vrne računalnikovo potezo
			@Override
			protected List<Poteza> doInBackground() {
				List<Poteza> poteze = MonteCarlo.izberiPotezo(tempIgra, vrziKocki(), 100);				
				return poteze;
			}
			//Odigra potezo
			@Override
			protected void done () {
				List<Poteza> poteze = null;
				try {poteze = get();} catch (Exception e) {};
				if (poteze == null) {
					igra.naPotezi = igra.naPotezi().nasprotnik();
					igramo();
				}
				else if (poteze.isEmpty()) {
					igra.naPotezi = igra.naPotezi().nasprotnik();
					igramo();
				}
				else {
					for (Poteza p : poteze) {
						igra.odigraj(p);
						okno.osveziGUI();
						try {TimeUnit.SECONDS.sleep(1);} catch (Exception e) {};
					}
				igra.naPotezi = igra.naPotezi().nasprotnik();

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
}
