import java.awt.Color; //Text colour
import java.awt.Font; //Text font
import javax.swing.JFrame; //JFrame
import javax.swing.JLabel; //Labels

@SuppressWarnings("serial")

public class PacManGUI extends JFrame {

	// Create labels and board
	public static JLabel scorelabel = new JLabel(); // Current score on a certain level
	public static JLabel lives = new JLabel(); // Lives
	public static JLabel highscore = new JLabel(); // Highscore
	public static JLabel ready = new JLabel(); // Ready text
	public static JLabel displayLevel = new JLabel();// Current level
	public static JLabel totalScore = new JLabel(); // Total game score
	private Board board = new Board(); // Board

	public PacManGUI() {

		// Set size of GUI with title
		setSize(600, 600);
		setLayout(null);
		setTitle("Brandon's PacMan Game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create and add total score display
		totalScore.setText("Total Score: " + board.totalScore);
		totalScore.setBounds(5, 538, 150, 20);
		totalScore.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
		totalScore.setForeground(Color.WHITE);
		totalScore.setBackground(Color.BLACK);
		add(totalScore);

		// Create and add level display
		displayLevel.setText("Level " + (board.currentLevel));
		displayLevel.setBounds(270, 538, 100, 20);
		displayLevel.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
		displayLevel.setForeground(Color.WHITE);
		displayLevel.setBackground(Color.BLACK);
		add(displayLevel);

		// Create and add label for "Ready!" text
		ready.setText("Ready!");
		ready.setBounds(272, 256, 150, 20);
		ready.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		ready.setForeground(Color.WHITE);
		ready.setBackground(Color.BLACK);
		add(ready);

		// Create and add scoreboard
		scorelabel.setText("Score: " + (board.score) + "/" + (board.levelGoal));
		scorelabel.setBounds(5, 5, 150, 20);
		scorelabel.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
		scorelabel.setForeground(Color.WHITE);
		scorelabel.setBackground(Color.BLACK);
		add(scorelabel);

		// Create and add lives counter
		lives.setText("Lives: " + (board.lives));
		lives.setBounds(500, 5, 100, 20);
		lives.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
		lives.setForeground(Color.WHITE);
		lives.setBackground(Color.BLACK);
		add(lives);

		// Create and add highscore
		highscore.setText("Highscore: " + (board.hScore));
		highscore.setBounds(245, 5, 150, 20);
		highscore.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
		highscore.setForeground(Color.WHITE);
		highscore.setBackground(Color.BLACK);
		add(highscore);

		// Adds the board and keyboard listener
		addKeyListener(board);
		add(board);
		board.setBounds(0, 0, this.getWidth() - 15, this.getHeight() - 30);

		// Makes the GUI visible
		setVisible(true);

	}

}