package logic;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class generates a maze of a given side. The maze contains walls, hallways, a starting point and an ending point.
 * The ending point loads the next maze or terminates the game. More on that in the LevelHandler-class.
 * // TODO author
 */
public class Maze {
    private final int xLength;  // Number of tiles in x-direction
    private final int yLength;  // Number of tiles in y-direction
    private final int generationChance;

    private int[] startingPoint;    // Location of the point where the player starts initially
    private int[] exit; // Location of the tile which has to be reached

    /*
     * The integers should only have the values [0;2]
     * 0: Wall -> not passable
     * 1: Hallway -> passable
     * 2: Exit -> passable
     */
    private final int[][] map;

    /**
     * @param xLength // Length in x-direction
     * @param yLength // Length in y-direction
     */
    public Maze(int xLength, int yLength, int generationChance) {
        this.xLength = xLength;
        this.yLength = yLength;
        this.generationChance = generationChance;
        map = new int[xLength][yLength];
        makeMaze(); // Fills map out with different numbers which stand for different tiles which are to be placed
        findStartingPoint();    // On bottom left
        findExit(); // On top right
    }

    public void makeMaze() {
        makeBlank();
        growingTree();
    }

    /**
     * Fills the entire map with walls. The hallways are to be "cut-out"
     */
    private void makeBlank() {
        for (int i = 0; i < xLength; i++) {
            for (int j = 0; j < yLength; j++) {
                map[i][j] = 0;
            }
        }
    }

    /**
     * This method generates the maze using the growing tree algorithm. Follow this link for more information:
     * https://weblog.jamisbuck.org/2011/1/27/maze-generation-growing-tree-algorithm.
     * In the second point mentioned on the webpage we have a 50:50 chance of choosing a random tile or the newest.
     * Through that we can achieve properties of both: short dead-ends and longer corridors. We also want to have a wall
     * of thickness two around the entire maze, which is why we avoid certain values for the indices.
     */
    private void growingTree() {
        // Stores the indices of all tiles which are in the list which is equivalent to C from the webpage
        ArrayList<int[]> tree = new ArrayList<>();
        // Choose random tile from map and add it to tree
        int iRandom = ThreadLocalRandom.current().nextInt(2, xLength - 1);
        int jRandom = ThreadLocalRandom.current().nextInt(2, yLength - 1);
        int[] randomTileIndex = {iRandom, jRandom};
        tree.add(randomTileIndex);
        map[randomTileIndex[0]][randomTileIndex[1]] = 1;    // First hallway

        // Terminates when the rowing tree is empty and can't grow anymore
        while (tree.size() != 0) {
            int[] currentTile;  // This is the tile whose relevant neighbours can become a new hallway
            ArrayList<int[]> relevantNeighbours;

            /*
             * Random number to decide whether to choose random or newest tile.
             * Random tiles lead to more dead-end and choosing the newest leads to better looking, straight corridors.
             * The probability for each method is given by the player through "generationChance"
             */
            int random = ThreadLocalRandom.current().nextInt(0, 101); // Make int between 0  and 100
            if (random < generationChance) {   // Choose the newest tile
                currentTile = tree.get(tree.size() - 1);
                relevantNeighbours = getRelevantNeighbours(currentTile);
                // Remove tile if there are no neighbours
                if (relevantNeighbours.size() == 0) {
                    tree.remove(tree.size() - 1);
                } else {  // Add random relevant neighbour
                    addTileToTree(tree, relevantNeighbours);
                }
            } else {  // Choose random tile from the tree
                int randomTreeTileIndex = ThreadLocalRandom.current().nextInt(0, tree.size());
                currentTile = tree.get(randomTreeTileIndex);
                relevantNeighbours = getRelevantNeighbours(currentTile);
                // Remove tile if there are no neighbours
                if (relevantNeighbours.size() == 0) {
                    tree.remove(randomTreeTileIndex);
                } else {  // Add random relevant neighbour if there are any
                    addTileToTree(tree, relevantNeighbours);
                }
            }
        }
    }

    /**
     * Helper method for growingTree(). It takes the tree and current relevant neighbours and chooses a random neighbour
     * to be added to the tree.
     *
     * @param tree  Tree to be expanded
     * @param relevantNeighbours    Set of tiles from whose we choose a random tile
     */
    private void addTileToTree(ArrayList<int[]> tree, ArrayList<int[]> relevantNeighbours) {
        int randomTile = ThreadLocalRandom.current().nextInt(0, relevantNeighbours.size());
        tree.add(relevantNeighbours.get(randomTile));
        map[relevantNeighbours.get(randomTile)[0]][relevantNeighbours.get(randomTile)[1]] = 1;
    }

