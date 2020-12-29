import edu.princeton.cs.algs4.In;

import java.util.*;


public class Boggle {
    
    // File path of dictionary file
    static String dictPath = "words.txt";
    //static String dictPath = "trivial_words.txt";

    /**
     * Solves a Boggle puzzle.
     * 
     * @param k The maximum number of words to return.
     * @param boardFilePath The file path to Boggle board file.
     * @return a list of words found in given Boggle board.
     *         The Strings are sorted in descending order of length.
     *         If multiple words have the same length,
     *         have them in ascending alphabetical order.
     */
    public static List<String> solve(int k, String boardFilePath) {
        // YOUR CODE HERE

        /**
         * Self:
         * 1) build a minHeap with size = k to store the k longest words from board.
         *      The words value should be unique in the minHeap.
         *      The order is based on length of the word as well as the alphabetical order.
         *      add to a size k minheap:
         *          comparing the new key with heap.peek()
         *          if new key > heap.peek(): heap.pop() and heap.add(new key); -- O(logK)
         *
         * 2) build a Trie to store all the words in dictionary of allowed words.
         *      Using nodes with boolean isleaf and map children.
         * 3) a helper function for board to return all the adjacent unvisited position of a position
         * on board.
         * 4) a visited 2d array to mark the positions on board that have been used.
         * 5) Recursive idea: Check(x, y, cache), char s = board(x, y)
         *      base case: s adjacent unvisited positions set and Trie(s).children have no common char.
         *                  Then return;
         *      for each char c as the common char in adjacent set and Trie children:
         *          if (c.isleaf) & (cache+c).len >= 3： minHeap.add(cache + c)
         *          check (x(c), y(c), cache + c)
         *      pop out all k words in maxHeap.
         *
         * 6）Iterative idea: while adjacent positions set and Trie.children have common char.
         * 7) since for the longest length of the word in allowed dictionary is constant(const1),
         *      and number of adjacent positions is constant(const2), for each position on board M*N,
         *      the runtime should be the pow(const2 * logK, const1), should be O(logK)
         */
        // error cases
        if (k <= 0) {
            throw new IllegalArgumentException("K should be positive.");
        }

        // declare trie and read in allowed words and insert them into the trie
        Helper.StringTrie allowedWordsTrie = new Helper.StringTrie();
        In in = new In(dictPath);
        String[] allowedWords = in.readAllLines();
        for (String s: allowedWords) {
            allowedWordsTrie.insert(s);
        }

        // declare minHeap with size k to store the result and List for return result
        Helper.MinHeapSizeK resHeap = new Helper.MinHeapSizeK(k);
        List<String> resList = new LinkedList<>();

        // read in Board txt and stored in Board
        Helper.Board board = new Helper.Board(boardFilePath);

        // loop every position in the board as the start position
        for (int r = 0; r < board.getRowNum(); r++) {
            for (int c = 0; c < board.getColNum(); c++) {
                // for each position as the start,
                // use visited[][] to make one node is only used once
                boolean[][] visited = new boolean[board.getRowNum()][board.getColNum()];
                String cache = ""; //used to store the path along the trie
                Map<Character, Helper.StringTrie.Node> childrenInTrie = allowedWordsTrie.getRoot().getChildren();
                ArrayList<Helper.Board.Node> adjNodes = new ArrayList<>();
                adjNodes.add(board.getNode(r, c));

                // try recursive version first
                check(childrenInTrie, adjNodes, board, visited, cache, resHeap);

            }
        }

        while (!resHeap.isEmpty()) {
            resList.add(0, resHeap.poll());
        }

        return resList;
    }

    // given children in trie and adjacent nodes on board, return a map
    // consisted by valid common nodes with board node as key and Trie node as value.
    private static HashMap<Helper.Board.Node, Helper.StringTrie.Node> commonNodes(
            Map<Character, Helper.StringTrie.Node> childrenInTrie,
            ArrayList<Helper.Board.Node> adjNodes,
            boolean[][] visited) {
        HashMap<Helper.Board.Node, Helper.StringTrie.Node> resNodes = new HashMap<>();
        for (Helper.Board.Node n: adjNodes) {
            if (childrenInTrie.keySet().contains(n.charValue) && !visited[n.r][n.c]) {
                resNodes.put(n, childrenInTrie.get(n.charValue));
            }
        }
        return resNodes;
    }

    private static void check(Map<Character, Helper.StringTrie.Node> childrenInTrie,
                                   ArrayList<Helper.Board.Node> adjNodes,
                                   Helper.Board board,
                                   boolean[][] visited, String cache,
                                   Helper.MinHeapSizeK resHeap) {
        Map<Helper.Board.Node, Helper.StringTrie.Node> common = commonNodes(childrenInTrie,
                adjNodes, visited);
        // base case
        if (common.isEmpty()) {
            return;
        }
        for (Helper.Board.Node boardNode: common.keySet()) {
            visited[boardNode.r][boardNode.c] = true;
            if (common.get(boardNode).isLeaf && cache.length() >= 2) {
                resHeap.add(cache + boardNode.charValue);
            }
            check(common.get(boardNode).getChildren(),
                    board.getAdjacentNode(boardNode.r, boardNode.c),
                    board, visited, cache + boardNode.charValue, resHeap);
            visited[boardNode.r][boardNode.c] = false;
        }

    }




    // main for test
    public static void main(String[] args) {
        //System.out.println(solve(7, "exampleBoard.txt"));
        System.out.println(solve(20, "exampleBoard2.txt"));
    }
}
