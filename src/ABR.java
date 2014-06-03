import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/*
Klasse: ABR
Beschreibung: 

*/


public class ABR implements Comparable{

	public final static String DISCRIMINATOR = "ABR";
	private int ID;
	private double Bruttolohn;
	private QUP Qup;
	private SSL Ssl;
	private int Jahr;
	private int Monat;
	
	final static Comparator ABR_id = new ABRidComp();
	final static Comparator ABR_bfs = new ABRbfsComp();
	
	 public ABR(int iD, QUP qup, SSL ssl, int jahr, int monat, double bruttolohn) {
		this.ID = iD;
		this.Bruttolohn = bruttolohn;
		this.Qup = qup;
		this.Ssl = ssl;
		this.Jahr = jahr;
		this.Monat = monat;
	}
	
	public int getID() {
		return ID;
	}

	public double getBruttolohn() {
		return Bruttolohn;
	}
	
	public int getJahr() {
		return Jahr;
	}

	public QUP getQup(){
		return Qup; 
	}
	
	public SSL getSsl() {
		return Ssl;
	}
	
	public static ABR getABR( List<String> values ) {
		   	int NewID = 0;
		   	int tmpq = 0;
		   	int tmps = 0;
		   	int Jahr = 0;
		   	int Monat = 0;
		   	double Betrag = 0;
		   
		   	if( values.size() == 5 ){
		   		if(QuellenSteuer.getAbrs().size() == 0) {
					NewID = 1;
				} else {
					Collections.sort(QuellenSteuer.getAbrs(), ABR.ABR_id);
					int LargestId = QuellenSteuer.getAbrs().get(QuellenSteuer.getAbrs().size()-1).getID();				
					NewID = LargestId + 1;
				}
		   		
		   		tmpq = Integer.parseInt(values.get(0));
		    	tmps = Integer.parseInt(values.get(1));
		    	Jahr = Integer.parseInt(values.get(2));		      
		    	Monat = Integer.parseInt(values.get(3));
		    	Betrag = Double.parseDouble(values.get(4));  
		    	
		   	} else if( values.size() == 6 ) {
		   		NewID = Integer.parseInt( values.get(0) ); // Hier noch die IDs ect.. von den Objekten implementieren
		   		tmpq = Integer.parseInt(values.get(1));
		   		tmps = Integer.parseInt(values.get(2));
		   		Jahr = Integer.parseInt(values.get(3));		      
		    	Monat = Integer.parseInt(values.get(4));
		    	Betrag = Double.parseDouble(values.get(5));  
		    } else {
		    	  throw new RuntimeException("Falsche Anzahl von Werten: " + values.size() + "\n" + format());
		    }
		   	  try {
		   		  
		   		  SSL s = null;
		   		  for(int i = 0; i < QuellenSteuer.getSsls().size(); i++){
		   			  if(QuellenSteuer.getSsls().get(i).getID() == tmps)
		   				s = QuellenSteuer.getSsls().get(i);
		   		  }
		   		  
		   		  if(s == null){
		   			throw new RuntimeException("Keine gültige SSL id mitgegeben");
		   		  }
		   		  
		   		  QUP q = null;
		   		  for(int i = 0; i < QuellenSteuer.getQups().size(); i++){
		   			  if(QuellenSteuer.getQups().get(i).getID() == tmpq)
		   				q = QuellenSteuer.getQups().get(i);
		   		  }
		   		  
		   		  if(q == null){
		   			throw new RuntimeException("Keine gültige QUP id mitgegeben");
		   		  }
		   		  
		    	  return new ABR(NewID, q, s, Jahr, Monat, Betrag);
		      }
		      catch(RuntimeException r) {
		         throw new RuntimeException("Error: " + r.getMessage() + "\n" + format());
		      }
		   }

	   public String toString(){
		   return ID + "; " + Bruttolohn + ";" ;
	   }

	   public int compareTo( Object o ) {
		   ABR that = (ABR)o;
		   //QUP that = (QUP)o;
		   /*  Anzeige aller Abrechnungen. Sortierung nach steuerlich relevantem Sitz
		    * vergleich wohnsaessig
		    * 
		    */
		   int cmp;
		   
		   boolean tmp = Qup.isAnsaessig();
		   // debug
		   //System.out.println("ist Ansaessig: " + tmp);
		   
		   if (tmp == true) {
			   cmp = this.Qup.getWohnort()  - that.getQup().getWohnort();
			   // debug
			   //System.out.println("Ist Ansaessig: this.qup.getWohnort \"" + this.Qup.getWohnort() + "\" that.getQup.getWohnort \"" + that.getQup().getWohnort() + "\"");
		   }
		   else {
			   cmp = this.Ssl.getSitz() - that.getSsl().getSitz();
		   }
		   
		   return cmp;
		   
	   }
	   
	   private static String format() {
		      return "Erwartetes Format:\n"+ DISCRIMINATOR + ":" + " ID, QUP_ID; SSL_ID; Jahr; Monat; Betrag";
		   }

	   static class ABRidComp implements Comparator {
			public int compare(Object o1, Object o2) {
				ABR a1 = (ABR)o1;
				ABR a2 = (ABR)o2;
				
				return a1.ID - a2.ID;
			}

		}
	   
	   static class ABRbfsComp implements Comparator {
			public int compare(Object o1, Object o2) {
				ABR a1 = (ABR)o1;
				ABR a2 = (ABR)o2;
				
				return a1.Qup.getID() - a2.Qup.getID();
			}

		}
	
}
