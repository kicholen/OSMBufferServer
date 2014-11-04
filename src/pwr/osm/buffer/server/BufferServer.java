//==============================================================================
//	Serwer WWW
//  - każda paczka od klientów obsługiwana jest przez osobny wątek
//	  po otrzymaniu paczki tworzymy wątek do jej obsługi
//==============================================================================
package pwr.osm.buffer.server;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.List;

import pwr.osm.data.representation.MapPosition;
@SuppressWarnings("resource")
class BufferServer
{
	public final static int PORT = 9876;
	
	@SuppressWarnings("unchecked")
	public static void main(String args[]) throws Exception
	{
		Log log = new Log();
		log.onStart();
		System.out.println("SERVER WORKING");
		System.out.println("waiting for packets...");
		DatagramSocket serverUDPSocket = new DatagramSocket(PORT);   // Server WWW pod portem 9876
        while(true)
        	{	
        		// odbior od klienta
		      	byte[] receiveData = new byte[5000];
    			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
    			serverUDPSocket.receive(receivePacket);			// odbior paczki		  
				ByteArrayInputStream inputStream = new ByteArrayInputStream(receiveData);
				ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(inputStream));
				List<MapPosition> geoPoints = (List<MapPosition>) is.readObject();
    			System.out.println("From Client: " + geoPoints);    // odczyt zdania
    			log.info("Got package, creating thread to handle");
    			// wyslanie na server
    			Runnable r = new ConnectionThread(geoPoints, receivePacket.getAddress(), receivePacket.getPort());
    			new Thread(r).start();
           }
	}
}
