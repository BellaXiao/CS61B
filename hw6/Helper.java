import edu.princeton.cs.algs4.In;

import java.util.*;

public class Helper {
    /**
     * Use this class to build trie class for storing dictionary allowed words.
     * Build minHeap with fixed size k class to store the longest k words.
     * Build Position class to store the value and the position info on borad.
     */


    /**
     *  Self Trie class：based on the one created in Project 3
     */
    public static class StringTrie {
        public class Node {
            boolean isLeaf;
            Map<Character, Node> children;

            Node() {
                isLeaf = false;
                children = new HashMap<>();
            }

            // get all the children chars for given node, return
            public Map<Character, Node> getChildren() {
                return children;
            }
        }

        private Node root;
        StringTrie() {
            root = new Node();
        }

        public Node getRoot() {
            return root;
        }

        public void insert(String key) {
            Node cur = root;
            for (char c: key.toCharArray()) {
                if (!cur.children.containsKey(c)) {
                    cur.children.put(c, new Node());
                }

                cur = cur.children.get(c);
            }
            cur.isLeaf = true;
        }

        public List<String> keysWithPrefix(String prefix) {
            List<String> resList = new ArrayList<>();
            String cache = prefix;
            Node cur = root;
            for (char c: prefix.toCharArray()) {
                if (!cur.children.containsKey(c)) {
                    return null;
                }
                cur = cur.children.get(c);
            }
            // need a helper function to do it recursively
            getKeys(cache, cur, resList);

            return resList;
        }

        // helper function of the above keysWithPrefix function
        private void getKeys(String cache, Node cur, List<String> resList) {
            if (cur.isLeaf) {
                resList.add(cache);
            }

            for (char c: cur.children.keySet()) {
                Node n = cur.children.get(c);
                getKeys(cache + c, n, resList);
            }
        }


    }


    /**
     * Build minHeap with fixed size k class to store the longest k words.
     */
    public static class MinHeapSizeK {
        private int fixedSize;
        private PriorityQueue<String> minHeap;

        MinHeapSizeK(int k) {
            fixedSize = k;
            minHeap = new PriorityQueue<>(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    if (o1.length() > o2.length()) {
                        return 1;
                    } else if (o1.length() < o2.length()) {
                        return -1;
                    } else {
                        return -o1.compareTo(o2);
                    }
                }
            });
        }

        public void add(String s) {
            // if the size of the minHeap is already k, need to compare the new string s
            // with the min of the heap, if s is longer then min or s has the same length as min,
            // but is alphabetically ahead, then delete min and add s.
            // This way we keep the minheap to have maximum size k.
            if (minHeap.contains(s)) { // make sure no duplicates in the heap
                return;
            }
            if (minHeap.size() == fixedSize) {
                String min = minHeap.peek();
                if (s.length() > min.length()
                        || (s.length() == min.length() && s.compareTo(min) < 0)) {
                    minHeap.poll();
                    minHeap.add(s);
                }
            } else if (minHeap.size() < fixedSize) {
                minHeap.add(s);
            }

        }

        public String poll() {
            return minHeap.poll();
        }

        public String peek() {
            return minHeap.peek();
        }

        public boolean isEmpty() {
            return minHeap.isEmpty();
        }
    }


    /**
     * Build Position class to store the value and the position info on borad.
     */
    public static class Board {
        private Node[][] board;
        private int rowNum, colNum;
        public class Node {
            public int r, c;
            public char charValue;

            Node(int row, int col, char c) {
                this.r = row;
                this.c = col;
                this.charValue = c;
            }
        }

        Board(String boardFilePath) {
            In text = new In(boardFilePath);
            String[] lines = text.readAllLines();
            if (lines.length == 0 || lines == null) {
                throw new IllegalArgumentException("Board file reading error.");
            }
            rowNum = lines.length;
            colNum = lines[0].length();
            this.board = new Node[rowNum][colNum];
            for (int i = 0; i < rowNum; i++) {
                for (int j = 0; j < colNum; j++) {
                    if (lines[i].length() != colNum) {
                        throw new IllegalArgumentException("Input board is not rectangular.");
                    }
                    this.board[i][j] = new Node(i, j, lines[i].charAt(j));
                }
            }
        }

        public Node[][] getBoard() {
            return this.board;
        }

        public int getColNum() {
            return colNum;
        }

        public int getRowNum() {
            return rowNum;
        }

        public char getChar(int r, int c) {
            return board[r][c].charValue;
        }

        public Node getNode(int r, int c) {
            return board[r][c];
        }

        // given a position on board with row r and col c,
        // return an Array of adjacent node for that position.
        // (normally 9 except positions on edge.)
        public ArrayList<Node> getAdjacentNode(int r, int c) {
            ArrayList<Node> resArray = new ArrayList<>();
            for (int i = -1; i <= 1; i++) {
                if (r + i < 0 || r + i >= rowNum) {
                    continue;
                }
                for (int j = -1; j <= 1; j++) {
                    if (c + j < 0 || c + j >= colNum || (i == 0 && j == 0)) {
                        continue;
                    }
                    resArray.add(this.board[r + i][c + j]);
                }
            }
            return resArray;
        }


    }


    // Use main to do unit test
    public static void main(String[] args) {
        // Test Board class - succeed
        /*Board b = new Board("exampleBoard.txt");
        for (Board.Node n: b.getAdjacentNode(1,2)) {
            System.out.print(n.charValue + " ");
        }*/

        // Test MinHeapSizeK class(fixed size k without duplicates) - succeed
        /*MinHeapSizeK h = new MinHeapSizeK(3);
        h.add("abd");
        h.add("abde");
        h.add("abd");
        h.add("dbd");
        while (!h.isEmpty()) {
            System.out.println(h.poll());
        }*/

        // Test StringTrie - succeed
        /*StringTrie t = new StringTrie();
        t.insert("setback");
        t.insert("setbacks");
        t.insert("sad");
        t.insert("small");
        for (char c: t.nextCharSet(t.getRoot().children.get('s'))) {
            System.out.print(c + "  ");
        }*/




    }








}
