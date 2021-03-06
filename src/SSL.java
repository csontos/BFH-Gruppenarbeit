/* Names: Lucas Badertscher, J�rg W�st, Fabian Fischer, Robin Csontos / class 2o
 * Program: Quellensteuer
 * Version: 1.0
 * Class: SSL.java
 * Description: Klasse f�r Schuldner steuerbarer Leistungen (SSL)
 */

import java.util.Comparator;
import java.util.List;
import java.util.Collections;


public class SSL implements Comparable{
	public final static String DISCRIMINATOR = "SSL";
	private int ID; //wenn es diese nicht gibt, setzen
	private String Firmenname;
	private int Sitz;
	private String Wohnkanton;
	private String Wohngemeinde;
	
	final static Comparator SSL_id = new SSLidComp();
	final static Comparator SSL_K = new SSLkComp();
	
	public SSL(int id, String firmenname, int sitz) {
		ID = id;
		Firmenname = firmenname;
		Sitz = sitz;
	}

	public int getID() {
		return ID;
	}

	public int getSitz() {
		return Sitz;
	}
	
	public String getWohnkanton() {
		Wohnkanton = "";
		for (int i = 0; i < QuellenSteuer.getGems().size(); i++) {
			if (QuellenSteuer.getGems().get(i).getBfs() == Sitz) {
				Wohnkanton = QuellenSteuer.getGems().get(i).getKanton();
			}
		}
		return Wohnkanton;
	}
	
	public String getGemeindeName() {
		Wohngemeinde = "";
		for (int i = 0; i < QuellenSteuer.getGems().size(); i++) {
			if (QuellenSteuer.getGems().get(i).getBfs() == Sitz) {
				Wohngemeinde = QuellenSteuer.getGems().get(i).getGemeindeName();
			}
		}
		return Wohngemeinde;
	}
	
	public String getFirmenname(){
		return this.Firmenname;
	}
	
	public static SSL getSSL( List<String> values ) {  
		int NewID = 0;
		String Name;
		int bfs;
			
		if (values.size() == 2) {
			if(QuellenSteuer.getSsls().size() == 0) {
				NewID = 1;
			} else {
				Collections.sort(QuellenSteuer.getSsls(), SSL.SSL_id);
				int LargestId = QuellenSteuer.getSsls().get(QuellenSteuer.getSsls().size()-1).getID();
				NewID = LargestId + 1;
			}
			Name = values.get(0);
			bfs = Integer.parseInt(values.get(1));
		} else if (values.size() == 3) {
			NewID = Integer.parseInt(values.get(0));
			Name = values.get(1);
			bfs = Integer.parseInt(values.get(2));
		} else {
			throw new RuntimeException("Falsche Anzahl von Werten: "
					+ values.size() + "\n" + format());
		}
		try {
			return new SSL(NewID, Name, bfs);
		} catch (RuntimeException r) {
			throw new RuntimeException("Error: " + r.getMessage() + "\n"
					+ format());
		}
	}
	
	public String toString(){
		return Firmenname + "; " + getGemeindeName() + "; " + getWohnkanton();
	}
	
	static class SSLidComp implements Comparator {
		public int compare(Object o1, Object o2) {
			SSL s1 = (SSL)o1;
			SSL s2 = (SSL)o2;
			
			return s1.ID - s2.ID;
		}
	}
	
	   static class SSLkComp implements Comparator {
		   /*  Sortierung: Kanton - Name - bfs
		    */
			public int compare(Object o1, Object o2) {
				SSL s1 = (SSL)o1;
				SSL s2 = (SSL)o2;
				// Sitz noch vergleichen. Wiso int? entspricht dies der BFS Nr.?
				//int cmp = s1.Sitz - s2.Sitz;
				int cmp = s1.getWohnkanton().compareTo(s2.getWohnkanton());
				if (cmp != 0)
					return cmp;
				cmp = s1.Firmenname.compareTo(s2.Firmenname);
				if (cmp != 0)
					return cmp;
				return 0;
			}
	   }

	private static String format() {
		      return "Erwartetes Format:\n"+ DISCRIMINATOR + ":" + " ID; Name; Sitz";
	}


	public int compareTo(Object o) {
		SSL that = (SSL)o;
		
		int cmp = this.Firmenname.compareTo(that.Firmenname);
		if (cmp != 0)
			return cmp;
		
		cmp = this.Sitz - that.Sitz;
		if (cmp != 0)
			return cmp;
		
		return 0;
	}	   
}
