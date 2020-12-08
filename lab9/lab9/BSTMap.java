package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Your name here
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns the value mapped to by KEY in the subtree rooted in P.
     *  or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        //throw new UnsupportedOperationException();
        if (p == null) {
            return null;
        }
        if (key.compareTo(p.key) == 0) {
            return p.value;
        } else if (key.compareTo(p.key) < 0) {
            return getHelper(key, p.left);
        } else {
            return getHelper(key, p.right);
        }
    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        //throw new UnsupportedOperationException();
        return getHelper(key, root);
    }

    /** Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
      * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        //throw new UnsupportedOperationException();
        Node add = new Node(key, value);
        if (p == null) {
            return add;
        }
        if (key.compareTo(p.key) < 0) {
            p.left = putHelper(key, value, p.left);
        } else if (key.compareTo(p.key) > 0) {
            p.right = putHelper(key, value, p.right);
        } else {
            p.value = value;
        }
        return p;
    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        //throw new UnsupportedOperationException();
        root = putHelper(key, value, root);
        size = size(root);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        //throw new UnsupportedOperationException();
        return size(root);
    }

    /* return the size of the tree with root p; */
    private int size(Node p) {
        if (p == null) {
            return 0;
        }
        return size(p.left) + size(p.right) + 1;
    }


    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////



    /* Return a Set view of the keys contained in the tree with root p. */
    private Set<K> keySetHelper(Node p) {
        Set<K> resSet = new HashSet<>();
        if (p == null) {
            return resSet;
        }
        resSet.add(p.key);
        resSet.addAll(keySetHelper(p.left));
        resSet.addAll(keySetHelper(p.right));
        return resSet;
    }

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        //throw new UnsupportedOperationException();
        return keySetHelper(root);
    }


    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */
    @Override
    public V remove(K key) {
        //throw new UnsupportedOperationException();
        V val = get(key);
        root = remove(key, root);
        return val;
    }

    /* Remove the key-value pair with K as key in the tree rooted in p. */
    private Node remove(K key, Node p) {
        if (p == null) {
            return null;
        }
        int comp = key.compareTo(p.key);
        if (comp < 0) {
            p.left = remove(key, p.left);
        } else if (comp > 0) {
            p.right = remove(key, p.right);
        } else {
            if (p.left == null) {
                return p.right;
            } else if (p.right == null) {
                return p.left;
            } else {
                Node minNode = min(p.right);
                minNode.right = deleteMin(p.right);
                minNode.left = p.left;
                return minNode;
            }
        }
        return p;
    }

    // Using main func to test -- both remove func and keySet func looks good
    /*public static void main(String args[]) {
        BSTMap<Integer, Integer> b = new BSTMap<>();
        b.put(7,2);
        b.put(9,3);
        b.put(5,1);
        b.put(6,4);
        // Test iterator: succeed
        for (Integer k: b) {
            System.out.format("%d->", k);
        }
        System.out.println(b.keySet());
        System.out.println(b.remove(7));
        System.out.println(b.remove(5,2));
        System.out.println(b.keySet());
        System.out.println(b.remove(5,1));
        System.out.println(b.keySet());
        System.out.println(b.remove(9,3));
        System.out.println(b.keySet());
        System.out.println(b.remove(6,4));
        System.out.println(b.keySet());
    }*/


    /* delete the (k,v) with minimum k in the map. */
    public void deleteMin() {
        root = deleteMin(root);
    }
    /* delete the min key in the tree rooted in p and return the root node of the new tree. */
    private Node deleteMin(Node p) {
        if (p.left == null) {
            return p.right;
        }
        p.left = deleteMin(p.left);
        size = size(p.left) + size(p.right) + 1;
        return p;
    }


    /* Return the minimum K in the map*/
    public K min() {
        return min(root).key;
    }
    /* return the minimum Node in the tree rooted in p */
    private Node min(Node p) {
        if (p == null) {
            return null;
        }
        if (p.left == null) {
            return p;
        } else {
            return min(p.left);
        }
    }


    /*
    Similarly, we have deleteMax
     */

    /* delete the (k,v) with max key in the map. */
    //public void deleteMax() {
    //     root = deleteMax(root);
    // }
    // // delete the min key in the tree rooted in p and return the root node of the new tree.
    // private Node deleteMax(Node p) {
    //     if (p.right == null) {
    //         return p.left;
    //     }
    //     p.right = deleteMax(p.right);
    //     size = size(p.left) + size(p.right) + 1;
    //     return p;
    // }
    //
    // // Return the maximum K in the map
    // public K max() {
    //     return max(root).key;
    // }
    // // return the maximum Node in the tree rooted in p
    // private Node max(Node p) {
    //     if (p == null) {
    //         return null;
    //     }
    //     if (p.right == null) {
    //         return p;
    //     } else {
    //         return max(p.right);
    //     }
    // }


    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        //throw new UnsupportedOperationException();
        if (get(key) == value) {
            root = remove(key, root);
            return value;
        }
        return null;
    }






    @Override
    public Iterator<K> iterator() {
        //throw new UnsupportedOperationException();
        return new BSTMapIterator();
    }

    private class BSTMapIterator implements Iterator<K> {
        private int count;
        private List<K> keyList = new ArrayList<>();

        BSTMapIterator() {
            count = 0;
            for (K key: keySet()) {
                keyList.add(key);
            }
        }
        @Override
        public boolean hasNext() {
            return count < keyList.size();
        }

        @Override
        public K next() {
            K item = keyList.get(count);
            count += 1;
            return item;
        }
    }
}
