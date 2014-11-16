package pwr.osm.buffer.server;

import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * BufforServer that handels packets received from Clients.
 * @author Sobot
 *
 */
class BufferServer
{
	public final static int PORT = 9876;
	
	public static void main(String args[]) throws Exception
	{
		ExecutorService execService = Executors.newFixedThreadPool(10);
		Log log = new Log();
		log.onStart();
		System.out.println("SERVER WORKING");
		System.out.println("waiting for packets...");
		@SuppressWarnings("resource")
		DatagramSocket serverUDPSocket = new DatagramSocket(PORT);   // BufferServer port: 9876
        while(true)
    	{	
	      	byte[] receiveData = new byte[5000];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverUDPSocket.receive(receivePacket);
			System.out.println("Got package, creating threads to handle");
			log.info("Got package, creating thread to handle");		
			execService.execute(new RequestHandler(receivePacket, receiveData));
       }
	}
}
