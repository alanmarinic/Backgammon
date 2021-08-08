package inteligenca;

import java.util.HashMap;
import java.util.List;

import splosno.Igra;
import splosno.PoljeInZetoni;
import splosno.Poteza;

//AI uporablja Monte Carlo Tree Search
public class MonteCarlo {
	
	//kljuci so postavitve, vrednosti pa trojice zmag, porazov in stevil odigranih iger
	private static HashMap<PoljeInZetoni[], Triple> drevo;
	
	public static Poteza izberiPotezo(Igra igra, int[] meti, int globina) {
		while (drevo.size() < globina) {
			List<Poteza> moznePoteze = Igra.moznePoteze(meti);
			
			
		}
		
	}

}
