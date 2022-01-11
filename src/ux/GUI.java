package ux;

import logic.GameState;
import logic.Maze;
import logic.Player;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

/**
 * This class combines the basic GUI with the GameState. It also contains the termination condition for this program
 * // TODO author
 */
public class GUI {

    // How long the program has been running in milliseconds
    private double runtime;
    private JLabel[][] tiles; // The actual tiles which make up the maze and contain sprites
    private MyGameFrame frame;
    private int size;   // Size of the square maze, entered through a JOptionPane
    // Chance of two different algorithms being used -> different look & feel. Entered through a JOptionsPane
    private int generationChance;
    private Maze maze;
    private Player player;
    private Color backgroundColor;    // Color used on all tiles as a background color

    public void build() {
        configuration();    // JOptionsPane for instructions and settings
        frame = new MyGameFrame(size, size);
        GameState gameState = new GameState(size, size, generationChance);
        backgroundColor = new Color(66, 91, 121);
        maze = gameState.getMaze();
        player = gameState.getPlayer();
        tiles = new JLabel[size][size];

        // Set background on all tiles and add them
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                tiles[i][j] = new JLabel();
                tiles[i][j].setOpaque(true);
                Color backgroundColor = new Color(66, 91, 121);
                tiles[i][j].setBackground(backgroundColor);

                // Add painted JLabel to mainPanel
                frame.addToMainPanel(tiles[i][j]);
            }
        }
        // Load frame new because components on it only have a size when displayed, and we need this size for the image scaling
        frame.validate();
        frame.repaint();
        // Scale and add images to tiles
        addAssets();
        // Start runtime
        runtime = 0.0;
    }

    /**
     * This method is executed at very first and provides instructions and options for the player to choose from.
     */
    private void configuration() {
        // Make custom JPanel to add to the JOptionPane
        JPanel optionsPanel = new JPanel();
        // BoxLayout to make a "tower" of JComponents that stacks vertically
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.PAGE_AXIS));
        // Add instructions
        optionsPanel.add(new JLabel("Move the red kndight with WASD through the maze to the ladder to escape the maze." +
                "The faster the better!"));
        // Add option for maze-style as a JSlider
        optionsPanel.add(new JLabel("Choose the style of your maze"));
        JSlider slider = new JSlider(0, 100, 50);
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        // Add explanation text to the slider
        labelTable.put(100, new JLabel("Long corridors"));
        labelTable.put(0, new JLabel("Many dead-ends"));
        slider.setLabelTable(labelTable);
        slider.setPaintLabels(true);
        optionsPanel.add(slider);
        // Add caption for option for side length
        optionsPanel.add(new JLabel("Enter the side length for the square maze as a positive integer bigger than 5. Sizes up to 100 work the best."));

        JOptionPane dialog = new JOptionPane();
        dialog.add(optionsPanel);

        String sizeAsString = JOptionPane.showInputDialog(optionsPanel);
        // These things are only true if cancel button has been pressed because "Okay" causes sizeAsString = ""
        if (sizeAsString == null) {
            System.exit(0);
        }

        // Parse String to int. Also add 4 because on each side we have two tiles of buffer which is not used for the maze
        try {
            this.size = Integer.parseInt(sizeAsString) + 4;
            // True if input yb the user was smaller than 5
        }
        // User entered wrong info
        catch (Exception e) {
            e.printStackTrace();
            this.size = 24; // Standard size
        }
        this.generationChance = slider.getValue();
    }

    /**
     * This method updates the player graphically, as it is the only thing that can change. For that it removes the
     * players' sprite from a tile, so it looks like the player moves
     */
    public void update() {
        runtime += 8;   // Count elapsed time
        int[] newPlayerLoc = player.getLocation();   // Current location of the player
        // https://opengameart.org/content/a-blocky-dungeon
        // Changes hallways which could have had an icon back to the background color; watch out for exit
        ScaledImageFactory imageScalar = new ScaledImageFactory();
        int wantedWidth = tiles[newPlayerLoc[0]][newPlayerLoc[1]].getWidth();
        int wantedHeight = tiles[newPlayerLoc[0]][newPlayerLoc[1]].getHeight();
        ImageIcon playerSprite = imageScalar.getScaledImage(new ImageIcon("src/assets/PlayerSprite.png"),
                wantedWidth, wantedHeight);
        tiles[newPlayerLoc[0]][newPlayerLoc[1]].setIcon(playerSprite);

        boolean[] areRelevantHallways = player.getNeighbourBooleans();
        // Remove sprites right of player if it is not the exit
        if (areRelevantHallways[0]) {
            if (!maze.isExit(new int[]{newPlayerLoc[0], newPlayerLoc[1] + 1})) {
                tiles[newPlayerLoc[0]][newPlayerLoc[1] + 1].setIcon(null);
            }
        }
        // Color hallway left of player if it is not the exit
        if (areRelevantHallways[1]) {
            if (!maze.isExit(new int[]{newPlayerLoc[0], newPlayerLoc[1] - 1})) {
                tiles[newPlayerLoc[0]][newPlayerLoc[1] - 1].setIcon(null);
            }
        }
        // Color hallway below player if it is not the exit
        if (areRelevantHallways[2]) {
            if (!maze.isExit(new int[]{newPlayerLoc[0] + 1, newPlayerLoc[1]})) {
                tiles[newPlayerLoc[0] + 1][newPlayerLoc[1]].setIcon(null);
            }
        }
        // Color hallway above player if it is not the exit
        if (areRelevantHallways[3]) {
            if (!maze.isExit(new int[]{newPlayerLoc[0] - 1, newPlayerLoc[1]})) {
                tiles[newPlayerLoc[0] - 1][newPlayerLoc[1]].setBackground(backgroundColor);
                tiles[newPlayerLoc[0] - 1][newPlayerLoc[1]].setIcon(null);
            }
        }
        // Check whether player has reached exit
        if (maze.isExit(newPlayerLoc)) {
            int reply = JOptionPane.showConfirmDialog(frame, "You have finished in " + runtime / 1000 + " seconds!\n" +
                    "Do you want to continue?", "Continue?", JOptionPane.YES_NO_OPTION);
            // Start a new game
            if (reply == JOptionPane.YES_OPTION) {
                frame.dispose();    // Throw current GUI away
                build();
            } else {  // Close program
                System.exit(0);
            }
        }
    }

    /**
     * This method is executed after all JComponents have been added because now we know the fixed sizes of all
     * these components. With this information we can now scale and add all images/assets accordingly.
     */
    private void addAssets() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int tileID = maze.getMap()[i][j];
                // Scale assets such that they are just as big as the JLabel they are in
                ScaledImageFactory imageScalar = new ScaledImageFactory();
                int wantedWidth = tiles[i][j].getWidth();
                int wantedHeight = tiles[i][j].getHeight();
                ImageIcon wall = new ImageIcon("src/assets/Wall.png");
                wall = imageScalar.getScaledImage(wall, wantedWidth, wantedHeight);
                ImageIcon exit = new ImageIcon("src/assets/Exit.png");
                exit = imageScalar.getScaledImage(exit, wantedWidth, wantedHeight);
                switch (tileID) {
                    // https://opengameart.org/content/a-blocky-dungeon
                    case 0 -> tiles[i][j].setIcon(wall);   // Paint walls
                    // Hallways stay in the background color
                    case 2 -> tiles[i][j].setIcon(exit);    // Paint exit point
                }
            }
        }
    }
}