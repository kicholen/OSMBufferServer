package pwr.osm.connection.data;

import java.io.Serializable;
import java.util.List;

import pwr.osm.data.representation.MapPosition;

public class Message implements Serializable{

	private static final long serialVersionUID = 8162777493113340457L;
	private Information information;
	private List<MapPosition> data;
	private long id;
	
	public Message(long id, Information info, List<MapPosition> data ) {
		this.information = info; this.data = data; this.id = id;
	}
	
	public long getId() {
		return id; 
	}
		
	public void setId(long id) { 
		this.id = id; 
	}
	
	public Information getInformation() {
		return information;
	}
	
	public void setInformation(Information information) {
		this.information = information;
	}
	
	public List<MapPosition> getData() {
		return data;
	}
	
	public void setData(List<MapPosition> data) {
		this.data = data;
	}
}
