package pwr.osm.buffer.server;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import pwr.osm.buffer.threads.ConnectionThread;
import pwr.osm.buffer.threads.DbAddThread;
import pwr.osm.buffer.threads.ReplyThread;
import pwr.osm.data.representation.MapPosition;

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
		ExecutorService execSendToServerService = Executors.newFixedThreadPool(10);
		ExecutorService execSendToClientService = Executors.newFixedThreadPool(10);
		ExecutorService execAddToDbService = Executors.newFixedThreadPool(10);
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
    			// receiving from Client	  
				ByteArrayInputStream inputStream = new ByteArrayInputStream(receiveData);
				ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(inputStream));
				@SuppressWarnings("unchecked")
				List<MapPosition> geoPoints = (List<MapPosition>) is.readObject();
    			System.out.println("From Client: " + geoPoints);
    			log.info("Got package, creating thread to handle");
    			// sending to MainServer
    			log.info("message sent to SERVER");
    			Future<List<MapPosition>> points = execSendToServerService.submit(new ConnectionThread(geoPoints));
    			log.info("message received from SERVER");
    			// sending back to Client
    			execSendToClientService.execute(new ReplyThread(
    					points.get(), receivePacket.getAddress(), receivePacket.getPort()));
    			log.info("path sent to Client");
    			execAddToDbService.execute(new DbAddThread(points.get()));
    			log.info("path added to Db");
           }
	}
}
