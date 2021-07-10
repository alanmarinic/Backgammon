package splosno;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Igra {


	// Igralno polje
	public Polje[] plosca;
	
	// Igralec, ki je trenutno na potezi.
		// Vrednost je poljubna, če je igre konec (se pravi, lahko je napačna).
	public Igralec naPotezi;
	
	private static final Random RANDOM = new Random();

	
	public Igralec kdoZacne() {
		int bela = metKocke();
		int crna = metKocke();
		if (bela > crna) {
			System.out.println("Zacne beli, ki je vrgel" + bela);
			return Igralec.Bela;
		}
		else if (bela < crna) {
			System.out.println("Zacne crni, ki je vrgel" + crna);
			return Igralec.Crna;
		}
		else {kdoZacne();}	
	}
	
	public static int metKocke() {
		return RANDOM.nextInt(6) + 1;
	}
	
	
	public Igra() {
		plosca = new Polje[28];
		//polji 0 in 25 predstavljata skatlico. 0 od crnih in 25 od belih
		//polji 26 in 27 predstavljata sredinsko polje z zbitimi zetoni. 26 od belih in 27 od crnih 
		List<Integer> bele = Arrays.asList(1, 12, 17, 19, 25, 26);
		List<Integer> crne = Arrays.asList(0, 6, 8, 13, 24, 27);
		for (int i = 0; i < 26; i++) {
			if (bele.contains(i)) {plosca[i] = Polje.Bela;}
			else if (crne.contains(i)) {plosca[i] = Polje.Crna;}
			else {plosca[i] = Polje.PRAZNO;}
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
		this.plosca = new Polje[26];
		for (int i = 0; i < 26; i++) {
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
	
	public List<Poteza> moznePoteze(int[] meti) {
		List<Poteza> moznePoteze;
		//Najprej preverimo, ce je kaksen zeton na sredini
		if (naPotezi == Igralec.Bela) {
			if (plosca[26].steviloZetonov > 0) {
				for (int i: meti) {
					if (plosca[i] == Polje.PRAZNO || plosca[i] == Polje.Bela || (plosca[i] == Polje.Crna && plosca[i].steviloZetonov == 1)) 
						{moznePoteze.add(new Poteza(26, i));}
				}
			return moznePoteze;
			}
			else {
				for (int polje = 1; polje < 25; polje++) {
					if (plosca[polje] == Polje.Bela) {
						for (int met: meti) {
							
							////////////////
							if (plosca[polje + met] == Polje.Crna && plosca[polje + met].steviloZetonov > 1) {
								continue;
							}
							else {
								
							}
						}
					}
				}
				
			}
		}
		///////////////
		else if (naPotezi == Igralec.Crna && plosca[27].steviloZetonov > 0) {
			for (int i: meti) {
				if (plosca[25 - i] == Polje.PRAZNO || plosca[25 - i] == Polje.Crna || (plosca[25 - i] == Polje.Bela && plosca[25 - i].steviloZetonov == 1)) 
					{moznePoteze.add(new Poteza(27, 25 - i));}
			}
			return moznePoteze;
		}

		return moznePoteze;
		
	}
	
	public boolean neodlocenoPogoj(int zacetek, int konec) {
		Polje barva;
		if (zacetek < 10) {barva = Polje.Crna;}
		else {barva = Polje.Bela;}
		for (int i = zacetek; i < konec + 1; i++) {
			if (plosca[i] == barva && plosca[i].steviloZetonov > 1) {continue;}
			else {return false;}
		}
		return true;
	}
	
	public Stanje stanje() {
		// Ali imamo zmagovalca?
		if (plosca[0].steviloZetonov == 15) {return Stanje.ZMAGA_CRNA;}
		else if (plosca[25].steviloZetonov == 15) {return Stanje.ZMAGA_BELA;}
		//Ce oba igralca ne moreta vrniti zbitih zetonov na plosco, je stanje neodloceno.
		else if (plosca[26].steviloZetonov > 0 && plosca[27].steviloZetonov > 0 && neodlocenoPogoj(1, 6) && neodlocenoPogoj(19, 24))
			{return Stanje.NEODLOCENO;}
		else {return Stanje.V_TEKU;}		
	}
}
