package pwr.osm.buffer.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import pwr.osm.buffer.server.Log;
import pwr.osm.data.representation.MapPosition;
import pwr.osm.buffer.util.HibernateUtil;
import pwr.osm.buffer.db.DbMapPosition;
import pwr.osm.buffer.db.DbPath;

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
			e.printStackTrace();
			log.error("Exception occured: " + e.getMessage());
		}
		return pointsFromDb;
	}

	private List<MapPosition> SearchDb() {
		
		List<MapPosition> pointsFromDb = null;
		double delta = 0.0005;
        SessionFactory sf = HibernateUtil.createSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();
        
        Query query = session.createQuery("from DbPath");
    	@SuppressWarnings("unchecked")
		List<DbPath> result = query.list();
		for(DbPath dbPath : result)
		{			
			if (pointsFromClient.get(0).getLatitude() - delta < dbPath.getStartLatitude()
					&& dbPath.getStartLatitude() < pointsFromClient.get(0).getLatitude() + delta)
				if (pointsFromClient.get(0).getLongitude() - delta < dbPath.getStartLongitude()
						&& dbPath.getStartLongitude() < pointsFromClient.get(0).getLongitude() + delta)
					if (pointsFromClient.get(pointsFromClient.size()-1).getLatitude() - delta < dbPath.getEndLatitude()
							&& dbPath.getEndLatitude() < pointsFromClient.get(pointsFromClient.size()-1).getLatitude() + delta)
						if (pointsFromClient.get(pointsFromClient.size()-1).getLongitude() - delta < dbPath.getEndLongitude()
								&& dbPath.getEndLongitude() < pointsFromClient.get(pointsFromClient.size()-1).getLongitude() + delta)if (dbPath.getEndLongitude() == pointsFromClient.get(pointsFromClient.size()-1).getLongitude())
						{
							System.out.println("tutaj");
					        Query pathQuery = session.createQuery("from DbMapPosition where path_id = :path_id")
					        		.setParameter("path_id",dbPath.getPathId());
							@SuppressWarnings("unchecked")
							List<DbMapPosition> path = pathQuery.list();
							
							pointsFromDb = new ArrayList<MapPosition>();
					        for(DbMapPosition dbmp : path)
					        {
					        	pointsFromDb.add(new MapPosition(dbmp.getLatitude(), dbmp.getLongitude()));
					        	System.out.println(dbmp.getLatitude());
					        }
					        session.close();
					        return pointsFromDb;
						}
		}
        session.close();
        
		return null;
	}

}
