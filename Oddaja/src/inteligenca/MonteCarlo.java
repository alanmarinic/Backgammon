package inteligenca;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import splosno.Igra;
import splosno.Igralec;
import splosno.Poteza;

// AI uporablja Monte Carlo Tree Search
public class MonteCarlo {
	
	// Kljuci so postavitve, vrednosti pa trojice zmag, porazov in stevil odigranih iger
	private static HashMap<Postavitev, List<Postavitev>> drevo = new HashMap<>();
	
	// Igralec, ki izbira potezo
	private static Igralec Carlo;
	
	public static List<Poteza> izberiPotezo(Igra igra, int[] meti, int globina) {
		Igra tempIgra = new Igra(igra);
		Postavitev glavnaPostavitev = new Postavitev(tempIgra, 0, 0, 0);
		Carlo = tempIgra.naPotezi();
		// Razvejamo glavno igro
		HashMap<Igra, List<Poteza>> sPotezami = razvojSPotezami(glavnaPostavitev, meti);
		while (drevo.size() < globina) {
			monte(glavnaPostavitev);
		}
		double max = Double.MIN_VALUE;
		Postavitev najboljsa = null;
		for (Postavitev postavitev: drevo.get(glavnaPostavitev)) {
			if (postavitev.odigraneIgre != 0) {
				double vrednost = postavitev.stZmag / postavitev.odigraneIgre;
				if (vrednost > max) {
					max = vrednost;
					najboljsa = postavitev;
				}
			}
		}
		if (najboljsa == null) 	{
			najboljsa = drevo.get(glavnaPostavitev).get(0);
		}
		if (najboljsa == null) {return new ArrayList<>();}
		List<Poteza> poteze = sPotezami.get(najboljsa.igra);
		return poteze;
	}
	
	
	public static void monte(Postavitev postavitev) {
		// Ce postavitev ni list
		if (drevo.keySet().contains(postavitev)) {
			Postavitev izbranaPostavitev = izbor(postavitev);
			monte(izbranaPostavitev);
		}
		// Postavitev je list
		else {
			// Nismo se odigrali se nobene igre do konca
			if (postavitev.odigraneIgre == 0) {
				Igra tempIgra = new Igra(postavitev.igra);
				rollout(tempIgra);
				if (zmagovalec == Carlo) {
					postavitev.stZmag += 1;
					postavitev.odigraneIgre += 1;
					update(postavitev, 1, 0);
				}
				else {
					postavitev.stZmagNasprotnik += 1;
					postavitev.odigraneIgre += 1;
					update(postavitev, 0, 1);
				}
			}
			// Je ze odigrana vsaj ena igra
			else {
				// Razvoj z naklucnima metoma
				razvoj(postavitev, vrziKockiMC());
			}
		}
	}
	
	// Razvije igro z danimi meti
	public static void razvoj(Postavitev postavitev, int[] meti) {
		Set<Igra> zacetnaIgra = new HashSet<Igra>();
		Igra tempIgra = new Igra(postavitev.igra);
		zacetnaIgra.add(tempIgra);
		Set<Igra> noveIgre;
		if (meti.length == 4) {
			noveIgre = odigrajVsePoteze(zacetnaIgra, meti[0]);
			noveIgre = odigrajVsePoteze(noveIgre, meti[1]);
			noveIgre = odigrajVsePoteze(noveIgre, meti[2]);
			noveIgre = odigrajVsePoteze(noveIgre, meti[3]);
		}
		else {
			noveIgre = odigrajVsePoteze(zacetnaIgra, meti[0]);
			noveIgre = odigrajVsePoteze(noveIgre, meti[1]);
			noveIgre.addAll(odigrajVsePoteze(
								odigrajVsePoteze(zacetnaIgra, meti[1]),
									meti[0]));
		}
		List<Postavitev> seznam = new ArrayList<>();
		// Iz mnozice iger v seznam postavitev
		for (Igra posameznaIgra : noveIgre) {
			posameznaIgra.naPotezi = posameznaIgra.naPotezi().nasprotnik();
			seznam.add(new Postavitev(posameznaIgra, 0, 0, 0));
		}
		// Ce je seznam prazen, te postavitve ne dodamo kot kljuc
		if (seznam.isEmpty()) {
			Igra tempIgra2 = new Igra(postavitev.igra); 
			tempIgra.naPotezi = tempIgra2.naPotezi().nasprotnik();
			seznam.add(new Postavitev(tempIgra2, postavitev.stZmag, postavitev.stZmagNasprotnik, postavitev.odigraneIgre));
			drevo.put(postavitev, seznam);
		}
		else {
			drevo.put(postavitev, seznam);
		}
	}
	
