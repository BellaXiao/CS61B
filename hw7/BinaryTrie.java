import edu.princeton.cs.algs4.MinPQ;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BinaryTrie implements Serializable {
    private Node root;

    public BinaryTrie(Map<Character, Integer> frequencyTable) {
        // Use a MinPQ to return the smallest two nodes
        MinPQ<Node> nodesPQ = new MinPQ();

        for (char c: frequencyTable.keySet()) {
            nodesPQ.insert(new Node(c, frequencyTable.get(c), null, null));
        }

        while (nodesPQ.size() > 1) {
            Node left = nodesPQ.delMin();
            Node right = nodesPQ.delMin();
            Node parent = new Node('\0', left.freq + right.freq, left, right);
            nodesPQ.insert(parent);
        }

        this.root = nodesPQ.delMin();
    }

    public Match longestPrefixMatch(BitSequence querySequence) {
        int i = 0;
        Node cur = this.root;

        while (!cur.isleaf()) {
            int k = querySequence.bitAt(i);
            if (k == 0) {
                cur = cur.left;
            } else {
                cur = cur.right;
            }
            i += 1;
        }
        char symbol = cur.ch;
        BitSequence sequence = querySequence.firstNBits(i);
        return  new Match(sequence, symbol);
    }

    public Map<Character, BitSequence> buildLookupTable() {
        // use recursive way
        Map<Character, BitSequence> resMap = new HashMap<>();
        String path = "";
        check(this.root, path, resMap);
        return resMap;
    }

    private void check(Node cur, String path, Map<Character, BitSequence> resMap) {
        // base case
        if (cur.isleaf()) {
            resMap.put(cur.ch, new BitSequence(path));
            return;
        }
        check(cur.left, path + '0', resMap);
        check(cur.right, path + '1', resMap);
    }

    // return the total freq sum of all symbols
    public int symbolFreqSum() {
        return root.freq;
    }


    // Node class used in BinaryTrie
    private static class Node implements Comparable<Node>, Serializable {
        private final char ch;
        private final int freq;
        private final Node left, right;

        Node(char ch, int freq, Node left, Node right) {
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        private boolean isleaf() {
            assert ((left == null && right == null) || (left != null && right != null));
            return (left == null && right == null);
        }


        @Override
        public int compareTo(Node o) {
            return this.freq - o.freq;
        }
    }




}
