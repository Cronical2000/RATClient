package com.cronical.client;

import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;
import javax.swing.ImageIcon;
import com.cronical.client.network.packet.Command;
import com.cronical.client.network.packet.ImagePacket;
import com.cronical.client.network.packet.Packet;
import com.cronical.client.network.packet.RectangleObject;

import com.cronical.client.network.packet.Response;
import com.cronical.client.network.packet.SystemName;
import com.cronical.client.network.packet.Tasks;
import com.cronical.client.network.packet.TaskKillName;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;

public class MyClient implements Runnable {

	private int TCP_PORT, UDP_PORT, TIMEOUT;	
	private String ip = "127.0.0.1";
	private PacketHandler pHandler;
	private Kryo kryo;
	//TODO wirklich notwendig? @serverip
	private String serverIp;
	private Thread t;
	
	public Client client;

	/**
	 * Constructor of MyClient
	 * @param int tcp, int udp, int timeout 
	 */
	public MyClient(int tcp, int udp, int timeout) {
	
		this.TCP_PORT = tcp;
		this.UDP_PORT = udp;
		this.TIMEOUT = timeout;
		
		client = new Client(930000,930000); //Creates a new Client
		client.start();
		System.out.println("[Client]: Client started");
		kryo = client.getKryo();		
		registerKryoClasses();		
		pHandler = new PacketHandler(this);
		connect(ip);
	}

	/**
	 * Client connecting to the Server with Port in a new Thread
	 * @param String ServerIp
	 */
	public void connect(String serverIp) {
		this.serverIp = serverIp;		
		
		try {
			//Log.TRACE();
			client.connect(TIMEOUT, serverIp, TCP_PORT, UDP_PORT); //Kryonet connect method
			System.out.println("[Client]: Client connected to the Server");
			client.addListener(new MyClientListener(this));
			System.out.println("[Client]: Client added MyClientListener (Listen to the Packets from the Server now!)");
			t = new Thread(this);
			t.start();
			System.out.println("[Client]: Thread started MyClient");			
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SystemName SysName = new SystemName();
		SysName.systemName = System.getProperty("user.name");
		System.out.println("[Client]: Send SystemName to the Server --> " +  System.getProperty("user.name"));
		client.sendTCP(SysName);	
	}

	/**
	 * Disconnects the client
	 */
	public void disconnect() {
		client.stop();
	}
	
	/**
	 * Thread hold the connection
	 */
	@Override
	public void run() {
		while (true) { //Keep the application running		
			try {
				Thread.sleep(5000); //Check if connected or not every 10 seconds				
				if (!client.isConnected())
					System.out.println("[Client]: Client is not connected to the Server!");
					Thread.sleep(5000);					
					if(!client.isConnected()){									
						try {
							System.out.println("[Client]: Try to reconnect to the Server");
							client.connect(TIMEOUT, serverIp, TCP_PORT, UDP_PORT);
							SystemName SysName = new SystemName();
							SysName.systemName = System.getProperty("user.name");
							client.sendTCP(SysName);
						} 
						catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}								
					}
			} 
			catch (InterruptedException e) {
				//TODO pHandler.checkException(e);
			}
		}
	}
		
	/**
	 * Registers Packets from com.cronical.client.network.packet
	 * The Packets have to be in the same order as the client side
	 */	
	private void registerKryoClasses() {
		//Super Classes
		kryo.register(String[].class);
		System.out.println("[Client]: String[].class registered");
		kryo.register(String.class);
		System.out.println("[Client]: String.class registered");
		kryo.register(Rectangle.class);
		System.out.println("[Client]: Rectangle.class registered");
		kryo.register(ImageIcon.class);
		System.out.println("[Client]: ImageIcon.class registered");
		kryo.register(Packet.class);
		System.out.println("[Client]: Packet.class registered");
		kryo.register(Image.class);
		System.out.println("[Client]: Image.class registered");
		kryo.register(byte[].class);
		System.out.println("[Client]: byte[].class registered");
			
		//Packets
		kryo.register(Response.class);
		System.out.println("[Client]: Response.class registered");
		kryo.register(SystemName.class);	
		System.out.println("[Client]: SystemName.class registered");
		kryo.register(Tasks.class);
		System.out.println("[Client]: Tasks.class registered");
		kryo.register(RectangleObject.class);	
		System.out.println("[Client]: RectangleObject.class registered");
		kryo.register(ImagePacket.class);
		System.out.println("[Client]: ImagePacket.class registered");
		kryo.register(Command.class);
		System.out.println("[Client]: Command.class registered");
		kryo.register(TaskKillName.class);
		System.out.println("[Client]: TaskKillName.class registered");
		
		System.out.println("[Client]: All Kryo Classes registered");
	}

	/**
	 * Gets the packethandler from the instance MyClient (clientlistener using this)
	 */	
	public PacketHandler getpHandler(){
		return pHandler;
	}	
}