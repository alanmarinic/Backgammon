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

//AI uporablja Monte Carlo Tree Search
public class MonteCarlo {
	
	//kljuci so postavitve, vrednosti pa trojice zmag, porazov in stevil odigranih iger
	private static HashMap<Postavitev, List<Postavitev>> drevo = new HashMap<>();
	
	//igralec, ki izbira potezo
	private static Igralec Carlo;
	
	//seznam = null
	public static List<Poteza> izberiPotezo(Igra igra, int[] meti, int globina) {
		System.out.println("AI izbira potezo");
		Igra tempIgra = new Igra(igra);
		Postavitev glavnaPostavitev = new Postavitev(tempIgra, 0, 0, 0);
		Carlo = tempIgra.naPotezi();
		HashMap<Igra, List<Poteza>> sPotezami = razvojSPotezami(glavnaPostavitev, meti);
		while (drevo.size() < globina) {
			System.out.println("drveo size:" + drevo.size());
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
		if (najboljsa == null) 		System.out.println("najboljsa null");

		List<Poteza> poteze = sPotezami.get(najboljsa.igra);
		System.out.println("izberi potezo poteze:");

		return poteze;
	}
	
	
	public static void monte(Postavitev postavitev) {
		System.out.println("monte");
		//ce postavitev ni list
		if (drevo.keySet().contains(postavitev)) {
			System.out.println("contains");
			Postavitev izbranaPostavitev = izbor(postavitev);
			if (izbranaPostavitev == null) 		System.out.println("null postavitev");
			monte(izbranaPostavitev);
		}
		//postavitev je list
		else {
			System.out.println("else v monte");
			//nismo se odigrali vsaj nobene igro do konca
			if (postavitev.odigraneIgre == 0) {
				System.out.println("monte else if");
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
			//je ze odigrana vsaj ena igra
			else {
				System.out.println("monte else else");
				//z naklucnima metoma
				razvoj(postavitev, vrziKockiMC());
				
				/*if (drevo.get(postavitev).size() != 0) {
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
				}*/
			}
		}
		System.out.println("konec monte");
	}
	
	//@SuppressWarnings("null")
	public static void razvoj(Postavitev postavitev, int[] meti) {
		System.out.println("razvoj zacetetk");
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

		//iz mnozice iger v seznam postavitev
		for (Igra posameznaIgra : noveIgre) {
			posameznaIgra.naPotezi = posameznaIgra.naPotezi().nasprotnik();
			seznam.add(new Postavitev(posameznaIgra, 0, 0, 0));
		}
		//ce je seznam prazen, te postavitve ne dodamo kot kljuc
		if (seznam.isEmpty()) {
			System.out.println("prazen");
			Igra tempIgra2 = new Igra(postavitev.igra); 
			tempIgra.naPotezi = tempIgra2.naPotezi().nasprotnik();
			seznam.add(new Postavitev(tempIgra2, postavitev.stZmag, postavitev.stZmagNasprotnik, postavitev.odigraneIgre));
			drevo.put(postavitev, seznam);
		}
		else {
			drevo.put(postavitev, seznam);
			System.out.println("razvoj konec");
		}
		
	}
	
	public static HashMap<Igra, List<Poteza>> razvojSPotezami(Postavitev postavitev, int[] meti) {
		System.out.println("razvoj s potezami");

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
		//iz mnozice iger v seznam postavitev
		for (Igra posameznaIgra : noveIgre.keySet()) {
			posameznaIgra.naPotezi = posameznaIgra.naPotezi().nasprotnik();
			seznam.add(new Postavitev(posameznaIgra, 0, 0, 0));
		}
		if (seznam.isEmpty()) {
			System.out.println("prazen rsp");
			Igra tempIgra2 = new Igra(postavitev.igra); 
			tempIgra.naPotezi = tempIgra2.naPotezi().nasprotnik();
			seznam.add(new Postavitev(tempIgra2, postavitev.stZmag, postavitev.stZmagNasprotnik, postavitev.odigraneIgre));
			drevo.put(postavitev, seznam);
		}
		else {
			drevo.put(postavitev, seznam);
			System.out.println("rsp konec");
		}
		
		return noveIgre;
	}
		
	
	public static Postavitev izbor(Postavitev postavitev) {
		System.out.println("izbor zacetek");
		List<Postavitev> otroci = drevo.get(postavitev);
		System.out.println(otroci.size());

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
	
	public static void update(Postavitev postavitev, int stZmag, int stZmagNasprotnik) {
		System.out.println("update zacetek");
		for (Postavitev p : drevo.keySet()) {
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
				odigrajMC(tempIgra, poteza);
				noveIgre.add(tempIgra);
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
				List<Poteza> novePoteze = new ArrayList<>(igre.get(igra));
				novePoteze.add(poteza);
				////////////spremenila
				odigrajMC(tempIgra, poteza);
				noveIgre.put(tempIgra, novePoteze);
			}
		}
		System.out.println("konec ovp2");

		return noveIgre;
	}
	
	
		
	public static void odigrajMC(Igra igra, Poteza poteza) {
		//Igra tempIgra = new Igra(igra);
		//izbrisemo zacetni zeton
		igra.izbrisiZeton(poteza.zacetnoPolje);
		//dodamo zeton na koncnem polju
		igra.dodajZeton(poteza.koncnoPolje);
		//return tempIgra;
	}
	
	
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
