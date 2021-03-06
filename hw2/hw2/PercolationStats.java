package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;
//import edu.princeton.cs.introcs.Stopwatch;

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
            // need to cast to double, otherwise always 0.0
            double x = (double) grid.numberOfOpenSites() / (this.N * this.N);
            xSamplesArray[i] = x;
        }
        mean = StdStats.mean(xSamplesArray);
        std = StdStats.stddev(xSamplesArray);
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
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
    /*public static void main(String[] args) {
        int N = 20;
        int T = 10;
        double time;
        PercolationFactory pf = new PercolationFactory();

        System.out.println("QFUF:");
        int i = 1;
        while (i > 0) {
            Stopwatch watch = new Stopwatch();
            PercolationStats stats = new PercolationStats(N, T, pf);
            //System.out.format("Mean:%f, Std:%f\n", stats.mean(), stats.stddev());
            //time = watch.elapsedTime();
            //System.out.format("N:%d, T:%d, Time:%f\n", N, T, time);
            for (double x: stats.xSamplesArray) {
                System.out.println(x);
            }
            System.out.format("%f, %f, %f, %f", stats.confidenceLow(),
                    stats.stddev(), stats.confidenceHigh(), stats.confidenceLow());
            i -= 1;
            T *= 2;
        }

    }*/


}
