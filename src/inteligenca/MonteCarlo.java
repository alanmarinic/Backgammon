package inteligenca;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
	
	//seznam = null
	@SuppressWarnings("null")
	public static Poteza izberiPotezo(Igra igra, int[] meti, int globina) {
		while (drevo.size() < globina) {
			Set<Igra> noveIgre;
			if (meti.length == 4) {
				noveIgre = odigrajVsePoteze(
					odigrajVsePoteze(
					odigrajVsePoteze(
					odigrajVsePoteze(new HashSet<Igra>(), meti[0]),
						meti[0]),
						meti[0]),
						meti[0]);
			}
			else {
				noveIgre = odigrajVsePoteze(
					odigrajVsePoteze(new HashSet<Igra>(), meti[0]),
						meti[1]);
				noveIgre.addAll(odigrajVsePoteze(
									odigrajVsePoteze(new HashSet<Igra>(), meti[1]),
										meti[0]));
			}
			List<Postavitev> seznam = null;
			for (Igra posameznaIgra : noveIgre) {
				switch (posameznaIgra.stanje()) {
				case ZMAGA_BELA:
					if (igra.naPotezi() == Igralec.Bela) {
						Postavitev postavitev = new Postavitev(posameznaIgra, 1, 0, 1);
						drevo.put(postavitev, null);
						seznam.add(postavitev);
					}
					else {
						Postavitev postavitev = new Postavitev(posameznaIgra, 0, 1, 1);
						drevo.put(postavitev, null);
						seznam.add(postavitev);
					}
					break;
				case ZMAGA_CRNA:
					if (igra.naPotezi() == Igralec.Crna) {
						Postavitev postavitev = new Postavitev(posameznaIgra, 1, 0, 1);
						drevo.put(postavitev, null);
						seznam.add(postavitev);					}
					else {
						Postavitev postavitev = new Postavitev(posameznaIgra, 0, 1, 1);
						drevo.put(postavitev, null);
						seznam.add(postavitev);					
						}
					break;
				case V_TEKU:
					Postavitev postavitev = new Postavitev(posameznaIgra, 0, 0, 0);
					drevo.put(postavitev, null);
					seznam.add(postavitev);				
					break;}
			//////////////////////popravi
			drevo.put(new Postavitev(igra, 0, 0, 0), seznam);
			}
		}
		
	}
	
	public static Set<Igra> odigrajVsePoteze(Set<Igra> igre, int met) {
		Set<Igra> noveIgre = new HashSet<>();
		for (Igra igra: igre) {
			List<Poteza> moznePoteze = igra.moznePoteze(new int[] {met});
			for (Poteza poteza: moznePoteze) {
				noveIgre.add(odigraj(igra, poteza));
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
