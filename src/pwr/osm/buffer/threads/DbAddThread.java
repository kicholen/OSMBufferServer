package pwr.osm.buffer.threads;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import pwr.osm.buffer.db.DbMapPosition;
import pwr.osm.buffer.db.DbPath;
import pwr.osm.data.representation.MapPosition;
import pwr.osm.buffer.util.HibernateUtil;

/**
* Thread for adding calculated path to database.
* @author Sobot
*
*/
public class DbAddThread implements Runnable{
	
	private List<MapPosition> pointsFromServer;

	public DbAddThread(List<MapPosition> pointsFromServer){
		
		this.pointsFromServer = pointsFromServer;
	}
	
	@Override
	public void run(){
		
		addToDb();
	}
	
	/**
	 *	Adds calculated path to database.
	 */
	private void addToDb() {
		
        SessionFactory sf = HibernateUtil.createSessionFactory();
        Session session = sf.openSession();
        session.beginTransaction();
        
        DbPath path = new DbPath(pointsFromServer.get(0).getLatitude(),
        		pointsFromServer.get(0).getLongitude(),
        		pointsFromServer.get(pointsFromServer.size()-1).getLatitude(),
        		pointsFromServer.get(pointsFromServer.size()-1).getLongitude());
        path.setDbPath(new ArrayList<DbMapPosition>());
        
        for(MapPosition mp : pointsFromServer)
        {
        	path.getDbPath().add(new DbMapPosition(mp.getLatitude(), mp.getLongitude()));
        }
        session.save(path);
        session.getTransaction().commit();
        session.close();
	}

}
