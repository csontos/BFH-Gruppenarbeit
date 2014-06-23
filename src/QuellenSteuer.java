/* Names: Lucas Badertscher, Jürg Wüst, Fabian Fischer, Robin Csontos / class 2o
 * Program: Quellensteuer
 * Version: 1.0
 * Class: QuellenSteuer.java
 * Description: Funktionalität gemäss help()
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;


public class QuellenSteuer {

	/*signalisiert Ende der Eingabe*/
	private final static char EOF_CHAR = '-'; 

	/* Daten werden in Listen verwaltet */
	private static List<Gemeinde> gems = new LinkedList<Gemeinde>();
	private static List<SSL> ssls = new LinkedList<SSL>();
	private static List<QUP> qups = new LinkedList<QUP>();
	private static List<ABR> abrs = new LinkedList<ABR>();

	public static List<SSL> getSsls() {
		return ssls;
	}
	
	public static List<Gemeinde> getGems() {
		return gems;
	}
	
	public static List<QUP> getQups() {
		return qups;
	}
	
	public static List<ABR> getAbrs() {
		return abrs;
	}
	
	/* Methode: main()
	 * Aufruf der Methode waitforinput() */
	public static void main(String[] args) {
		waitforInput(args);
	}

	/* Methode: waitforinput()
	 * Die Methode prüft, ob bereits Argumente mitgegeben werden. 
	 * Solange nicht 'exit' eingegeben wird, soll die Eingabe von Argumenten wiederholt werden. */
	public static void waitforInput(String[] args) {	
		
		/* Ohne Argumente wird help aufgerufen */
		if( args.length==0 ) {
			help();
			Scanner input = new Scanner(System.in);
			System.out.print("Bitte Auswahl angeben: "); 
			String s = input.nextLine();
			
			/* Bei der Eingabe von "exit" schliesst sich das Programm */
			if(s.equals("exit")){
				System.out.println("Das Programm wurde beendet");
				System.exit(-1);
			}
			
			/* Der eingegebene String wird nach dem Leerzeichen getrennt und die einzelnen Elemente als die Argumente des Programms definiert */
			args = s.split(" ");   
	    }

		/* Überprüfung des erstes Arguments. 
		 * Mögliche Werte: imp, exp, show, del, stat */
		String cmd = args[0];

		if (cmd.equals("imp")) {
			Scanner sc = null;
			if (args.length > 1) { /* Einlesen von Datei */
				File f = null;
				try {
					f = new File(args[1].trim());
					sc = new Scanner(f);
				} catch (FileNotFoundException fnfe) {
					System.out.println("Datei " + f.getName()
							+ " kann nicht gefunden werden.");
					waitforInput(new String[0]);
				}
			} else { /* Einlesen von stdin */
				sc = new Scanner(System.in);
			}
			
			/* Aufruf der Funktion imp() */
			imp(sc);

			
		} else if (cmd.equals("exp")) {
			PrintStream out = null;
			String discriminator = null;

			if (args.length <= 1) {
				System.out.println("Falsche Anzahl von Argumenten für exp.");
				waitforInput(new String[0]);
			}

			discriminator = args[1].trim();

			if (args.length > 2) {
				File f = null;
				try {
					f = new File(args[2].trim()); /* Export in Datei */
					out = new PrintStream(f);
					export(discriminator, out);
					out.close();
				} catch (FileNotFoundException fnfe) {
					System.out.println("Datei " + f.getName()
							+ " kann nicht geöffnet werden.");
					waitforInput(new String[0]);
				}
			} else {
				/* Aufruf der Funktion export() */
				export(discriminator, System.out);
			}
			
		} else if (cmd.equals("show")) {
			Scanner sc = null;
			sc = new Scanner(System.in);

			if (args.length > 3) {
				System.out.println("Falsche Anzahl von Argumenten für show.");
				waitforInput(new String[0]);
			}
			
			/* Aufruf der Funktion show() */
			show(sc);

		} else if (cmd.equals("del")) {
			String discriminator = null;
			
			Scanner sc = null;
			sc = new Scanner(System.in);
			
			if (args.length != 1) {
				System.out.println("Falsche Anzahl von Argumenten für del.");
				waitforInput(new String[0]);
			}
			
			/* Aufruf der Funktion del() */
			del(sc);
			
		} else if (cmd.equals("stat")) {
			String discriminator = null;
			
			Scanner sc = null;
			sc = new Scanner(System.in);
			
			if ((args.length < 1)&&(args.length < 2)) {
				System.out.println("Falsche Anzahl von Argumenten für stat.");
				waitforInput(new String[0]);
			}
			
			/* Aufruf der Funktion stat() */
			stat(sc);
			
			
		} else {
			System.out.println("Falsche Argumentliste");
		}
		
		waitforInput(new String[0]);
			
	}
	
	/* Methode: export()
	 * Diese Methode überprüft den übergebenen Discriminator mit allen möglichen Optionen und exportiert die Daten an die angegebene Stelle. 
	 * Beim Discriminator 'ALL' wird jede If-Schlaufe einzeln abgearbeitet. */
	private static void export(String discriminator, PrintStream out) {
		out.println("# exporting '" + discriminator + "' at "
				+ new java.util.Date());
		
		if (discriminator.equals("ALL")
				|| discriminator.equals(Gemeinde.DISCRIMINATOR)) {
			for (Gemeinde g : gems)
				out.println(Gemeinde.DISCRIMINATOR + ": " + g);
		}
		 if( discriminator.equals("ALL") || discriminator.equals(SSL.DISCRIMINATOR)) {
		 for( SSL s : ssls )
			 out.println( SSL.DISCRIMINATOR + ": " + s );
		 }
		 if( discriminator.equals("ALL") || discriminator.equals(QUP.DISCRIMINATOR)) {
		 for( QUP q : qups )
			 out.println( QUP.DISCRIMINATOR + ": " + q );
		 }
		 if( discriminator.equals("ALL") || discriminator.equals(ABR.DISCRIMINATOR)) {
		 for( ABR a : abrs )
			 out.println( ABR.DISCRIMINATOR + ": " + a );
		 }
	}
	
	/* Methode: imp()
	 * Diese Methode importiert Daten anhand des angegebenen Discriminators.  */
	private static void imp( Scanner sc ) {
      
		/* Counter für Datensätze und Zeilen */
	  int line_ct = 0;
      int imp_ct = 0;
      
      String line = "";
      
      while( true ) {
         System.out.println("Eingabe: ");
         line = sc.nextLine();
         
         if( line==null )
            break;
         if (line.length() == 0 || line.charAt(0) == '#') /* Leere Zeilen oder Kommentarzeilen ignorieren */
            continue;
         if( line.charAt(0)==EOF_CHAR ) /* Bei Eingabe des Bindestrichs wird die Funktion verlassen */
            break;
         
         line_ct++;
         
         /* suche ":". z.B. "GEM: 2732; AG; Uezwil" */
         String discriminator; 
         int colonPosition;
         if ((colonPosition = line.indexOf(":")) < 0) {
            System.out.println(
                  "Parsing-Fehler. Kein Discriminator in Zeile " + line);
            continue;
         }
         discriminator = line.substring(0, colonPosition).trim();
         line = line.substring(colonPosition + 1).trim();

         /* Alle Tokens einlesen, Trenner ist ";" */
         List<String> tokens = new LinkedList<String>();
         while (true) {
            int delimPos = line.indexOf(";");
            if (delimPos < 0) {
               tokens.add(line.trim());
               break; /* kein ";" mehr */
            } else {
               tokens.add(line.substring(0, delimPos).trim());
               line = line.substring(delimPos + 1);
            }
         }

         /* Import von Gemeinden */
         if (discriminator.equals("GEM")) {
            try {
               Gemeinde g = Gemeinde.getGemeinde(tokens);
               if( gems.contains( g )) {
                  System.out.println("Eine Gemeinde mit dieser BFS-Nummer existiert bereits.");
                  continue;
               }
               gems.add(g);
               imp_ct++;
               System.out.println(g);
            } catch (RuntimeException re) {
               System.out.println("Error: " + re.getMessage());
            }
         }
         /* Import von Schuldnern steuerbarer Leistungen (SSL) */
          else if ( discriminator.equals("SSL")) {
        	  try {
        		  SSL s = SSL.getSSL(tokens);
        		  if( ssls.contains( s )) {
        			  System.out.println("Ein Arbeitgeber mit dieser ID existiert bereits.");
        			  continue;
        		  }
        		  ssls.add(s);
                  imp_ct++;
                  System.out.println(s);
        	  } catch (RuntimeException re) {
                  System.out.println("Error: " + re.getMessage());
              }
          }
         /* Import von Quellensteuerpflichtigen */
          else if ( discriminator.equals("QUP")) {
         	  try {
        		  QUP q = QUP.getQUP(tokens);
        		  if( qups.contains( q )) {
        			  System.out.println("Der Quellensteuerpflichtige mit dieser ID existiert bereits.");
        			  continue;
        		  }
        		  qups.add(q);
                  imp_ct++;
                  System.out.println(q);
        	  } catch (RuntimeException re) {
                  System.out.println("Error: " + re.getMessage());
              }
          }
         
         /* Import von Quellensteuerabrechnungen */
          else if ( discriminator.equals("ABR")) {
         	  try {
        		  ABR a = ABR.getABR(tokens);
        		  if( abrs.contains( a )) {
        			  System.out.println("Die Abrechnung mit dieser ID existiert bereits.");
        			  continue;
        		  }
        		  abrs.add(a);
                  imp_ct++;
                  System.out.println(a);
        	  } catch (RuntimeException re) {
                  System.out.println("Error: " + re.getMessage());
              }
          }
         
         else {
            System.out.println("Parsing error. Kein gültiger Discriminator: "
                  + discriminator);
         }
      }
      System.out.println("Anzahl der Zeilen: " + line_ct);
      System.out.println("Anzahl der Datensätze: " + imp_ct);
   }
	
	/* Methode: del()
	 * Diese Methode  löscht bestimmte Einträge in der Liste. Die genaue Syntax zur Löschung ist im Code genauer beschrieben  */
	private static void del(Scanner sc) {
	      
	    String line = "";
	      
	      while( true ) {
	         System.out.println("Eingabe:");
	         line = sc.nextLine();
	         
	       //String vorbereiten um die Listenpositionen der zu löschenden Gems zu speichern
	 		String deleteId = "";
	         
	         if( line==null )
	            break;
	         
	          /* Leere Zeilen oder Kommentarzeilen ignorieren */
	         if (line.length() == 0 || line.charAt(0) == '#')
	            continue;
	         if( line.charAt(0)==EOF_CHAR )
	            break;

	        String[] discriminator = line.split(" ");
	        
	        /* Abarbeitung der folgenden Abfrage, falls 'del GEM' eingegeben wird. */
	     	if (discriminator[0] == "GEM" || discriminator[0].equals(Gemeinde.DISCRIMINATOR)) {

     			for(int i = 0; i < gems.size(); i++){
     				
     				boolean match = false;
 					
     				//Durch Qups iterieren und Verknüpfungen auf GEM suchen
     				for(QUP q: qups){
     					if(q.getWohnort() == gems.get(i).getBfs())
     						match = true;
     				}
     				
     				//Durch SSLs iteriern und Verknüpfungen auf GEM suchen
     				for(SSL s: ssls){
     					if(s.getSitz() == gems.get(i).getBfs())
     						match = true;
     				}
     				
     				//Falls gelöscht werden soll, wird Position mit Trennzeichen ; gespeichert
     				if(match == false){
     					deleteId = deleteId + i + ";"; 
     				}
     			}
     			
     			//Wenn es Gemeinden zum löschen gibt
     			if(deleteId.length() > 0){
     				//zu löschende Positionen in Array schreiben
     				deleteId = deleteId.substring(0, deleteId.length()-1);
	     			String[] ArrDeleteId = deleteId.split(";");
	     			
	     			//Geminden die zu löschen sind aufgrund von Position in Liste in ein eigenes Array schreiben
	     			Gemeinde[] DelGems = new Gemeinde[ArrDeleteId.length];
	     			for(int i = 0;i < ArrDeleteId.length; i++ ){
	     				int IntDelId = Integer.parseInt(ArrDeleteId[i]);
	     				DelGems[i] = gems.get(IntDelId); 
	     			}
	     			
	     			
     				if(discriminator.length == 1){	
     					//Gemeinden löschen
    	     			for(Gemeinde delGem: DelGems){
    	     				gems.remove(delGem);
    	     			}
    	     			
    	     			System.out.println(DelGems.length + " Gemeinde(n) gelöscht");
    	     			
 					/* Abarbeitung der folgenden Abfrage, falls 'del GEM bfs' eingegeben wird. */
     				} else if(discriminator.length <= 2){
 		     			
 		     			int bfsid = 0;
 	     				try{
 	     					bfsid = Integer.parseInt(discriminator[1]);
 	     				} 
 	     				catch (RuntimeException re) {
 	     					System.out.println("BFS ID hat kein valides Format");
 	     					waitforInput(new String[0]);
 	     				}
 	     				
 	     				//Durch Gems iterieren welche keinen Verknüpfung haben
 	     				for(Gemeinde delGem: DelGems){
 	     					if(delGem.getBfs() == bfsid){ //Wenn bfsid der eingegebenen bfs id entspricht wird das element gelöscht
 	     						gems.remove(delGem);
 	     						System.out.println("Gemeinde " + bfsid + " gelöscht");
 	     					}
    	     			}
 	     				
 		     		} else {
 		     			System.out.println("Keine gültige Eingabe");
	     				waitforInput(new String[0]);
 		     		}
     		
     			} else {
     				System.out.println("keine Gemeinde gelöscht");
     			}
	     	/* Abarbeitung der folgenden Abfrage, falls 'del QUP' eingegeben wird. */
	     	} else if (discriminator[0] == "QUP" || discriminator[0].equals(QUP.DISCRIMINATOR)) {
	     		
	 			for(int i = 0; i < qups.size(); i++){
	 				boolean match = false;
	 					
	 				for(ABR a: abrs){
	 					if(a.getQup().getID() == qups.get(i).getID())
	 						match = true;
	 				}
	 					
	 				if(match == false){
	 					deleteId = deleteId + i + ";"; 
	 				}
	 			}
	 			
	 			//Wenn es QUPs zum löschen gibt
	 			if(deleteId.length() > 0){
	 				//zu löschende Positionen in Array schreiben
	 				deleteId = deleteId.substring(0, deleteId.length()-1);
	     			String[] ArrDeleteId = deleteId.split(";");
	     			
	     			//Geminden die zu löschen sind aufgrund von Position in Liste in ein eigenes Array schreiben
	     			QUP[] DelQups = new QUP[ArrDeleteId.length];
	     			for(int i = 0;i < ArrDeleteId.length; i++ ){
	     				int IntDelId = Integer.parseInt(ArrDeleteId[i]);
	     				DelQups[i] = qups.get(IntDelId); 
	     			}
	     			
	     			
	 				if(discriminator.length == 1){	
	 					//Gemeinden löschen
		     			for(QUP delQup: DelQups){
		     				qups.remove(delQup);
		     			}
		     			
		     			System.out.println(DelQups.length + " QUP(s) gelöscht");
		     			
		     		/* Abarbeitung der folgenden Abfrage, falls 'del QUP id' eingegeben wird. */
	 				} else if(discriminator.length <= 2){
		     			
		     			int qupsid = 0;
	     				try{
	     					qupsid = Integer.parseInt(discriminator[1]);
	     				} 
	     				catch (RuntimeException re) {
	     					System.out.println("Die QUP ID hat kein valides Format");
	     					waitforInput(new String[0]);
	     				}
	     				
	     				//Durch Gems iterieren welche keinen Verknüpfung haben
	     				for(QUP delQup: DelQups){
	     					if(delQup.getID() == qupsid){ //Wenn bfsid der eingegebenen bfs id entspricht wird das element gelöscht
	     						qups.remove(delQup);
	     						System.out.println("QUP " + qupsid + " gelöscht");
	     					}
		     			}
	 				}else{
	 					System.out.println("Keine gültige Eingabe");
	     				waitforInput(new String[0]);
	 				}
	 			}
	     	/* Abarbeitung der folgenden Abfrage, falls 'del SSL' eingegeben wird.	*/
	      	} else if (discriminator[0] == "SSL" || discriminator[0].equals(SSL.DISCRIMINATOR)) {

     			for(int i = 0; i < ssls.size(); i++){
     				boolean match = false;
     					
     				for(ABR a: abrs){
     					if(a.getSsl().getID() == ssls.get(i).getID())
     						match = true;
     				}
     					
     				if(match == false){
     					deleteId = deleteId + i + ";"; 
     				}
     			}
     			//Wenn es Gemeinden zum löschen gibt
	 			if(deleteId.length() > 0){
	 				//zu löschende Positionen in Array schreiben
	 				deleteId = deleteId.substring(0, deleteId.length()-1);
	     			String[] ArrDeleteId = deleteId.split(";");
	     			
	     			//Geminden die zu löschen sind aufgrund von Position in Liste in ein eigenes Array schreiben
	     			SSL[] DelSsls = new SSL[ArrDeleteId.length];
	     			for(int i = 0;i < ArrDeleteId.length; i++ ){
	     				int IntDelId = Integer.parseInt(ArrDeleteId[i]);
	     				DelSsls[i] = ssls.get(IntDelId); 
	     			}
	     			
	 				if(discriminator.length == 1){	
	 					//Gemeinden löschen
		     			for(SSL delSsl: DelSsls){
		     				ssls.remove(delSsl);
		     			}
		     			
		     			System.out.println(DelSsls.length + " SSL(s) gelöscht");
		     			
		     		/* Abarbeitung der folgenden Abfrage, falls 'del QUP id' eingegeben wird. */
	 				} else if(discriminator.length >= 2){
	 					
	 					int sslid = 0;
	     				try{
	     					sslid = Integer.parseInt(discriminator[1]);
	     				} 
	     				catch (RuntimeException re) {
	     					System.out.println("SSL ID hat kein valides Format");
	     					waitforInput(new String[0]);
	     				}
	     				
	     				//Durch Gems iterieren welche keinen Verknüpfung haben
	     				for(SSL delSsl: DelSsls){
	     					if(delSsl.getID() == sslid){ //Wenn bfsid der eingegebenen bfs id entspricht wird das element gelöscht
	     						ssls.remove(delSsl);
	     						System.out.println("SSL " + sslid + " gelöscht");
	     					}
		     			}
	 				} else {
	 					System.out.println("Keine gültige Eingabe");
	     				waitforInput(new String[0]);
	 				}
	     		}
	      	/* Abarbeitung der folgenden Abfrage, falls 'del ABR' eingegeben wird. */
	      	} else if (discriminator[0] == "ABR" || discriminator[0].equals(ABR.DISCRIMINATOR)) {
	      		if(discriminator.length == 1){
	     			for(int i = abrs.size()-1; i >= 0; i--){
	     				abrs.remove(i);
	     			}
	     			System.out.println("Alle Abrechnungen wurden gelöscht");
	     			/* Abarbeitung der folgenden Abfrage, falls 'del ABR id' eingegeben wird. */	
	     		} else if(discriminator.length <= 2){

	     			int abrid = 0;
     				try {
     					abrid = Integer.parseInt(discriminator[1]);
     				} 
     				catch (RuntimeException re) {
     					System.out.println("ABR ID hat kein valides Format");
     					waitforInput(new String[0]);
     				}
	     			
     				for(int i = abrs.size()-1; i >= 0; i--){
	     				if(abrs.get(i).getID() == abrid) {
	     					abrs.remove(abrs.get(i));
	     				}
	     			}
     				System.out.println("Abrechnung mit der id " + abrid + " wurde erfolgreich gelöscht");
	     		} else {
	     			System.out.println("Keine gültige Eingabe");
     				waitforInput(new String[0]);
	     		}
	      	} else {
	            System.out.println("Parsing error. Kein gültiger Discriminator: " + discriminator);
	     	}
	     }
	}
	
	/* Methode: stat()
	 * description */
	private static void stat(Scanner sc) {
		 String line = "";
	      
	      while( true ) {
	         System.out.println("Eingabe:");
	         line = sc.nextLine();
	         
	         if( line==null )
	            break;
	         /* Leere Zeilen oder Kommentarzeilen ignorieren */
	         if (line.length() == 0 || line.charAt(0) == '#')
	            continue;
	         if( line.charAt(0)==EOF_CHAR )
	            break;

	        String[] discriminator = line.split(" ");
	        int year = 0;
	        
	     	if (discriminator[0].equals("BUND")) {
	     		
	     		if((discriminator.length > 2)||(discriminator.length < 1)){
	     			System.out.println("falsche Anzahl von Argumenten für BUND angegeben");
	     			waitforInput(new String[0]);
	     		}

	     		double SummeBruttolohn = 0.00;
	     		double QuellensteuerBund = 0.00;
	     		
	     		//Falls ein Jahr angegeben wurde wird dieses in die Year Variabel gespeichert. Ansonsten behält die Variabel den Wert 0
	     		if(discriminator.length == 2){
	     			try{
	     				year = Integer.parseInt(discriminator[1]);
	     			} catch(RuntimeException re){
	     				System.out.println("Kein gültiges Jahresformat angegeben");
	     				waitforInput(new String[0]);
	     			}
	     		}
	     		
	     		//Durch Alle Arechnungen iterieren, mit ausgewählten Jahr vergleichen und Bruttolohn zusammenzählen
	     		//Ist kein Jahr gewählt werden alle Löhne unabhängig vom Jahr zusammengezählt, da Variabel Year den Wert 0 enthält
	     		for(int i = 0; i < abrs.size(); i++){
	     			if((abrs.get(i).getJahr() == year)||(year == 0)){
	     				SummeBruttolohn = SummeBruttolohn + abrs.get(i).getBruttolohn();
	     			}
	     		}
	     		
	     		System.out.println("Summe Bruttolohn: " + roundTwo(SummeBruttolohn));
	     		
	     		QuellensteuerBund = SummeBruttolohn * 0.01;
	     		
	     		System.out.println("Quellensteuer Bund: " + roundTwo(QuellensteuerBund));
	     		
	     	} else if(discriminator[0].equals("KANT")){
	     		
	     		double SummeBruttolohnQUP = 0.00;
	     		double SummeBruttolohnSSL = 0.00;
	     		double SummeBruttolohnABR = 0.00;
	     		double QuellensteuerKanton = 0.00;
	     		double vermSteuersatz;
	     		
	     		if((discriminator.length > 3)||(discriminator.length < 2)){
	     			System.out.println("falsche Anzahl von Argumenten für KANT angegeben");
	     			waitforInput(new String[0]);
	     		}
	     		
	     		String KantID = discriminator[1];
	     		
	     		//Prüfen ob ein Jahr migegeben wurde und dieses speichern
	     		if(discriminator.length == 3){
	     			try{
	     				year = Integer.parseInt(discriminator[2]);
	     			} catch(RuntimeException re){
	     				System.out.println("Kein gültiges Jahresformat angegeben");
	     				waitforInput(new String[0]);
	     			}
	     		}
	     		
	     		//Durch alle Abrechnungen iterieren
	     		for(ABR a : abrs){
	     			if((a.getJahr() == year)||(year == 0)){
		     			for(Gemeinde g: gems){
		     				if(g.getKantonId().equals(KantID)){ //Prüfen ob der Kanton angewendet wird
		     					//Abzüge für Kinder für diese ABR berechnen
			     				vermSteuersatz = vermSatz( g.steuerKanton(), a.getQup().getKinder() );
		     					//Durch alle Gemeinden iterieren und die Gemiende heraussuchen, welche zum QUP der Abrechnung gehört
		     					if(a.getQup().getWohnort() == g.getBfs()){
		     						SummeBruttolohnQUP = SummeBruttolohnQUP + a.getBruttolohn();
		     						QuellensteuerKanton = QuellensteuerKanton + a.getBruttolohn() * vermSteuersatz;
		     					} else if(!a.getQup().isAnsaessig()){ //Wenn keine Gemeinde für die QUP gefunden wurde, wird geprüft ob der QUP einen Schweizer Wohnisitz hat
		     						if(a.getSsl().getSitz() == g.getBfs()){
		     							SummeBruttolohnSSL = SummeBruttolohnSSL + a.getBruttolohn();
		     							QuellensteuerKanton = QuellensteuerKanton + a.getBruttolohn() * vermSteuersatz;
		     						}
		     					}
		     				}
		     			}
	     			}
	     		}
	     		
	     		SummeBruttolohnABR = SummeBruttolohnQUP + SummeBruttolohnSSL;
	     		
	     		System.out.println( "Summe Bruttolhon der QUP für den Kanton: " + roundTwo(SummeBruttolohnQUP ));
	     		System.out.println( "Summe Bruttolohn der SSL für den Kanton (wenn QUP keinen Wohnsitz hat): " + roundTwo(SummeBruttolohnSSL ));
	     		System.out.println( "Summe Bruttolhon der QUP und SSL: " + roundTwo(SummeBruttolohnABR ));
	     		System.out.println( "Quellen Steuer für den Kanton " + KantID + ": " + roundTwo(QuellensteuerKanton ));
	     	
	     	} else if(discriminator[0].equals("GEM")){
	     		double SummeBruttolohnQUP = 0.00;
	     		double SummeBruttolohnSSL = 0.00;
	     		double SummeBruttolohn = 0.00;
	     		double QuellensteuerGem = 0.00;
	     		double vermSteuersatzGem = 0.00;
	     		
	     		if((discriminator.length > 3)||(discriminator.length < 2)){
	     			System.out.println("falsche Anzahl von Argumenten für GEM angegeben");
	     			waitforInput(new String[0]);
	     		}
	     		
	     		int bfs = Integer.parseInt(discriminator[1]);
	     		
	     		//Prüfen ob ein Jahr migegeben wurde und dieses speichern
	     		if(discriminator.length == 3){
	     			try{
	     				year = Integer.parseInt(discriminator[2]);
	     			} catch(RuntimeException re){
	     				System.out.println("Kein gültiges Jahresformat angegeben");
	     				waitforInput(new String[0]);
	     			}
	     		}
	     		
	     		//Durch alle Abrechnungen iterieren
	     		for(ABR a : abrs){
	     			//Prüfen ob im gewählten Jahr oder ob kein Jahr gewählt
	     			if((a.getJahr() == year)||(year == 0)){
	     				//Prüfen ob Wohnort für Abrechnung korrekt
		     			if(((a.getQup().getWohnort() == bfs))&&(a.getQup().isAnsaessig())){
		     				SummeBruttolohnQUP = SummeBruttolohnQUP + a.getBruttolohn();
		     			} else if ((!a.getQup().isAnsaessig()) && (a.getSsl().getSitz() == bfs)){
		     				SummeBruttolohnSSL = SummeBruttolohnSSL + a.getBruttolohn();
		     			}
		     			//Gemeinde für diese Abrechnung suchen und vermindeter Steuersatz berechnen
		     			for(Gemeinde g : gems){
	     					if(g.getBfs() == bfs){
	     						vermSteuersatzGem = vermSatz(g.steuerGemeinde(), a.getQup().getKinder());
	     					}
	     				}
		     			
		     			SummeBruttolohn = SummeBruttolohnSSL + SummeBruttolohnQUP;
		     			
		     			//Quellsteuer für die ABR berechnen und addieren
	     				QuellensteuerGem = SummeBruttolohn * vermSteuersatzGem;
	     			}
	     		}
	     		
	     		//Ausgabe Summe aller Bruttolöhne
	     		System.out.print("Summe aller Bruttolöhne von QUPs in der Gemeinde " + bfs );
	     		System.out.print((year == 0) ? " für alle Jahre" : " für das Jahr " + year);
	     		System.out.println(": " + roundTwo(SummeBruttolohnQUP));
	     		
	     		System.out.print("Summe aller Bruttolöhne von QUPs mit SSL in der Gemeinde (Falls nicht ansässig) " + bfs );
	     		System.out.print((year == 0) ? " für alle Jahre" : " für das Jahr " + year);
	     		System.out.println(": " + roundTwo(SummeBruttolohnSSL));
	     		
	     		System.out.print("Summe aller Bruttolöhne in der Gemeinde (Falls nicht ansässig) " + bfs );
	     		System.out.print((year == 0) ? " für alle Jahre" : " für das Jahr " + year);
	     		System.out.println(": " + roundTwo(SummeBruttolohn));
	     	
	     		//Ausgabe Quellsteuer Gemeinde
	     		System.out.print("Quellensteuer für Gemeinde " + bfs );
	     		System.out.print((year == 0) ? " für alle Jahre" : " für das Jahr " + year);
	     		System.out.println(": " + roundTwo(QuellensteuerGem));
	     		
	     	} else if(discriminator[0].equals("QUP")){
	     		
	     		double SummeBruttolohnQUP = 0.00;
	     		double QuellensteuerGesamt = 0.00;
	     		double QuellensteuerBund = 0.00;
	     		double QuellensteuerKant = 0.00;
	     		double QuellensteuerGem = 0.00;

	     		double vermSteuersatzBund = 0.00;
	     		double vermSteuersatzKant = 0.00;
	     		double vermSteuersatzGem = 0.00;
	     		
	     		String NameQup = "";
	     		
	     		if((discriminator.length > 3)||(discriminator.length < 2)){
	     			System.out.println("falsche Anzahl von Argumenten für QUP angegeben");
	     			waitforInput(new String[0]);
	     		}
	     		
	     		int QUPID = Integer.parseInt( discriminator[1] );
	     		
	     		//Prüfen ob ein Jahr migegeben wurde und dieses speichern
	     		if(discriminator.length == 3){
	     			try{
	     				year = Integer.parseInt(discriminator[2]);
	     			} catch(RuntimeException re){
	     				System.out.println("Kein gültiges Jahresformat angegeben");
	     				waitforInput(new String[0]);
	     			}
	     		}
	     		
	     		//Durch alle Abrechnungen iterieren
	     		for(ABR a : abrs){
	     			//Prüfen ob im gewählten Jahr oder ob kein Jahr gewählt
	     			if((a.getJahr() == year)||(year == 0)){
		     			if(a.getQup().getID() == QUPID){
		     				for(Gemeinde g : gems){
		     					//Prüfen ob ABR zur Wohngemeinde des QUP gehört.
		     					//Ist die QUP nicht Ansässig wird die gemeinde des SSL verwendet
		     					if((g.getBfs() == a.getQup().getWohnort()) || ((!a.getQup().isAnsaessig())) && (a.getSsl().getSitz() == g.getBfs())){
		     						vermSteuersatzBund = vermSatz(g.steuerBund(), a.getQup().getKinder());
		     						vermSteuersatzKant = vermSatz(g.steuerKanton(), a.getQup().getKinder());
		     						vermSteuersatzGem = vermSatz(g.steuerGemeinde(), a.getQup().getKinder());
		     					}
		     					//Summe Bruttolohn zusammenzählen
	     						SummeBruttolohnQUP = SummeBruttolohnQUP + a.getBruttolohn();
	     						//Steuerstätze berechnen
	     						QuellensteuerBund = QuellensteuerBund + a.getBruttolohn() * vermSteuersatzBund;
	     						QuellensteuerKant = QuellensteuerKant + a.getBruttolohn() * vermSteuersatzKant;
	     						QuellensteuerGem = QuellensteuerGem + a.getBruttolohn() * vermSteuersatzGem;
		     					
		     				}
		     				//Name des QUP auslesen
		     				if(NameQup.equals(""))
		     					NameQup = a.getQup().getVorname() + " " + a.getQup().getName();
		     			}
	     			}
	     			
	     		}
	     		
	     		QuellensteuerGesamt = QuellensteuerBund + QuellensteuerKant + QuellensteuerGem;
	     		
	     		System.out.println("Summe Bruttolohn für " + NameQup + ": " + roundTwo(SummeBruttolohnQUP));
	     		System.out.println("Quellensteuer Bund für " + NameQup + ": " + roundTwo(QuellensteuerBund));
	     		System.out.println("Quellensteuer Kanton für " + NameQup + ": " + roundTwo(QuellensteuerKant));
	     		System.out.println("Quellensteuer Gemeinde für " + NameQup + ": " + roundTwo(QuellensteuerGem));
	     		System.out.println("Quellensteuer Total für " + NameQup + ": " + roundTwo(QuellensteuerGesamt));
	     	
	     	} else if(discriminator[0].equals("SSL")){
	     		
	     		double SummeBruttolohnSSL = 0.00;
	     		double QuellensteuerGesamt = 0.00;
	     		double QuellensteuerBund = 0.00;
	     		double QuellensteuerKant = 0.00;
	     		double QuellensteuerGem = 0.00;

	     		double vermSteuersatzBund = 0.00;
	     		double vermSteuersatzKant = 0.00;
	     		double vermSteuersatzGem = 0.00;
	     		
	     		String NameSSL = "";
	     		
	     		if((discriminator.length > 3)||(discriminator.length < 2)){
	     			System.out.println("falsche Anzahl von Argumenten für QUP angegeben");
	     			waitforInput(new String[0]);
	     		}
	     		
	     		int SSLID = Integer.parseInt( discriminator[1] );
	     		
	     		//Prüfen ob ein Jahr migegeben wurde und dieses speichern
	     		if(discriminator.length == 3){
	     			try{
	     				year = Integer.parseInt(discriminator[2]);
	     			} catch(RuntimeException re){
	     				System.out.println("Kein gültiges Jahresformat angegeben");
	     				waitforInput(new String[0]);
	     			}
	     		}
	     		
	     		for(ABR a: abrs){
	     			//Prüfen ob im gewählten Jahr oder ob kein Jahr gewählt
	     			if((a.getJahr() == year)||(year == 0)){
	     				if(a.getSsl().getID() == SSLID){
	     					for(Gemeinde g: gems){
	     						if((g.getBfs() == a.getQup().getWohnort()) || ((!a.getQup().isAnsaessig())) && (a.getSsl().getSitz() == g.getBfs())){
	     							vermSteuersatzBund = vermSatz(g.steuerBund(), a.getQup().getKinder());
	     							vermSteuersatzKant = vermSatz(g.steuerKanton(), a.getQup().getKinder());
	     							vermSteuersatzGem = vermSatz(g.steuerGemeinde(), a.getQup().getKinder());
	     						}
	     					}
	     				
	     					//Summe Bruttolhn pro SSL berechnen
	     					SummeBruttolohnSSL = SummeBruttolohnSSL + a.getBruttolohn();
	     					//Steuerstätze berechnen
	     					QuellensteuerBund = QuellensteuerBund + a.getBruttolohn() * vermSteuersatzBund;
	     					QuellensteuerKant = QuellensteuerKant + a.getBruttolohn() * vermSteuersatzKant;
	     					QuellensteuerGem = QuellensteuerGem + a.getBruttolohn() * vermSteuersatzGem;
 						
	     					//Name des SSL setzen
	     					if(NameSSL == "")
	     						NameSSL = a.getSsl().getFirmenname();
	     				}
	     			}
	     			
	     		}
	     		
	     		QuellensteuerGesamt = QuellensteuerBund + QuellensteuerKant + QuellensteuerGem;
	     		
	     		System.out.println("Summe Bruttolohn für " + NameSSL + ": " + roundTwo(SummeBruttolohnSSL));
	     		System.out.println("Quellensteuer Bund für " + NameSSL + ": " + roundTwo(QuellensteuerBund));
	     		System.out.println("Quellensteuer Kanton für " + NameSSL + ": " + roundTwo(QuellensteuerKant));
	     		System.out.println("Quellensteuer Gemeinde für " + NameSSL + ": " + roundTwo(QuellensteuerGem));
	     		System.out.println("Quellensteuer Total für " + NameSSL + ": " + roundTwo(QuellensteuerGesamt));
	     		
	      	}else {
	            System.out.println("Parsing error. Kein gültiger Discriminator: " + discriminator);
	     	}
	   }
		
	}
	
	private static double vermSatz(double steuersatz, int kinder){
		
		//Obergrenze von 9 Kindern festlegen
		if(kinder > 9)
			kinder = 9;
		
		//Für jedes Kind 5 Prozent abziehen
		for(int i = 0; i < kinder; i++){
			steuersatz = steuersatz * 0.95;
		}
		
		return steuersatz;
	}
	
	 private static double roundTwo(double number) { 
		 double round = Math.round(number*10000); 
	     round = round / 10000; 
	     round = Math.round(round*1000); 
	     round = round / 1000; 
	     round = Math.round(round*100); 
	     return round / 100; 
	}

	/* Methode: stat()
	 * Anhand des Discriminators werden die jeweiligen Datensätze angezeigt. */
	private static void show(Scanner sc) {

		String line = "";
		while (true) {
			System.out.println("Eingabe:");
			line = sc.nextLine();

			if (line == null)
				break;
			/* Leere Zeilen oder Kommentarzeilen ignorieren */
			if (line.length() == 0 || line.charAt(0) == '#')
				continue;
			if (line.charAt(0) == EOF_CHAR)
				break;

			String[] discriminator = line.split(" ");
			// String discriminator; // suche ":" in "GEM: 2732; AG; Uezwil"
			// discriminator = line.substring(0).trim();
			// System.out.println(discriminator);

			/* alle Tokens einlesen, Trenner ist ";" */
			List<String> tokens = new LinkedList<String>();

			if (discriminator[0] == "GEM"
					|| discriminator[0].equals(Gemeinde.DISCRIMINATOR)) {
				if(discriminator.length == 1){
					Collections.sort(gems);
					System.out.println(gems.toString());
				}
				else {
										
					if (discriminator[1].equals("k")) {
						Collections.sort(gems, Gemeinde.GEM_K);
						System.out.println(gems.toString());
					}
					if (discriminator[1].equals("b")) {
						Collections.sort(gems, Gemeinde.GEM_B);
						System.out.println(gems.toString());
					}
				}
			}
			

			else if (discriminator[0] == "QUP"
					|| discriminator[0].equals(QUP.DISCRIMINATOR)) {
				if(discriminator.length == 1){
					Collections.sort(qups);
					System.out.println(qups.toString());
				}
				else {
					
					if (discriminator[1].equals("k")) {
						Collections.sort(qups, QUP.QUP_K);
						System.out.println(qups.toString());
					}
				}
			}
				
			else if (discriminator[0] == "SSL"
						|| discriminator[0].equals(SSL.DISCRIMINATOR)) {
				if(discriminator.length == 1) {
					Collections.sort(ssls);
					System.out.println(ssls.toString());
				}
				else {
						
					if (discriminator[1].equals("k")) {
						Collections.sort(ssls, SSL.SSL_K);
						System.out.println(ssls.toString());
					}
				}
			}
			
			else if (discriminator[0] == "ABR"
					|| discriminator[0].equals(ABR.DISCRIMINATOR)) {
				if(discriminator.length == 1){
					Collections.sort(abrs);
					System.out.println(abrs.toString());
				}
				/*
				 * Hier noch nach "show ABR kanton_kuerel" implementieren
				 * Anzeige aller Abrechnungen mit steuerl. relev. Sitz im Kanton
				 */
				
				else {
					Kanton kuerzel = null;
					try {
						kuerzel = Kanton.valueOf(discriminator[1]);
					} catch (RuntimeException re) {
						System.out.println("Dieses Kanton Kürzel existiert nicht!");
					}
					// debug Ausgabe des ausgewählten Kantons
					//System.out.println("Kanton ist " + kuerzel);
					boolean match = false;
					
					Collections.sort(abrs);
					
					for(int i = 0; i < abrs.size(); i++){
	     				int sslid = abrs.get(i).getSsl().getID();
	     				
	     				for(int j = 0; j < ssls.size(); j++){
	     					if (sslid == ssls.get(j).getID()) {
	     						int bfsnr = ssls.get(j).getSitz();
	     						
	     						for (int h = 0; h < gems.size(); h++) {
		     						if (bfsnr == gems.get(h).getBfs()) {
		     							String kt = gems.get(h).getKantonId();
		     							
		     							if (discriminator[1].equals(kt)) {
		     								match = true;
		     								// Ausgabe der Abrechnung, dessen SSL Sitz im ausgewählten Kanton (kanton_kuerzel) ist.
		     								System.out.println(abrs.get(i));
		     								// optional noch mit Kanton Kürzel
		     								//System.out.println(abrs.get(i) + " " + kt);
		     							}
		     						}
		     					}
	     					}
	     				}
	     			}
					
					if (match == false) {
						System.out.println("Es wurde keine Abrechnung im Kanton " + kuerzel + " gefunden.");
					}
					
					
//					if (kuerzel != null) {
//						/*
//						 * Irgendwie müsste ich hier doch den Kanton weitergeben kann, damit ich nach diesem Sortieren kann.
//						 * Ansonsten müsste ich ja hier bereits eine IF/Case Abfrage für 26 Kantone mit 26 implementierte sorts die das gewünschte zurück liefern..
//						 * Weiss jemand gerade wie das gehen soll?	bzw. Es sollen dann nur noch die Einträge mit diesem Kanton ausgegeben werden, so wie ich das verstehe.
//						 */
//						Collections.sort(abrs, ABR.ABR_kuerzel);
//						System.out.println(abrs.toString());
//					}
					
					
//					if (discriminator[1].equals("k")) {
//						Collections.sort(abrs, ABR.ABR_id); // muss noch implementiert werden, daher vorerst ABR_id damit kein Fehler kommt
//						System.out.println(abrs.toString());
//					}
				}
				
			} else {
				System.out.println("Parsing error. Kein gültiger Discriminator: " + discriminator);
			}
		}
	}

	
	/* Methode: help()
	 * Zeigt die Hilfe zum Programm an. */
	private static void help() {
		System.out.println( "Das Programm Quellensteuer verwaltet Gemeinden (GEM), Quellensteuerpflichtige (QUP), " +
				   " Schuldner Steuerbarer Leistungen (SSLs) und Quellensteuerabrechnungen (ABR) und hat folgende Funktionalität:");
		 String s = "imp  : Einlesen von Datenzeilen (GEMs, SSLs, QUPs und/oder ABRr aus Datei oder stdin\n"
				+ "       Formate der Zeilen:\n"
				+ "        'GEM: bfs_nr; kanton_kuerzel; gemeinde_name'\n" 
				+ "        'QUP: qup_id; name; vorname; wohnort(bfs_nr); ...' (bei nicht vorhandener qup_id wird diese automatisch vergeben)\n"
				+ "        'SSL: ssl_id; name; sitz(bfs_nr); ...(bei nicht vorhandener ssl_id wird diese automatisch vergeben)\n"
				+ "        'ABR: abr_id; qup(qup_id); ssl(ssl_id); jahr; monat; betrag...' (bei nicht vorhandender abr_id wird diese automatisch vergeben)\n"
				+ "exp  : Exportieren von Daten in Datei oder nach stdout\n"
				+ "   exp ALL      : Exportieren aller Daten in Datei oder nach stdout\n"
				+ "   exp GEM      : Exportieren aller Gemeinde-Daten in Datei oder nach stdout\n"
				+ "   exp QUP      : Exportieren aller QUP-Daten in Datei oder nach stdout\n"
				+ "   exp SSL      : Exportieren aller SSL-Daten in Datei oder nach stdout\n"
				+ "   exp ABR      : Exportieren aller ABR-Daten in Datei oder nach stdout\n"
				+ "show : Anzeige von Daten mit verschiedenen Sortierkriterien:\n"
				+ "   show GEM     : Standardsortierung: bfs - name - kanton\n"
				+ "   show GEM k   : Sortierung: kanton - name - bfs\n"
				+ "   show GEM b   : Sortierung: bfs - name - kanton\n"
				+ "   show QUP     : Sortierung: name - vorname\n"
				+ "   show QUP k   : Sortierung: kanton der wohngemeinde - name - vorname\n"
				+ "   show SSL     : Sortierung: name - sitz\n"
				+ "   show SSL k   : Sortierung: kanton des sitzes - name\n"
				+ "   show ABR     : Anzeige aller Abrechnungen. Sortierung nach steuerlich relevantem Sitz \n"
				+ "   show ABR kanton_kuerel   : Anzeige aller Abrechnungen mit steuerl. relev. Sitz im Kanton\n"
				+ "del  : Löschen von Datensätzen:\n"
				+ "   del GEM     : Löschen aller Gemeinden (auf die keine Beziehungen existieren)\n"
				+ "   del GEM bfs : Löschen der Gemeinde (falls keine Beziehung darauf existiert)\n"
				+ "   del QUP     : Löschen aller QUPs (auf die keine Beziehung existieren)\n"
				+ "   del QUP id  : Löschen des QUPs (falls keine Beziehung darauf existiert)\n"
				+ "   del SSL     : Löschen aller SSLs (auf die keine Beziehung existieren)\n"
				+ "   del SSL id  : Löschen des SSLs (falls keine Beziehung darauf existiert)\n"
				+ "   del ABR     : Löschen aller ABRs.\n"
				+ "   del ABR id  : Löschen des ABRs\n"
				+ "stat : Statistik\n"
				+ "   stat BUND jahr : Bundesstatistik für das Jahr\n"
				+ "            Summe Bruttolohn aller QUPs und Summe Quellensteuerbetrag für Bund\n"
				+ "   stat KANT id   : Kantonsstatistik für das Jahr \n"
				+ "            Summe Bruttolöhne aller im Kanton ansässigen QUPs\n"
				+ "            Summe Bruttolöhne aller QUPs, deren SSL im Kanton ansässig ist\n"
				+ "            Summe Bruttolöhne aller QUPs, von deren ABR Quellensteuer an den Kanton abgeführt wird\n"
				+ "            Summe Quellensteuerbetrag, den Kanton erhält\n"
				+ "   stat GEM bfs jahr  : Gemeindestatistik der Gemeinde für das Jahr\n"
				+ "            Summe Bruttolohn aller ABRs von QUPs in dem Jahr mit Wohnsitz in Gemeinde\n"
				+ "            Summe Bruttolohn aller ABRs von SSLs in dem Jahr mit Sitz in Gemeinde\n"
				+ "            Betrag Quellensteuer, den die Gemeinde in dem Jahr erhält\n"
				+ "   stat QUP id  jahr  : Individuelle Statistik eines QUPs für das Jahr\n"
				+ "            Bruttojahreslohn des QUP, Quellensteuer und wieviel Quellensteuer Bund, Kanton(e) und Gemeinde(n) erhalten\n"
				+ "   stat SSL id  jahr  : Individuelle Statistik eines SSL für das Jahr\n"
				+ "            Summe Bruttolöhne aller QUPs, deren ABRs beim SSL erfolgen, sowie Summe Quellensteuer mit Aufteilung an Bund, Kantone und Gemeinden."

		;
		System.out.println(s);
	}

}
