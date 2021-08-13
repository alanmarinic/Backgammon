package splosno;

public class PoljeInZetoni {
	
	public Polje polje;
	public int steviloZetonov;
	
	public PoljeInZetoni (Polje polje, int steviloZetonov) {
		this.polje = polje;
		this.steviloZetonov = steviloZetonov;
	}
	public static PoljeInZetoni kopija(PoljeInZetoni piz) {
		return new PoljeInZetoni(piz.polje, piz.steviloZetonov);
	}

}
