/* Names: Lucas Badertscher, Jürg Wüst, Fabian Fischer, Robin Csontos / class 2o
 * Program: Quellensteuer
 * Version: 1.0
 * Class: QUP.java
 * Description: Klasse für die Quellensteuerpflichtigen
 */

import java.util.Collections;
import java.util.List;
import java.util.Comparator;

public class QUP implements Comparable {
	public final static String DISCRIMINATOR = "QUP";
	private int ID;
	private String Name;
	private String Vorname;
	private boolean Ansaessig;
	private int Kinder;
	private int Wohnort;
	private String Wohnkanton;

	
	final static Comparator QUP_K = new QUPkComp();
	final static Comparator QUP_id = new QUPidComp();

	public QUP(int ID, String name, String vorname, int wohnort, boolean ansaessig, int kinder) {
		this.ID = ID;
		this.Name = name;
		this.Vorname = vorname;
 		this.Wohnort = wohnort;
		this.Ansaessig = ansaessig;
		this.Kinder = kinder;	
	}
	
	public int getID() {
		return ID;
	}
	
	public int getKinder() {
		return Kinder;
	}

	public int getWohnort() {
		return Wohnort;
	}
	
	public String getWohnkanton() {
		Wohnkanton = "";
		for (int i = 0; i < QuellenSteuer.getGems().size(); i++) {
			if (QuellenSteuer.getGems().get(i).getBfs() == Wohnort) {
				Wohnkanton = QuellenSteuer.getGems().get(i).getKanton();
			}
		}
		return Wohnkanton;
	}

	public boolean isAnsaessig() {
		return Ansaessig;
	}

	public String getVorname() {
		return Vorname;
	}

	public String getName() {
		return Name;
	}

	public static QUP getQUP(List<String> values) {
		int NewID;
		String name;
		String vorname;
		int wohnort;
		boolean ansaesig;
		int kinder;
		
		if (values.size() == 5) {
			if(QuellenSteuer.getQups().size() == 0) {
				NewID = 1;
			} else {
				Collections.sort(QuellenSteuer.getQups(), QUP.QUP_id);
				int LargestId = QuellenSteuer.getQups().get(QuellenSteuer.getQups().size()-1).getID();				
				NewID = LargestId + 1;
			}
			name = values.get(0);
			vorname = values.get(1);
			wohnort = Integer.parseInt(values.get(2));
			ansaesig = Boolean.parseBoolean(values.get(3));
			kinder = Integer.parseInt(values.get(4));
		} else if (values.size() == 6) {
			NewID = Integer.parseInt(values.get(0));
			name = values.get(1);
			vorname = values.get(2);
			wohnort = Integer.parseInt(values.get(3));
			ansaesig = Boolean.parseBoolean(values.get(4));
			kinder = Integer.parseInt(values.get(5));
		} else {
			throw new RuntimeException("Falsche Anzahl von Werten: "
					+ values.size() + "\n" + format());
		}
		
		try {			
			return new QUP(NewID, name, vorname, wohnort, ansaesig, kinder);
		} catch (RuntimeException r) {
			throw new RuntimeException("Error: " + r.getMessage() + "\n"
					+ format());
		}
	}
	
	   public String toString(){
		   return getWohnkanton() + "; " + Name + "; " + Vorname;
	   }
	
	   public int compareTo( Object o ) {
		   QUP that = (QUP)o;
		   /*  Sortierung: name - vorname
		    * 
		    */
		   int cmp = this.Name.compareTo(that.Name);
		   if (cmp != 0)
			   return cmp;
		   
		   cmp = this.Vorname.compareTo(that.Vorname);
		   if (cmp != 0)
			   return cmp;
		   
		   return 0;
	   }
	   
	   static class QUPkComp implements Comparator {
		   /*  Sortierung: Kanton - Name - bfs
		    */
			public int compare(Object o1, Object o2) {
				QUP q1 = (QUP)o1;
				QUP q2 = (QUP)o2;
				
				int cmp = q1.Wohnort - q2.Wohnort;
				if (cmp != 0)
					return cmp;
				
				cmp = q1.Name.compareTo(q2.Name);
				if (cmp != 0)
					return cmp;
				
				cmp = q1.Vorname.compareTo(q2.Vorname);
				if (cmp != 0)
					return cmp;
				
				return 0;
			}
	   }

	private static String format() {
		return "Erwartetes Format:\n" + DISCRIMINATOR + ":"
				+ " ID; Name; Vorname; Wohnort";
	}

	static class QUPidComp implements Comparator {
		public int compare(Object o1, Object o2) {
			QUP q1 = (QUP)o1;
			QUP q2 = (QUP)o2;
			
			return q1.ID - q2.ID;
		}

	}

}
