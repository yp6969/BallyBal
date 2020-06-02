import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Display extends JPanel implements Runnable{
	
	private static final long serialVersionUID = 1L;
	private Game game;
	private JFrame frame;

	private boolean location_for_start_game = true;
	
	private int ballSize = 30;
	private double oldX ;
	private double oldY ;
	private double newX ;
	private double newY ;
	private static final double speed = 1.5;

	

	
	public Display(Game game ) {
		
		this.game = game;
		frame = new JFrame("Mesirot");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(800,800));
		this.setPreferredSize(new Dimension(100,100));
		frame.add(this);

		frame.pack();
		frame.setVisible(true);
		
	}
	
	
	@Override
	public void run() {		
				
		while ( true ) {	
			
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			repaint();	
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		printBoard(g);
		fly(g);
	}


	private void fly(Graphics g) {
	
		if (game.getPlayers().getCount() <=1 ) {
			return;
		}
		
		Point owner = game.getBall().getOwner().getPoint();
		Point dest = game.getBall().getDestination().getPoint();	
		
			if (  Math.abs( (newX - dest.getX()) ) <= 13  &&  Math.abs(newY - dest.getY()) <= 13  ) {
				
				game.getBall().setInFlight(false);
				repaint();
				return;
			}
			
			double distance = getDistance(owner , dest);	
						
			g.setColor(	getBackground()	);
			g.fillRect(	(int)oldX,	(int)oldY,	ballSize,	ballSize );
			g.setColor(	Color.blue );
			g.fillOval(	(int)newX,	(int)newY,	ballSize,	ballSize );
			
			oldX = newX;
			oldY = newY;
			
			newX =  oldX - ( speed*( (owner.getX() - dest.getX() ) / distance ) );
			newY =   oldY -( speed*( (owner.getY() - dest.getY() ) / distance ) );
	
	}
	

	private void printBoard(Graphics g) {
		
		setPlayerPositions();
		
		for(int i = 0 ; i < game.getPlayers().getPlayersList().size() ; i++ ){
			
				Player p = game.getPlayers().getPlayersList().get(i);
				if (p.equals(game.getBall().getOwner())) {
					g.setColor(	Color.GREEN );	
					g.drawOval( (int)p.getPoint().getX(),(int)p.getPoint().getY(),40 , 30 );
				}
				else if (p.equals(game.getBall().getDestination())) {
					g.setColor(	Color.RED );	
					g.drawOval( (int)p.getPoint().getX(),(int)p.getPoint().getY(),40 , 30 );
				}else {
					g.setColor(	Color.BLACK );
					g.drawOval( (int)p.getPoint().getX(),(int)p.getPoint().getY(),	40 , 30 );
				}
				g.drawString( Integer.toString(p.getMyName()) ,
				(int)p.getPoint().getX()+16, (int)p.getPoint().getY()+19 );    
			
		}
	}
	
	public void setPlayerPositions(){
		double radius = this.getHeight()/2 - 120;
		double angle = (2*Math.PI)/game.getPlayers().getCount();    
		
		for( int i = 0 ; i < game.getPlayers().getPlayersList().size() ; i++ ) {
				Player p = game.getPlayers().getPlayersList().get(i);
				p.getPoint().setLocation((this.getWidth()/2)+radius*Math.cos(angle*i),
										(this.getHeight()/2)+radius*Math.sin(angle*i));
		 }
		
		
		if (location_for_start_game  &&  game.getPlayers().getCount() > 1)  {	
				oldX = game.getBall().getOwner().getPoint().getX();
				oldY = game.getBall().getOwner().getPoint().getY();
				newX = oldX ; 
				newY = oldY	;
				location_for_start_game = false; //change when a new player join
		}
		
	}

	
	public void setLocation_for_start_game(boolean location_for_start_game) {
		this.location_for_start_game = location_for_start_game;
	}

	public JFrame getFrame() {
		return frame;
	}

	private double getDistance(Point p1 , Point p2) {
		
		return   (   Math.sqrt(Math.pow(p1.getX()-p2.getX(), 2) + Math.pow(p1.getY()-p2.getY(), 2) )     );
		
	}	
	
}