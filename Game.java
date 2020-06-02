import java.util.Iterator;
import java.util.Random;
import javax.swing.JOptionPane;

public class Game extends Thread {

	private static Random random = new Random();

	/*
	 * Determines the ratio between the simulated time and running time.
	 * Specifically, this number states how many milliseconds should the program
	 * wait to simulate one minute, if periods in the constructor to store are
	 * specified in minutes. Thus, if 'TIME_SIMULATION_FACTOR' is set to 1, a
	 * service time of 1 minutes will be simulated as 1 milliseconds, and a service
	 * time of 10 minutes will be simulated as 10 milliseconds.
	 * 
	 * A good value is 1000, making the simulation clock run 60 faster than the
	 * processes it simulates. Then a service time of 1 minutes(=60 seconds) will be
	 * simulated as 1000 milliseconds(=1 second). Simulating a 1-hour game should
	 * take 1 minute.
	 * 
	 * For example, to simulate that a thread is sleeping x minutes. You should use
	 * sleep(x*TIME_SIMULATION_FACTOR);
	 */
	public static final int TIME_SIMULATION_FACTOR = 1000;
	
	private Players players;
	private Ball ball;
	private Display display;
	private int numPlayers;
	private long timeForGameM;
	private double playerActiveMean;
	private double playerActiveVar;
	private double playerArrivalMean;
	private double playerArrivalVar;
	private long timeArrivalPlayer;
	private long stoperForNewPlayer;
	
	public int getNumPlayers() {
	return numPlayers;
	}


	private long startGame;
	int i;

	/**
	 * Constructor
	 *
	 */
	public Game(long timeForGameM, int numPlayers, double playerActiveMean, double playerActiveVar,
			double playerArrivalMean, double playerArrivalVar) throws Exception {
		
		this.timeForGameM = timeForGameM *TIME_SIMULATION_FACTOR;
		this.numPlayers = numPlayers;
		this.playerActiveMean = playerActiveMean;
		this.playerActiveVar = playerActiveVar;
		this.playerArrivalMean = playerArrivalMean;
		this.playerArrivalVar = playerArrivalVar;
		this.startGame = System.currentTimeMillis();
		this.stoperForNewPlayer = System.currentTimeMillis();
		
		ball= new Ball();
		players = new Players();
		
		System.out.println("******************start game***********************");

		for (i=1; i<=numPlayers; i++) {
			Player temp = new Player(i,this, gaussian(playerActiveMean,playerActiveVar),ball,players );
			temp.setDaemon(true);
			players.addPlayer(temp);
		}
		
		ball.setOwner(players.getRandomPlayer());
		display = new Display(this );
		Thread thread = new Thread(display);
		thread.setDaemon(true);
		display.setPlayerPositions();
		thread.start();
		
		for (Player p : players.getPlayersList()) {
			p.start();
		}

		
		
		
	}

	
	//********************************************************************************************
	//********************************************************************************************
	

	public void run() {

		
		timeArrivalPlayer  = gaussian(playerArrivalMean,playerArrivalVar) * TIME_SIMULATION_FACTOR;
		while (true) {
							
			if (timeArrivalPlayer < (System.currentTimeMillis() - stoperForNewPlayer) ) {
				
				Player temp = new Player(i++,this, gaussian(playerActiveMean,playerActiveVar),ball,players);
				temp.setDaemon(true);
				while ( ball.getInFlight() == true ) {
					try {
						Thread.sleep(2);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}	
				}
				players.addPlayer(temp);
				temp.start();
				display.setLocation_for_start_game(true);
				
				timeArrivalPlayer  = gaussian(playerArrivalMean,playerArrivalVar) * TIME_SIMULATION_FACTOR;
				stoperForNewPlayer = System.currentTimeMillis();
			}
			
			if (!isGameLive()) {

				Iterator<Player> it = players.iterator();
				while (it.hasNext()) {
				    	 it.next();
						it.remove();
				}
				/*try {
					sleep(600);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}*/
		        JOptionPane.showMessageDialog(display.getFrame(), " **** game over **** ");
			   break;
			}	
		}
	}

	


	
	public boolean isGameLive() {
		
		long time = System.currentTimeMillis()- this.startGame;	
		
		if (time > this.timeForGameM) {
			return false;
		}
		return true;
	}

	
	public Ball getBall() {
		return ball;
	}
	public Players getPlayers() {
		return players;
	}


	public Display getDisplay() {
		return display;
	}


	/**
	 * gaussian - compute a random number drawn from a normal (Gaussian)
	 * distribution
	 *
	 * @param periodMean
	 *            - the mean of the distribution
	 * @param periodVar
	 *            - the variance of the distribution
	 * @return
	 */
	public static int gaussian(double periodMean, double periodVar) {
		double period = 0;
		while (period < 1)
			period = periodMean + periodVar * random.nextGaussian();
		return ((int) (period));
	}

}
