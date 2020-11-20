package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.*;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class RandomWorld {
    private static final int WIDTH = 80;
    private static final int HEIGHT = 30;

    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    private static final int minWidth = 3;
    private static final int minHeight = 3;
    private  static  final int maxNRect = 70;

    // adding a set in the class contains all the existed rects in this world.
    private static Set <Rectangular> rectSet = new HashSet<>();

    /**
     * Position Class.
     */
    private static class Position {
        private int x;
        private int y;

        private Position() {
            x = 0;
            y = 0;
        }
        private Position(int x_pos, int y_pos) {
            x = x_pos;
            y = y_pos;
        }
    }


    /**
     * Private func: adjust number out of range to be min or max of the range
     */
    private static int adjInRange(int t, int min, int max) {
        if (min <= t && t <= max) {
            return t;
        } else if (t < min) {
            return min;
        } else {
            return max;
        }
    }



    /**
     * Rectangular Class.
     * Method: Rectangular.isOverlap().
     */
    private static class Rectangular {
        private Position leftDown = new Position();
        private int width;
        private int height;
        private TETile t_out;
        private TETile t_in;

        // ToDo: also add the other 3 vertexes. (Can change isOverlap/ )
        private Position leftUp = new Position();
        private Position rightDown = new Position();
        private Position rightUp = new Position();

        private Rectangular() {
            leftDown.x = 0;
            leftDown.y = 0;
            width = minWidth;
            height = minHeight;
            t_out = Tileset.WALL;
            t_in = Tileset.FLOOR;

            // calculate position of other 3 vertexes
            leftUp.x = leftDown.x;
            leftUp.y = leftDown.y + height - 1;
            rightDown.x = leftDown.x + width -1;
            rightDown.y = leftDown.y;
            rightUp.x = rightDown.x;
            rightUp.y = leftUp.y;
        }
        private Rectangular(Position leftDown_pos, int wid, int hei, TETile t_o, TETile t_i) {
            /** adjusted leftDown position and width, height into desired range for this world
             *  Meaning any rectangular generated in this world should satisfy the below constraints.
             *  The leftDown, width and height range here guarantee the rectangular is inside the world with minimum width/height as minWidth/minHeight.
             */
            // 0 <= leftDown.x / leftDown.y <= WIDTH - minWidth - 1 / HEIGHT - minHeight - 1
            // minWidth / minHeight <= width / height <= WIDTH - leftDown.x - 1 / HEIGHT - leftDown.y - 1
            if (leftDown_pos != null) {
                leftDown.x = adjInRange(leftDown_pos.x, 0, WIDTH - minWidth - 1);
                leftDown.y = adjInRange(leftDown_pos.y, 0, HEIGHT - minHeight - 1);
            } else {
                System.out.print("Error: LeftDown position provided is null! Initialized with (0,0).");
                leftDown.x = 0;
                leftDown.y = 0;
            }
            width = adjInRange(wid, minWidth, WIDTH - leftDown.x - 1 );
            height = adjInRange(hei, minHeight, HEIGHT - leftDown.y - 1);
            t_out = t_o;
            t_in = t_i;
            // calculate position of other 3 vertexes
            leftUp.x = leftDown.x;
            leftUp.y = leftDown.y + height - 1;
            rightDown.x = leftDown.x + width -1;
            rightDown.y = leftDown.y;
            rightUp.x = rightDown.x;
            rightUp.y = leftUp.y;
        }

        /**
         * Rectangular class method: check whether two rects overlap with each other
         */
        private static boolean isOverlap(Rectangular a, Rectangular b) {
            // swap size smaller one to be a, larger one to be b
            if (a.width * a.height > b.width * b.height) {
                Rectangular temp = a;
                a = b;
                b = temp;
            }
            // the range of XY b covers
            int otherXmin = b.leftDown.x;
            int otherXmax = otherXmin + b.width - 1;
            int otherYmin = b.leftDown.y;
            int otherYmax = otherYmin + b.height - 1;
            // loop the edge of a, if any point is in b XY range then overlap
            int yLow = a.leftDown.y;
            int yHigh = yLow + a.height - 1;
            int xLeft = a.leftDown.x;
            int xRight = xLeft + a.width - 1;
            // check long edge
            for (int i = a.leftDown.x; i < a.leftDown.x + a.width; i++) {
                if (otherXmin <= i && i <= otherXmax &&
                        ((otherYmin <= yLow && yLow <= otherYmax) || (otherYmin <= yHigh && yHigh <= otherYmax))) {
                    return true;
                }
            }
            // check wide edge
            for (int i = a.leftDown.y; i < a.leftDown.x + a.height; i++) {
                if (otherYmin <= i && i <= otherYmax &&
                        ((otherYmin <= xLeft && xLeft <= otherYmax) || (otherYmin <= xRight && xRight <= otherYmax))) {
                    return true;
                }
            }
            // if 4 edges of a are all not in b range XY, another special case is the whole a is inside b
            // check whether center of a is inside b to determine whether overlap
            double xCenter = (xLeft + xRight) / 2;
            double yCenter = (yLow + yHigh) / 2;
            if (otherXmin <= xCenter && xCenter <= otherXmax && otherYmin <= yCenter && yCenter <= otherYmax) {
                return true;
            }
            return false;
        }
    }

    /** Decide whether a rectangular overlaps with any existed rects in this world (in the rectSet)
     */
    private static boolean isOverlapSet (Rectangular r) {
        for (Rectangular existedRect : rectSet) {
            if (Rectangular.isOverlap(r, existedRect)) {
                return true;
            }
        }
        return false;
    }




    /**
     * Draw one rectangular frame in world, with leftDown corner starts at position leftDown.
     * Consists of random  TETile and the color will change slightly.
     * If the width or the height exceeds the world size, constrain it within the size of the world.
     * @param world
     * @param rect: the rectangular to draw
     */

    private static void addOneRect(TETile[][] world, Rectangular rect) {
        // handle exceptions
        /* No need for this, since all these are handled within Rectangular constructor.
        if (rect.leftDown==null || rect.leftDown.x < 0 || rect.leftDown.y < 0 || rect.width <= 0 || rect.height <= 0) {
            throw new IllegalArgumentException("Inappropriate starting position or width/height.");
        }
        // draw the edge using t_out
        // since the random leftdown position is limited by minWidth and minHeight,
        // thus the edge case is rectangular on the world side with wid or height >= 3.
        int adjWidth = Math.min(rect.width + 1, WIDTH - rect.leftDown.x) - 1;
        int adjHeight = Math.min(rect.height + 1, HEIGHT - rect.leftDown.y) - 1;*/

        for (int j = 0; j < rect.width; j += 1) {
            world[rect.leftDown.x + j][rect.leftDown.y] = TETile.colorVariant(rect.t_out, 50, 50, 50, RANDOM);
            world[rect.leftDown.x + j][rect.leftUp.y] = TETile.colorVariant(rect.t_out, 50, 50, 50, RANDOM);
        }
        for (int i = 0; i < rect.height; i += 1) {
            world[rect.leftDown.x][rect.leftDown.y + i] = TETile.colorVariant(rect.t_out, 50, 50, 50, RANDOM);
            world[rect.rightDown.x][rect.leftDown.y + i] = TETile.colorVariant(rect.t_out, 50, 50, 50, RANDOM);
        }

        // draw the inside part using t_in
        for (int j = 1; j < rect.width - 1; j += 1) {
            for (int i = 1; i < rect.height - 1; i += 1) {
                world[rect.leftDown.x + j][rect.leftDown.y + i] = TETile.colorVariant(rect.t_in, 50, 50, 50, RANDOM);
            }
        }
        // add this rectangular into the rectSet
        rectSet.add(rect);
    }


    /**
     * add N squares in the world with random starting position and random width and height.
     * @param world
     * @param N： generate N non-overlap rectangular
     * @param t_out: using TETile t_out as outside edge
     * @param t_in: using TETile t_in as inside
     */
    public static void addNRect(TETile[][] world, int N, TETile t_out, TETile t_in) {
        int count = 0;
        // if don't do this, The N could be too large that we can't have that many non-overlap rects in the whole world,
        // and goes into infinite loop. Theoretically, the max count can be WIDTH / 3 * HEIGHT / 3, but it's too slow and too much is meanless,
        // so just set the max Count to be 70. (set maxNRect as a class variable.)
        int maxCount = Math.min(maxNRect, N);
        while (count < maxCount) {
            // No need to adjust leftDown and width, height, thoes constraints are handled within Rectangular Constructor.
            int x = RANDOM.nextInt(WIDTH);
            int y = RANDOM.nextInt(HEIGHT);
            int width = RANDOM.nextInt(WIDTH/3);
            int height = RANDOM.nextInt(HEIGHT/3);
            Position p = new Position(x, y);
            Rectangular r = new Rectangular(p, width, height, t_out, t_in);
            // only add r that doesn't overlap with all the existed rectangulars in this world rectSet.
            if (!isOverlapSet(r)) {
                addOneRect(world, r);
                count += 1;
            }
        }
    }







    /** Picks a RANDOM tile with a 33% change of being
     *  a wall, 33% chance of being a flower, and 33%
     *  chance of being empty space.
     *  (method copied from RandomWorldDemo.java)
     */
    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(4);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.SAND;
            case 3: return Tileset.GRASS;
            default: return Tileset.NOTHING;
        }
    }

    /**
     * Initialize the world with TETile.nothing
     */
    public static TETile[][] nothingWorld(int width, int height) {
        TETile[][] newWorld = new TETile[width][height];
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                newWorld[x][y] = Tileset.NOTHING;
            }
        }
        return newWorld;
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // Test: initialize the world with tileset.nothing
        TETile[][] newWorld = nothingWorld(WIDTH, HEIGHT);

        // Test: addOneRect and addNRect (non-Overlap)
        //addOneRect(newWorld, p, 10, 5, Tileset.WALL, Tileset.FLOOR);
        addNRect(newWorld, 50, Tileset.WALL, Tileset.FLOOR);
        /*// Test Rectangular datatype and isOverlap method. -- Success!
        int wid = 30;
        int hei = 20;
        Position p1 = new Position(5, 5);
        Position p2 = new Position(34,24);
        Rectangular r1 = new Rectangular(p1, wid, hei);
        Rectangular r2 = new Rectangular(p2, 2, 3);
        addOneRect(newWorld, r1);
        addOneRect(newWorld, r2);

        if (Rectangular.isOverlap(r2, r1)) {
            System.out.print("They are Overlap!");
        }
        if (Rectangular.isOverlap(r1, r2)) {
            System.out.print("They are Overlap!");
        }*/

        ter.renderFrame(newWorld);

    }


}
