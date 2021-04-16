import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

@SuppressWarnings("serial")

public class Board extends JPanel implements KeyListener, ActionListener {

	// Game movement timer delayed by 250 milliseconds
	private Timer gameTimer = new Timer(250, this);

	// PacMan animation timer delayed by 10 milliseconds
	private Timer animateTimer = new Timer(10, this);

	// PacMan's power up timer running for 9 seconds
	private Timer powerUpTimer = new Timer(9000, this);

	// ImageIcon variable for wall
	private static ImageIcon wall = new ImageIcon("images/newWall.png");

	// ImageIcon constant for food
	private static final ImageIcon FOOD = new ImageIcon("images/StdFood.bmp");

	// ImageIcon constant for blank
	private static final ImageIcon BLANK = new ImageIcon("images/Black.bmp");

	// ImageIcon constant for door
	private static final ImageIcon DOOR = new ImageIcon("images/portal.png");

	// ImageIcon constant for skull
	private static final ImageIcon SKULL = new ImageIcon("images/Skull.bmp");

	// ImageIcon constant for gate
	private static final ImageIcon GATECLOSED = new ImageIcon("images/gate.png");

	// ImageIcon constant for opened gate
	private static final ImageIcon GATEOPEN = new ImageIcon("images/gateOpen.png");

	// ImageIcon constant for power up
	private static final ImageIcon POWER = new ImageIcon("images/powerup.png");

	// ImageIcon constant for blue ghost
	private static final ImageIcon BLUE = new ImageIcon("images/BlueGhost.bmp");

	// ImageIcon constant for cherry
	private static final ImageIcon CHERRY = new ImageIcon("images/cherry.bmp");

	// Array to hold game board characters from the text file
	private char[][] maze = new char[25][27];

	// Array to hold the game board images
	private JLabel[][] cell = new JLabel[25][27];

	// PacMan object
	private PacMan pacMan;

	// Array of ghost objects
	private Ghost[] ghost = new Ghost[3];

	// Track amount of food on board
	private int pellets = 0;

	// Sets the game level to 1 (for the label)
	public int currentLevel = 1;

	// Track game score
	public int score = 0;

	// Track score for each level
	public int levelScore = 0;

	// Track total score
	public int totalScore = 0;

	// Set the goal for each level
	public int levelGoal = 100;

	// Steps for animating PacMan's chomp
	private int pStep;

	// PacMan Lives set to 3
	public int lives = 3;

	// Highscore
	public int hScore = 0;

	// PacMan respawn coordinates
	private int xCoord;
	private int yCoord;

	// 3 Ghost respawn coordinates
	private int[] xGhostCoord = new int[3];
	private int[] yGhostCoord = new int[3];

	// Sets the ghosts state
	private boolean powerUpMove = false;

	// Sets the games level-ended state
	private boolean isLevelEnd = false;

	// Restart button
	JButton reset = new JButton("Restart Game");

	// Close game button
	JButton close = new JButton("Close Game");

	// Continue game button
	JButton continueButton = new JButton("Continue Game");

	// Create JLabel for level 1
	JLabel level1 = new JLabel();

	// Create JLabel for level 2
	JLabel level2 = new JLabel();

	// Create JLabel for level 3
	JLabel level3 = new JLabel();

	// Create JLabel for level completion image
	JLabel levelComplete = new JLabel();

	// Construct game board (layout, background, PacMan, ghosts)
	public Board() {

		// Sets the layout and background of the screen
		setLayout(new GridLayout(25, 27));
		setBackground(Color.BLACK);

		// Creates new objects for PacMan and the Ghosts
		pacMan = new PacMan();

		ghost[0] = new Ghost(0);
		ghost[1] = new Ghost(1);
		ghost[2] = new Ghost(2);

		// Call loadBoard method
		loadBoard();

	}

