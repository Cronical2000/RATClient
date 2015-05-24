package com.cronical.client.capture;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import com.cronical.client.network.packet.ImagePacket;
import com.cronical.client.network.packet.RectangleObject;
import com.esotericsoftware.kryonet.Connection;

public class Capture extends Thread {
	Robot robot = null;
	Connection con = null;
    Rectangle rectangle = null; //Used to represent screen dimensions
    public boolean continueLoop = true; //Used to exit the capture
  
    public int msgCounter;
    
	/**
	 * Constructor of Capture
	 * send also the rectangle to the server and starting to capture the screen in a 
	 * loop and each loop its sending the byte array to the Server
	 * @param Robot robot, Rectangle rect, Connection con 
	 */
    public Capture(Robot robot,Rectangle rect,Connection con) {
        this.robot = robot;
        this.con = con;       
        rectangle = rect;
        start();
    }

    //Send Rectangle and do the screenshot reading loop
    public void run(){
        RectangleObject rect = new RectangleObject();
        rect.rect = rectangle;
        con.sendTCP(rect);
        //Screenshot reading loop (Caution sysouts only with a Singleton)
        while(continueLoop){        
           BufferedImage bi = robot.createScreenCapture(rectangle);  		
           ByteArrayOutputStream baos = new ByteArrayOutputStream();
           try {
			ImageIO.write(bi, "jpg", baos);
			
           } catch (IOException e) {
			e.printStackTrace();
           }
           
           byte[] bytes = baos.toByteArray();  
           try {
			baos.close();
           } catch (IOException e) {
			e.printStackTrace();
           }
           ImagePacket imgPkt = new ImagePacket();
           imgPkt.image = bytes;
           SingletonMessageSendByteArray.getInstance ();           
           con.sendTCP(imgPkt); //Sending the byte array                     
        }
    }
}

		


