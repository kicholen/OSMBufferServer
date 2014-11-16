package pwr.osm.buffer.threads;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import pwr.osm.buffer.util.Log;
import pwr.osm.data.representation.MapPosition;

/**
 * Thread that handles connection with MainServer.
 * @author Sobot
 */
public class FromServerThread implements Callable<List<MapPosition>>{
	
	private List<MapPosition> pointsFromClient;
	
	/**
	 * Constructor.
	 * @param pointsFromClient points to MainServer
	 */
	public FromServerThread(List<MapPosition> pointsFromClient){
		
		this.pointsFromClient = pointsFromClient;
	}
	
	@Override
	public List<MapPosition> call(){
		
		List<MapPosition> pointsFromServer=new ArrayList<MapPosition>();
		Log log = new Log();
		
		try
		{
			pointsFromServer = HandleConnectionToServer();
		}
		catch (Exception e)
		{
			log.error("Exception occured: " + e.getMessage());
		}
		return pointsFromServer;
	}

	/**
	 * Sends points from client to MainServer and receives the path.
	 * @return points received from MainServer
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private List<MapPosition> HandleConnectionToServer() throws Exception{
		
        List<MapPosition> pointsFromServer = new ArrayList<MapPosition>();       
		Socket serverTCPSocket = new Socket("localhost", 6789);	 // Server pod portem 6789
		ObjectOutputStream output = new ObjectOutputStream(serverTCPSocket.getOutputStream());
		ObjectInputStream input = new ObjectInputStream(serverTCPSocket.getInputStream());
		
		output.writeObject(pointsFromClient);	
		pointsFromServer = (List<MapPosition>)input.readObject();
		serverTCPSocket.close();

		return pointsFromServer;
		
	}
}
