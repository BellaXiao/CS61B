package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;
import edu.princeton.cs.introcs.Stopwatch;

public class PercolationStats {
    private int T;
    private int N;
    private double[] xSamplesArray;
    private double mean;
    private double std;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("Both N and T should be positive!");
        }

        xSamplesArray = new double[T];
        this.T = T;
        this.N = N;

        // perform T times percolation process, store all sample x(threshold) into xSampleArray
        for (int i = 0; i < this.T; i += 1) {
            Percolation grid = pf.make(this.N);
            //Percolation_QuickFindUF grid = pf.make_QuickFindUF(this.N);
            while (!grid.percolates()) {
                int row = StdRandom.uniform(this.N);
                int col = StdRandom.uniform(this.N);
                grid.open(row, col);
            }
            double x = grid.numberOfOpenSites() / (this.N * this.N);
            xSamplesArray[i] = x;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        mean = StdStats.mean(xSamplesArray);
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        std = StdStats.stddev(xSamplesArray);
        return std;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        return mean - 1.96 * std / Math.sqrt(this.T);
    }
    public double confidenceHigh() {
        return mean + 1.96 * std / Math.sqrt(this.T);
    }


    /*
    Using Stopwatch to test speed of Percolation using WeightedQuickUnionUF for large N and T
    with Percolation using QuickFindUF
     */
    public static void main(String[] args) {
        int N = 10;
        int T = 5000;
        double time;
        PercolationFactory pf = new PercolationFactory();

        System.out.println("QFUF:");
        int i = 10;
        while (i > 0) {
            Stopwatch watch = new Stopwatch();
            PercolationStats stats = new PercolationStats(N, T, pf);
            time = watch.elapsedTime();
            System.out.format("N:%d, T:%d, Time:%f\n", N, T, time);
            i -= 1;
            T *= 2;
        }

    }


}