	// Loads the maze onto the screen from the maze.txt file
	private void loadBoard() {

		// Rows variable
		int r = 0;

		// Takes variables from the maze.txt
		Scanner input;

		// Runs the code below
		try {

			input = new Scanner(new File("maze.txt"));

			// Checks input
			while (input.hasNext()) {

				// Converts letters to characters
				maze[r] = input.nextLine().toCharArray();

				// Display an image for each maze section
				for (int c = 0; c < maze[r].length; c++) {

					cell[r][c] = new JLabel();

					// Sets all the letter 'W' in the maze.txt file to the wall image
					if (maze[r][c] == 'W')

						cell[r][c].setIcon(wall);

					// Sets all the letter 'F' in the maze.txt file to the food image
					else if (maze[r][c] == 'F' || maze[r][c] == 'T') {

						cell[r][c].setIcon(FOOD);
						pellets++; // Keeps track of the amount of food
					}

					// Sets the letter 'P' in the maze.txt file to the PacMan image
					else if (maze[r][c] == 'P') {

						xCoord = r; // PacMans original spawn point (rows)
						yCoord = c; // PacMans original spawn point (columns)

						cell[r][c].setIcon(pacMan.getIcon()); // Resets PacMan's icon
						pacMan.setRow(r); // Sets PacMan to the original spawn point (rows)
						pacMan.setColumn(c); // Sets PacMan to the original spawn point (columns)
						pacMan.setDirection(0); // Start facing left

					}

					// Sets the letter 'G' in the maze.txt file to the gate image for the ghost
					// house
					else if (maze[r][c] == 'G') {

						cell[r][c].setIcon(GATECLOSED);
					}

					// Sets the letter 'S' in the maze.txt file to the power up image
					else if (maze[r][c] == 'S') {

						cell[r][c].setIcon(POWER);

					}

					// Sets the letter 'C' in the maze.txt file to the cherry image
					else if (maze[r][c] == 'C') {

						cell[r][c].setIcon(CHERRY);

					}

					// Sets the numbers '0', '1', and '2' in the
					// maze.txt file to the appropriate ghost icon
					else if (maze[r][c] == '0' || maze[r][c] == '1' || maze[r][c] == '2') {

						int gNum = (int) (maze[r][c]) - 48; // Gets the characters from the maze.txt file

						cell[r][c].setIcon(ghost[gNum].getIcon());
						ghost[gNum].setRow(r);
						ghost[gNum].setColumn(c);

						xGhostCoord[gNum] = r; // Ghosts original spawn point (rows)
						yGhostCoord[gNum] = c; // Ghosts original spawn point (columns)
					}

					// Sets the letter 'D' in the maze.txt file to the door image (black)
					else if (maze[r][c] == 'D') {

						cell[r][c].setIcon(DOOR);

					}

					// Creates a cell for each row and column
					add(cell[r][c]);
				}

				// Next row
				r++;

			}

			// Close input
			input.close();

			// Displays an error message if there is a missing file
		} catch (FileNotFoundException e) {

			System.out.println("File not found");
		}

		// Call sounds method to play beginning music
		sound("GAMEBEGINNINGbetter.wav");

	}

	// Method for keyboard entries
	public void keyPressed(KeyEvent key) {

		/*
		 * Starts the game when a key is pressed Checks if the game timer is not running
		 * Checks if PacMan's state is not dead Checks if the state of the game is not
		 * ended
		 */
		if (gameTimer.isRunning() == false && pacMan.isDead() == false && isLevelEnd == false) {
			gameTimer.start();

		}

		// Checks if PacMan is not dead and if there are still pellets
		if (pacMan.isDead() == false && score != pellets) {

			/*
			 * Keyboard direction variable Subtracts the ASCII code for the arrow key by 37
			 * to get certain numbers 0 is left, 1 is up, 2 is right, 3 is down
			 */
			int direction = key.getKeyCode() - 37;

			// Keyboard directions for PacMan
			if (direction == 0 && maze[pacMan.getRow()][pacMan.getColumn() - 1] != 'W')
				pacMan.setDirection(0); // Left
			else if (direction == 1 && maze[pacMan.getRow() - 1][pacMan.getColumn()] != 'W')
				pacMan.setDirection(1); // Up
			else if (direction == 2 && maze[pacMan.getRow()][pacMan.getColumn() + 1] != 'W')
				pacMan.setDirection(2); // Right
			else if (direction == 3 && maze[pacMan.getRow() + 1][pacMan.getColumn()] != 'W'
					&& maze[pacMan.getRow() + 1][pacMan.getColumn()] != 'G') // Stop PacMan from getting into ghost room
				pacMan.setDirection(3); // Down

		}

	}

	// Mandatory method to implement KeyListener interface
	public void keyReleased(KeyEvent key) {

	}

	// Mandatory method to implement KeyListener interface
	public void keyTyped(KeyEvent key) {

	}

