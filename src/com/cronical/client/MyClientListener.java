package com.cronical.client;

import com.cronical.client.network.packet.Command;
import com.cronical.client.network.packet.Packet;
import com.cronical.client.network.packet.Response;
import com.cronical.client.network.packet.TaskKillName;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class MyClientListener extends Listener {

	private MyClient myC;
	
	/**
	 * Constructor of MyClientListener which needs the instance of MyClient to get the control 
	 * of pHandler (PacketHandler).
	 * Also have the received method to listen to packets from the server
	 * @param MyClient myC
	 */
	public MyClientListener(MyClient myC){
		this.myC = myC;		
	}
	
	public void received(Connection connection, Object object) {		
		if (object instanceof Packet) {			
			if (object instanceof Response) {
				Response response = (Response) object;//cast			
				if(response.response.equalsIgnoreCase("gettasks")){ //Receive gettasks Response Object and send it to handler		
					System.out.println(response.response);				
					myC.getpHandler().handleOrder(response);				
					System.out.println("[Client]: TSK Packet received and sent to Handler");
				}				
				if (response.response.equalsIgnoreCase("startCapture")){ //Receive startCapture Response Object and send it to handler										
					myC.getpHandler().handleOrder(response,connection);				
					System.out.println("[Client]: Screen Capture Response Packet received and sent to Handler");
				}
				else if (response.response.equalsIgnoreCase("stopImgSend")){ //Receive stopImgSend Response Object send it to handler and in the handler set the boolean of the loop to false of the capture method 
					myC.getpHandler().handleOrder(response);
					System.out.println("[Client]: StopImgSend Packet received and sent to Handler");					
				}				
			}						
			if(object instanceof Command){
				//TODO Command ertellen
				Command cmd = (Command) object;
				//MyClient.pHandler.handleCmd(cmd,connection);
				myC.getpHandler().handleCmd(cmd,connection);
			}			
			if(object instanceof TaskKillName){
				TaskKillName tskKill = (TaskKillName) object;
				String taskToKill = tskKill.task; 
				myC.getpHandler().handleTaskKill(taskToKill);				
			}
		}
	}
}
