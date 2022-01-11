package logic;

/**
 * This class is the logical representation of the player. It manages the location of the player and whether he can move
 * in certain directions or not.
 * // TODO author
 */
public class Player {
    private final int[] location; // Where the player stand on the map
    // True if these neighbours of the player are hallways
    private boolean right;  // True if the player could go right
    private boolean left;   // True if the player could go left
    private boolean down;   // True if the player could go down
    private boolean up; // True if the player could go up
    private final int[][] map;  // The map on which the player is moving. See more on the according class

    /**
     * @param location Where the player is
     * @param map      Entire map used in the game
     */
    public Player(int[] location, int[][] map) {
        this.location = location;
        this.map = map;
        findNeighbours();
    }

    /**
     * Initializes the direction-booleans by checking the mao values of the adjacent tiles
     */
    private void findNeighbours() {
        // We don't need to check if it is in range as the maze is surrounded by walls
        right = map[location[0]][location[1] + 1] != 0;
        left = map[location[0]][location[1] - 1] != 0;
        down = map[location[0] + 1][location[1]] != 0;
        up = map[location[0] - 1][location[1]] != 0;
    }

    public void moveRight() {
        if (right) {
            location[1]++;
            findNeighbours();
        }
    }

    public void moveLeft() {
        if (left) {
            location[1]--;
            findNeighbours();
        }
    }

    public void moveDown() {
        if (down) {
            location[0]++;
            findNeighbours();
        }
    }

    public void moveUp() {
        if (up) {
            location[0]--;
            findNeighbours();
        }
    }

    /**
     * @return The players location
     */
    public int[] getLocation() {
        return location;
    }

    /**
     * @return A summary of all booleans which tell where the player is near a hallway-tile
     */
    public boolean[] getNeighbourBooleans() {
        return new boolean[]{right, left, down, up};
    }
}