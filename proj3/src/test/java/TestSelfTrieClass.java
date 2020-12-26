import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestSelfTrieClass {
    private static class Trie {
        private class Node {
            boolean isLeaf;
            Map<Character, Node> children;

            Node() {
                isLeaf = false;
                children = new HashMap<>();
            }
        }

        private Node root;
        Trie() {
            root = new Node();
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

    public static void main(String[] args) {
        Trie trie = new Trie();
        trie.insert("a");
        trie.insert("sa");
        trie.insert("sa");
        trie.insert("awls");
        trie.insert("sad");
        trie.insert("sam");
        trie.insert("same");
        trie.insert("sapp");

        List<String> res = trie.keysWithPrefix("a");
        System.out.println(res);
        List<String> res2 = trie.keysWithPrefix("sa");
        System.out.println(res2);
        List<String> res3 = trie.keysWithPrefix("");
        System.out.println(res3);
    }



}
