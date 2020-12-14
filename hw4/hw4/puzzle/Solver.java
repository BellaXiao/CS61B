package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayDeque;
import java.util.Deque;
//import java.util.HashSet;


public class Solver {
    searchNode goal;
    searchNode initialNode;
    int minPQcount = 0;

    public Solver (WorldState initial) {
        MinPQ<searchNode> pq = new MinPQ<>();
        initialNode = new searchNode(initial, 0, null);
        pq.insert(initialNode);
        minPQcount += 1;

        //HashSet<WorldState> visitedWorldStateSet = new HashSet<>();

        while (!pq.isEmpty()) {
            searchNode s = pq.delMin();
            /*if (visitedWorldStateSet.contains(s.ws)) {
                continue;
            } else {
                visitedWorldStateSet.add(s.ws);
            }*/

            if (s.ws.isGoal()) {
                goal = s;
                break;
            }
            for (WorldState n : s.ws.neighbors()) {
                if (s.preNode == null || !n.equals(s.preNode.ws)) {
                    pq.insert(new searchNode(n, s.moveNum + 1, s));
                    minPQcount += 1;
                }
            }
        }
    }


    private class searchNode implements Comparable {
        public WorldState ws;
        public int moveNum;
        public searchNode preNode;
        public int estimatedDistanceToGoal;

        searchNode (WorldState world, int moves, searchNode node) {
            ws = world;
            moveNum = moves;
            preNode = node;
            estimatedDistanceToGoal = ws.estimatedDistanceToGoal();
        }

        @Override
        public int compareTo (Object o) {
            if (o.getClass() != this.getClass()) {
                return -999999;
            }
            searchNode other = (searchNode) o;
            int thisNum = this.moveNum + this.estimatedDistanceToGoal;
            int otherNum = other.moveNum + other.estimatedDistanceToGoal;
            if (thisNum < otherNum) {
                return -1;
            } else if (thisNum > otherNum) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public int moves() {
        return goal.moveNum;
    }

    public Iterable<WorldState> solution() {
        Deque<WorldState> resList = new ArrayDeque<>();
        searchNode cur = goal;
        while (!cur.ws.equals(initialNode.ws)) {
            resList.addFirst(cur.ws);
            cur = cur.preNode;
        }
        resList.addFirst(cur.ws);
        return resList;
    }

}