	// Method for moving
	private void performMove(Mover mover) {

		// Door for teleporting to the left and right
		if (mover.getColumn() == 1) { // Checks if PacMan or a ghosts are in column 1
			mover.setColumn(24); // Teleports them to column 24
			cell[12][1].setIcon(DOOR); // Replaces door icon back after PacMan or a ghost goes through it
			sound("teleport.wav"); // Play teleporting sound effect

		} else if (mover.getColumn() == 25) { // Checks if PacMan or a ghosts are in column 25
			mover.setColumn(2); // Teleports them to column 2
			cell[12][25].setIcon(DOOR); // Replaces door icon back after PacMan or a ghost goes through it
			sound("teleport.wav"); // Play teleporting sound effect
		}

		// Checks if a character is not 'W' in maze.txt
		if (maze[mover.getNextRow()][mover.getNextColumn()] != 'W') {

			// Starts animate timer for every movement
			if (mover == pacMan)
				animateTimer.start();

			// Code to set characters from maze.txt into icons
			else {

				// Sets 'F' and 'T' to the food icon when a ghost is in the spot
				// This makes sure the ghosts cannot eat the pellets
				if (maze[mover.getRow()][mover.getColumn()] == 'F' || maze[mover.getRow()][mover.getColumn()] == 'T')
					cell[mover.getRow()][mover.getColumn()].setIcon(FOOD);

				// Sets 'C' to the cherry icon when a ghost is in the spot
				// This makes sure the ghosts cannot eat the cherries
				else if (maze[mover.getRow()][mover.getColumn()] == 'C')
					cell[mover.getRow()][mover.getColumn()].setIcon(CHERRY);

				// Sets 'S' to the power up icon when a ghost is in the spot
				// This makes sure the ghosts cannot eat the power ups
				else if (maze[mover.getRow()][mover.getColumn()] == 'S')
					cell[mover.getRow()][mover.getColumn()].setIcon(POWER);

				// Sets 'G' to the opened gate icon when a ghost is in the spot
				else if (maze[mover.getRow()][mover.getColumn()] == 'G')
					cell[mover.getRow()][mover.getColumn()].setIcon(GATEOPEN);

				// Set others to the blank icon when a ghost is in the spot
				else
					cell[mover.getRow()][mover.getColumn()].setIcon(BLANK);

				// Calls move method from the mover class
				mover.move();

				// Checks if the power up is active and PacMan collides with a ghost
				if (collided() && powerUpMove) {

					// For all 3 ghosts
					for (int g = 0; g < 3; g++) {

						// Checks if the ghost and PacMan is in the same spot
						if (ghost[g].getRow() == pacMan.getRow() && ghost[g].getColumn() == pacMan.getColumn()) {

							// Resets ghosts spawn after being eaten
							ghost[g].setRow(xGhostCoord[g]);
							ghost[g].setColumn(yGhostCoord[g]);

							// Play ghost eaten sound
							sound("GHOSTEATEN.wav");

							// Pause the game for 0.5 seconds after eating a ghost
							try {
								Thread.sleep(500);

								// Add 10 points to all scores
								score += 10;
								levelScore += 10;

							} catch (InterruptedException e) {

								e.printStackTrace();
							}

						}

					}

				}

				// Checks if PacMan collides with a ghost and the lives are greater than 1
				else if (collided() && lives > 1) {

					// Call deathWithLives method
					deathWithLives();

				}

				// Checks if PacMan collides with a ghost and the lives are 1
				else if (collided() && lives == 1) {

					// Update highscore label
					checkHighscore(totalScore);
					getHighscore();
					PacManGUI.highscore.setText("Highscore: " + hScore);

					// Score is added to total score
					totalScore += score;

					// Reset back to level 1 score goal
					levelGoal = 100;

					// Call death method
					death();

					// Call reset method: Reset game button appears when you die
					reset();

					// Call close method: Close game button appears when you die
					close();

				}

				// Checks if PacMan is alive
				if (lives >= 1) {

					// Variable for game timer, Used to speed up game after certain levels
					int game = 250;

					// Start at level 1 and increase the level
					for (int level = 1; level <= 10000; level++) {

						// Checks if the current level score is greater than or equal to the level goal
						if (levelScore >= levelGoal) {

							// Import and display level complete label
							levelComplete.setBounds(-10, 170, 600, 200);
							this.getParent().add(levelComplete);
							this.getParent().repaint(); // Prevents icons from overlapping the label
							levelComplete.setIcon(new ImageIcon("images/levelComplete.png")); // Import level complete
																								// image
							this.getParent().setComponentZOrder(levelComplete, 0);
							levelComplete.setVisible(true);

							isLevelEnd = true; // Sets levels state
							sound("interm.wav"); // Play level complete sound
							gameTimer.stop(); // End game timer
							animateTimer.stop(); // End animation timer
							continueGame(); // Call continueGame method

							totalScore += levelScore; // Adds the level score to the total score
							levelScore = 0; // Reset the current level score to 0
							levelGoal += 20; // Increase the goal of each level by 20 more score

							// Adds 1 to the level display and updates JLabel
							currentLevel++;
							PacManGUI.displayLevel.setText("Level " + currentLevel);

							// Sets maximum level goal to 230 points
							if (levelGoal >= 230) {
								levelGoal = 230;
							}

							// Checks if game level is 2
							if (currentLevel == 2) {
								gameTimer.setDelay(game - 10); // Speed game up by 10 milliseconds
								wall = new ImageIcon("images/wallLv2.png"); // Changes wall icon
							}
							// Checks if the games level is in between 3 and 4
							else if (currentLevel >= 3 && currentLevel <= 4) {
								gameTimer.setDelay(game - 20); // Speed game up by 20 milliseconds
								wall = new ImageIcon("images/wallLv3.png"); // Changes wall icon
							}
							// Checks if the games level is in between 5 and 6
							else if (currentLevel >= 5 && currentLevel <= 6) {
								gameTimer.setDelay(game - 50); // Speed game up by 50 milliseconds
								wall = new ImageIcon("images/wallLv5.png"); // Changes wall icon
							}
							// Checks if the games level is in between 7 and 8
							else if (currentLevel >= 7 && currentLevel <= 8) {
								gameTimer.setDelay(game - 80); // Speed game up by 80 milliseconds
								wall = new ImageIcon("images/wallLv7.png"); // Changes wall icon
							}
							// Checks if the game level is in between 9 and 10
							else if (currentLevel >= 9 && currentLevel <= 10) {
								gameTimer.setDelay(game - 100); // Speed game up by 100 milliseconds
								wall = new ImageIcon("images/wallLv9.png"); // Changes wall icon
							}
							// All levels passed 10 will be set to a game timer of 130 milliseconds
							else if (currentLevel >= 11 && currentLevel <= 12) {
								gameTimer.setDelay(game - 120); // Speed game up by 100 milliseconds
								wall = new ImageIcon("images/wallLv11.png"); // Changes wall icon
							}
							// All levels passed 12 will be set with red walls
							else {
								wall = new ImageIcon("images/wallLv11.png"); // Changes wall icon
							}
						}
					}
				}
			}
		}

		// Changes the ghost to blue when the power up is active
		if (powerUpMove && mover != pacMan)
			cell[mover.getRow()][mover.getColumn()].setIcon(BLUE);

		// Sets the location to the appropriate icon
		else if (mover != pacMan)
			cell[mover.getRow()][mover.getColumn()].setIcon(mover.getIcon());
	}

