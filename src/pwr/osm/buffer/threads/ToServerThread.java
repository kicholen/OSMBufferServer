package pwr.osm.buffer.threads;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.Callable;

import pwr.osm.buffer.util.Log;
import pwr.osm.connection.Message;
import pwr.osm.data.representation.MapPosition;

/**
 * Thread that handles connection with MainServer.
 * @author Sobot
 */
public class ToServerThread implements Callable<List<MapPosition>>{
	
	private Message message;
	
	/**
	 * Constructor.
	 * @param pointsFromClient points to MainServer
	 */
	public ToServerThread(Message message){
		
		this.message = message;
	}
	
	@Override
	public List<MapPosition> call(){
		
		return HandleConnectionToServer();
	}

	/**
	 * Sends message to MainServer and receives the path.
	 * @return points received from MainServer
	 */
	private List<MapPosition> HandleConnectionToServer() {
		    
		Socket serverTCPSocket;
		ObjectOutputStream output;
		ObjectInputStream input;
		Message messageFromServer = null;
		Log log = new Log();
		
		try {
			serverTCPSocket = new Socket("localhost", 6789);  // Server pod portem 6789
			output = new ObjectOutputStream(serverTCPSocket.getOutputStream());
			input = new ObjectInputStream(serverTCPSocket.getInputStream());
			output.writeObject(message);	
			messageFromServer = (Message)input.readObject();
			serverTCPSocket.close();
			System.out.println(messageFromServer.getData());

		} catch (IOException | ClassNotFoundException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}	
		return messageFromServer.getData();
		
	}
}
