package pwr.osm.buffer.threads;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

import pwr.osm.buffer.util.Log;
import pwr.osm.data.representation.MapPosition;

/**
 * Thread for sending a path to client.
 * @author Sobot
 */
public class ReplyThread implements Runnable{
	
	private List<MapPosition> pointsToClient;
	private InetAddress IPAddress;
	private int port;

	/**
	 * Constructor.
	 * @param pointsToClient a path for client
	 * @param address reply address
	 * @param port reply port
	 */
	public ReplyThread(List<MapPosition> pointsToClient, InetAddress address, int port){
		
		this.pointsToClient = pointsToClient;
		this.IPAddress = address;
		this.port = port;
	}

	@Override
	public void run(){

		HandlePacket();
	}
	
	/**
	 * Sends calculated path to client.
	 * @throws Exception
	 */
	private void HandlePacket(){
		
		byte[] replyBuff = new byte [5000];
        byte[] sendData = new byte[replyBuff.length];
        DatagramSocket replySocket = null;
		try {
	        replySocket = new DatagramSocket(9875);
		
			String wspolrzedne = "";
			for(MapPosition w : pointsToClient)
				wspolrzedne += Double.toString(w.getLatitude())+"#"
						+Double.toString(w.getLongitude())+"#";
			sendData = wspolrzedne.getBytes();			
			DatagramPacket sendPacket =
					new DatagramPacket(sendData, sendData.length, IPAddress, port);

			replySocket.send(sendPacket);
		} catch (IOException e) {
			Log log = new Log();
			
			log.error(e.getMessage());
			e.printStackTrace();
		}
		replySocket.close();
	}
}