	// Method for checking collisions with PacMan and the ghosts
	private boolean collided() {

		// Checks for all 3 ghosts
		for (int g = 0; g < 3; g++) {

			// Checks if a ghost and PacMan is in the same spot
			if (ghost[g].getRow() == pacMan.getRow() && ghost[g].getColumn() == pacMan.getColumn())
				return true;
		}

		// Returns false if they are not in the same spot
		return false;
	}

	// Method for PacMan's death when lives are still avaliable
	private void deathWithLives() {

		// Removes 1 live and updates the JLabel
		lives--;
		PacManGUI.lives.setText("Lives: " + lives);

		cell[xCoord][yCoord].setIcon(pacMan.getIcon()); // Sets PacMans icon back at the spawn point

		// Play killed sound
		sound("killed.wav");

		// Stop timers
		animateTimer.stop();
		gameTimer.stop();

		// Delay for 2 seconds and recontinue the game
		// PacMan's spawn point is reset
		try {
			Thread.sleep(2000);

			pacMan.setRow(xCoord);
			pacMan.setColumn(yCoord);
			pacMan.setDirection(0); // Facing left

		} catch (InterruptedException e) {

			e.printStackTrace();
		}
	}

	// Method for PacMan's death when there are no lives left
	private void death() {

		// Sets PacMan as dead
		pacMan.setDead(true);

		// Play killed sound
		sound("killed.wav");

		// Call stopGame method
		stopGame();

		// Sets PacMan icon to skull
		cell[pacMan.getRow()][pacMan.getColumn()].setIcon(SKULL);
	}

	// Method for ending the game
	private void stopGame() {

		// Checks if PacMan is dead
		if (pacMan.isDead()) {
			animateTimer.stop(); // Stops animation timer
			gameTimer.stop(); // Stops game timer

		}

	}

