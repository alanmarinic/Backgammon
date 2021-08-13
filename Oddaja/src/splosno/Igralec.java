package splosno;

public enum Igralec {
	
	Bela, Crna;

	 public Igralec nasprotnik() {
         return (this == Bela ? Crna : Bela);
	 }

	 public Polje getPolje() {
		 return (this == Bela ? Polje.Bela : Polje.Crna);
	 }

}
