package ux;

import javax.swing.*;
import java.awt.*;

/**
 * This class is the JFrame which will contain the mainPanel and with it the actual game.
 * // TODO author
 */
public class MyGameFrame extends JFrame {

    private final JPanel mainPanel;

    public MyGameFrame(int x, int y) {
        // Get screenSize to make frame square
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        // All content will be on one big JPanel, as panels are easier to manage than drawing directly in the JFrame
        this.mainPanel = new JPanel();
        // Arranges all added components in an x by y checkerboard-pattern
        this.mainPanel.setLayout(new GridLayout(x, y));
        this.mainPanel.setBackground(Color.BLACK);
        this.add(mainPanel);
        // Make it a little smaller than possible to avoid overlaps
        this.setSize(screenSize.height - 50, screenSize.height - 50);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Terminate program when JFrame is closed
        this.setResizable(false);   // Make the size fixed or else image scaling becomes a lot harder
        this.setTitle("MazeEscape");
        // https://opengameart.org/content/a-blocky-dungeon
        this.setIconImage(new ImageIcon("src/assets/PlayerSprite.png").getImage());
        this.setVisible(true);
    }

    /**
     * Provides a method to add to mainPanel
     *
     * @param component JComponent with content to be added to grid; is always the tiles of the map
     */
    public void addToMainPanel(JComponent component) {
        mainPanel.add(component);
        mainPanel.revalidate(); // Update after each new component is added
    }
}