    /**
     * This method returns tiles that are adjacent to a given tile. These adjacent tiles can only border one hallway to
     * be considered relevant neighbours.
     *
     * @param tile whose neighbours are wanted
     * @return A list of the indices of all relevant neighbours
     */
    private ArrayList<int[]> getRelevantNeighbours(int[] tile) {
        ArrayList<int[]> neighbours = new ArrayList<>();
        int x = tile[0];
        int y = tile[1];
        // Check for positive x-direction whether values are in range
        if (0 < x + 2 && x + 2 < xLength - 1 &&  // If x+2 is in range so is x+1
                0 < y + 1 && y + 1 < yLength - 1 && 0 < y - 1 && y - 1 < yLength - 1) {
            // Check whether neighbours are also relevant/walls
            if (map[x + 1][y] == 0 && map[x + 2][y] == 0 && map[x + 1][y + 1] == 0 && map[x + 1][y - 1] == 0) {
                neighbours.add(new int[]{x + 1, y});
            }
        }
        // Check for negative x-direction whether values are in range
        if (0 < x - 2 && x - 2 < xLength - 1 &&  // If x-2 is in range so is x-1
                0 < y + 1 && y + 1 < yLength - 1 && 0 < y - 1 && y - 1 < yLength - 1) {
            // Check whether neighbours are also relevant/walls
            if (map[x - 1][y] == 0 && map[x - 2][y] == 0 && map[x - 1][y + 1] == 0 && map[x - 1][y - 1] == 0) {
                neighbours.add(new int[]{x - 1, y});
            }
        }
        // Check for positive y-direction whether values are in range
        if (0 < y + 2 && y + 2 < yLength - 1 &&  // If y+2 is in range so is y+1
                0 < x + 1 && x + 1 < xLength - 1 && 0 < x - 1 && x - 1 < yLength - 1) {
            // Check whether neighbours are also relevant/walls
            if (map[x][y + 1] == 0 && map[x][y + 2] == 0 && map[x + 1][y + 1] == 0 && map[x - 1][y + 1] == 0) {
                neighbours.add(new int[]{x, y + 1});
            }
        }
        // Check for negative y-direction whether values are in range
        if (0 < y - 2 && y - 2 < yLength - 1 &&  // If y-2 is in range so is y-1
                0 < x + 1 && x + 1 < xLength - 1 && 0 < x - 1 && x - 1 < yLength - 1) {
            // Check whether neighbours are also relevant/walls
            if (map[x][y - 1] == 0 && map[x][y - 2] == 0 && map[x + 1][y - 1] == 0 && map[x - 1][y - 1] == 0) {
                neighbours.add(new int[]{x, y - 1});
            }
        }
        return neighbours;
    }

    /**
     * @return map as a 2D array of ints
     */
    public int[][] getMap() {
        return map;
    }

    /**
     * This method finds a random starting point for the player. It is in the bottom left
     */
    private void findStartingPoint() {
        outerloop:  // Label this because we want to terminate the entire loop and not just the inner one
        for (int i = xLength - 1; i >= 0; i--) {
            for (int j = 0; j < yLength; j++) {
                if (map[i][j] != 0) {
                    startingPoint = new int[]{i, j};
                    break outerloop;
                }
            }
        }
    }

    /**
     * Find a random exit. When touched the program ends. It is in the top right
     */
    private void findExit() {
        outerloop:  // Label this because we want to terminate the entire loop and not just the inner one
        for (int i = 0; i < xLength; i++) {
            for (int j = yLength - 1; j >= 0; j--) {
                if (map[i][j] != 0) {
                    exit = new int[]{i, j};
                    map[i][j] = 2;
                    break outerloop;
                }
            }
        }
    }

    /**
     * @return startingPoint
     */
    public int[] getStartingPoint() {
        return startingPoint;
    }

    /**
     * Checks whether a given tile/its location is the exit-tile
     * @param checkLoc  location of the tile to be checked
     * @return  whether it is the exit (true) or not (false)
     */
    public boolean isExit(int[] checkLoc) {
        return checkLoc[0] == exit[0] && checkLoc[1] == exit[1];
    }
}