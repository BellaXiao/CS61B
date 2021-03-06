package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import org.w3c.dom.css.Rect;

import java.util.*;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class RandomWorld {
    /* Feel free to change the width and height. */
    public static int WIDTH = 80;
    public static int HEIGHT = 30;

    //public static long SEED = 2873123;
    //public static Random RANDOM = new Random(SEED);

    public static final int minWidth = 3;
    public static final int minHeight = 3;
    public  static  final int maxNRect = 100;

    // adding a list in the class contains all the existed rects in this world.
    // can'y use set, otherwise the world will be different even with same SEED, because of no order in set.
    public static List <Rectangular> rectSet = new LinkedList<>();


    /**
     * Position Class.
     */
    public static class Position {
        public int x;
        public int y;

        public Position() {
            x = 0;
            y = 0;
        }
        public Position(int x_pos, int y_pos) {
            x = x_pos;
            y = y_pos;
        }
    }


    /**
     * public func: adjust number out of range to be min or max of the range
     */
    public static int adjInRange(int t, int min, int max) {
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
    public static class Rectangular {
        public Position leftDown = new Position();
        public int width;
        public int height;
        public TETile t_out;
        public TETile t_in;

        // ToDo: also add the other 3 vertexes. (Can change isOverlap/ )
        public Position leftUp = new Position();
        public Position rightDown = new Position();
        public Position rightUp = new Position();

        public Rectangular() {
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
        public Rectangular(Position leftDown_pos, int wid, int hei, TETile t_o, TETile t_i) {
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

        // Add another Rectangular constructor, this one is rectangular used for connection, which has width or height < 3 , equal to 2.
        public Rectangular(Position leftDown_pos, int wid, int hei, TETile t) {
            if (leftDown_pos != null) {
                leftDown.x = adjInRange(leftDown_pos.x, 0, WIDTH  - 1);
                leftDown.y = adjInRange(leftDown_pos.y, 0, HEIGHT - 1);
            } else {
                System.out.print("Error: LeftDown position provided is null! Initialized with (0,0).");
                leftDown.x = 0;
                leftDown.y = 0;
            }
            width = adjInRange(wid, 0, WIDTH - leftDown.x - 1 );
            height = adjInRange(hei, 0, HEIGHT - leftDown.y - 1);
            t_out = t;
            t_in = t;
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
        public static boolean isOverlap(Rectangular a, Rectangular b) {
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
            for (int i = a.leftDown.y; i < a.leftDown.y + a.height; i++) {
                if (otherYmin <= i && i <= otherYmax &&
                        ((otherXmin <= xLeft && xLeft <= otherXmax) || (otherXmin <= xRight && xRight <= otherXmax))) {
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

        /**
         * Rectangular class method: check whether two rects are adjacent to each other.
         * first requires two rects are non-overlap, then adjacent to each other and has common length >= 3.
         */
        public static boolean isAdjacent(Rectangular a, Rectangular b) {
            if (!Rectangular.isOverlap(a, b)) {
                //System.out.println("is not overlap.");
                boolean con1 = (b.leftDown.x == a.rightDown.x + 1);
                boolean con2 = (b.rightDown.x == a.leftDown.x - 1);
                boolean con3 = (b.leftUp.y >= a.leftDown.y + minHeight - 1 && b.leftDown.y <= a.leftUp.y - (minHeight - 1));
                if ((con1 || con2) && con3 ) {
                    return true;
                }
                con1 = (b.leftDown.y == a.rightUp.y + 1);
                con2 = (b.rightUp.y == a.leftDown.y - 1);
                con3 = (b.rightUp.x >= a.leftDown.x + minWidth - 1 && b.leftDown.x <= a.rightUp.x - (minWidth - 1));
                if ((con1 || con2) && con3 ) {
                    return true;
                }

            }
            //System.out.println("is overlap???");
            return false;
        }




    }




    /**
     * ToDo: connect adjacent rects
     * first need to make sure rects isAdjacent, and then replace the tile within the adjacent range.
     */
    public static void addConnection (TETile[][] world, Rectangular existR, Rectangular r, TETile t, Random RANDOM) {
        if (!Rectangular.isAdjacent(existR, r)) {
            System.out.println("Can only add connection to adjacent rectangulars.");
            return;
        }
        boolean rInLeft = (existR.leftDown.x == r.rightDown.x + 1);
        boolean rInRight = (existR.rightDown.x == r.leftDown.x - 1);
        int lowY = Math.max(r.leftDown.y, existR.leftDown.y);
        int highY = Math.min(r.leftUp.y, existR.leftUp.y);
        int replaceHei = highY - lowY - 1;

        boolean rInDown = (existR.leftDown.y == r.rightUp.y + 1);
        boolean rInUp  = (existR.rightUp.y == r.leftDown.y - 1);
        int leftX = Math.max(r.leftDown.x, existR.leftDown.x);
        int rightX = Math.min(r.rightUp.x, existR.rightUp.x);
        int replaceWid = rightX - leftX - 1;

        Rectangular connectR = new Rectangular();

        if (rInLeft) {
            Position leftdown = new Position(r.rightDown.x, lowY + 1);
            connectR = new Rectangular(leftdown, 2, replaceHei, t);
            //System.out.format("rInLeft: %d, %d, %d", leftdown.x,leftdown.y,replaceHei);
        } else if (rInRight) {
            Position leftdown = new Position(existR.rightDown.x, lowY + 1);
            connectR = new Rectangular(leftdown, 2, replaceHei, t);
        } else if (rInDown) {
            Position leftdown = new Position(leftX + 1, r.leftUp.y);
            connectR = new Rectangular(leftdown, replaceWid, 2, t);
        } else if (rInUp) {
            Position leftdown = new Position(leftX + 1, existR.leftUp.y);
            connectR = new Rectangular(leftdown, replaceWid, 2, t);
        }
        addOneRect(world, connectR, RANDOM);

    }

    /** Decide whether a rectangular overlaps with any existed rects in this world (in the rectSet)
     */
    public static boolean isOverlapSet (Rectangular r) {
        for (Rectangular existedRect : rectSet) {
            if (Rectangular.isOverlap(r, existedRect)) {
                return true;
            }
        }
        return false;
    }

    /** Decide whether a rectangular is adjacent to any existed rects in this world (in the rectSet)
     * if we find one, return that rectangular, if no one is found, return null.
     */
    public static Rectangular isAdjacentSet (Rectangular r) {
        for (Rectangular existedRect : rectSet) {
            if (Rectangular.isAdjacent(r, existedRect)) {
                return existedRect;
            }
        }
        return null;
    }




    /**
     * Draw one rectangular frame in world, with leftDown corner starts at position leftDown.
     * Consists of random  TETile and the color will change slightly.
     * If the width or the height exceeds the world size, constrain it within the size of the world.
     * @param world
     * @param rect: the rectangular to draw
     */

    public static void addOneRect(TETile[][] world, Rectangular rect, Random RANDOM) {
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
            world[rect.leftDown.x + j][rect.leftDown.y] = TETile.colorVariant(rect.t_out, 32, 32, 32, RANDOM);
            world[rect.leftDown.x + j][rect.leftUp.y] = TETile.colorVariant(rect.t_out, 32, 32, 32, RANDOM);
        }
        for (int i = 0; i < rect.height; i += 1) {
            world[rect.leftDown.x][rect.leftDown.y + i] = TETile.colorVariant(rect.t_out, 32, 32, 32, RANDOM);
            world[rect.rightDown.x][rect.leftDown.y + i] = TETile.colorVariant(rect.t_out, 32, 32, 32, RANDOM);
        }

        // draw the inside part using t_in
        for (int j = 1; j < rect.width - 1; j += 1) {
            for (int i = 1; i < rect.height - 1; i += 1) {
                world[rect.leftDown.x + j][rect.leftDown.y + i] = TETile.colorVariant(rect.t_in, 32, 32, 32, RANDOM);
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
    public static void addNRect(TETile[][] world, int N, TETile t_out, TETile t_in, Random RANDOM) {
        int count = 0;
        // if don't do this, The N could be too large that we can't have that many non-overlap rects in the whole world,
        // and goes into infinite loop. Theoretically, the max count can be WIDTH / 3 * HEIGHT / 3, but it's too slow and too much is meanless,
        // so just set the max Count to be 70. (set maxNRect as a class variable.)
        int maxCount = Math.min(maxNRect, N);
        int loopCount = 0;
        int loopMax = 10000;
        while (count < maxCount) {
            // Sometimes because the first random rectangular is too large or has special location, it's hard to get to N rects.
            // We use loopcount to control the maximum loop time to avoid infinite loop.
            loopCount += 1;
            if (loopCount > loopMax) {
                System.out.format("Loopcount exceeds %d, get %d rectangulars.", loopMax, count);
                break;
            }
            // No need to adjust leftDown and width, height, thoes constraints are handled within Rectangular Constructor.
            int x = RANDOM.nextInt(world.length);
            int y = RANDOM.nextInt(world[0].length);
            // adjust the range for width and height to make the generation quicker,
            // otherwise it takes too much time to have a non-overlap rect accepted.
            int width = RANDOM.nextInt(world.length/5);
            int height = RANDOM.nextInt(world[0].length/5);

            //debug purpose:
            // System.out.format("x:%d, y: %d, width: %d, height: %d\n", x,y,width,height);

            Position p = new Position(x, y);
            Rectangular r = new Rectangular(p, width, height, t_out, t_in);
            // only add r that doesn't overlap with all the existed rectangulars in this world rectSet.
            if (rectSet.isEmpty()) {
                addOneRect(world, r, RANDOM);
                count += 1;
            } else if (isAdjacentSet(r) != null && !isOverlapSet(r)) {
                Rectangular existR = isAdjacentSet(r);
                addOneRect(world, r, RANDOM);
                addConnection(world, existR, r, t_in, RANDOM);
                count += 1;
            }
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
        //addNRect(newWorld, 100, Tileset.WALL, Tileset.FLOOR, args[0]);
        /*// Test Rectangular datatype and isOverlap method. -- Success!
        int wid = 3;
        int hei = 3;
        Position p1 = new Position(5, 27);
        Rectangular r1 = new Rectangular(p1, wid, hei, Tileset.WALL, Tileset.FLOOR);
        Position p2 = new Position(8,27);
        Rectangular r2 = new Rectangular(p2, wid, hei, Tileset.WALL, Tileset.FLOOR);
        addOneRect(newWorld, r1);
        addOneRect(newWorld, r2);
        Rectangular.isAdjacent(r1, r2);
        //System.out.println("Adjacent ran.");
        addConnection (newWorld, r1, r2, Tileset.FLOWER);
        /*Rectangular h1 = addVerticalHallway(r1, Tileset.WALL, Tileset.FLOOR);
        while (isOverlapSet(h1)) {
            h1 = addVerticalHallway(r1, Tileset.WALL, Tileset.FLOOR);
        }
        addOneRect(newWorld,h1);*/

        ter.renderFrame(newWorld);

    }


}
