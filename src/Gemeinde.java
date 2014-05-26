/*
Klasse: Gemeinde
Beschreibung: 

*/


import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class Gemeinde implements Comparable {
   public final static String DISCRIMINATOR = "GEM";
   
   private int bfs;        //BFS-Gemeindenummer
   private String name;
   private Kanton kanton;
   
   public final static Comparator GEM_K = new GEMkComp();
   public final static Comparator GEM_B = new GEMbComp();

   public Gemeinde(int bfs, String name, Kanton kanton) {
      this.bfs=bfs; this.name=name; this.kanton=kanton;
   }
   
   public Gemeinde(int bfs, String name, String kanton) {
      this(bfs, name, Kanton.valueOf( kanton ));
   }
   
   public int getBfs() {
	   return bfs;
   }
   
   public static Gemeinde getGemeinde( List<String> values ) {
      if( values.size() != 3 ) {
         throw new RuntimeException("Falsche Anzahl von Werten: " + values.size() + "\n" + format());
      }
      try {
      int bfs = Integer.parseInt( values.get(0) );
      String kanton = values.get(1);
      String name = values.get(2);
      return new Gemeinde( bfs, name, kanton );
      }
      catch(RuntimeException r) {
         throw new RuntimeException("Error: " + r.getMessage() + "\n" + format());
      }
   }
   
   public String toString(){
      return bfs + "; " + name + "; " + kanton.name();
   }
   
   public double steuerGemeinde() {
      return 0.0;
   }
   public double steuerKanton() {
    return this.kanton.steuerSatz();
   }
   public double steuerBund() {
      return 0.0;
   }
   public double steuerAlle() {
      return steuerGemeinde() + steuerKanton() + steuerBund();
   }
   
   public boolean equals( Object o) {
      if( this==o ) return true;
      if( o==null || this.getClass()!=o.getClass() ) return false;
      Gemeinde that = (Gemeinde)o;
      return that.bfs==this.bfs;
   }
   
   public int compareTo( Object o ) {
	   Gemeinde that = (Gemeinde)o;
	   /*  Standardsortierung: bfs - name - kanton
	    * 
	    */
	   int cmp = this.bfs - that.bfs;
	   if (cmp != 0)
		   return cmp;
	   
	   cmp = this.name.compareTo(that.name);
	   if (cmp != 0)
		   return cmp;
	   
	   cmp = this.kanton.compareTo(that.kanton);
	   if (cmp != 0)
		   return cmp;
	   
	   return 0;
   }
   
   static class GEMkComp implements Comparator {
	   /*  Sortierung: Kanton - Name - bfs
	    */
		public int compare(Object o1, Object o2) {
			Gemeinde g1 = (Gemeinde)o1;
			Gemeinde g2 = (Gemeinde)o2;
			
			int cmp = g1.kanton.compareTo(g2.kanton);
			if (cmp != 0)
				return cmp;
			
			cmp = g1.name.compareTo(g2.name);
			if (cmp != 0)
				return cmp;
			
			cmp = g1.bfs - g2.bfs;
			if (cmp != 0)
				return cmp;
			
			return 0;
		}
   }
   
   static class GEMbComp implements Comparator {
	   /*  Sortierung: Kanton - Name - bfs
	    */
		public int compare(Object o1, Object o2) {
			Gemeinde g1 = (Gemeinde)o1;
			Gemeinde g2 = (Gemeinde)o2;
			
			int cmp = g1.bfs - g2.bfs;
			if (cmp != 0)
				return cmp;
			
			cmp = g1.name.compareTo(g2.name);
			if (cmp != 0)
				return cmp;
			
			cmp = g1.kanton.compareTo(g2.kanton);
			if (cmp != 0)
				return cmp;
			
			return 0;
		}
   }
   
   public int hashCode() {
      return bfs;
   }
   
   private static String format() {
      return "Erwartetes Format:\n"+ DISCRIMINATOR + ":" + " bfs_nummer; kanton_kuerzel; gemeinde_name";
   }
}
