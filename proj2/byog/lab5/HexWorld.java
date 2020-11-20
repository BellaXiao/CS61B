package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import javax.management.openmbean.CompositeDataSupport;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;

    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

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
    } // p specifies the lower left corner of the hexagon.

    /** Draw one row of tetile t for Hexagon with left starts at position p and length l. */
    private static void addOneRow(TETile[][] world, Position p, int l, TETile t) {
        for (int j = 0; j < l; j+=1) {
            if (p.x + j < world.length && p.x + j > 0 &&
                    p.y < world[0].length && p.y > 0) {
                world[p.x + j][p.y] = TETile.colorVariant(t, 32, 32, 32, RANDOM);
            }
        }
    }

    /** Draw rows of tetile t for Hexagon with left corner starts at position p
     * and first row with length l.
     * moveDir determines the change of the left starting position of each row;
     * the down part(moveDir = -1) or the up part(moveDir = 1)*/
    private static void addRows(TETile[][] world, Position p, int l, TETile t, int rows, int moveDir) {
        for (int i = 0; i < rows; i++) {
            addOneRow(world, p, l, t);
            p.x += moveDir;
            p.y += 1;
            l += 2 * (-moveDir);
        }
        p.x -= moveDir;
        p.y -= 1;//in order to have position stop at the left side of the top row
    }

    /** draw one hexagon in the world, with lower left corner starting at position t,
     *  the length of each edge is s and the hexagon consists of TETile t with random color.
     */
    public static void addHexagon(TETile[][] world, Position p, int s, TETile t) {
        if (s < 2) {
            throw new IllegalArgumentException("Hexagon mush be at least size 2.");
        }

        addRows(world, p, s, t, s, -1);
        p.y += 1;
        addRows(world, p, s + (s-1) * 2, t, s, 1);
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
     * Helper function for generating 19 hexagons
     * This functions can generate n hexagons with length s in a row,
     * with the lowest left corner starting at position p.
     */
    public static void addHexagonCol(TETile[][] world, Position p, int s, TETile t, int n) {
        for (int i = 0; i < n; i++) {
            //addHexagon(world, p, s, t);
            addHexagon(world, p, s, randomTile()); // different hexagon makes it easier to debug.
            p.y += 1;
        }

    }

    /**
     * Given original starting position of existing col,
     * calculates the starting position of adjacent cols.
     * @param p: The lower left corner position of existing col
     * @param adj: the relative position of the new row w.r.t the existing col.
     *           e.g. adj = -1 means the first left side column, adj = 2 means the second right side column.
     * @param l: the length of the edge of hexagon.
     * @return starting position of another adjacent columns.
     */
    private static Position adjColPos(Position p, int adj, int l) {
        int newY = p.y + Math.abs(adj) * l;
        int newX = p.x + adj * (2 * l - 1);
        Position newPos = new Position(newX, newY);
        return newPos;
    }

    /**
     * Draw the 19 hexagons in one function, based on addHexagonCol func and adjColPos func.
     * @param world: the world we are going to draw on.
     * @param p: the lower left corner (the starting position) of the central column.
     * @param s: the edge length of a single hexagon.
     * @param t: the TETile used to draw. (not totally random here.)
     * @param n: the number of hexagons in each edge of the large hexagon (e.g. 19 hexagons with number 3).
     */
    public static void addHexagonHex(TETile[][] world, Position p, int s, TETile t, int n) {
    // try calculate the starting position of each row based on p before draw the center column.
        List<Position> posList = new ArrayList<>();
        posList.add(p);
        for (int i = 1; i < n; i++) {
            posList.add(adjColPos(p, i, s));
            posList.add(adjColPos(p, -i, s));
        }
        for (int i = 0; i < posList.size(); i++) {
            int numHex = 2 * n - 1 - (i + 1) / 2;
            addHexagonCol(world, posList.get(i), s, t, numHex);
        }
    }





    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize the world with tileset.nothing
        TETile[][] hexagonWorld = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                hexagonWorld[x][y] = Tileset.NOTHING;
            }
        }

        /* // Test for first part of lab5
        Position p1 = new Position(10,10);
        Position p2 = new Position(20,10);
        Position p3 = new Position(30,10);
        Position p4 = new Position(30,30);
        TETile t = Tileset.WALL;
        addHexagon(hexagonWorld, p1, 2, t);
        addHexagon(hexagonWorld, p2, 3, t);
        addHexagon(hexagonWorld, p3, 4, t);
        addHexagon(hexagonWorld, p4, 5, t);
        //addOneRow(hexagonWorld, p, 2, t);
        //int s = 2;
        //addRows(hexagonWorld, p, s, t, s, -1);
        //addRows(hexagonWorld, p, s + (s-1) * 2, t, s, 1);
        ter.renderFrame(hexagonWorld);*/

        // Test for part2: 19 hexagons
        Position p1 = new Position(20, 20);
        //addHexagonCol(hexagonWorld, p1, 3, randomTile(), 5);
        addHexagonHex(hexagonWorld, p1, 3, randomTile(), 3);
        ter.renderFrame(hexagonWorld);

    }


}
