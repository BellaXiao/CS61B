package creatures;

import huglife.*;
import org.junit.Test;

import java.awt.*;
import java.util.HashMap;

import static org.junit.Assert.*;

public class TestClorus {
    @Test
    public void testBasics() {
        Clorus p = new Clorus(2);
        assertEquals(2, p.energy(), 0.01);
        assertEquals(new Color(34, 0, 231), p.color());
        p.move();
        assertEquals(1.97, p.energy(), 0.01);
        p.move();
        assertEquals(1.94, p.energy(), 0.01);
        p.stay();
        assertEquals(1.93, p.energy(), 0.01);
        p.stay();
        assertEquals(1.92, p.energy(), 0.01);
    }

    @Test
    public void testReplicate() {
        Clorus p = new Clorus(2);
        Clorus baby = p.replicate();
        assertNotSame(baby, p);
        assertEquals(1.00, p.energy(), 0.01);
        assertEquals(1.00, baby.energy(), 0.01);

    }

    @Test
    public void testChoose() {
        Clorus p = new Clorus(2);
        HashMap<Direction, Occupant> surrounded = new HashMap<Direction, Occupant>();
        surrounded.put(Direction.TOP, new Impassible());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());

        Clorus p2 = new Clorus(2);
        HashMap<Direction, Occupant> surrounded2 = new HashMap<Direction, Occupant>();
        surrounded2.put(Direction.TOP, new Plip(2));
        surrounded2.put(Direction.BOTTOM, new Empty());
        surrounded2.put(Direction.LEFT, new Impassible());
        surrounded2.put(Direction.RIGHT, new Impassible());

        Clorus p3 = new Clorus(1);
        HashMap<Direction, Occupant> surrounded3 = new HashMap<Direction, Occupant>();
        surrounded3.put(Direction.TOP, new Impassible());
        surrounded3.put(Direction.BOTTOM, new Empty());
        surrounded3.put(Direction.LEFT, new Impassible());
        surrounded3.put(Direction.RIGHT, new Impassible());

        //You can create new empties with new Empty();
        //Despite what the spec says, you cannot test for Cloruses nearby yet.
        //Sorry!

        Action actual = p.chooseAction(surrounded);
        Action expected = new Action(Action.ActionType.STAY);
        assertEquals(expected, actual);

        Action actual2 = p2.chooseAction(surrounded2);
        Action expected2 = new Action(Action.ActionType.ATTACK, Direction.TOP);
        assertEquals(expected2, actual2);

        Action actual3 = p3.chooseAction(surrounded3);
        Action expected3 = new Action(Action.ActionType.REPLICATE, Direction.BOTTOM);
        assertEquals(expected3, actual3);

        p3.stay();
        Action actual4 = p3.chooseAction(surrounded3);
        Action expected4 = new Action(Action.ActionType.MOVE, Direction.BOTTOM);
        assertEquals(expected4, actual4);

    }

    public static void main(String[] args) {
        System.exit(jh61b.junit.textui.runClasses(TestPlip.class));
    }









}
