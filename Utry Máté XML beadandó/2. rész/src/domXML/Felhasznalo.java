//Eloado -> Felhasznalo

package domXML;

public class Felhasznalo {
	private String nev; //ez marad
	private String felhasznalonev; //orszag
	
	public Felhasznalo(String nev, String felhasznalonev) {
		this.nev = nev;
		this.felhasznalonev = felhasznalonev;
	}
	
	public Felhasznalo() {
		this("", "");
	}
	
	public String getNev() {
		return nev;
	}
	
	public void setNev(String nev) {
		this.nev = nev;
	}
	
	public String getFelhasznalonev() {
		return felhasznalonev;
	}
	
	public void setFelhasznalonev(String felhasznalonev) {
		this.felhasznalonev = felhasznalonev;
	}
	
	@Override
	public String toString() {
		return "Felhasznalo [nev=" + nev + ", felhasznalonev=" + felhasznalonev + "]";
	}
}