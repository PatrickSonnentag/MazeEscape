package logic;

import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;

/**
 * This class combines all logic. It uses a lambda keyListener as only the players inputs can change the game state.
 * // TODO author
 */
public class GameState {

    private final Maze maze;
    private final Player player;

    public GameState(int x, int y, int generationChance) {
        maze = new Maze(x, y, generationChance);
        player = new Player(maze.getStartingPoint(), maze.getMap());
        inputChecker();
    }

    /**
     * Implements an event listener through a lambda method as a KeyListener needs to be added to a JFrame which
     * means we would have to mash up logic & graphics.
     */
    public void inputChecker() {
        AWTEventListener listener = event -> {
            KeyEvent evt = (KeyEvent) event;
            // True if any key has been pressed. Without this the player doesn't stop moving until a wall is hit
            if(evt.getID() == KeyEvent.KEY_PRESSED) {
                // Move with standard WASD & small easter-egg
                switch (evt.getKeyCode()) {
                    case KeyEvent.VK_W -> player.moveUp();
                    case KeyEvent.VK_A -> player.moveLeft();
                    case KeyEvent.VK_S -> player.moveDown();
                    case KeyEvent.VK_D -> player.moveRight();
                }
            }
        };
        Toolkit.getDefaultToolkit().addAWTEventListener(listener, AWTEvent.KEY_EVENT_MASK);
    }

    public Maze getMaze() {
        return maze;
    }

    public Player getPlayer() {
        return player;
    }
}