package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.Set;
import java.util.HashSet;

public class Percolation {
    // create N-by-N grid, with all sites initially blocked
    /* this disjoint set is used for judging whether percolates
        contains both virtual top and virtual bottom.
     */
    private WeightedQuickUnionUF grid;
    /* this disjoint set is used for judging whether one cell is full.
        contains only virtual top, excluding virtual bottom to avoid backwash problem.
     */
    private WeightedQuickUnionUF noBackWashGrid;
    private int N;
    private int virtualTop;
    private int virtualBottom;
    private Set<Integer> opened; // store all the pos that have been opened

    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N needs to be greater than 0!");
        }
        grid = new WeightedQuickUnionUF(N * N + 2);
        noBackWashGrid = new WeightedQuickUnionUF(N * N + 1);
        // add virtual top and virtual bottom
        virtualTop = N * N;
        virtualBottom = N * N + 1;
        this.N = N;
        opened = new HashSet<>();

        // connect the first row with virtual top and last row with virtual bottom
        for (int i = 0; i < N; i += 1) {
            // first row connected to virtual top for both grids
            grid.union(xyTo1D(0, i), virtualTop);
            noBackWashGrid.union(xyTo1D(0, i), virtualTop);
            // last row connected to virtual bottom for only grid
            grid.union(xyTo1D(N - 1, i), virtualBottom);
        }
    }

    // convert 2d row and col to 1d index
    private int xyTo1D(int row, int col) {
        // make sure it is within the grid.
        row = Math.min(N - 1, row);
        row = Math.max(0, row);
        col = Math.min(N - 1, col);
        col = Math.max(0, col);
        return N * row + col;
    }

    // if adjacent p2 is in opened set, connect p1 with p2
    private void adjOpenThenConnect(int p1, int p2) {
        if (opened.contains(p2)) {
            grid.union(p1, p2);
            noBackWashGrid.union(p1, p2);
        }
    }

    // validate row and col in range(0,N-1)
    private void validRowCol(int row, int col) {
        if (row < 0 || col < 0 || row >= N || col >= N) {
            throw  new IndexOutOfBoundsException("Index out of bound!");
        }
    }



    // open the site (row, col) if it is not open already
    public void open(int row, int col) {
        validRowCol(row, col);
        // if adjacent pos is already opened, connect them
        int p1 = xyTo1D(row, col);
        int p2 = xyTo1D(row, col + 1);
        int p3 = xyTo1D(row, col - 1);
        int p4 = xyTo1D(row + 1, col);
        int p5 = xyTo1D(row - 1, col);
        adjOpenThenConnect(p1, p2);
        adjOpenThenConnect(p1, p3);
        adjOpenThenConnect(p1, p4);
        adjOpenThenConnect(p1, p5);
        // added it to opened set
        opened.add(p1);

    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validRowCol(row, col);
        return opened.contains(xyTo1D(row, col));
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validRowCol(row, col);
        // use noBackWashGrid to avoid backwash problem
        return isOpen(row, col) && noBackWashGrid.connected(xyTo1D(row, col), virtualTop);
    }

    // number of open sites
    public int numberOfOpenSites() {
        return opened.size();
    }

    // does the system percolate?
    public boolean percolates() {
        // need to deal with corner case N=1
        if (this.N == 1) {
            return opened.contains(xyTo1D(0, 0));
        }

        // use grid to see whether percolates
        return grid.connected(virtualTop, virtualBottom);
    }


    public static void main(String[] args) {
        // unit Test
        Percolation gg = new Percolation(1);
        //gg.open(0, 0);
        gg.open(2, 3);
        gg.open(2, 1);
        gg.open(1, 1);
        gg.open(0, 1);
        gg.open(3, 1);
        gg.open(4, 1);

        gg.open(4, 3);
        gg.open(3, 3);

        gg.open(3, 2);

        System.out.println(gg.numberOfOpenSites());
        //System.out.println(gg.isFull(1, 1));
        //System.out.println(gg.isFull(2, 3));
        System.out.println(gg.percolates());

    }  // use for unit testing (not required)



}
