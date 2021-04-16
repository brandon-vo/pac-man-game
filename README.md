# pac-man-game
Java Pac-Man game with increasing difficulty

My PacMan game consists of a playable Pac-Man character that uses arrow keys to move and 3 ghosts with 
tracking movement. The goal of the game is to achieve a certain score for each level, starting at 100 
points for the first level with increments of 20 points per level until a maximum score goal of 230 points. 
PacMan can eat the pellets by walking over the item to gain 1 point per pellet. Each set of levels will also 
increase the speed of the game until a maximum game timer speed of 130 milliseconds after level 10. 
On the board, there are 4 power ups which gives the player 9 seconds to eat the ghosts, giving 10 points per ghost. 
A cherry item is also active, giving 10 points to the score. If a ghost collides with PacMan, it will take away 
1 out of the 3 lives. If 1 life is avaliable and the player gets eaten, the game will end and prompt the user 
with a restart and quit button. The score is also checked to see if it is a highscore.
