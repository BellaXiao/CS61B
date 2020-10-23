/** An Deque
 *  This deque must use arrays as the core data structure.
 *  For this implementation, your operations are subject to the following rules:
 *  1. add and remove must take constant time, except during resizing operations.
 *  2. get and size must take constant time.
 *  3. The starting size of your array should be 8.
 *  4. The amount of memory that your program uses at any given time must be proportional
 *  to the number of items.
 *  For example, if you add 10,000 items to the deque, and then remove 9,999 items,
 *  you shouldn’t still be using an array of length 10,000ish.
 *  For arrays of length 16 or more, your usage factor should always be at least 25%.
 *  For smaller arrays, your usage factor can be arbitrarily low.
 */


public class ArrayDeque<T> {
    private T[] items;
    private int size;
    private int startPos;

    /** Constructor: creates an empty array deque. */
    public ArrayDeque() {
        // 3. The starting size of your array should be 8.
        items = (T[]) new Object[8];
        size = 0;
        startPos = -1;
    }

    /** Resizes the underlying array to the target capacity. */
    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        // should consider not only size increase but also size decrease
        if (startPos + size > items.length) {
            System.arraycopy(items, startPos, a, 0, items.length - startPos);
            System.arraycopy(items, 0, a, items.length - startPos, startPos + size - items.length);
        } else {
            System.arraycopy(items, startPos, a, 0, size);
        }

        items = a;
        startPos = 0;
    }


    /** Adds an item of type T to the front of the deque. */
    public void addFirst(T item) {
        if (size == 0) {
            startPos = 0;
        } else {
            if (size >= items.length) {
                resize(items.length * 2);
            }
            startPos = (startPos - 1 + items.length) % items.length;
        }

        items[startPos] = item;
        size += 1;
    }

    /** Adds an item of type T to the back of the deque. */
    public void addLast(T item) {
        if (size == 0) {
            startPos = 0;
            items[startPos] = item;
        } else {
            if (size >= items.length) {
                resize(items.length * 2);
            }
        }
        int newPos = (startPos + size) % items.length;
        items[newPos] = item;
        size += 1;
    }

    /** Returns true if deque is empty, false otherwise.*/
    public boolean isEmpty() {
        return size == 0;
    }

    /** Returns the number of items in the deque.*/
    public int size() {
        return size;
    }

    /** Prints the items in the deque from first to last, separated by a space.*/
    public void printDeque() {
        int i = 0;
        while (i < size - 1) {
            System.out.print(items[(startPos + i) % items.length] + " ");
            i += 1;
        }
        System.out.print(items[(startPos + i) % items.length]);
    }

    /** Removes and returns the item at the front of the deque.
     * If no such item exists, returns null.*/
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        size -= 1;
        T removeItem = items[startPos];
        startPos = (startPos + 1) % items.length;
        if (items.length >= 16 && size / items.length < 0.25) {
            resize(size * 4);
        }
        return removeItem;
    }

    /** Removes and returns the item at the back of the deque.
     * If no such item exists, returns null.*/
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T removeItem = items[(startPos + size - 1) % items.length];
        size -= 1;
        if (items.length >= 16 && size / items.length < 0.25) {
            resize(size * 2);
        }
        return removeItem;
    }


    /** Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null. Must not alter the deque!*/
    public T get(int index) {
        if (index >= size) {
            return null;
        }
        return items[(startPos + index) % items.length];
    }








}