	// Method for the ghost's movement
	private void moveGhosts() {

		// Direction variable for the green ghost
		int dirGreen = 0;

		// 1 in 300 chance for this to run
		if ((int) ((Math.random() * 300) + 1) == 1) {

			// Randomize the movement for ghost
			do {
				dirGreen = (int) (Math.random() * 4);
			} while (Math.abs(ghost[0].getDirection() - dirGreen) == 1);

			// Set the direction for the green ghost
			ghost[0].setDirection(dirGreen);

		}

		// Checks for 'T' or 'S' in maze.txt and calls checkMove method
		else if (maze[ghost[0].getRow()][ghost[0].getColumn()] == 'T'
				|| maze[ghost[0].getRow()][ghost[0].getColumn()] == 'S') {

			// Checks if powerUpMove is true and calls checkPowerMove method
			if (powerUpMove)
				ghost[0].setDirection(checkPowerMove(0));

			// Otherwise, set the ghosts movement to follow PacMan with the checkMove method
			else
				ghost[0].setDirection(checkMove(0));
		}

		// Call checkNoIntersection method
		else
			ghost[0].setDirection(checkNoIntersection(0));

		// Sets the last move of the ghost and gets their direction
		ghost[0].setLastMove(ghost[0].getDirection());

		// Calls performMove for the ghosts to move
		performMove(ghost[0]);

		// Direction variable for the yellow ghost
		int dirYellow = 0;

		// 1 in 400 chance for this to run
		if ((int) ((Math.random() * 401) + 1) == 1) {

			// Randomize the movement for ghost
			do {
				dirYellow = (int) (Math.random() * 4);
			} while (Math.abs(ghost[1].getDirection() - dirYellow) == 2);

			// Set the direction for the yellow ghost
			ghost[1].setDirection(dirYellow);

		}

		// Checks for 'T' or 'S' in maze.txt and calls checkMove method
		else if (maze[ghost[1].getRow()][ghost[1].getColumn()] == 'T'
				|| maze[ghost[1].getRow()][ghost[1].getColumn()] == 'S') {

			// Checks if powerUpMove is true and calls checkPowerMove method
			if (powerUpMove)
				ghost[1].setDirection(checkPowerMove(1));

			// Otherwise, set the ghosts movement to follow PacMan with the checkMove method
			else
				ghost[1].setDirection(checkMove(1));
		}

		// Call checkNoIntersection method
		else
			ghost[1].setDirection(checkNoIntersection(1));

		// Set sthe last move of the ghost and gets their direction
		ghost[1].setLastMove(ghost[1].getDirection());

		// Calls performMove for the ghosts to move
		performMove(ghost[1]);

		// Direction variable for the red ghost
		int dirRed = 0;

		// 1 in 500 chance for this to run
		if ((int) ((Math.random() * 501) + 1) == 1) {

			// Randomize the movement for ghost
			do {
				dirRed = (int) (Math.random() * 4);
			} while (Math.abs(ghost[2].getDirection() - dirRed) == 3);

			// Set the direction for the red ghost
			ghost[2].setDirection(dirRed);

		}

		// Checks for 'T' or 'S' in maze.txt and calls checkMove method
		else if (maze[ghost[2].getRow()][ghost[2].getColumn()] == 'T'
				|| maze[ghost[2].getRow()][ghost[2].getColumn()] == 'S') {

			// Checks if powerUpMove is true and calls checkPowerMove method
			if (powerUpMove)
				ghost[2].setDirection(checkPowerMove(2));

			// Otherwise, set the ghosts movement to follow PacMan with the checkMove method
			else
				ghost[2].setDirection(checkMove(2));
		}

		// Call checkNoIntersection method
		else
			ghost[2].setDirection(checkNoIntersection(2));

		// Set the last move of the ghost and gets their direction
		ghost[2].setLastMove(ghost[2].getDirection());

		// Calls performMove for the ghosts to move
		performMove(ghost[2]);
	}

	// Method to check the distance between PacMan and the ghosts
	public double checkPacDis(int row, int column) {

		// Stores distance
		double distance;

		// Finds the difference from PacMan and the given row/column
		int rowDiff = pacMan.getRow() - row;
		int columnDiff = pacMan.getColumn() - column;

		// Use pythagorean theorem to find the distance between PacMan and the ghosts
		distance = Math.sqrt(Math.pow(rowDiff, 2) + Math.pow(columnDiff, 2));

		return distance;
	}

	// Method to check which direction the ghosts should move to get closest to PacMan
	public int checkMove(int ghostNum) {

		// Variables to store the distances in the directions
		int upDistance = 1000;
		int downDistance = 1000;
		int leftDistance = 1000;
		int rightDistance = 1000;

		// Gets the distance from every direction the ghost can move, excluding the wall
		if (maze[ghost[ghostNum].getRow()][ghost[ghostNum].getColumn() - 1] != 'W')
			leftDistance = (int) checkPacDis(ghost[ghostNum].getRow(), ghost[ghostNum].getColumn() - 1);

		if (maze[ghost[ghostNum].getRow() - 1][ghost[ghostNum].getColumn()] != 'W')
			upDistance = (int) checkPacDis(ghost[ghostNum].getRow() - 1, ghost[ghostNum].getColumn());

		if (maze[ghost[ghostNum].getRow()][ghost[ghostNum].getColumn() + 1] != 'W')
			rightDistance = (int) checkPacDis(ghost[ghostNum].getRow(), ghost[ghostNum].getColumn() + 1);

		if (maze[ghost[ghostNum].getRow() + 1][ghost[ghostNum].getColumn()] != 'W')
			downDistance = (int) checkPacDis(ghost[ghostNum].getRow() + 1, ghost[ghostNum].getColumn());

		// Array to store the distances in the directions
		double[] lowest = { upDistance, leftDistance, downDistance, rightDistance };

		// Sort them by ascending order
		Arrays.sort(lowest);

		// Gets the shortest distance and sends the ghost in that direction
		if (leftDistance == lowest[0])
			return 0; // Left
		else if (upDistance == lowest[0])
			return 1; // Up
		else if (rightDistance == lowest[0])
			return 2; // Right
		else if (downDistance == lowest[0])
			return 3; // Down
		else
			return 0;

	}

