package hw2;

public class PercolationFactory {
    public Percolation make(int N) {
        return new Percolation(N);
    }

    public Percolation_QuickFindUF make_QuickFindUF(int N) {
        return new Percolation_QuickFindUF(N);
    }
}
