package inteligenca;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import splosno.Igra;
import splosno.Igralec;
import splosno.PoljeInZetoni;
import splosno.Poteza;
import splosno.Vodja;

//AI uporablja Monte Carlo Tree Search
public class MonteCarlo {
	
	//kljuci so postavitve, vrednosti pa trojice zmag, porazov in stevil odigranih iger
	private static HashMap<Postavitev, List<Postavitev>> drevo;
	
	//igralec, ki izbira potezo
	private static Igralec Carlo;
	
	//seznam = null
	@SuppressWarnings("null")
	public static List<Poteza> izberiPotezo(Igra igra, int[] meti, int globina) {
		Postavitev glavnaPostavitev = new Postavitev(igra, 0, 0, 0);
		Carlo = igra.naPotezi();
		HashMap<Igra, List<Poteza>> sPotezami = razvojSPotezami(glavnaPostavitev, meti);
		while (drevo.size() < globina) {
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
		return poteze;
	}
	
	
	public static void monte(Postavitev postavitev) {
		//;
		
		//ce postavitev ni list
		if (drevo.keySet().contains(postavitev)) {
			Postavitev izbranaPostavitev = izbor(postavitev);
			monte(izbranaPostavitev);
		}
		//postavitev je list
		else {
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
	}
	
	
	
	@SuppressWarnings("null")
	public static void razvoj(Postavitev postavitev, int[] meti) {
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
		/*int zmage = 0;
		int zmageNasprotnik = 0;
		int odigrane = 0;*/
		
		//iz mnozice iger v seznam postavitev
		for (Igra posameznaIgra : noveIgre) {
			seznam.add(new Postavitev(posameznaIgra, 0, 0, 0));
			/*switch (posameznaIgra.stanje()) {
			case ZMAGA_BELA:
				if (igra.naPotezi() == Igralec.Bela) {
					Postavitev postavitev = new Postavitev(posameznaIgra, 1, 0, 1);
					//drevo.put(postavitev, null);
					seznam.add(postavitev);
					zmage += 1;
					odigrane += 1;
				}
				else {
					Postavitev postavitev = new Postavitev(posameznaIgra, 0, 1, 1);
					//drevo.put(postavitev, null);
					seznam.add(postavitev);
					zmageNasprotnik += 1;
					odigrane += 1;
				}
				break;
			case ZMAGA_CRNA:
				if (igra.naPotezi() == Igralec.Crna) {
					Postavitev postavitev = new Postavitev(posameznaIgra, 1, 0, 1);
					//drevo.put(postavitev, null);
					seznam.add(postavitev);
					zmage += 1;
					odigrane += 1;
				}
				else {
					Postavitev postavitev = new Postavitev(posameznaIgra, 0, 1, 1);
					//drevo.put(postavitev, null);
					seznam.add(postavitev);		
					zmageNasprotnik += 1;
					odigrane += 1;
				}
				break;
			case V_TEKU:
				Postavitev postavitev = new Postavitev(posameznaIgra, 0, 0, 0);
				//drevo.put(postavitev, null);
				seznam.add(postavitev);				
				break;}*/
		drevo.put(postavitev, seznam);
		}
	}
	
	public static HashMap<Igra, List<Poteza>> razvojSPotezami(Postavitev postavitev, int[] meti) {
		HashMap<Igra, List<Poteza>> zacetnaIgra = new HashMap<>();
		zacetnaIgra.put(postavitev.igra, new ArrayList<Poteza>());
		HashMap<Igra, List<Poteza>> noveIgre = new HashMap<>();
		//HashMap<Igra, List<Poteza>> igreSPotezami ;
		if (meti.length == 4) {
			noveIgre = odigrajVsePoteze2(
				odigrajVsePoteze2(
				odigrajVsePoteze2(
				odigrajVsePoteze2(zacetnaIgra, meti[0]),
					meti[0]),
					meti[0]),
					meti[0]);
		}
		else {
			noveIgre = odigrajVsePoteze2(
				odigrajVsePoteze2(zacetnaIgra, meti[0]),
					meti[1]);
			noveIgre.putAll(odigrajVsePoteze2(
								odigrajVsePoteze2(zacetnaIgra, meti[1]),
									meti[0]));
		}
		List<Postavitev> seznam = null;
		
		//iz mnozice iger v seznam postavitev
		for (Igra posameznaIgra : noveIgre.keySet()) {
			seznam.add(new Postavitev(posameznaIgra, 0, 0, 0));
		}
		drevo.put(postavitev, seznam);
		return noveIgre;
	}
		
	
	public static Postavitev izbor(Postavitev postavitev) {
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
		switch (igra.stanje()) {
		case ZMAGA_BELA: 
			/*if (Carlo == Igralec.Bela) {
				postavitev.stZmag += 1;
				postavitev.odigraneIgre += 1;
			}
			else {
				postavitev.stZmagNasprotnik += 1;
				postavitev.odigraneIgre += 1;
			}*/
			zmagovalec = Igralec.Bela;
			break;
		case ZMAGA_CRNA:
			/*if (Carlo == Igralec.Crna) {
				postavitev.stZmag += 1;
				postavitev.odigraneIgre += 1;
			}
			else {
				postavitev.stZmagNasprotnik += 1;
				postavitev.odigraneIgre += 1;
			}*/
			zmagovalec = Igralec.Crna;
			break;
		case V_TEKU: 
			Igra tempIgra = new Igra(igra);
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
			List<Poteza> moznePoteze = Igra.moznePoteze(meti);
			if (moznePoteze.size() == 0) {
				break;
			}
			int i = RANDOM.nextInt(moznePoteze.size());	
			Poteza poteza = moznePoteze.get(i);
			igra.odigraj(poteza);
		}
		Igra.naPotezi = Igra.naPotezi.nasprotnik();
		rollout(igra);
	}
	
	public static void update(Postavitev postavitev, int stZmag, int stZmagNasprotnik) {
		for (Postavitev p : drevo.keySet()) {
			//sumljiv contains!!
			if (drevo.get(p).contains(postavitev)) {
				p.stZmag += stZmag;
				p.stZmagNasprotnik += stZmagNasprotnik;
				p.odigraneIgre += 1;
				update(p, stZmag, stZmagNasprotnik);
			}
		}
	}
	
	
	
	
		
	
	
	public static Set<Igra> odigrajVsePoteze(Set<Igra> igre, int met) {
		Set<Igra> noveIgre = new HashSet<Igra>();
		for (Igra igra: igre) {
			List<Poteza> moznePoteze = Igra.moznePoteze(new int[] {met});
			for (Poteza poteza: moznePoteze) {
				noveIgre.add(odigraj(igra, poteza));
			}
		}
		return noveIgre;
	}
	
	public static HashMap<Igra, List<Poteza>> odigrajVsePoteze2(HashMap<Igra, List<Poteza>> igre, int met) {
		HashMap<Igra, List<Poteza>> noveIgre = new HashMap<>();
		for (Igra igra: igre.keySet()) {
			List<Poteza> moznePoteze = Igra.moznePoteze(new int[] {met});
			for (Poteza poteza: moznePoteze) {
				List<Poteza> novePoteze = igre.get(igra);
				novePoteze.add(poteza);
				noveIgre.put(odigraj(igra, poteza), novePoteze);
			}
		}
		return noveIgre;
	}
	
	
		
	public static Igra odigraj(Igra igra, Poteza poteza) {
		Igra tempIgra = new Igra(igra);
		//izbrisemo zacetni zeton
		tempIgra.izbrisiZeton(poteza.zacetnoPolje);
		//dodamo zeton na koncnem polju
		tempIgra.dodajZeton(poteza.koncnoPolje);
		return tempIgra;
	}

}
