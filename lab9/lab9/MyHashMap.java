package lab9;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;



/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  @author Your name here
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    private static final int DEFAULT_SIZE = 16;
    private static final double MAX_LF = 0.75;

    private ArrayMap<K, V>[] buckets;
    private int size;

    private double loadFactor() {
        return 1.0 * size / buckets.length;
    }

    public MyHashMap() {
        buckets = new ArrayMap[DEFAULT_SIZE];
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        this.size = 0;
        for (int i = 0; i < this.buckets.length; i += 1) {
            this.buckets[i] = new ArrayMap<>();
        }
    }

    /** Computes the hash function of the given key. Consists of
     *  computing the hashcode, followed by modding by the number of buckets.
     *  To handle negative numbers properly, uses floorMod instead of %.
     */
    private int hash(K key) {
        if (key == null) {
            return 0;
        }

        int numBuckets = buckets.length;
        return Math.floorMod(key.hashCode(), numBuckets);
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        //throw new UnsupportedOperationException();
        return buckets[hash(key)].get(key);
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        //throw new UnsupportedOperationException();
        ArrayMap bucket = buckets[hash(key)];
        int oriBucketSize = bucket.size;
        bucket.put(key, value);
        size += bucket.size - oriBucketSize;

        // need to resize if loadfactor exceeds max_lf
        if (loadFactor() > MAX_LF) {
            resize();
        }
    }

    /* Resize the current buckets array to 2*original size and put all elements back. */
    private void resize() {
        ArrayMap<K, V>[] newBuckets = new ArrayMap [buckets.length * 2];
        for (int i = 0; i < newBuckets.length; i += 1) {
            newBuckets[i] = new ArrayMap<>();
        }
        for (ArrayMap b: buckets) {
            Set<K> keySet = b.keySet();
            for (K k: keySet) {
                V v = (V) b.get(k);
                // !!! need to use a new Hash function here,
                // !!! otherwise it used the length of the old buckets
                newBuckets[newHash(k, newBuckets.length)].put(k, v);
            }
        }
        this.buckets = newBuckets;
    }

    private int newHash(K key, int newNumBuckets) {
        if (key == null) {
            return 0;
        }
        return Math.floorMod(key.hashCode(), newNumBuckets);
    }





    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        //throw new UnsupportedOperationException();
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        //throw new UnsupportedOperationException();
        Set<K> resSet = new HashSet<>();
        for (ArrayMap b: buckets) {
            Set<K> keySet = b.keySet();
            for (K k: keySet) {
                resSet.add(k);
            }
        }
        return resSet;
    }

    /* Removes the mapping for the specified key from this map if exists.
     * Not required for this lab. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
        //throw new UnsupportedOperationException();
        if (get(key) == null) {
            return null;
        }
        ArrayMap bucket = buckets[hash(key)];
        V val = get(key);
        bucket.remove(key);
        return val;
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for this lab. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        //throw new UnsupportedOperationException();
        if (get(key) != value) {
            return null;
        }
        return remove(key);
    }

    @Override
    public Iterator<K> iterator() {
        //throw new UnsupportedOperationException();
        return new MyHashMapIterator();
    }

    private class MyHashMapIterator implements Iterator<K> {
        private int count;
        private List<K> keyList = new ArrayList<>();

        MyHashMapIterator() {
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



    // main func to test
    /*public static void main(String args[]) {
        MyHashMap<Integer, Integer> b = new MyHashMap<>();
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
        System.out.println(b.remove(6,4));
        System.out.println(b.keySet());
        System.out.println(b.remove(9,3));
        System.out.println(b.keySet());

        MyHashMap<String, Integer> b = new MyHashMap<String, Integer>();
        for (int i = 0; i < 455; i++) {
            b.put("hi" + i, 1);
            if (i == 48) {
                String s = "hi" + i;
                System.out.println(s.hashCode());
                System.out.println(Math.floorMod(s.hashCode(), 64));
                System.out.println(Math.floorMod(s.hashCode(), 128));
            }

            //make sure put is working via containsKey and get
            assertTrue(null != b.get("hi" + i)
                    && b.containsKey("hi" + i));
        }
    }*/


}