	// Method to check which direction ghosts should move to
	// get the furthest from PacMan when the power up is active
	public int checkPowerMove(int ghostNum) {

		// Variable to store the distances in the directions
		int upDistance = -1;
		int downDistance = -1;
		int leftDistance = -1;
		int rightDistance = -1;

		// Gets the distance from every direction the ghost can move, excluding the wall
		leftDistance = (int) checkPacDis(ghost[ghostNum].getRow(), ghost[ghostNum].getColumn() - 1);

		if (maze[ghost[ghostNum].getRow() - 1][ghost[ghostNum].getColumn()] != 'W')
			upDistance = (int) checkPacDis(ghost[ghostNum].getRow() - 1, ghost[ghostNum].getColumn());

		if (maze[ghost[ghostNum].getRow()][ghost[ghostNum].getColumn() + 1] != 'W')
			rightDistance = (int) checkPacDis(ghost[ghostNum].getRow(), ghost[ghostNum].getColumn() + 1);

		if (maze[ghost[ghostNum].getRow() + 1][ghost[ghostNum].getColumn()] != 'W')
			downDistance = (int) checkPacDis(ghost[ghostNum].getRow() + 1, ghost[ghostNum].getColumn());

		// Array to store the distances in the directions
		double[] highest = { upDistance, leftDistance, downDistance, rightDistance };

		// Sort them by ascending order
		Arrays.sort(highest);

		// Gets the furthest distance and sends the ghost in that direction
		if (leftDistance == highest[3])
			return 0; // Left
		else if (upDistance == highest[3])
			return 1; // Up
		else if (rightDistance == highest[3])
			return 2; // Right
		else if (downDistance == highest[3])
			return 3; // Down
		else
			return 0;

	}

	// Method to check if a ghost is not in an intersection
	public int checkNoIntersection(int ghostNum) {

		// Get the direction of their last move
		int direction = ghost[ghostNum].getLastMove();

		// Checks if the direction they are going in is not a wall and sets that as the direction
		// If there is a wall, set it to another direction
		if (direction == 0 && maze[ghost[ghostNum].getRow()][ghost[ghostNum].getColumn() - 1] != 'W')
			return direction;
		else if (maze[ghost[ghostNum].getRow() - 1][ghost[ghostNum].getColumn()] != 'W'
				&& 1 != getOpposite(ghost[ghostNum].getLastMove()))
			return 1;
		else if (maze[ghost[ghostNum].getRow()][ghost[ghostNum].getColumn() + 1] != 'W'
				&& 2 != getOpposite(ghost[ghostNum].getLastMove()))
			return 2;
		else if (maze[ghost[ghostNum].getRow() + 1][ghost[ghostNum].getColumn()] != 'W'
				&& 3 != getOpposite(ghost[ghostNum].getLastMove()))
			return 3;

		if (direction == 1 && maze[ghost[ghostNum].getRow() - 1][ghost[ghostNum].getColumn()] != 'W')
			return direction;
		else if (maze[ghost[ghostNum].getRow()][ghost[ghostNum].getColumn() - 1] != 'W'
				&& 0 != getOpposite(ghost[ghostNum].getLastMove()))
			return 0;
		else if (maze[ghost[ghostNum].getRow()][ghost[ghostNum].getColumn() + 1] != 'W'
				&& 2 != getOpposite(ghost[ghostNum].getLastMove()))
			return 2;
		else if (maze[ghost[ghostNum].getRow() + 1][ghost[ghostNum].getColumn()] != 'W'
				&& 3 != getOpposite(ghost[ghostNum].getLastMove()))
			return 3;

		if (direction == 2 && maze[ghost[ghostNum].getRow()][ghost[ghostNum].getColumn() + 1] != 'W')
			return direction;
		else if (maze[ghost[ghostNum].getRow()][ghost[ghostNum].getColumn() - 1] != 'W'
				&& 0 != getOpposite(ghost[ghostNum].getLastMove()))
			return 0;
		else if (maze[ghost[ghostNum].getRow() - 1][ghost[ghostNum].getColumn()] != 'W'
				&& 1 != getOpposite(ghost[ghostNum].getLastMove()))
			return 1;
		else if (maze[ghost[ghostNum].getRow() + 1][ghost[ghostNum].getColumn()] != 'W'
				&& 3 != getOpposite(ghost[ghostNum].getLastMove()))
			return 3;

		if (direction == 3 && maze[ghost[ghostNum].getRow() + 1][ghost[ghostNum].getColumn()] != 'W')
			return direction;
		else if (maze[ghost[ghostNum].getRow()][ghost[ghostNum].getColumn() - 1] != 'W'
				&& 0 != getOpposite(ghost[ghostNum].getLastMove()))
			return 0;
		else if (maze[ghost[ghostNum].getRow() - 1][ghost[ghostNum].getColumn()] != 'W'
				&& 1 != getOpposite(ghost[ghostNum].getLastMove()))
			return 1;
		else if (maze[ghost[ghostNum].getRow()][ghost[ghostNum].getColumn() + 1] != 'W'
				&& 2 != getOpposite(ghost[ghostNum].getLastMove()))
			return 2;
		else
			return 999;

	}

