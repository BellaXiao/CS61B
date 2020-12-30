package byog.lab6;

import edu.princeton.cs.introcs.StdDraw;
import edu.princeton.cs.introcs.StdStats;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
        /*//Test:
        String randStr = game.generateRandomString(5);
        System.out.println(randStr);
        game.flashSequence(randStr);*/
    }

    public MemoryGame(int width, int height, int seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        //TODO: Initialize random number generator
        rand = new Random(seed);
    }

    public String generateRandomString(int n) {
        //TODO: Generate random string of letters of length n
        String res = "";
        while (res.length() < n) {
            int index = rand.nextInt(CHARACTERS.length);
            res += CHARACTERS[index];
        }
        return res;
    }

    public void drawFrame(String s) {
        //TODO: Take the string and display it in the center of the screen
        StdDraw.clear(Color.BLACK);

        if (!gameOver) {
            Font fontSide = new Font("Arial", Font.BOLD, 20);
            StdDraw.setFont(fontSide);
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.textLeft(1, height - 1, "Round: " + round);
            StdDraw.text(width /2 , height - 1, playerTurn ? "Type!" : "Watch!");
            StdDraw.textRight(width - 1, height - 1, ENCOURAGEMENT[round % ENCOURAGEMENT.length]);
            StdDraw.line(0, height - 2, width, height - 2);
        }


        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.YELLOW);
        StdDraw.text(width / 2, height / 2, s);
        StdDraw.show();





        //TODO: If game is not over, display relevant game information at the top of the screen
    }

    public void flashSequence(String letters) {
        //TODO: Display each character in letters, making sure to blank the screen between letters
        for (char c: letters.toCharArray()) {
            drawFrame(String.valueOf(c));
            StdDraw.pause(1000);
            drawFrame("");
            StdDraw.pause(500);
        }

    }

    public String solicitNCharsInput(int n) {
        //TODO: Read n letters of player input
        int i = 1;
        String res = "";
        drawFrame(res); // add this to change watch to type in the sidebar before user typing in.

        while (i <= n) {
            if (StdDraw.hasNextKeyTyped()) {
                res += String.valueOf(StdDraw.nextKeyTyped());
                drawFrame(res);
                i += 1;
            }
        }

        StdDraw.pause(500);
        return res;

        /*String input = "";
        drawFrame(input);

        while (input.length() < n) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            input += String.valueOf(key);
            drawFrame(input);
        }
        StdDraw.pause(500);
        return input;*/
    }

    public void startGame() {
        //TODO: Set any relevant variables before the game starts
        round = 1;
        gameOver = false;
        playerTurn = false;
        //TODO: Establish Game loop
        while (!gameOver) {
            playerTurn = false;
            String roundStr = "Round: " + round;
            drawFrame(roundStr);
            StdDraw.pause(1500);

            String randStr = generateRandomString(round);
            flashSequence(randStr);

            playerTurn = true;
            String playerStr = solicitNCharsInput(round);
            if (!playerStr.equals(randStr)) {
                gameOver = true;
                drawFrame("Game Over! Final level: " + round);
            } else {
                drawFrame("Correct, well done!");
                StdDraw.pause(1500);
            }
            round += 1;
        }
    }

}
