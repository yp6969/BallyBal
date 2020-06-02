

public class Ball {
	
	private Player owner;
	private Player receiver;
	private boolean in_flight;
	
	public Ball() {
		
		in_flight = false;
	}
	
	//*******************************************************************************//

	public Player getOwner() {
		
		return this.owner; 
	}
		
	public void setOwner(Player p) {
		
		this.owner = p;
	}
	
	//*******************************************************************************//
	
	public Player getDestination() {
		
		return this.receiver; 	
	}
		
	public void setDestination(Player pd) {
		
		this.receiver = pd;
	}
	
	//*******************************************************************************//
	
	public boolean getInFlight() {
		
		return in_flight;
	}
	
	
	public void setInFlight(boolean b) {
		
		this.in_flight = b;
	}
	
	

}
