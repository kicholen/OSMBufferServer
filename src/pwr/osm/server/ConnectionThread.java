//==============================================================================
//	Wątek obsługujący paczke od jednego klienta
//  - łączy sie z Serwerem TCP
//	- odpowiedź z Serwera przesyła klientowi
//==============================================================================
package pwr.osm.server;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;

import pwr.osm.data.representation.MapPosition;


public class ConnectionThread implements Runnable{
	private List<MapPosition> geoPoints;
	private InetAddress IPAddress;
	private int port;
	private Log log = new Log();
	
	public ConnectionThread(List<MapPosition> geoPoints, InetAddress address, int port)
	{
		this.geoPoints = geoPoints;
		this.IPAddress = address;
		this.port = port;
	}
	
	public void run(){
		try
		{
			HandlePacket();
		}
		catch (Exception e)
		{
			log.error("Exception occured: " + e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	private void HandlePacket() throws Exception{
		byte[] replyBuff = new byte [5000];
        byte[] sendData = new byte[replyBuff.length];
        DatagramSocket replySocket = new DatagramSocket();
        
		Socket serverTCPSocket = new Socket("localhost", 6789);	// Server pod portem 6789
		
		try (
				ObjectOutputStream output = new ObjectOutputStream(serverTCPSocket.getOutputStream());
				ObjectInputStream input = new ObjectInputStream(serverTCPSocket.getInputStream());
				ByteArrayOutputStream byteStream = new ByteArrayOutputStream(5000);
				ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(byteStream));
		){
			output.writeObject(geoPoints);
			log.info("message sent to SERVER");
			
	        List<MapPosition> newPoints = (List<MapPosition>)input.readObject();
	        System.out.println("[SERVER] Received data: " + newPoints);
			log.info("got message from SERVER");
			serverTCPSocket.close();
			
			os.flush();
			os.writeObject(newPoints);
			os.flush();
			sendData = byteStream.toByteArray();
			DatagramPacket sendPacket =
			new DatagramPacket(sendData, sendData.length, IPAddress, port);
			replySocket.send(sendPacket);
			log.info("message sent to Client");
			replySocket.close();
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
	}
}
