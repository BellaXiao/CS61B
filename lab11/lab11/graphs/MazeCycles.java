package lab11.graphs;

import edu.princeton.cs.algs4.Stack;


/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    int s = 0;
    int[] parentTo;
    boolean hasCycle = false;


    public MazeCycles(Maze m) {
        super(m);
        parentTo = new int[maze.V()];
        for (int i = 0; i < maze.V(); i += 1) {
            parentTo[i] = Integer.MAX_VALUE;
        }
    }

    @Override
    public void solve() {
        // TD: Your code here!
        // try recursive dfs first
        dfs(s);
    }

    /**
     * Seems like use iterative version instead of recursion can solve below index problem.
     * */
    private void dfs(int r) {
        Stack<Integer> stack = new Stack<>();
        stack.push(r);
        while (!stack.isEmpty()) {
            int v = stack.pop();
            marked[v] = true;
            for (int w: maze.adj(v)) {
                if (marked[w] && parentTo[v] != w) {
                    //copy cycle edges from parentTo to edgeTo, so that only them will be drawn.
                    int pre = v;
                    while (pre != w) {
                        edgeTo[pre] = parentTo[pre];
                        pre = parentTo[pre];
                    }
                    edgeTo[w] = v;
                    announce();
                    //hasCycle = true;
                    return;
                }
                if (!marked[w]) {
                    parentTo[w] = v;
                    stack.push(w);
                }
            }
        }
    }


    /*
    still not quite sure why has IndexOutofBound Error for deleteOtherEdge func
    // Helper methods go here
    private void dfs(int v) {
        marked[v] = true;
        if (hasCycle) {
            return;
        }
        for (int w: maze.adj(v)) {
            if (marked[w] && edgeTo[v] != w) {
                // delete all edges from source to w, only left edges for cycle.
                deleteOtherEdge(v, w);
                edgeTo[w] = v;
                announce();
                hasCycle = true;
                return;
            }
            if (!marked[w]) {
                edgeTo[w] = v;
                dfs(w);
            }
        }
    }

    // delete edges from s to w
    private void deleteOtherEdge(int v, int w) {
        HashSet<Integer> cycleVertex = new HashSet<>();
        int pre = v;
        while (pre != w) {
            cycleVertex.add(pre);
            pre = edgeTo[pre];
        }

        for (int i = 0; i < edgeTo.length; i += 1) {
            if (!cycleVertex.contains(i)) {
                edgeTo[i] = Integer.MAX_VALUE;
            }
        }
    }*/
}

