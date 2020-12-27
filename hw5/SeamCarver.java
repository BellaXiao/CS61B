import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture picture;


    public SeamCarver(Picture picture) {
        this.picture = picture;
    }
    // current picture
    public Picture picture() {
        return new Picture(this.picture);
    }
    // width of current picture
    public int width() {
        return this.picture.width();
    }

    // height of current picture
    public int height() {
        return this.picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        validateXY(x, y);
        int x1, x2, y1, y2;
        x1 = adjustWithin(x + 1, width());
        x2 = adjustWithin(x - 1, width());
        y1 = adjustWithin(y + 1, height());
        y2 = adjustWithin(y - 1, height());
        return squareRGB(x1, x2, y, y) + squareRGB(x, x, y1, y2);

    }

    private int adjustWithin(int pos, int range) {
        if (pos < 0) {
            return pos + range;
        } else if (pos >= range) {
            return pos - range;
        } else {
            return pos;
        }
    }

    private double squareRGB(int x1, int x2, int y1, int y2) {
        validateXY(x1, y1);
        validateXY(x2, y2);
        double redDiff = picture.get(x1, y1).getRed() - picture.get(x2, y2).getRed();
        double greenDiff = picture.get(x1, y1).getGreen() - picture.get(x2, y2).getGreen();
        double blueDiff = picture.get(x1, y1).getBlue() - picture.get(x2, y2).getBlue();
        return redDiff * redDiff + greenDiff * greenDiff + blueDiff * blueDiff;
    }



    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[] colRes = new int[height()];
        Pixel[][] matrix = new Pixel[width()][height()];
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                if (y == 0) {
                    matrix[x][y] = new Pixel(energy(x, y));
                } else {
                    int minFromCol = getMinEnergyCol(x, y, matrix);
                    matrix[x][y] = new Pixel(energy(x, y) + matrix[minFromCol][y - 1].getEnergy());
                    matrix[x][y].setFromCol(minFromCol);
                }
            }
        }
        // find the col with minimum energy in the last row of the matrix
        int minCol = -1;
        double minEnergy = Double.POSITIVE_INFINITY;
        for (int x = 0; x < width(); x++) {
            if (matrix[x][height() - 1].getEnergy() < minEnergy) {
                minEnergy = matrix[x][height() - 1].getEnergy();
                minCol = x;
            }
        }
        // now minCol represents the Col in last row of matrix with min Energy
        int y = height() - 1;
        while (y >= 0) {
            colRes[y] = minCol;
            minCol = matrix[minCol][y].getFromCol();
            y -= 1;
        }
        return colRes;
    }

    // Self: create Pixel to store the energy and the fromCol simultaneously.
    private class Pixel {
        double energy;
        int fromCol = -1;

        Pixel(double energy) {
            this.energy = energy;
        }

        public void setFromCol(int col) {
            fromCol = col;
        }
        public int getFromCol() {
            return this.fromCol;
        }
        public double getEnergy() {
            return this.energy;
        }
    }
    // Self: Helper function. Given matrix and pos x, y, return the col of last row (x - 1)
    // that has the minimum energy among three adjacent pixels.
    private int getMinEnergyCol(int x, int y, Pixel[][] m) {
        validateXY(x, y);
        double minEnergy = Double.POSITIVE_INFINITY;
        int minCol = -1;
        for (int i = -1; i <= 1; i++) {
            if (x + i < 0 || x + i >= m.length) {
                continue;
            }
            if (m[x + i][y - 1].getEnergy() < minEnergy) {
                minEnergy = m[x + i][y - 1].getEnergy();
                minCol = x + i;
            }
        }
        return minCol;
    }



    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        int[] res;
        this.picture = transpose(this.picture);
        res = findVerticalSeam();
        this.picture = transpose(this.picture);
        return res;
    }

    private Picture transpose(Picture pic) {
        Picture trans = new Picture(pic.height(), pic.width());
        for (int x = 0; x < pic.height(); x++) {
            for (int y = 0; y < pic.width(); y++) {
                trans.set(x, y, pic.get(y, x));
            }
        }
        return trans;

    }


    // remove horizontal seam from picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam.length == 0) {
            return;
        }
        validateSeam(seam, width());
        SeamRemover.removeHorizontalSeam(this.picture, seam);
    }


    // remove vertical seam from picture
    public void removeVerticalSeam(int[] seam) {
        if (seam.length == 0) {
            return;
        }
        validateSeam(seam, height());
        SeamRemover.removeVerticalSeam(this.picture, seam);
    }

    // Self: a validated function to throw exceptions if called with invalid x and y
    private void validateXY(int x, int y) {
        if (x < 0 || y < 0 || x >= width() || y >= height()) {
            throw new IndexOutOfBoundsException("X or Y is out of valid range.");
        }
    }

    // Self: a validated function to throw exceptions if called with invalid seam
    private void validateSeam(int[] seam, int expectedLength) {
        if (seam.length != expectedLength) {
            throw new IllegalArgumentException("Invalid seam length.");
        }
        int prev = seam[0];
        for (int i = 1; i < seam.length; i++) {
            if (Math.abs(seam[i] - prev) > 1) {
                throw new IllegalArgumentException("Invalid seam entries.");
            }
            prev = seam[i];
        }

    }


    // Unit test purpose
    /*public static void main(String[] args) {
        Picture p = new Picture("images/6x5.png");
        SeamCarver sc = new SeamCarver(p);

        int[] seam = sc.findVerticalSeam();
    }*/






}
