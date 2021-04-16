import javax.swing.ImageIcon;

@SuppressWarnings("serial")

public class PacMan extends Mover {

	public static final ImageIcon[][] IMAGE = {

			// Import PacMan's animation images
			{ new ImageIcon("images/PacLeftClosed.bmp"), new ImageIcon("images/PacLeftOpen.bmp") },
			{ new ImageIcon("images/PacUpClosed.bmp"), new ImageIcon("images/PacUpOpen.bmp") },
			{ new ImageIcon("images/PacRightClosed.bmp"), new ImageIcon("images/PacRightOpen.bmp") },
			{ new ImageIcon("images/PacDownClosed.bmp"), new ImageIcon("images/PacDownOpen.bmp") }, };

	public PacMan() {

		this.setIcon(IMAGE[0][0]); // Sets PacMan's default image facing left with closed mouth
	}

}
