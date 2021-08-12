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
import splosno.Vodja;

//AI uporablja Monte Carlo Tree Search
public class MonteCarlo {
	
	//kljuci so postavitve, vrednosti pa trojice zmag, porazov in stevil odigranih iger
	private static HashMap<Postavitev, List<Postavitev>> drevo = new HashMap<>();
	
	//igralec, ki izbira potezo
	private static Igralec Carlo;
	
	//seznam = null
	//@SuppressWarnings("null")
	public static List<Poteza> izberiPotezo(Igra igra, int[] meti, int globina) {
		System.out.println("AI izbira potezo");
		Postavitev glavnaPostavitev = new Postavitev(igra, 0, 0, 0);
		Carlo = igra.naPotezi();
		HashMap<Igra, List<Poteza>> sPotezami = razvojSPotezami(glavnaPostavitev, meti);
		System.out.println("prazvili s potezami");
		System.out.println("pred while" + sPotezami);
		while (drevo.size() < globina) {
			System.out.println("drveo size:" + drevo.size());
			monte(glavnaPostavitev);
		}
		double max = Double.MIN_VALUE;
		Postavitev najboljsa = null;
		for (Postavitev postavitev: drevo.get(glavnaPostavitev)) {
			double vrednost = postavitev.stZmag / postavitev.odigraneIgre;
			if (vrednost > max) {
				max = vrednost;
				najboljsa = postavitev;
			}
		}
		List<Poteza> poteze = sPotezami.get(najboljsa.igra);
		//Vodja.izpisiSeznamPotez(poteze);
		return poteze;
	}
	
	
	public static void monte(Postavitev postavitev) {
		//;
		System.out.println("pmonte");
		
		//ce postavitev ni list
		
		////////contains
		if (drevo.keySet().contains(postavitev)) {
			System.out.println("contains");
			Postavitev izbranaPostavitev = izbor(postavitev);
			monte(izbranaPostavitev);
		}
		//postavitev je list
		else {
			System.out.println("else v monte");
			//nismo se odigrali vsaj nobene igro do konca
			if (postavitev.odigraneIgre == 0) {
				rollout(postavitev.igra);
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
			else {
				//z naklucnima metoma
				int prviMet = Igra.metKocke();
				int drugiMet = Igra.metKocke();
				int[] meti;
				if (prviMet == drugiMet) {
					meti = new int[] {prviMet, prviMet, prviMet, prviMet};
				}
				else {meti = new int[] {prviMet, drugiMet};}
				razvoj(postavitev, meti);
				rollout(drevo.get(postavitev).get(0).igra);
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
		}
		System.out.println("konec monte");
	}
	
	
	
	@SuppressWarnings("null")
	public static void razvoj(Postavitev postavitev, int[] meti) {
		System.out.println("razvoj zacetetk");
		Set<Igra> zacetnaIgra = new HashSet<Igra>();
		zacetnaIgra.add(postavitev.igra);
		Set<Igra> noveIgre;
		if (meti.length == 4) {
			noveIgre = odigrajVsePoteze(
				odigrajVsePoteze(
				odigrajVsePoteze(
				odigrajVsePoteze(zacetnaIgra, meti[0]),
					meti[0]),
					meti[0]),
					meti[0]);
		}
		else {
			noveIgre = odigrajVsePoteze(
				odigrajVsePoteze(zacetnaIgra, meti[0]),
					meti[1]);
			noveIgre.addAll(odigrajVsePoteze(
								odigrajVsePoteze(zacetnaIgra, meti[1]),
									meti[0]));
		}
		List<Postavitev> seznam = null;
				
		//iz mnozice iger v seznam postavitev
		for (Igra posameznaIgra : noveIgre) {
			posameznaIgra.naPotezi = posameznaIgra.naPotezi.nasprotnik();
			seznam.add(new Postavitev(posameznaIgra, 0, 0, 0));
		}
		drevo.put(postavitev, seznam);
		System.out.println("razvoj konec");
		
	}
	
	public static HashMap<Igra, List<Poteza>> razvojSPotezami(Postavitev postavitev, int[] meti) {
		HashMap<Igra, List<Poteza>> zacetnaIgra = new HashMap<>();
		zacetnaIgra.put(postavitev.igra, new ArrayList<Poteza>());
		HashMap<Igra, List<Poteza>> noveIgre;
		Vodja.izpisiSeznam(meti);
		if (meti.length == 4) {
			System.out.println("if");

			noveIgre = odigrajVsePoteze2(
				odigrajVsePoteze2(
				odigrajVsePoteze2(
				odigrajVsePoteze2(zacetnaIgra, meti[0]),
					meti[0]),
					meti[0]),
					meti[0]);
			System.out.println("po if");

		}
		else {
			System.out.println("else");
			noveIgre = odigrajVsePoteze2(zacetnaIgra, meti[0]);
			System.out.println("ovp2 je delu enkrat");
			noveIgre = odigrajVsePoteze2(noveIgre, meti[1]);
			
			noveIgre.putAll(odigrajVsePoteze2(
								odigrajVsePoteze2(zacetnaIgra, meti[1]),
									meti[0]));
			System.out.println("po else");

		}
		List<Postavitev> seznam = new ArrayList<>();
		//iz mnozice iger v seznam postavitev
		for (Igra posameznaIgra : noveIgre.keySet()) {
			posameznaIgra.naPotezi = posameznaIgra.naPotezi.nasprotnik();
			seznam.add(new Postavitev(posameznaIgra, 0, 0, 0));
		}
		System.out.println("pred drevo");
		
		drevo.put(postavitev, seznam);
		System.out.println("pred return");
		return noveIgre;
	}
		
	
	public static Postavitev izbor(Postavitev postavitev) {
		System.out.println("pizbor zacetek");
		List<Postavitev> otroci = drevo.get(postavitev);
		double max = 0;
		Postavitev najboljsa = null;
		for (Postavitev igra: otroci) {
			double vrednost = UCB(igra, postavitev.odigraneIgre);
			if (vrednost == Double.MAX_VALUE) return igra;
			if (vrednost > max) {
				max = vrednost;
				najboljsa = igra;
			}
		}
		System.out.println("izbor konecn");
		return najboljsa;
	}
	
	public static double UCB(Postavitev postavitev, double odigraneIgre) {
		int c = 2;
		if (postavitev.odigraneIgre == 0) {
			return Double.MAX_VALUE;
		}
		return postavitev.stZmag/postavitev.odigraneIgre + c * Math.sqrt(Math.log(odigraneIgre)/postavitev.odigraneIgre);
	}
	
	private static final Random RANDOM = new Random();

	
	//rollout sprejme igro, na koncu potebno posodobiti postavitev glede na zmagovalca
	private static Igralec zmagovalec;
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
	
	
	public static void poteza(Igra igra) {
		int[] meti;
		int prviMet = Igra.metKocke();
		int drugiMet = Igra.metKocke();
		if (prviMet == drugiMet) {
			meti = new int[] {prviMet, prviMet, prviMet, prviMet};
		}
		else {meti = new int[] {prviMet, drugiMet};}
		int dolzina = meti.length;
		for (int j = 0; j < dolzina; j ++) {
			List<Poteza> moznePoteze = igra.moznePoteze(meti);
			if (moznePoteze.size() == 0) {
				break;
			}
			int i = RANDOM.nextInt(moznePoteze.size());	
			Poteza poteza = moznePoteze.get(i);
			odigrajMC(igra, poteza);
		}
		igra.naPotezi = igra.naPotezi.nasprotnik();
		rollout(igra);
	}
	
	public static void update(Postavitev postavitev, int stZmag, int stZmagNasprotnik) {
		System.out.println("update zacetek");
		for (Postavitev p : drevo.keySet()) {
			//sumljiv contains!!
			if (drevo.get(p).contains(postavitev)) {
				p.stZmag += stZmag;
				p.stZmagNasprotnik += stZmagNasprotnik;
				p.odigraneIgre += 1;
				update(p, stZmag, stZmagNasprotnik);
			}
		}
		System.out.println("update konec");
	}
	
	
	
	
		
	
	
	public static Set<Igra> odigrajVsePoteze(Set<Igra> igre, int met) {
		System.out.println("ovp");

		Set<Igra> noveIgre = new HashSet<Igra>();
		for (Igra igra: igre) {
			

			List<Poteza> moznePoteze = igra.moznePoteze(new int[] {met});
			for (Poteza poteza: moznePoteze) {
				Igra tempIgra = new Igra(igra);
				noveIgre.add(odigrajMC(tempIgra, poteza));
			}
		}
		return noveIgre;
	}
	
	public static HashMap<Igra, List<Poteza>> odigrajVsePoteze2(HashMap<Igra, List<Poteza>> igre, int met) {
		System.out.println("ovp2");

		HashMap<Igra, List<Poteza>> noveIgre = new HashMap<>();
		for (Igra igra: igre.keySet()) {
			
			List<Poteza> moznePoteze = igra.moznePoteze(new int[] {met});
			for (Poteza poteza: moznePoteze) {
				Igra tempIgra = new Igra(igra);
				List<Poteza> novePoteze = igre.get(igra);
				novePoteze.add(poteza);
				noveIgre.put(odigrajMC(tempIgra, poteza), novePoteze);
			}
		}
		System.out.println("konec ovp2");

		return noveIgre;
	}
	
	
		
	public static Igra odigrajMC(Igra igra, Poteza poteza) {
		Igra tempIgra = new Igra(igra);
		//izbrisemo zacetni zeton
		tempIgra.izbrisiZeton(poteza.zacetnoPolje);
		//dodamo zeton na koncnem polju
		tempIgra.dodajZeton(poteza.koncnoPolje);
		return tempIgra;
	}

}
