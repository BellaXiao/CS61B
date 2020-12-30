package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;

import byog.TileEngine.Tileset;
import java.util.*;





public class Game {
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().

        // ToDo: parse the input string to SEED number
        String seed = "";
        int i = 1;
        char[] inputChar = input.toLowerCase().toCharArray();
        while (inputChar[i] != 's') {
            seed += inputChar[i];
            i += 1;
        }
        long SEED = Long.parseLong(seed);
        Random RANDOM = new Random(SEED);

        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);


        // Test: initialize the world with tileset.nothing
        TETile[][] finalWorldFrame = RandomWorld.nothingWorld(WIDTH, HEIGHT);
        // set WIDTH and HEIGHT in RandomWorld package
        RandomWorld.WIDTH = WIDTH;
        RandomWorld.HEIGHT = HEIGHT;
        RandomWorld.addNRect(finalWorldFrame, 50, Tileset.WALL, Tileset.FLOOR, RANDOM);
        //Your game should NOT render any tiles or play any sound when played with playWithInputString().
        //ter.renderFrame(finalWorldFrame);

        //TETile[][] finalWorldFrame = null;
        return finalWorldFrame;
    }
}
