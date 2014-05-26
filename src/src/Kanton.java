/*
Klasse:
Beschreibung: 

*/



public enum Kanton {
   AG, AI, AR, BE, BL, BS, FR, GE, GL, GR, JU, LU, NE, NW, OW, SG, SH, SO, SZ, TG, TI, UR, VD, VS, ZG, ZH;
   
   private String[] longNames = { "Aargau", "Appenzell Innerhoden", "Appenzell Ausserhoden", "Basel-Landschaft", 
		   "Basel-Stadt", "Bern", "Fribourg", "Genf", "Glarus", "Graubünden", "Jura", "Luzern", "Neuenburg",
		   "Nidwalden", "Obwalden", "Schaffhausen", "Schwyz", "Solothurn", "St. Gallen", "Thurgau", "Tessin",
		   "Uri", "Wallis", "Waadt", "Zug", "Zürich"};
   private Double[] kantSteuer = { 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 6.0, 5.0, 5.0, 6.0, 5.0, 5.0, 5.0, 5.0,
		   5.0, 5.0, 5.0, 4.0, 5.0, 5.0, 5.0, 5.0, 6.0, 4.0, 5.0 };
   
   public String toString() {
      return longNames[this.ordinal()];
   }
   public double steuerSatz() {
      return kantSteuer[ this.ordinal() ];
   }
}
