package com.cronical.client;

public class Main {
	
	public static void main(String[] args) {		
		//Creates a new client with ports and timeout
		new MyClient(28000,28001,5000);	
	}
}
