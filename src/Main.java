import ux.GUI;
import ux.MusicPlayer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This class contains the main method to start the program. That main method calls the GUI. It also calls for a
 * regular update of said GUI; called a ticker. It also allows for an easy measure of time.
 * // TODO author
 */
public class Main {
    public static void main(String[] args) {
        MusicPlayer musicPlayer = new MusicPlayer();
        musicPlayer.play();
        GUI gui = new GUI();
        gui.build();
        Runnable runnableGUI = gui::update;
        Runnable runnableMusic = musicPlayer::checkForNewLoop;

        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
        // Schedules for the runnableGUI to be executed every 16 milliseconds and the runnableMusic every 3 seconds
        ses.scheduleAtFixedRate(runnableGUI, 0, 16, TimeUnit.MILLISECONDS);   // Update with roughly 60FPS
        ses.scheduleAtFixedRate(runnableMusic, 0, 3, TimeUnit.SECONDS);   // Update every 3 seconds
    }
}