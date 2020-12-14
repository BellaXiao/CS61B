package hw4.puzzle;

import java.util.ArrayList;

public class Board implements WorldState {
    int[][] boardArray;
    int N;

    public Board(int[][] tiles) {
        N = tiles.length;
        boardArray = new int[N][N];
        for (int i = 0; i < N; i += 1) {
            for (int j = 0; j < tiles[0].length; j += 1) {
                boardArray[i][j] = tiles[i][j];
            }
        }
    }
    public int tileAt(int i, int j) {
        if (i >= 0 && i <= N - 1 && j >= 0 && j <= N - 1) {
            return boardArray[i][j];
        } else {
            throw new IndexOutOfBoundsException("i or j not in reasonable range.");
        }
    }
    public int size() {
        return N;
    }

    /**
     * You may assume that the constructor receives an N-by-N array containing the N^2 integers
     * between 0 and N2 − 1, where 0 represents the blank square.
     * @return
     */
    public Iterable<WorldState> neighbors() {
        int iZero = -1;
        int jZero = -1;
        boolean findZero = false;
        ArrayList<WorldState> neighborsList = new ArrayList<>();
        // find 0 position
        for (int i = 0; i < N; i += 1) {
            for (int j = 0; j < N; j += 1) {
                if (boardArray[i][j] == 0) {
                    findZero = true;
                    iZero = i;
                    jZero = j;
                    break;
                }
            }
            if (findZero) {
                break;
            }
        }
        // swap 0 with its neighbors
        if (findZero) {
            WorldState neighbor1 = swapWithZero(boardArray, iZero, jZero, iZero - 1, jZero);
            WorldState neighbor2 = swapWithZero(boardArray, iZero, jZero, iZero + 1, jZero);
            WorldState neighbor3 = swapWithZero(boardArray, iZero, jZero, iZero, jZero - 1);
            WorldState neighbor4 = swapWithZero(boardArray, iZero, jZero, iZero, jZero + 1);
            if (neighbor1 != null) {
                neighborsList.add(neighbor1);
            }
            if (neighbor2 != null) {
                neighborsList.add(neighbor2);
            }
            if (neighbor3 != null) {
                neighborsList.add(neighbor3);
            }
            if (neighbor4 != null) {
                neighborsList.add(neighbor4);
            }
        }
        return neighborsList;

    }

    private WorldState swapWithZero(int[][] board, int iZero, int jZero, int iOther, int jOther) {
        Board boardAfterSwap = new Board(board);

        if (iOther >= 0 && iOther <= boardAfterSwap.N - 1
                && jOther >= 0 && jOther <= boardAfterSwap.N - 1) {
            boardAfterSwap.boardArray[iZero][jZero] = boardAfterSwap.boardArray[iOther][jOther];
            boardAfterSwap.boardArray[iOther][jOther] = 0;
            return boardAfterSwap;
        } else {
            return null;
        }
    }


    public int hamming() {
        int ham = 0;
        for (int i = 0; i < N; i += 1) {
            for (int j = 0; j < N; j += 1) {
                if (boardArray[i][j] == 0) {
                    continue;
                }
                if (boardArray[i][j] != (i * N + j + 1) % (N * N)) {
                    ham += 1;
                }
            }
        }
        return ham;
    }

    public int manhattan() {
        int man = 0;
        for (int i = 0; i < N; i += 1) {
            for (int j = 0; j < N; j += 1) {
                int num = boardArray[i][j];
                if (num == 0) {
                    continue;
                }
                int iAim = (num - 1) / N;
                int jAim = num - 1 - iAim * N;
                man += Math.abs(iAim - i) + Math.abs(jAim - j);
            }
        }
        return man;
    }


    public int estimatedDistanceToGoal() {
        return manhattan();
    }
    public boolean equals(Object y) {
        if (this.getClass() != y.getClass()) {
            return false;
        }
        Board other = (Board) y;
        if (other.N != N) {
            return false;
        }
        for (int i = 0; i < N; i += 1) {
            for (int j = 0; j < N; j += 1) {
                if (boardArray[i][j] != other.boardArray[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }


    /** Returns the string representation of the board. 
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
