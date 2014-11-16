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
import pwr.osm.buffer.threads.FromServerThread;
import pwr.osm.buffer.threads.ReplyThread;
import pwr.osm.data.representation.MapPosition;

public class RequestHandler implements Runnable{
	
	private ExecutorService execService = Executors.newFixedThreadPool(4);
	private DatagramPacket receivePacket;
	private byte[] receiveData;
	private Log log = new Log();

	public RequestHandler(DatagramPacket receivePacket, byte[] receiveData) {
		this.receivePacket = receivePacket;
		this.receiveData = receiveData;
	}		

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
			FromServerThread getFromServerThread = new FromServerThread(pointsFromClient);
			Future<List<MapPosition>> pointsFromDb = execService.submit(dbSearchThread);
			Future<List<MapPosition>> pointsFromServer = execService.submit(getFromServerThread);
			if (pointsFromDb.get() != null){
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
