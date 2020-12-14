package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayDeque;
import java.util.Deque;
//import java.util.HashSet;


public class Solver {
    private SearchNode goal;
    private SearchNode initialNode;
    private int minPQcount = 0;

    public Solver(WorldState initial) {
        MinPQ<SearchNode> pq = new MinPQ<>();
        initialNode = new SearchNode(initial, 0, null);
        pq.insert(initialNode);
        minPQcount += 1;

        //HashSet<WorldState> visitedWorldStateSet = new HashSet<>();

        while (!pq.isEmpty()) {
            SearchNode s = pq.delMin();
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
                    pq.insert(new SearchNode(n, s.moveNum + 1, s));
                    minPQcount += 1;
                }
            }
        }
    }


    private class SearchNode implements Comparable {
        private WorldState ws;
        private int moveNum;
        private SearchNode preNode;
        private int estimatedDistanceToGoal;

        private SearchNode(WorldState world, int moves, SearchNode node) {
            ws = world;
            moveNum = moves;
            preNode = node;
            estimatedDistanceToGoal = ws.estimatedDistanceToGoal();
        }

        @Override
        public int compareTo(Object o) {
            if (o.getClass() != this.getClass()) {
                return -999999;
            }
            SearchNode other = (SearchNode) o;
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
        SearchNode cur = goal;
        while (!cur.ws.equals(initialNode.ws)) {
            resList.addFirst(cur.ws);
            cur = cur.preNode;
        }
        resList.addFirst(cur.ws);
        return resList;
    }

    public int getMinPQcount () {
        return minPQcount;
    }
}





