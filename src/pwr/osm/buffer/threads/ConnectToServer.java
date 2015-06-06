package pwr.osm.buffer.threads;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import pwr.osm.buffer.util.Log;
import pwr.osm.connection.data.Message;
import pwr.osm.data.representation.MapPosition;

/**
 * Thread that handles connection with MainServer.
 * @author Sobot
 */
public class ConnectToServer{
	
	private Message message;
	
	/**
	 * Constructor.
	 * @param pointsFromClient points to MainServer
	 */
	public ConnectToServer(Message message){
		
		this.message = message;
	}

	/**
	 * Sends message to MainServer and receives the path.
	 * @return points received from MainServer
	 */
	public List<MapPosition> handleConnection() {
		    
		Socket serverTCPSocket;
		ObjectOutputStream output;
		ObjectInputStream input;
		Message messageFromServer = null;
		
		try {
			serverTCPSocket = new Socket("localhost", 6789);  // Server pod portem 6789
			output = new ObjectOutputStream(serverTCPSocket.getOutputStream());
			input = new ObjectInputStream(serverTCPSocket.getInputStream());
			output.writeObject(message);	
			messageFromServer = (Message)input.readObject();
			serverTCPSocket.close();
			System.out.println(messageFromServer.getData());

		} catch (IOException | ClassNotFoundException e) {
			Log log = new Log();
			
			log.error(e.getMessage());
			e.printStackTrace();
			return null;
		}	
		return messageFromServer.getData();
		
	}
}
