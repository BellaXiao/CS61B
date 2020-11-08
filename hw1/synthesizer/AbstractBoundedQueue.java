package synthesizer;

//import javax.swing.plaf.BorderUIResource;

public abstract class AbstractBoundedQueue<T> implements BoundedQueue<T> {

    /** "Protected" just means that only subclasses of AbstractBoundedQueue and classes
     *  in the same package as AbstractBoundedQueue can access this variable.
     */
    protected int fillCount;
    protected int capacity;

    public int capacity() {
        return capacity;
    }
    public int fillCount() {
        return fillCount;
    }








}