	// Method to get the opposite direction of the ghosts
	public int getOpposite(int direction) {

		// Return the opposite direction they are going
		if (direction == 0) // If right
			return 2; // Return left
		else if (direction == 2) // If left
			return 0; // Return right
		else if (direction == 1) // If up
			return 3; // Return down
		else if (direction == 3) // If up
			return 1; // Return up
		else
			return 0;

	}

	// Method for performed actions
	public void actionPerformed(ActionEvent e) {

		// Game starter
		if (e.getSource() == gameTimer) {

			// Call performMove and moveGhosts method
			performMove(pacMan);
			moveGhosts();

			// Animation starter
		} else if (e.getSource() == animateTimer) {

			// Call animatePacMan method
			animatePacMan();

			// Add 1 to PacMan animation step
			pStep++;

			// Resets pStep back to 0 once it reaches 3
			if (pStep == 3)
				pStep = 0;
		}

		// Reset button to reset the game and clear buttons
		if (e.getSource() == reset) {

			// Call refresh method
			refresh();

			// Resets scores back to 0
			score = 0;
			levelScore = 0;
			totalScore = 0;

			// Resets lives back to 3
			lives = 3;

			// Sets gameTimer delay back to 250 milliseconds
			gameTimer.setDelay(250);

			// Set level back to 1 and update text
			currentLevel = 1;
			PacManGUI.displayLevel.setText("Level " + currentLevel);

			// Call sounds method to play beginning music
			sound("GAMEBEGINNINGbetter.wav");

		}

		// Close button
		if (e.getSource() == close) {

			// Closes the window
			System.exit(0);
		}

		// Continue button
		if (e.getSource() == continueButton) {

			// Call refresh method
			refresh();

			// Sets levels state
			isLevelEnd = false;

			// End power up (if it is running)
			powerUpMove = false;
			powerUpTimer.stop();

			// Set level score to 0
			levelScore = 0;
			score = 0;
		}

		// Power up timer
		if (e.getSource() == powerUpTimer) {

			// Stops power up ability when the timer runs out
			powerUpMove = false;
			powerUpTimer.stop();
		}

	}

	// Method to animate PacMan's movement in 3 steps
	private void animatePacMan() {

		// if pStep is 0
		if (pStep == 0) {

			// Open mouth animation
			cell[pacMan.getRow()][pacMan.getColumn()].setIcon(PacMan.IMAGE[pacMan.getDirection()][1]);

			// Changes the animation timer to 50 milliseconds
			animateTimer.setDelay(50);
		}

		// if pStep is 1
		else if (pStep == 1)

			// Sets icon to blank
			cell[pacMan.getRow()][pacMan.getColumn()].setIcon(BLANK);

		// If pStep is 2
		else if (pStep == 2) {

			// Moves PacMan
			pacMan.move();

			// Checks if PacMan is in the same spot as 'F'
			if (maze[pacMan.getRow()][pacMan.getColumn()] == 'F' || maze[pacMan.getRow()][pacMan.getColumn()] == 'T') {

				// Add to the score when PacMan eats a pellet
				score++;
				levelScore++;

				// Update labels
				PacManGUI.scorelabel.setText("Score: " + score + "/" + levelGoal);
				PacManGUI.totalScore.setText("Total Score: " + totalScore);

				// Play chomping sound when eating a pellet
				sound("pacChompBetter.wav");

				// Replaces the pellet with a blank icon (eaten food)
				maze[pacMan.getRow()][pacMan.getColumn()] = 'E';
			}

			// Checks if PacMan eats a power up
			if (maze[pacMan.getRow()][pacMan.getColumn()] == 'S') {

				// Call powerUp method
				powerUp();

				// Replaces the power up with a blank icon (eaten powerup)
				maze[pacMan.getRow()][pacMan.getColumn()] = 'E';
			}

			// Checks if PacMan eats a cherry
			if (maze[pacMan.getRow()][pacMan.getColumn()] == 'C') {

				// Plays eaten fruit sound
				sound("fruiteat.wav");

				// Adds 10 points to all scores
				score += 10;
				levelScore += 10;

				// Replaces the cherry with a blank icon (eaten cherry)
				maze[pacMan.getRow()][pacMan.getColumn()] = 'E';
			}

			// Stops animation timer
			animateTimer.stop();

			// If PacMan is dead, replace the PacMan icon with a skull
			if (pacMan.isDead())
				cell[pacMan.getRow()][pacMan.getColumn()].setIcon(SKULL);

			// Closed mouth animation
			else
				cell[pacMan.getRow()][pacMan.getColumn()].setIcon(PacMan.IMAGE[pacMan.getDirection()][0]);
		}

	}

