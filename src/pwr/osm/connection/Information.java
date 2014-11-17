package pwr.osm.connection;

public enum Information{

	WAY_IS_ALREADY_FOUND("Stop you job"),
	FIND_WAY("Find way"),
	WAY_IS_FOUND("Way is found");
	
	private String info;

	private Information(String info){
		this.info = info;
	} 
	
	@Override 
	public String toString(){ 
		return info; 
	}
}
