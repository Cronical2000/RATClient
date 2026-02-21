package com.cronical.client.capture.control;

import java.awt.Robot;
import java.util.Scanner;

import com.esotericsoftware.kryonet.Connection;

/*
 * Used to recieve server commands then execute them at the client side
 */
public class ControlClient extends Thread {

    Connection con = null;
    Robot robot = null;
    int input;
    
    
    boolean continueLoop = true;

    public ControlClient(Connection con, Robot robot,int input) {
        this.con = con;
        this.robot = robot;
        this.input = input;
        start(); //Start the thread and hence calling run method
    }

    @SuppressWarnings("null")
	public void run(){
        Scanner scanner = null;                	
        //Socket socket = new Socket(new Proxy(Proxy.Type.SOCKS, con.getRemoteAddressTCP()));
        //socket.connect(socket.get);            	        	
        //prepare Scanner object      	
        System.out.println("Preparing InputStream");
        //scanner = new Scanner(socket.getInputStream());
        System.out.println("getinputstream");
        while(continueLoop){
        	//recieve commands and respond accordingly
            System.out.println("Waiting for command");
            
            if (scanner == null) {
                System.err.println("Scanner is not initialized! Stopping ControlClient loop.");
                break;
            }

            //int command = scanner.nextInt();
            System.out.println("New command: " + input);
                
            switch(input){
                    case -1:
                        if(scanner != null) robot.mousePress(scanner.nextInt());
                    break;
                    case -2:
                        if(scanner != null) robot.mouseRelease(scanner.nextInt());
                    break;
                    case -3:
                        if(scanner != null) robot.keyPress(scanner.nextInt());
                    break;
                    case -4:
                        if(scanner != null) robot.keyRelease(scanner.nextInt());
                    break;
                    case -5:
                        if(scanner != null) robot.mouseMove(scanner.nextInt(), scanner.nextInt());
                    break;
                }
            }
    }
}