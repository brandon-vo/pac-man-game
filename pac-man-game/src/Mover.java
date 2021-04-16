import javax.swing.JLabel;

@SuppressWarnings("serial")

public abstract class Mover extends JLabel {

	// Variables for the current position of Mover
	private int row;
	private int column;

	// Variables for the current direction of Mover
	private int dRow;
	private int dColumn;

	// Variable to check if PacMan is dead or not
	private boolean isDead;

	// Getters and Setters
	public int getdRow() {
		return dRow;
	}

	public void setdRow(int dRow) {
		this.dRow = dRow;
	}

	public int getdColumn() {
		return dColumn;

	}

	public void setdColumn(int dColumn) {
		this.dColumn = dColumn;
	}

	public int getRow() {
		return row;

	}

	public void setRow(int row) {
		this.row = row;

	}

	public int getColumn() {
		return column;

	}

	public void setColumn(int column) {
		this.column = column;

	}

	public int getDRow() {
		return dRow;

	}

	public void setDRow(int dRow) {
		this.dRow = dRow;

	}

	public int getDColumn() {
		return dColumn;

	}

	public boolean isDead() {
		return isDead;

	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;

	}

	// Change Movers current position based on their current direction
	public void move() {

		row += dRow;
		column += dColumn;

	}

	// Sets the direction of the mover based on a direction parameter
	public void setDirection(int direction) {

		// Current direction variables
		dRow = 0;
		dColumn = 0;

		// Directions
		if (direction == 0)
			dColumn = -1; // Left
		else if (direction == 1)
			dRow = -1; // Up
		else if (direction == 2)
			dColumn = 1; // Right
		else if (direction == 3)
			dRow = 1; // Down
	}

	// Returns the Mover's direction based on their current row or column direction
	public int getDirection() {

		if (dRow == 0 && dColumn == -1) // Left
			return 0;
		else if (dRow == -1 && dColumn == 0) // Up
			return 1;
		else if (dRow == 0 && dColumn == 1) // Right
			return 2;
		else
			return 3; // Down

	}

	// Return the Mover's next row
	public int getNextRow() {

		return row + dRow;

	}

	// Return the Mover's next column
	public int getNextColumn() {

		return column + dColumn;

	}

}
