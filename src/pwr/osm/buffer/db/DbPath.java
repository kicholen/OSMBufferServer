package pwr.osm.buffer.db;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

/**
 * For mapping to db table that stores paths points.
 * @author Sobot
 *
 */
@Entity
@Table(name="DBPATH")
public class DbPath {

	@Id
	@GeneratedValue
	@Column(name="path_id")
	private Long pathId;
	
	@Column(name="start_latitude")
	private double startLatitude;
	
	@Column(name="start_longitude")
	private double startLongitude;

	@Column(name="end_latitude")
	private double endLatitude;
	
	@Column(name="end_longitude")
	private double endLongitude;
	
	@OneToMany(cascade={CascadeType.ALL})
    @JoinColumn(name="path_id")
    @OrderColumn(name="idx")
	private List<DbMapPosition> dbPathPoints;
	
	@Column(name="add_date")
	private Date addDate;
	
	@Column(name="times_used")
	private int timesUsed;

	// Constructor
	public DbPath() {
		
	}
	
	public DbPath(double startLatitude, double startLongitude, double endLatitude, double endLongitude) {
		this.startLatitude = startLatitude;
		this.startLongitude = startLongitude;
		this.endLatitude = endLatitude;
		this.endLongitude = endLongitude;
		this.addDate = new Date();
		this.timesUsed = 1;
	}
	
	// Getter and Setter methods
	public Long getPathId() {
		return pathId;
	}

	public void setPathId(Long pathId) {
		this.pathId = pathId;
	}

	public double getStartLatitude() {
		return startLatitude;
	}

	public void setStartLatitude(double startLatitude) {
		this.startLatitude = startLatitude;
	}

	public double getStartLongitude() {
		return startLongitude;
	}

	public void setStartLongitude(double startLongitude) {
		this.startLongitude = startLongitude;
	}

	public double getEndLatitude() {
		return endLatitude;
	}

	public void setEndLatitude(double endLatitude) {
		this.endLatitude = endLatitude;
	}

	public double getEndLongitude() {
		return endLongitude;
	}

	public void setEndLongitude(double endLongitude) {
		this.endLongitude = endLongitude;
	}

	public List<DbMapPosition> getDbPath() {
		return dbPathPoints;
	}

	public void setDbPath(List<DbMapPosition> dbPathPoints) {
		this.dbPathPoints = dbPathPoints;
	}
	
	public Date getAddDate() {
		return addDate;
	}

	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}
	
	public int getTimesUsed() {
		return timesUsed;
	}

	public void setTimesUsed(int timesUsed) {
		this.timesUsed = timesUsed;
	}

}
