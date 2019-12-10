//Album.java -> Megrendelo.java
package domXML;

public class Megrendelo {
	private String nev; //cim
	private Integer telefon; //megjeleve
	private String felhasznalo; //eloado
	private String mkod; //katalogusszam
	
	
	public Megrendelo(String nev, Integer telefon, String felhasznalo, String mkod) {
		this.nev = nev;
		this.telefon = telefon;
		this.felhasznalo = felhasznalo;
		this.mkod = mkod;
	}
	
	public Megrendelo() {
		this("", 0, "", "");
	}
	
	public String getNev() {
		return nev;
	}
	
	public void setNev(String nev) {
		this.nev = nev;
	}
	
	public Integer getTelefon() {
		return telefon;
	}
	
	public void setTelefon(Integer telefon) {
		this.telefon = telefon;
	}
	
	public String getFelhasznalo() {
		return felhasznalo;
	}
	
	public void setFelhasznalo(String felhasznalo) {
		this.felhasznalo = felhasznalo;
	}
	
	public String getMkod() {
		return mkod;
	}
	
	public void setMkod(String mkod) {
		this.mkod = mkod;
	}
	
	@Override
	public String toString() {
		return String.format("Megrendelo[nev=%s, telefon=%d, felhasznalo=%s, mkod=%s]", 
						nev, telefon, felhasznalo, mkod);
	}	
}