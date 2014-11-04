package pwr.osm.buffer.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="DBMAPPOSITION")
public class DbMapPosition{
	
	@Id
	@GeneratedValue
	@Column(name="position_id")
	private Long positionId;
	
	@Column(name="latitude")
	private double latitude;
	
	@Column(name="longitude")
	private double longitude;
	
	@ManyToOne
	@JoinColumn(name="path_id", insertable=false, updatable=false, nullable=false)
	private DbPath dbPath;

	// Constructor
	public DbMapPosition() {

	}
	
	public DbMapPosition(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	// Getter and Setter methods	
	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public DbPath getDbPath(){
		return dbPath;
	}
	
	public void setDbPath(DbPath dbPath){
		this.dbPath = dbPath;
	}
}
