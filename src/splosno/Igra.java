package splosno;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class Igra {


	// Igralno polje
	public static PoljeInZetoni[] plosca;
	
	// Igralec, ki je trenutno na potezi.
		// Vrednost je poljubna, če je igre konec (se pravi, lahko je napačna).
	public static Igralec naPotezi;
	
	private static final Random RANDOM = new Random();

	
	public Igralec kdoZacne() {
		int bela = metKocke();
		int crna = metKocke();
		Vodja.prviMet = bela;
		Vodja.drugiMet = crna;
		if (bela > crna) {
			System.out.println("Zacne beli, ki je vrgel " + bela);
			return Igralec.Bela;
		}
		else if (bela < crna) {
			System.out.println("Zacne crni, ki je vrgel " + crna);
			return Igralec.Crna;
		}
		else {return kdoZacne();}	
	}
	
	public static int metKocke() {
		return RANDOM.nextInt(6) + 1;
	}
	
	
	public Igra() {
		plosca = new PoljeInZetoni[28];
		//polji 0 in 25 predstavljata skatlico. 0 od crnih in 25 od belih
		//polji 26 in 27 predstavljata sredinsko polje z zbitimi zetoni. 26 od belih in 27 od crnih 
		List<Integer> bele = Arrays.asList(1, 12, 17, 19, 25, 26);
		List<Integer> crne = Arrays.asList(0, 6, 8, 13, 24, 27);
		for (int i = 0; i < 28; i++) {
			plosca[i] = new PoljeInZetoni(Polje.PRAZNO, 0);
			if (bele.contains(i)) {plosca[i].polje = Polje.Bela;}
			else if (crne.contains(i)) {plosca[i].polje = Polje.Crna;}
		}
		plosca[0].steviloZetonov = 0;
		plosca[1].steviloZetonov = 2;
		plosca[6].steviloZetonov = 5;
		plosca[8].steviloZetonov = 3;
		plosca[12].steviloZetonov = 5;
		plosca[13].steviloZetonov = 5;
		plosca[17].steviloZetonov = 3;
		plosca[19].steviloZetonov = 5;
		plosca[24].steviloZetonov = 2;
		plosca[25].steviloZetonov = 0;
		plosca[26].steviloZetonov = 0;
		plosca[27].steviloZetonov = 0;

		naPotezi = kdoZacne();
	}
	
	public Igra(Igra igra) {
		this.plosca = new PoljeInZetoni[28];
		for (int i = 0; i < 28; i++) {
				this.plosca[i] = igra.plosca[i];
		}
		this.naPotezi = igra.naPotezi;
	}

	
	public PoljeInZetoni[] getPlosca () {
		return plosca;
	}
	
	public Igralec naPotezi() {
		return naPotezi;
	}
	
	public static List<Poteza> moznePoteze(int[] meti) {
		List<Poteza> moznePoteze = new ArrayList<Poteza>();
		//Najprej preverimo, ce je kaksen zeton na sredini
		if (naPotezi == Igralec.Bela) {
			if (plosca[26].steviloZetonov > 0) {
				for (int i: meti) {
					if (plosca[i].polje == Polje.PRAZNO || plosca[i].polje == Polje.Bela || (plosca[i].polje == Polje.Crna && plosca[i].steviloZetonov == 1)) 
						{moznePoteze.add(new Poteza(26, i));}
				}
			}
			else {
				boolean cetrtiKvadrant = true;
				for (int polje = 1; polje < 25; polje++) {
					if (plosca[polje].polje == Polje.Bela) {
						if (polje < 19) {cetrtiKvadrant = false;}
						for (int met : meti) {
							if (polje + met < 25) {
								if (plosca[polje + met].polje == Polje.Crna && plosca[polje + met].steviloZetonov > 1) {
									continue;
								}
								else moznePoteze.add(new Poteza(polje, polje + met));
							}
							else if (cetrtiKvadrant) {moznePoteze.add(new Poteza(polje, 25));}
							
						}
					}
				}
			}
		}
		else if (naPotezi == Igralec.Crna) {
			if (plosca[27].steviloZetonov > 0) {
				for (int i: meti) {
					if (plosca[25 - i].polje == Polje.PRAZNO || plosca[25 - i].polje == Polje.Crna || (plosca[25 - i].polje == Polje.Bela && plosca[25 - i].steviloZetonov == 1)) 
						{moznePoteze.add(new Poteza(27, 25 - i));}
				}
			}
			else {
				boolean prviKvadrant = true;
				for (int polje = 24; polje > 0; polje--) {
					if (plosca[polje].polje == Polje.Crna) {
						if (polje > 6) {prviKvadrant = false;}
						for (int met : meti) {
							if (polje - met > 0) {
								if (plosca[polje - met].polje == Polje.Bela && plosca[polje - met].steviloZetonov > 1) {
									continue;
								}
								else moznePoteze.add(new Poteza(polje, polje - met));
							}
							else if (prviKvadrant) {moznePoteze.add(new Poteza(polje, 0));}
						}
					}
				}
			}
		}
		return moznePoteze;
	}
	
	
	public Stanje stanje() {
		// Ali imamo zmagovalca?
		if (plosca[0].steviloZetonov == 15) {return Stanje.ZMAGA_CRNA;}
		else if (plosca[25].steviloZetonov == 15) {return Stanje.ZMAGA_BELA;}
		else {return Stanje.V_TEKU;}		
	}
	
	public void izbrisiZeton(int polje) {
		if (plosca[polje].steviloZetonov > 1) {
			plosca[polje].steviloZetonov -= 1;
		}
		else {
			plosca[polje].steviloZetonov = 0;
			if (polje < 25) {
				plosca[polje].polje = Polje.PRAZNO;
			}
		}
	}
	
	public void dodajZeton(int polje) {
		if (plosca[polje].polje == Polje.PRAZNO) {
			plosca[polje].polje = naPotezi.getPolje();
			plosca[polje].steviloZetonov = 1;
		}
		else if (plosca[polje].polje == naPotezi.getPolje()) {
			plosca[polje].steviloZetonov += 1;
		}
		else {
			plosca[polje].polje = naPotezi.getPolje();
			plosca[polje].steviloZetonov = 1;
			if (naPotezi == Igralec.Crna) {
				plosca[26].steviloZetonov += 1;
			}
			else {
				plosca[27].steviloZetonov += 1;
			}
		}
	}
	
	public boolean odigraj(Poteza poteza) {
		//pogledamo, ce je poteza veljavna
		//ce je pocasen, odstrani primer za racunalnik
		List<Poteza> moznePoteze = moznePoteze(Vodja.meti);
		System.out.println("mozne poteze: ");
		Vodja.izpisiSeznamPotez(moznePoteze);
		System.out.println("nasa poteza: " + poteza.zacetnoPolje + "-" + poteza.koncnoPolje);
		System.out.println(vsebuje(moznePoteze, poteza));
		if (vsebuje(moznePoteze, poteza)) {
			System.out.println("poteza veljavna");

			//izbrisemo zacetni zeton
			izbrisiZeton(poteza.zacetnoPolje);
			//dodamo zeton na koncnem polju
			dodajZeton(poteza.koncnoPolje);
			//Vodja.izpisiSeznam(Vodja.meti);
			//izbrisemo met iz seznam metov
			if (poteza.zacetnoPolje == 26) Vodja.meti = odstraniMet(Vodja.meti, poteza.koncnoPolje);
			else if (poteza.zacetnoPolje == 27) Vodja.meti = odstraniMet(Vodja.meti, 25 - poteza.koncnoPolje);
			else Vodja.meti = odstraniMet(Vodja.meti, Math.abs(poteza.zacetnoPolje - poteza.koncnoPolje));
			//Vodja.izpisiSeznam(Vodja.meti);
			return true;
		}
		else {
			return false;
		}
	}
	
	
	
	public boolean vsebuje(List<Poteza> list, Poteza poteza) {
		for (Poteza vrednost: list) {
			if (vrednost.zacetnoPolje == poteza.zacetnoPolje && vrednost.koncnoPolje == poteza.koncnoPolje) {
				return true;
			}
		}
		return false;
	}
	
	public int[] odstraniMet(int[] meti, int met) {
		Vodja.izpisiSeznam(meti);
		List<Integer> novi = new ArrayList<>();
		boolean nadaljuj = true;
		for (int i : meti) {
			if (i == met && nadaljuj) {
				nadaljuj = false;
			}
			else novi.add(i);
		}
		int[] array = new int[novi.size()];
		for(int i = 0; i < novi.size(); i++) array[i] = novi.get(i);
		Vodja.izpisiSeznam(array);
		return array;

	}
		
		
	
	
}