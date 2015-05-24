package com.cronical.client.capture;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import com.esotericsoftware.kryonet.Connection;

public class StartCapture extends Thread{
	
	private Connection con;
	private Rectangle rectangle = null; //Used to represent screen dimensions
	
	public static Robot robot = null; //Used to capture the screen    
    public static Capture cptr;
    	
	public StartCapture(Connection con){
		this.con = con;
		start();
	}
		
	public void run(){
    
		try {      
			//Get default screen device
			GraphicsEnvironment gEnv=GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice gDev=gEnv.getDefaultScreenDevice();

			//Get screen dimensions
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			rectangle = new Rectangle(dim);

			//Prepare Robot object
			robot = new Robot(gDev);
			//Capture is sending screenshots to the server
			cptr = new Capture(robot,rectangle,con);
			//TODO
			//ServerDelegate recieves server commands and execute them
			//new ControlClient(con,robot);     
		} 
		catch (AWTException ex4) {
            ex4.printStackTrace();
		}
	}
	
	public Capture getCapture(){
		return cptr;
	}
}


