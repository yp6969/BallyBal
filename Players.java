import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Players implements Iterable<Player> {
	private List<Player> players;
	Random rand = new Random();
	Iterator<Player> it;

	public Players() {
		players = new ArrayList<Player>();
	}
	
	
	/**
	 * add player p
	 */
	public void addPlayer(Player p) {
		
		players.add(p);
		System.out.println(" Add Player " + p.getMyName());
	}
	
	/**
	 * remove player p
	 */
	public synchronized void removePlayer(Player p) {
		it = this.iterator();
		Player tmp;
		while (it.hasNext()) {
			tmp = it.next();
			if (tmp.equals(p)) {
				it.remove();
				System.out.println(" remove Player " + p.getMyName() );
				break;
			}
		}
	}

	/** 
	 * @return a player from the list players
	 */
	public synchronized Player getRandomPlayer() {
		
		if (this.getCount() <= 1) return null;
		int r = rand.nextInt(this.getCount());
		if( r <=getCount()  && players.get(r) == null) return null;
		return players.get(r);
	}
	
	
	/**
	 * @return a player which is not p.
	 */
	public Player getOther(Player p) {
		
		while(true) {
			Player temp = getRandomPlayer();
			if (this.getCount()<2 || temp == null) {
				return null;
			}
			if (! p.equals(temp))  
				return temp;
		}
	}

	/**
	 * @return the number of players
	 */
	public int getCount() {
		
		return players.size();
	}

	@Override
	public Iterator<Player> iterator() {
		
		return players.iterator();	
	}
	
	public List<Player> getPlayersList() {
		return players;
	}
	

}