	// Razvoj z danimi meti za prvo pozicijo, kjer shranimo tudi poteze
	public static HashMap<Igra, List<Poteza>> razvojSPotezami(Postavitev postavitev, int[] meti) {
		HashMap<Igra, List<Poteza>> zacetnaIgra = new HashMap<>();
		Igra tempIgra = new Igra(postavitev.igra);
		zacetnaIgra.put(tempIgra, new ArrayList<Poteza>());
		HashMap<Igra, List<Poteza>> noveIgre;
		if (meti.length == 4) {
			noveIgre = odigrajVsePoteze2(zacetnaIgra, meti[0]);
			noveIgre = odigrajVsePoteze2(noveIgre, meti[1]);
			noveIgre = odigrajVsePoteze2(noveIgre, meti[2]);
			noveIgre = odigrajVsePoteze2(noveIgre, meti[3]);
		}
		else {
			noveIgre = odigrajVsePoteze2(zacetnaIgra, meti[0]);
			noveIgre = odigrajVsePoteze2(noveIgre, meti[1]);
			
			noveIgre.putAll(odigrajVsePoteze2(
								odigrajVsePoteze2(zacetnaIgra, meti[1]),
									meti[0]));

		}
		List<Postavitev> seznam = new ArrayList<>();
		// Iz mnozice iger v seznam postavitev
		for (Igra posameznaIgra : noveIgre.keySet()) {
			posameznaIgra.naPotezi = posameznaIgra.naPotezi().nasprotnik();
			seznam.add(new Postavitev(posameznaIgra, 0, 0, 0));
		}
		if (seznam.isEmpty()) {
			Igra tempIgra2 = new Igra(postavitev.igra); 
			tempIgra.naPotezi = tempIgra2.naPotezi().nasprotnik();
			seznam.add(new Postavitev(tempIgra2, postavitev.stZmag, postavitev.stZmagNasprotnik, postavitev.odigraneIgre));
			drevo.put(postavitev, seznam);
		}
		else {
			drevo.put(postavitev, seznam);
		}
		return noveIgre;
	}
		
	// Izbere najboljso naslednjo postavitev
	public static Postavitev izbor(Postavitev postavitev) {
		List<Postavitev> otroci = drevo.get(postavitev);
		double max = Double.MIN_VALUE;
		Postavitev najboljsa = null;
		for (Postavitev igra: otroci) {
			double vrednost = UCB(igra, postavitev.odigraneIgre);
			if (vrednost == Double.MAX_VALUE) return igra;
			if (vrednost > max) {
				max = vrednost;
				najboljsa = igra;
			}
		}
		return najboljsa;
	}
	
	// Formula za oceno
	public static double UCB(Postavitev postavitev, double N) {
		int c = 2;
		if (postavitev.odigraneIgre == 0) {
			return Double.MAX_VALUE;
		}
		return postavitev.stZmag/postavitev.odigraneIgre + c * Math.sqrt((Math.log(N))/postavitev.odigraneIgre);
	}

	private static Igralec zmagovalec;
	
