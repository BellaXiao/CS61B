package synthesizer;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer(4);
        assertTrue(arb.isEmpty());
        arb.enqueue(1);
        arb.enqueue(2);
        arb.enqueue(3);
        arb.enqueue(4);
        assertTrue(arb.isFull());
        assertEquals(1, (int) arb.dequeue());
        assertFalse(arb.isFull());
        assertEquals(3, arb.fillCount);
        assertEquals(2, (int) arb.peek());
        // Test Iterator
        Integer sum = 0;
        for (Integer x: arb) {
            sum += x;
        }
        assertEquals((Integer) 9, sum);


    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 
