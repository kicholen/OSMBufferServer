package pwr.osm.buffer.threads;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import pwr.osm.buffer.db.DbPath;
import pwr.osm.buffer.util.HibernateUtil;

/**
 * Thread for clearing old paths from database.
 * @author Sobot
 *
 */
public class DbClearThread implements Runnable{

	private int timeLimit;
	private int timesUsed;

	/**
	 * Constructor.
	 * @param timeLimit path deleted id older than timeLimit
	 * @param timesUsed if used more times timesUsed than path is not deleted
	 */
	public DbClearThread(int timeLimit, int timesUsed){
		this.timeLimit = timeLimit;
		this.timesUsed = timesUsed;
	}
	
	/**
	 * Removes paths older than timeLimit parameter.
	 */
	@Override
	public void run() {
        SessionFactory sf = HibernateUtil.createSessionFactory();
        while(true)
        {
        	Date time = new Date();
	        Session session = sf.openSession();
	        session.beginTransaction();
	        
	        Query query = session.createQuery("from DbPath");
	    	@SuppressWarnings("unchecked")
			List<DbPath> result = query.list();
			for(DbPath dbPath : result)
			{
				if((time.getTime() - dbPath.getAddDate().getTime()) > timeLimit*60000
						&& dbPath.getTimesUsed() <= timesUsed)
					session.delete(dbPath);
			}
	        session.getTransaction().commit();
	        session.close();
        	try {
				Thread.sleep(timeLimit*60000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
		
	}

}