	// Rollout sprejme igro, jo odigra do konca in doloci zmagovalca
	public static void rollout(Igra igra) {
		Igra tempIgra = new Igra(igra);
		switch (tempIgra.stanje()) {
		case ZMAGA_BELA: 
			zmagovalec = Igralec.Bela;
			break;
		case ZMAGA_CRNA:
			zmagovalec = Igralec.Crna;
			break;
		case V_TEKU: 
			poteza(tempIgra);
		}		
	}
	
	private static final Random RANDOM = new Random();
	
	// Pomozna funkcija rollouta z nakljucnimi potezami
	public static void poteza(Igra igra) {
		int[] meti = vrziKockiMC();
		int dolzina = meti.length;
		for (int j = 0; j < dolzina; j ++) {
			List<Poteza> moznePoteze = igra.moznePoteze(meti);
			if (moznePoteze.size() == 0) {
				break;
			}
			int i = RANDOM.nextInt(moznePoteze.size());	
			Poteza poteza = moznePoteze.get(i);
			odigrajMC(igra, poteza);
			if (poteza.zacetnoPolje == 26)
				meti = igra.odstraniMet(meti, poteza.koncnoPolje);
			else if (poteza.zacetnoPolje == 27)
				meti = igra.odstraniMet(meti, 25 - poteza.koncnoPolje);
			else 
				meti = igra.odstraniMet(meti, Math.abs(poteza.zacetnoPolje - poteza.koncnoPolje));
		}
		igra.naPotezi = igra.naPotezi().nasprotnik();
		rollout(igra);
	}
	
	// Posodobi stevilo zmag in porazov
	public static void update(Postavitev postavitev, int stZmag, int stZmagNasprotnik) {
		for (Postavitev p : drevo.keySet()) {
			if (drevo.get(p).contains(postavitev)) {
				p.stZmag += stZmag;
				p.stZmagNasprotnik += stZmagNasprotnik;
				p.odigraneIgre += 1;
				update(p, stZmag, stZmagNasprotnik);
			}
		}
	}
	
	// Za dolocene igre odigra vse mozne poteze
	public static Set<Igra> odigrajVsePoteze(Set<Igra> igre, int met) {
		Set<Igra> noveIgre = new HashSet<Igra>();
		for (Igra igra: igre) {
			List<Poteza> moznePoteze = igra.moznePoteze(new int[] {met});
			for (Poteza poteza: moznePoteze) {
				Igra tempIgra = new Igra(igra);
				odigrajMC(tempIgra, poteza);
				noveIgre.add(tempIgra);
			}
		}
		return noveIgre;
	}
	
	// Za dolocene igre odigramo vse mozne poteze in shranjujemo poteze, uporabimo v prvem koraku izbiranje poteze
	public static HashMap<Igra, List<Poteza>> odigrajVsePoteze2(HashMap<Igra, List<Poteza>> igre, int met) {
		HashMap<Igra, List<Poteza>> noveIgre = new HashMap<>();
		for (Igra igra: igre.keySet()) {

			List<Poteza> moznePoteze = igra.moznePoteze(new int[] {met});
			for (Poteza poteza: moznePoteze) {
				Igra tempIgra = new Igra(igra);
				List<Poteza> novePoteze = new ArrayList<>(igre.get(igra));
				novePoteze.add(poteza);
				odigrajMC(tempIgra, poteza);
				noveIgre.put(tempIgra, novePoteze);
			}
		}
		return noveIgre;
	}
		
	public static void odigrajMC(Igra igra, Poteza poteza) {
		// Izbrisemo zacetni zeton
		igra.izbrisiZeton(poteza.zacetnoPolje);
		// Dodamo zeton na koncnem polju
		igra.dodajZeton(poteza.koncnoPolje);
	}
	
	// Met kock, vrne seznam metov
	public static int[] vrziKockiMC() {
		int[] meti;
		int prviMet = Igra.metKocke();
		int drugiMet = Igra.metKocke();
		if (prviMet == drugiMet) {
			meti = new int[] {prviMet, prviMet, prviMet, prviMet};
		}
		else {meti = new int[] {prviMet, drugiMet};}
		return meti;
	}
	
}
