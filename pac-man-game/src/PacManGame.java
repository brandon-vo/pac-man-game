/* PacMan Project
 * Created By Brandon Vo
 * 
 * Description: 
 * My PacMan game consists of a playable PacMan character that uses arrow keys to move and 3 ghosts with 
 * tracking movement. The goal of the game is to achieve a certain score for each level, starting 
 * at 100 points for the first level with increments of 20 points per level until a maximum score goal of  
 * 230 points. PacMan can eat the pellets by walking over the item to gain 1 point per pellet. Each set of 
 * levels will also increase the speed of the game until a maximum game timer speed of 130 milliseconds after 
 * level 10. On the board, there are 4 power ups which gives the player 9 seconds to eat the ghosts, which 
 * gives 10 points. A cherry item is also active, giving 10 points to the score. If a ghost collides with
 * PacMan, it will take away 1 out of the 3 lives. If 1 life is avaliable and the player gets eaten, the 
 * game will end and prompt the user with a restart and quit button. The score is also checked to see if
 * it is a highscore.
 * 
 * Features:
 * - Score and scoreboard(1 Pellet = 1 Point)
 * - PacMan cannot enter the ghosts house
 * - Ghost's tracking movement allowing for them to get out of the house
 * - Highscore for current session
 * - Power pellet power up, causing ghosts to run away from PacMan (1 Eaten Ghost = 10 Points)
 * - 3 Lives
 * - Unlimited playable levels with the speed increasing after every few levels to a maximum of level 10
 * - Score goal for each level starting at 100 points, increasing by 20 points until a maximum of 230 points
 * - Sounds (Start game, Eat pellets, Power up, Eat ghost, Eat cherry, Death, Complete level, Teleport)
 * - Additional icons and sound effects (Power up, Portal, Closed gate, Opened gate, Coloured walls, Teleporting SFX, Power up SFX)
 * - Theme: Walls change colour after every few levels until level 13
 */

public class PacManGame {

	// Main method
	public static void main(String[] args) {

		// Creates PacManGUI
		new PacManGUI();

	}

}
