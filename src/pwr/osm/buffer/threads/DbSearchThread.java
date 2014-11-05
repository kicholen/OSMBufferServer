package pwr.osm.buffer.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import pwr.osm.buffer.server.Log;
import pwr.osm.data.representation.MapPosition;

/**
*	Wątek przeszukujący bazę danych
* 	- łączy sie z bazą serwera
*	- przeszukuje tablicę DbPath w celu sprawdzenia czy
*	  żądana ścieżka nie zostałą wcześniej policzona
*	- jeśli tak, to odsyła do klienta
*/
public class DbSearchThread implements Callable<List<MapPosition>>{
	private List<MapPosition> pointsFromClient;

	public DbSearchThread(List<MapPosition> pointsFromClient){
		
		this.pointsFromClient = pointsFromClient;
	}
	
	@Override
	public List<MapPosition> call(){
		
		List<MapPosition> pointsFromDb=new ArrayList<MapPosition>();
		Log log = new Log();
		
		try
		{
			pointsFromDb = SearchDb();
		}
		catch (Exception e)
		{
			log.error("Exception occured: " + e.getMessage());
		}
		return pointsFromDb;
	}

	private List<MapPosition> SearchDb() {
		// TODO Auto-generated method stub
		return null;
	}

}
