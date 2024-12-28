import javax.swing.ImageIcon;

@SuppressWarnings("serial")

public class Ghost extends Mover {
	
	//Variable for lastMove
	private int lastMove = 0;
	
	//Getters and Setters
	public int getLastMove() {
		return lastMove;
	}

	public void setLastMove(int lastMove) {
		this.lastMove = lastMove;
	}

	//Ghost images
	public static final ImageIcon[] IMAGE = {
			
			//Import ghost images
			new ImageIcon("images/Ghost1.bmp"),
			new ImageIcon("images/Ghost2.bmp"),
			new ImageIcon("images/Ghost3.bmp"),
			
	};
	
	public Ghost(int gNum) {
		
		this.setIcon(IMAGE[gNum]);
	
	}

}
