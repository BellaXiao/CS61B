public class LinkedListDeque<T> implements Deque<T> {

    /** Embedded class for generic type. */
    private class StuffNode {
        private T item;
        private StuffNode prev;
        private StuffNode next;
        // constructor for StuffNode class
        private StuffNode(T i, StuffNode p, StuffNode n) {
            item = i;
            prev = p;
            next = n;
        }
    }

    /** class attributes of LinkedListDeque class*/
    // use circular sentinel node
    private StuffNode sentinel;
    private int size;


    /** constructor for LinkedListDeque Class */
    // creates an empty Deque
    public LinkedListDeque() {
        sentinel = new StuffNode(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    /** comment out to pass autograder
     // create a non-empty Deque
     public LinkedListDeque(T x){
     sentinel = new StuffNode(x, null, null);
     sentinel.next = new StuffNode(x, sentinel, sentinel);
     sentinel.prev = sentinel;
     }*/


    /** Adds an item of type T to the front of the deque. */
    @Override
    public void addFirst(T item) {
        StuffNode first = sentinel.next;
        StuffNode t = new StuffNode(item, sentinel, first);
        sentinel.next = t;
        first.prev = t;
        size += 1;
    }

    /** Adds an item of type T to the back of the deque. */
    @Override
    public void addLast(T item) {
        StuffNode last = sentinel.prev;
        StuffNode t = new StuffNode(item, last, sentinel);
        last.next = t;
        sentinel.prev = t;
        size += 1;
    }

    /** Returns true if deque is empty, false otherwise.*/
    @Override
    public boolean isEmpty() {
        return sentinel.next == sentinel;
    }

    /** Returns the number of items in the deque.*/
    @Override
    public int size() {
        return size;
    }

    /** Prints the items in the deque from first to last, separated by a space.*/
    @Override
    public void printDeque() {
        StuffNode p = sentinel.next;
        while (p.next != sentinel) {
            System.out.print(p.item + " ");
            p = p.next;
        }
        System.out.print(p.item);
    }

    /** Removes and returns the item at the front of the deque.
     * If no such item exists, returns null.*/
    @Override
    public T removeFirst() {
        if (sentinel.next == sentinel) {
            return null;
        }
        StuffNode first = sentinel.next;
        sentinel.next = first.next;
        first.next.prev = sentinel;
        size -= 1;
        return first.item;
    }

    /** Removes and returns the item at the back of the deque.
     * If no such item exists, returns null.*/
    @Override
    public T removeLast() {
        if (sentinel.next == sentinel) {
            return null;
        }
        StuffNode last = sentinel.prev;
        sentinel.prev = last.prev;
        last.prev.next = sentinel;
        size -= 1;
        return last.item;
    }


    /** Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null. Must not alter the deque!*/
    @Override
    public T get(int index) {
        if (index >= size) {
            return null;
        }
        int i = 0;
        StuffNode p = sentinel.next;
        while (i < index) {
            p = p.next;
            i += 1;
        }
        return p.item;
    }

    /** Same as get, but uses recursion.*/
    public T getRecursive(int index) {
        if (index >= size) {
            return null;
        }
        return getRecurHelper(sentinel.next, index);
    }

    private T getRecurHelper(StuffNode p, int index) {
        if (index == 0) {
            return p.item;
        }
        return getRecurHelper(p.next, index - 1);
    }









}
