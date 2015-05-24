package com.cronical.client.capture;

public class SingletonMessageSendByteArray {
	 
	  // Eine (versteckte) Klassenvariable vom Typ der eigenen Klasse
	  private static SingletonMessageSendByteArray instance;

	  // Verhindere die Erzeugung des Objektes ueber andere Methoden
	  private SingletonMessageSendByteArray () {
		  System.out.println("[Client]: byte[] (Image) send to the server now (loop)");
	  }

	  // Eine Zugriffsmethode auf Klassenebene, welches dir einmal ein konkretes 
	  // Objekt erzeugt und dieses zurueckliefert.
	  public static SingletonMessageSendByteArray getInstance () {
	    if (SingletonMessageSendByteArray.instance == null) {
	    	SingletonMessageSendByteArray.instance = new SingletonMessageSendByteArray ();
	    }
	    return SingletonMessageSendByteArray.instance;
	  }
}

