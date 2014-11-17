package pwr.osm.buffer.server;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import pwr.osm.buffer.threads.DbAddThread;
import pwr.osm.buffer.threads.DbSearchThread;
import pwr.osm.buffer.threads.ToServerThread;
import pwr.osm.buffer.threads.ReplyThread;
import pwr.osm.buffer.util.Log;
import pwr.osm.connection.Information;
import pwr.osm.connection.Message;
import pwr.osm.data.representation.MapPosition;

/**
 * Handles packet from client.
 * @author Sobot
 *
 */
public class RequestHandler implements Runnable{
	
	private ExecutorService execService = Executors.newFixedThreadPool(4);
	private long id;
	private DatagramPacket receivePacket;
	private byte[] receiveData;
	private Log log = new Log();

	/**
	 * Constructor.
	 * @param receivePacket datagramPacket
	 * @param receiveData data from client
	 */
	public RequestHandler(long id, DatagramPacket receivePacket, byte[] receiveData) {
		this.id = id;
		this.receivePacket = receivePacket;
		this.receiveData = receiveData;
	}		

	/**
	 * Creates and coordinates threads needed to handle client request.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void run() {	  
		ByteArrayInputStream inputStream = new ByteArrayInputStream(receiveData);
		ObjectInputStream is = null;
		try {
			is = new ObjectInputStream(new BufferedInputStream(inputStream));
			List<MapPosition> pointsFromClient = (List<MapPosition>) is.readObject();

			System.out.println("From Client: " + pointsFromClient);
			log.info("message sent to SERVER");
			DbSearchThread dbSearchThread = new DbSearchThread(pointsFromClient);
			ToServerThread getFromServerThread = new ToServerThread(new Message(id, Information.FIND_WAY, pointsFromClient));
			Future<List<MapPosition>> pointsFromDb = execService.submit(dbSearchThread);
			Future<List<MapPosition>> pointsFromServer = execService.submit(getFromServerThread);
			if (pointsFromDb.get() != null){
				execService.submit(new ToServerThread(new Message(id, Information.WAY_IS_ALREADY_FOUND, null)));
				execService.execute(new ReplyThread(pointsFromDb.get(), receivePacket.getAddress(), receivePacket.getPort()));
				System.out.println("Sending path to Client");
				log.info("Sending path to Client");
			}
			else{
				execService.execute(new ReplyThread(
						pointsFromServer.get(), receivePacket.getAddress(), receivePacket.getPort()));
				System.out.println("Sending path to Client");
				log.info("Sending path to Client");
				execService.execute(new DbAddThread(pointsFromServer.get()));
				System.out.println("Adding path to Db");
    			log.info("Adding path to Db");
				}
			}
			catch (IOException | InterruptedException | ExecutionException | ClassNotFoundException e) {
				log.error(e.getMessage());
				e.printStackTrace();
			}
			finally {
				execService.shutdown();
			}
	}
}
