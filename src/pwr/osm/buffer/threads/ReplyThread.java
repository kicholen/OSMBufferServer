package pwr.osm.buffer.threads;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;

import pwr.osm.buffer.server.Log;
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
		
		Log log = new Log();
		
		try
		{
			HandlePacket();
		}
		catch (Exception e)
		{
			log.error("Exception occured: " + e.getMessage());
		}
	}
	
	/**
	 * Sends calculated path to client.
	 * @throws Exception
	 */
	private void HandlePacket() throws Exception{
		
		byte[] replyBuff = new byte [5000];
        byte[] sendData = new byte[replyBuff.length];
        DatagramSocket replySocket = new DatagramSocket();
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream(5000);
		ObjectOutputStream objectOutStream = new ObjectOutputStream(new BufferedOutputStream(byteStream));
	
		objectOutStream.flush();
		objectOutStream.writeObject(pointsToClient);
		objectOutStream.flush();
		sendData = byteStream.toByteArray();
		DatagramPacket sendPacket =
		new DatagramPacket(sendData, sendData.length, IPAddress, port);
		replySocket.send(sendPacket);
		replySocket.close();
	}
}
