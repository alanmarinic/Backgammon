package splosno;


public class Igra {


	// Igralno polje
	public Polje[] plosca;
	
	// Igralec, ki je trenutno na potezi.
		// Vrednost je poljubna, če je igre konec (se pravi, lahko je napačna).
	public Igralec naPotezi;
	
	public Igra() {
		plosca = new Polje[24];
		for (int i = 0; i < 24; i++) {
				plosca[i] = Polje.PRAZNO;
		}
		naPotezi = kdoZacne();
		
		//funkcija kdo zacne
	}
	
	public Igra(Igra igra) {
		this.plosca = new Polje[24];
		for (int i = 0; i < 24; i++) {
				this.plosca[i] = igra.plosca[i];
		}
		this.naPotezi = igra.naPotezi;
	}

	
	public Polje[] getPlosca () {
		return plosca;
	}
	
	public Igralec naPotezi() {
		return naPotezi;
	}
	
	public int[] moznePoteze() {//manjka
		
	}
	
	public Stanje stanje() {
		// Ali imamo zmagovalca?
		
		
		//manjka. ce na polju ni vec zetonov je zmaga
		
		
		// Če imamo zetone obeh barv , igre ni konec in je nekdo na potezi
		
		//manjka
		
		return Stanje.V_TEKU;
			}
		}
		// je mozno neodloceno??
		return Stanje.NEODLOCENO;
	}
}
