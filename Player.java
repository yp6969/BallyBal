import java.awt.Point;

public class Player extends Thread {
	
	private Game game;
	private Ball ball;
	private Players players;
	private int name;
	private long startTime;
	private int timeMS; // the active time in milliseconds.
	private Point point;
	static int i=0;

	/**
	 * @param name
	 *            - the name of the player
	 * @param game
	 *            - the game the player participate
	 * @param timeActiveM
	 *            - the time (minutes) the player will play
	 */
	
	public Player(int name, Game game, int timeActiveM,Ball ball , Players players ) {
		super();
		this.name = name;
		this.game = game;
		this.ball = ball;
		this.players = players;
		this.point = new Point();
		this.startTime = System.currentTimeMillis();
		this.timeMS =  1000;//timeActiveM * Game.TIME_SIMULATION_FACTOR ;
	}


	public void run() {		
		
		while (isLive(this)) {
			
		synchronized (game) {
					
					while ( !this.equals(ball.getOwner()) )  {
						
						try {
						    game.wait();
						} catch (InterruptedException e) {
							return;
						}
					}
					
					

					 Player dest = players.getOther(this);
					 				 
					 while (dest != null && !isLive(dest) && dest.equals(ball.getDestination()) ) {
						 
							killPlayer(dest);

							dest = players.getOther(this);
						
							if (dest == null) {
								if (!isLive(this)) {
									killPlayer(this);

									game.notifyAll();
									return;
								}
							}
						}
													 
					if (ball.getInFlight() == false && dest != null) {
						
						ball.setDestination(dest);
						ball.setInFlight(true);

							
						while (ball.getInFlight() == true) {
							try {
								sleep(2);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}					
						}
						
						ball.setOwner(dest);	
						game.notifyAll();	
				}
				
		}// synchronized
		
		
	  }//while true
		
		killPlayer(this);
		game.getDisplay().setLocation_for_start_game(true);	
		
	} // run
	
	
	private boolean killPlayer(Player p) {
		
		if (p == null) return false;
		if (p.equals(ball.getOwner())) {
			ball.setOwner(players.getOther(p));
		}
		players.removePlayer(p);
		p.interrupt();
    	return true;
	} 
	
	
	public int getMyName() {
		return name;
	}
	
	private boolean isLive(Player p) {
		
		if (p == null) return false;
		long time = System.currentTimeMillis()- p.startTime;	
		
		if (time > p.timeMS) {
			return false;
		}
		return true;
	}
	
	
	public Point getPoint() {
		return point;
	}

}
