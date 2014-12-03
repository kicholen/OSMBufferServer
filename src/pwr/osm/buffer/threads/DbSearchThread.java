package pwr.osm.buffer.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import pwr.osm.data.representation.MapPosition;
import pwr.osm.buffer.util.HibernateUtil;
import pwr.osm.buffer.util.Log;
import pwr.osm.buffer.db.DbMapPosition;
import pwr.osm.buffer.db.DbPath;

/**
* Thread that checks database for path points.
* @author Sobot
*
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
		double delta = 0.002;
        SessionFactory sf = HibernateUtil.createSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();
        
        Query query = session.createQuery("from DbPath");
    	@SuppressWarnings("unchecked")
		List<DbPath> result = query.list();
		for(DbPath dbPath : result)
		{			
			if (Math.abs(pointsFromClient.get(0).getLatitude() - dbPath.getStartLatitude()) < delta)
				if (Math.abs(pointsFromClient.get(0).getLongitude() - dbPath.getStartLongitude()) < delta)
					if (Math.abs(pointsFromClient.get(pointsFromClient.size()-1).getLatitude() - dbPath.getEndLatitude()) < delta)
						if (Math.abs(pointsFromClient.get(pointsFromClient.size()-1).getLongitude() - dbPath.getEndLongitude()) < delta)
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
					        dbPath.setTimesUsed(dbPath.getTimesUsed()+1);
					        session.getTransaction().commit();
					        session.close();
					        return pointsFromDb;
						}
		}
        session.close();
        
		return null;
	}

}
