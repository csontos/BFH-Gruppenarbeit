/* Names: Lucas Badertscher, J�rg W�st, Fabian Fischer, Robin Csontos / class 2o
 * Program: Quellensteuer
 * Version: 1.0
 * Class: QuellenSteuer.java
 * Description: Funktionalit�t gem�ss help()
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
	 * Die Methode pr�ft, ob bereits Argumente mitgegeben werden. 
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

		/* �berpr�fung des erstes Arguments. 
		 * M�gliche Werte: imp, exp, show, del, stat */
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

//			 NICHT MEHR NOTWENDIG??? if (sc != null)
//				sc.close();
			
		} else if (cmd.equals("exp")) {
			PrintStream out = null;
			String discriminator = null;

			if (args.length <= 1) {
				System.out.println("Falsche Anzahl von Argumenten f�r exp.");
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
							+ " kann nicht ge�ffnet werden.");
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
				System.out.println("Falsche Anzahl von Argumenten f�r show.");
				waitforInput(new String[0]);
			}
			
			/* Aufruf der Funktion show() */
			show(sc);

		} else if (cmd.equals("del")) {
			String discriminator = null;
			
			Scanner sc = null;
			sc = new Scanner(System.in);
			
			if (args.length != 1) {
				System.out.println("Falsche Anzahl von Argumenten f�r del.");
				waitforInput(new String[0]);
			}
			
			/* Aufruf der Funktion del() */
			del(sc);
			
		} else if (cmd.equals("stat")) {
			String discriminator = null;
			
			Scanner sc = null;
			sc = new Scanner(System.in);
			
			if ((args.length < 1)&&(args.length < 2)) {
				System.out.println("Falsche Anzahl von Argumenten f�r stat.");
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
	 * Diese Methode �berpr�ft den �bergebenen Discriminator mit allen m�glichen Optionen und exportiert die Daten an die angegebene Stelle. 
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
      
		/* Counter f�r Datens�tze und Zeilen */
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
            System.out.println("Parsing error. Kein g�ltiger Discriminator: "
                  + discriminator);
         }
      }
      System.out.println("Anzahl der Zeilen: " + line_ct);
      System.out.println("Anzahl der Datens�tze: " + imp_ct);
   }
	
	/* Methode: del()
	 * Diese Methode  l�scht bestimmte Eintr�ge in der Liste. Die genaue Syntax zur L�schung ist im Code genauer beschrieben  */
	private static void del(Scanner sc) {
	      
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
	        
	        /* Abarbeitung der folgenden Abfrage, falls 'del GEM' eingegeben wird. */
	     	if (discriminator[0] == "GEM" || discriminator[0].equals(Gemeinde.DISCRIMINATOR)) {
	     		
	     		if(discriminator.length == 1){
	     			for(int i = 0; i < gems.size(); i++){
	     				boolean match = false;
	     					
	     				for(int j = 0; j < qups.size(); j++){
	     					if(qups.get(j).getWohnort() == gems.get(i).getBfs())
	     						match = true;
	     				}
	     					
	     				if(match == false){
	     					gems.remove(i);
	     				}
	     			}
	     		}
	     		/* Abarbeitung der folgenden Abfrage, falls 'del GEM bfs' eingegeben wird. */
	     		else if(discriminator[1].equals("bfs")){
	     			if(discriminator.length != 3){
	     				System.out.println("Keine BFS Nummer eingegeben. Bitte geben Sie einen Befehl im Format GEM bfs <BFS NR> ein");
	     				waitforInput(new String[0]);
	     			}
	     			
	     			int bfsid = 0;
     				try{
     					bfsid = Integer.parseInt(discriminator[2]);
     				} 
     				catch (RuntimeException re) {
     					System.out.println("BFS ID hat kein valides Format");
     					waitforInput(new String[0]);
     				}
	     			
	     			for(int i = 0; i < gems.size(); i++){
	     				boolean match = false;
     					
	     				for(int j = 0; j < qups.size(); j++){
	     					if(qups.get(j).getWohnort() == gems.get(i).getBfs())
	     						match = true;
	     				}
	     				if(match == false){
	     					if(gems.get(i).getBfs() == bfsid){
	     						gems.remove(gems.get(i));
	     					}
	     				}
	     			}
	     		}
	     	/* Abarbeitung der folgenden Abfrage, falls 'del QUP' eingegeben wird. */
	     	} else if (discriminator[0] == "QUP" || discriminator[0].equals(QUP.DISCRIMINATOR)) {
	     		
	     		if(discriminator.length == 1){
	     			for(int i = 0; i < qups.size(); i++){
	     				boolean match = false;
	     					
	     				for(int j = 0; j < gems.size(); j++){
	     					if(gems.get(j).getBfs() == qups.get(i).getWohnort())
	     						match = true;
	     				}
	     					
	     				if(match == false){
	     					qups.remove(i);
	     				}
	     			}
	     		}
	     		/* Abarbeitung der folgenden Abfrage, falls 'del QUP id' eingegeben wird. */
	     		else if(discriminator[1].equals("id")){
	     			if(discriminator.length != 3){
	     				System.out.println("Keine ID eingegeben. Bitte geben Sie einen Befehl im Format QUP ID <ID> ein");
	     				waitforInput(new String[0]);
	     			}
	     			
	     			int qupsid = 0;
     				try{
     					qupsid = Integer.parseInt(discriminator[2]);
     				} 
     				catch (RuntimeException re) {
     					System.out.println("Die QUP ID hat kein valides Format");
     					waitforInput(new String[0]);
     				}
	     			
	     			for(int i = 0; i < qups.size(); i++){
	     				boolean match = false;
	     				
	     				for(int j = 0; j < gems.size(); j++){
	     					if(gems.get(j).getBfs() == qups.get(i).getWohnort())
	     						match = true;
	     				}
	     				
	     				if(match == false){
		     				if(qups.get(i).getID() == qupsid){
		     					qups.remove(qups.get(i));
		     				}
	     				}
	     			}
	     		}
	     	/* Abarbeitung der folgenden Abfrage, falls 'del SSL' eingegeben wird.	*/
	      	} else if (discriminator[0] == "SSL" || discriminator[0].equals(SSL.DISCRIMINATOR)) {
	     	
	      		if(discriminator.length == 1){
	     			for(int i = 0; i < ssls.size(); i++){
	     				boolean match = false;
	     					
	     				for(int j = 0; j < gems.size(); j++){
	     					if(gems.get(j).getBfs() == ssls.get(i).getSitz())
	     						match = true;
	     				}
	     					
	     				if(match == false){
	     					ssls.remove(i);
	     				}
	     			}
	     		}
	      		/* Abarbeitung der folgenden Abfrage, falls 'del SSL id' eingegeben wird. */
	      		else if(discriminator[1].equals("id")){
	     			if(discriminator.length != 3){
	     				System.out.println("Keine ID eingegeben. Bitte geben Sie einen Befehl im Format SSL ID <ID> ein");
	     				waitforInput(new String[0]);
	     			}
	     			
	     			int sslid = 0;
     				try{
     					sslid = Integer.parseInt(discriminator[2]);
     				} 
     				catch (RuntimeException re) {
     					System.out.println("SSL ID hat kein valides Format");
     					waitforInput(new String[0]);
     				}
	     			
	     			for(int i = 0; i < ssls.size(); i++){
	     				boolean match = false;
	     				for(int j = 0; j < gems.size(); j++){
	     					if(gems.get(j).getBfs() == ssls.get(i).getSitz())
	     						match = true;
	     				}
	     				if(match == false){
	     				if(ssls.get(i).getID() == sslid){
	     					ssls.remove(ssls.get(i));
	     				}
	     				}
	     			}
	     		}
	      	/* Abarbeitung der folgenden Abfrage, falls 'del ABR' eingegeben wird. */
	      	} else if (discriminator[0] == "ABR" || discriminator[0].equals(ABR.DISCRIMINATOR)) {
	     	
	      		if(discriminator.length == 1){
	     			for(int i = 0; i < abrs.size(); i++){
	     	
	     					abrs.remove(i);
	     				
	     			}
	     			/* Abarbeitung der folgenden Abfrage, falls 'del ABR id' eingegeben wird. */	
	     		} else if(discriminator[1].equals("id")){
	     			if(discriminator.length != 3){
	     				System.out.println("Keine ID eingegeben. Bitte geben Sie einen Befehl im Format ABR ID <ID> ein");
	     				waitforInput(new String[0]);
	     			}
	     			
	     			int abrid = 0;
     				try {
     					abrid = Integer.parseInt(discriminator[2]);
     				} 
     				catch (RuntimeException re) {
     					System.out.println("ABR ID hat kein valides Format");
     					waitforInput(new String[0]);
     				}
	     			
	     			for(int i = 0; i < abrs.size(); i++) {
	     				if(abrs.get(i).getID() == abrid) {
	     					abrs.remove(abrs.get(i));
	     				}
	     			}
	     		}
	      	}	else {
	            System.out.println("Parsing error. Kein g�ltiger Discriminator: " + discriminator);
	     	}
	   }
		
		//ToDo: Pr�fung f�r alle Argumente WAS IST DAMIT GEMEINT??
		
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
	        
	        //Qups und Abrs nach ID sortieren, so dass der Vergleich schneller gemacht werden kann
	        //!!! pr�fen ob das am Schluss wirklich gebraucht wird -> w�hre f�r performance Steigerung
	        //Collections.sort(qups, QUP.QUP_id);
	        //Collections.sort(abrs, ABR.ABR_bfs);
	        
	     	if (discriminator[0].equals("BUND")) {
	     		
	     		if((discriminator.length > 2)||(discriminator.length < 1)){
	     			System.out.println("falsche Anzahl von Argumenten f�r BUND angegeben");
	     			waitforInput(new String[0]);
	     		}

	     		double SummeBruttolohn = 0.0;
	     		double QuellensteuerBund = 0.0;
	     		
	     		//Falls ein Jahr angegeben wurde wird dieses in die Year Variabel gespeichert. Ansonsten beh�lt die Variabel den Wert 0
	     		if(discriminator.length == 2){
	     			try{
	     				year = Integer.parseInt(discriminator[1]);
	     			} catch(RuntimeException re){
	     				System.out.println("Kein g�ltiges Jahresformat angegeben");
	     				waitforInput(new String[0]);
	     			}
	     		}
	     		
	     		//Durch Alle Arechnungen iterieren, mit ausgew�hlten Jahr vergleichen und Bruttolohn zusammenz�hlen
	     		//Ist kein Jahr gew�hlt werden alle L�hne unabh�ngig vom Jahr zusammengez�hlt, da Variabel Year den Wert 0 enth�lt
	     		for(int i = 0; i < abrs.size(); i++){
	     			if((abrs.get(i).getJahr() == year)||(year == 0)){
	     				SummeBruttolohn = SummeBruttolohn + abrs.get(i).getBruttolohn();
	     			}
	     		}
	     		
	     		System.out.println("Summe Bruttolohn: " + SummeBruttolohn);
	     		
	     		QuellensteuerBund = SummeBruttolohn * 0.01;
	     		
	     		System.out.println("Quellensteuer Bund: " + QuellensteuerBund);
	     		
	     	} else if(discriminator[0].equals("KANT")){
	     		
	     		double SummeBruttolohnQUP = 0.0;
	     		double SummeBruttolohnSSL = 0.0;
	     		double SummeBruttolohnABR = 0.0;
	     		double QuellensteuerKanton = 0.0;
	     		double vermSteuersatz;
	     		
	     		if((discriminator.length > 3)||(discriminator.length < 2)){
	     			System.out.println("falsche Anzahl von Argumenten f�r KANT angegeben");
	     			waitforInput(new String[0]);
	     		}
	     		
	     		String KantID = discriminator[1];
	     		
	     		//Pr�fen ob ein Jahr migegeben wurde und dieses speichern
	     		if(discriminator.length == 3){
	     			try{
	     				year = Integer.parseInt(discriminator[2]);
	     			} catch(RuntimeException re){
	     				System.out.println("Kein g�ltiges Jahresformat angegeben");
	     				waitforInput(new String[0]);
	     			}
	     		}
	     		
	     		//Durch alle Abrechnungen iterieren
	     		for(ABR a : abrs){
	     			if((a.getJahr() == year)||(year == 0)){
		     			for(Gemeinde g: gems){
		     				if(g.getKantonId().equals(KantID)){ //Pr�fen ob der Kanton angewendet wird
		     					//Abz�ge f�r Kinder f�r diese ABR berechnen
			     				vermSteuersatz = vermSatz( g.steuerKanton(), a.getQup().getKinder() );
		     					//Durch alle Gemeinden iterieren und die Gemiende heraussuchen, welche zum QUP der Abrechnung geh�rt
		     					if(a.getQup().getWohnort() == g.getBfs()){
		     						SummeBruttolohnQUP = SummeBruttolohnQUP + a.getBruttolohn();
		     						QuellensteuerKanton = QuellensteuerKanton + a.getBruttolohn() * vermSteuersatz;
		     					} else if(!a.getQup().isAnsaessig()){ //Wenn keine Gemeinde f�r die QUP gefunden wurde, wird gepr�ft ob der QUP einen Schweizer Wohnisitz hat
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
	     		
	     		System.out.println( "Summe Bruttolhon der QUP f�r den Kanton: " + SummeBruttolohnQUP );
	     		System.out.println( "Summe Bruttolohn der SSL f�r den Kanton (wenn QUP keinen Wohnsitz hat): " + SummeBruttolohnSSL );
	     		System.out.println( "Summe Bruttolhon der QUP und SSL: " + SummeBruttolohnABR );
	     		System.out.println( "Quellen Steuer f�r den Kanton " + KantID + ": " + QuellensteuerKanton );
	     	
	     	} else if(discriminator[0].equals("GEM")){
	     		double SummeBruttolohnQUP = 0.0;
	     		
	     		if((discriminator.length > 3)||(discriminator.length < 2)){
	     			System.out.println("falsche Anzahl von Argumenten f�r GEM angegeben");
	     			waitforInput(new String[0]);
	     		}
	     		
	     		int bfs = Integer.parseInt(discriminator[1]);
	     		
	     		//Pr�fen ob ein Jahr migegeben wurde und dieses speichern
	     		if(discriminator.length == 3){
	     			try{
	     				year = Integer.parseInt(discriminator[2]);
	     			} catch(RuntimeException re){
	     				System.out.println("Kein g�ltiges Jahresformat angegeben");
	     				waitforInput(new String[0]);
	     			}
	     		}
	     		
	     		//Durch alle Abrechnungen iterieren
	     		for(ABR a : abrs){
	     			//Pr�fen ob im gew�hlten Jahr oder ob kein Jahr gew�hlt
	     			if((a.getJahr() == year)||(year == 0)){
	     				//Pr�fen ob Wohnort f�r Abrechnung korrekt
		     			if(a.getQup().getWohnort() == bfs){
		     				SummeBruttolohnQUP = SummeBruttolohnQUP + a.getBruttolohn();
		     			}
	     			}
	     		}
	     		
	     		System.out.print("Summe aller Bruttol�hne in Gemeinde " + bfs );
	     		System.out.print((year == 0) ? " f�r alle Jahre" : " f�r das Jahr " + year);
	     		System.out.println(": " + SummeBruttolohnQUP);
	     		
	      	}else {
	            System.out.println("Parsing error. Kein g�ltiger Discriminator: " + discriminator);
	     	}
	   }
		
	}
	
	private static double vermSatz(double steuersatz, int kinder){
		
		//Obergrenze von 9 Kindern festlegen
		if(kinder > 9)
			kinder = 9;
		
		//F�r jedes Kind 5 Prozent abziehen
		for(int i = 0; i < kinder; i++){
			steuersatz = steuersatz * 0.95;
		}
		
		return steuersatz;
	}

	/* Methode: stat()
	 * Anhand des Discriminators werden die jeweiligen Datens�tze angezeigt. */
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
					
					/*
					 * Test von Ausgabe der BFS Nummer um Kanton zu erhalten
					 */
					// 	private static List<Gemeinde> gems = new LinkedList<Gemeinde>();
//					for ( Gemeinde m : gems ) {
//						int p = m.getBfs();
//						System.out.println( p );
//					}
					
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
					if (discriminator[1].equals("k")) {
						Collections.sort(abrs, ABR.ABR_id); // muss noch implementiert werden, daher vorerst ABR_id damit kein Fehler kommt
						System.out.println(abrs.toString());
					}
				}
				
			} else {
				System.out.println("Parsing error. Kein g�ltiger Discriminator: " + discriminator);
			}
		}
	}

	
	/* Methode: help()
	 * Zeigt die Hilfe zum Programm an. */
	private static void help() {
		/* 'QUP: qup_id; name; vorname; wohnort(bfs_nr); ...' (bei nicht vorhandener qup_id wird diese automatisch vergeben)\n"
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
				+ "del  : L�schen von Datens�tzen:\n"
				+ "   del GEM     : L�schen aller Gemeinden (auf die keine Beziehungen existieren)\n"
				+ "   del GEM bfs : L�schen der Gemeinde (falls keine Beziehung darauf existiert)\n"
				+ "   del QUP     : L�schen aller QUPs (auf die keine Beziehung existieren)\n"
				+ "   del QUP id  : L�schen des QUPs (falls keine Beziehung darauf existiert)\n"
				+ "   del SSL     : L�schen aller SSLs (auf die keine Beziehung existieren)\n"
				+ "   del SSL id  : L�schen des SSLs (falls keine Beziehung darauf existiert)\n"
				+ "   del ABR     : L�schen aller ABRs.\n"
				+ "   del ABR id  : L�schen des ABRs\n"
				+ "stat : Statistik\n"
				+ "   stat BUND jahr : Bundesstatistik f�r das Jahr\n"
				+ "            Summe Bruttolohn aller QUPs und Summe Quellensteuerbetrag f�r Bund\n"
				+ "   stat KANT id   : Kantonsstatistik f�r das Jahr \n"
				+ "            Summe Bruttol�hne aller im Kanton ans�ssigen QUPs\n"
				+ "            Summe Bruttol�hne aller QUPs, deren SSL im Kanton ans�ssig ist\n"
				+ "            Summe Bruttol�hne aller QUPs, von deren ABR Quellensteuer an den Kanton abgef�hrt wird\n"
				+ "            Summe Quellensteuerbetrag, den Kanton erh�lt\n"
				+ "   stat GEM bfs jahr  : Gemeindestatistik der Gemeinde f�r das Jahr\n"
				+ "            Summe Bruttolohn aller ABRs von QUPs in dem Jahr mit Wohnsitz in Gemeinde\n"
				+ "            Summe Bruttolohn aller ABRs von SSLs in dem Jahr mit Sitz in Gemeinde\n"
				+ "            Betrag Quellensteuer, den die Gemeinde in dem Jahr erh�lt\n"
				+ "   stat QUP id  jahr  : Individuelle Statistik eines QUPs f�r das Jahr\n"
				+ "            Bruttojahreslohn des QUP, Quellensteuer und wieviel Quellensteuer Bund, Kanton(e) und Gemeinde(n) erhalten\n"
				+ "   stat SSL id  jahr  : Individuelle Statistik eines SSL f�r das Jahr\n"
				+ "            Summe Bruttol�hne aller QUPs, deren ABRs beim SSL erfolgen, sowie Summe Quellensteuer mit Aufteilung an Bund, Kantone und Gemeinden."

		;
		System.out.println(s);*/
	}

}