	// Method for PacMan's powerup
	private void powerUp() {

		// Starts timer for powerup
		powerUpTimer.restart();

		// Activates the power up move
		powerUpMove = true;

		// Play Power Up sound effect
		sound("powerupsfx.wav");

	}

	// Method to play sounds
	private void sound(String filepath) {

		Clip clip;

		try {

			File soundFile = new File("sounds/" + filepath); // Gets sounds from the 'sounds' filepath
			AudioInputStream input = AudioSystem.getAudioInputStream(soundFile);

			clip = AudioSystem.getClip();
			clip.open(input);
			clip.start(); // Starts audio clip

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	// Method to set highscore to 0
	private void setHighscore(int highscore) {

		hScore = highscore;

	}

	// Method to get highscore
	private int getHighscore() {

		return hScore;

	}

	// Checks if the score is the highscore
	private void checkHighscore(int current) {

		if (current > hScore) {

			setHighscore(current);

		}

	}

	// Method for the reset button
	private void reset() {

		reset.setBounds(118, 250, 150, 60);
		reset.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		reset.setForeground(Color.BLACK);
		reset.setBackground(Color.WHITE);
		this.getParent().add(reset);
		this.getParent().repaint();
		reset.addActionListener(this);

	}

	// Method for the close game button
	private void close() {

		close.setText("Close Game");
		close.setBounds(318, 250, 150, 60);
		close.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		close.setForeground(Color.BLACK);
		close.setBackground(Color.WHITE);
		this.getParent().add(close);
		close.addActionListener(this);
	}

	// Method for the continue game button
	private void continueGame() {

		continueButton.setText("Continue Game");
		continueButton.setBounds(218, 350, 150, 60);
		continueButton.setFont(new Font("Bahnschrift", Font.PLAIN, 15));
		continueButton.setForeground(Color.BLACK);
		continueButton.setBackground(Color.WHITE);
		this.getParent().add(continueButton);
		continueButton.addActionListener(this);

	}

	// Method to refresh the board
	private void refresh() {

		// Remove all potential labels
		this.getParent().remove(continueButton);
		this.getParent().remove(reset);
		this.getParent().remove(close);
		this.getParent().remove(levelComplete);
		this.getParent().repaint();

		// Rows variable
		int r = 0;

		// Takes variables from the maze.txt
		Scanner input;

		// Runs the code below
		try {

			input = new Scanner(new File("maze.txt"));

			// Checks input
			while (input.hasNext()) {

				// Converts letters to characters
				maze[r] = input.nextLine().toCharArray();

				// Display an image for each maze section
				for (int c = 0; c < maze[r].length; c++) {

					// Sets all the letter 'W' in the maze.txt file to the wall image
					if (maze[r][c] == 'W')

						cell[r][c].setIcon(wall);

					// Sets all the letter 'F' in the maze.txt file to the food image
					else if (maze[r][c] == 'F' || maze[r][c] == 'T') {

						cell[r][c].setIcon(FOOD);
						pellets++; // Keeps track of the amount of food
					}

					// Sets the letter 'P' in the maze.txt file to the PacMan image
					else if (maze[r][c] == 'P') {

						cell[r][c].setIcon(pacMan.getIcon());
						pacMan.setRow(r);
						pacMan.setColumn(c);
						pacMan.setDirection(0); // Start facing left
						xCoord = r; // Set to rows
						yCoord = c; // Set to columns

					}

					// Sets the letter 'G' in the maze.txt file to the gate image for the ghost house
					else if (maze[r][c] == 'G') {

						cell[r][c].setIcon(GATECLOSED);
					}

					else if (maze[r][c] == 'S') {

						cell[r][c].setIcon(POWER);

					}

					else if (maze[r][c] == 'C') {

						cell[r][c].setIcon(CHERRY);

					}

					// Sets the numbers '0', '1', and '2' in the maze.txt file to the appropriate ghost icon
					else if (maze[r][c] == '0' || maze[r][c] == '1' || maze[r][c] == '2') {

						int gNum = (int) (maze[r][c]) - 48; //

						cell[r][c].setIcon(ghost[gNum].getIcon());
						ghost[gNum].setRow(r);
						ghost[gNum].setColumn(c);
					}

					// Sets the letter 'D' in the maze.txt file to the door image (black)
					else if (maze[r][c] == 'D') {

						cell[r][c].setIcon(DOOR);

					}

				}

				// Next row
				r++;

				SwingUtilities.getWindowAncestor(this).requestFocus();
			}

			// Displays an error message if there is a missing file
		} catch (FileNotFoundException x) {

			System.out.println("File not found");
		}

		// PacMan is not dead
		pacMan.setDead(false);

		// Starts timers
		gameTimer.start();
		animateTimer.start();

	}

}
