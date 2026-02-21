package com.cronical.client;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import com.cronical.client.capture.StartCapture;
import com.cronical.client.capture.control.ControlClient;
import com.cronical.client.network.packet.*;
import com.esotericsoftware.kryonet.Connection;

public class PacketHandler {
	
	private MyClient myC;
	private Robot robot;
	
	public PacketHandler(MyClient myC) {		
		this.myC = myC;				
		
		try {
			this.robot = new Robot();
		} 
		catch (AWTException e) {
			e.printStackTrace();
		}
	}

	/**
	 * The listener is sending objects to handleOrder. handleOrder sort the object 
	 * comparing the string in the object
	 * @param Response order
	 */
	public void handleOrder(Response order) {
		String response = order.response;
		
		try {
			if (response.equalsIgnoreCase("gettasks")) { // Wenn Tasks mit dem String get tasks angefragt werden
				System.out.println(order.response);
				System.out.println("Client: Handle Packet ");
				ArrayList<String> lines = new ArrayList<String>();
				String line = "";
				Process p = Runtime.getRuntime().exec(new String[]{System.getenv("windir") + "\\system32\\tasklist.exe"});
				try (BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
					while ((line = input.readLine()) != null) // Lese die Tasks bis keine Zeichen mehr eingelesen werden
						lines.add(line);
				}
				String[] tasklist = new String[lines.size()]; // Erstelle ein Array mit der groesse des ArrayList
				for (int i = 0; i < lines.size(); i++) {
					String taskLine = lines.get(i); // Task auslesen
					if (taskLine.contains(" ")) 
						taskLine = taskLine.substring(0, taskLine.indexOf(" ")); // schneidet den String ab dem ersten leerzeichen
					if (taskLine.contains(".exe")) // Nur wenn .exe add Task
						tasklist[i] = taskLine;
					else // ansonsten setze den index auf null
						tasklist[i] = "null";
				}
				Arrays.sort(tasklist); // Sortiere die Tasks Alphabetisch
				Tasks tsk = new Tasks(); // Erstelle neues Task Objekt
				tsk.tasks = tasklist; // schreibe in das Task Object die Tasklist
				for(int taskl = 0;taskl < tsk.tasks.length; taskl++){
					System.out.println(tsk.tasks[taskl]);
				}							
				myC.client.sendTCP(tsk); // Versende das Objekt
				System.out.println("Client: Packet handled and sent back ");
			}
			else if(response.equalsIgnoreCase("stopImgSend")){
				StartCapture.cptr.continueLoop = false;
				System.out.println("Client: byte[] Images Loop end ");
			}		
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * The listener is sending objects to handleOrder. handleOrder sort the object with comparing the string in the object
	 * @param Response, Connection
	 */
	public void handleOrder(Response order, Connection connection) {
		String response = order.response;
		Connection con = connection;		
		if( response.equalsIgnoreCase("startCapture")){			
			new StartCapture(con);
		}		
	}

	public void handleCmd(Command cmd, Connection con) {
		int input;
		input = cmd.cmd;
		new ControlClient(con, this.robot, input);		
	}
	
	public void handleTaskKill(String taskToKill){		
			try {
				Runtime.getRuntime().exec(new String[]{"taskkill", "/F", "/IM", taskToKill});
			} catch (IOException e) {
				e.printStackTrace();
			}		
	}
}
