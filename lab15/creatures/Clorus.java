package creatures;

import huglife.*;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class Clorus extends Creature {
    private final int r = 34;
    private final int g = 0;
    private final int b = 231;

    public Clorus(double e) {
        super("clorus");
        energy = e;
    }

    @Override
    public void move() {
        energy -= 0.03;
    }

    @Override
    public void attack(Creature c) {
        energy += c.energy();
    }

    @Override
    public Clorus replicate() {
        energy *= 0.5;
        return new Clorus(energy);
    }

    @Override
    public void stay() {
        energy -= 0.01;
    }

    @Override
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        List<Direction> empties = getNeighborsOfType(neighbors, "empty");
        if (empties.size() >= 1) {
            List<Direction> plips = getNeighborsOfType(neighbors, "plip");
            if (plips.size() > 0) {
                Direction moveDir = HugLifeUtils.randomEntry(plips);
                return new Action(Action.ActionType.ATTACK, moveDir);
            } else if (energy >= 1) {
                Direction moveDir = HugLifeUtils.randomEntry(empties);
                return new Action(Action.ActionType.REPLICATE, moveDir);
            } else {
                Direction moveDir = HugLifeUtils.randomEntry(empties);
                return new Action(Action.ActionType.MOVE, moveDir);
            }
        }
        return new Action(Action.ActionType.STAY);



    }

    @Override
    public Color color() {
        return color(r, g, b);
    }
}
