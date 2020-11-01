import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDequeGold {
    @Test
    public void testStudentArrayDeque() {
        StudentArrayDeque<Integer> test = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> standard = new ArrayDequeSolution<>();

        // use i as the element we add in each round
        int i = 0;
        // use output string to record each operation we made
        String output = "";

        while (true) {
            int randomNumber = StdRandom.uniform(0, 4);
            if (randomNumber == 0) {
                test.addFirst(i);
                standard.addFirst(i);

                output += "addFirst(" + i + ")\n";
                assertEquals(output, test.get(0), standard.get(0));
            } else if (randomNumber == 1) {
                test.addLast(i);
                standard.addLast(i);
                output += "addLast(" + i + ")\n";
                assertEquals(output, test.get(test.size() - 1), standard.get(standard.size() - 1));
            } else if (randomNumber == 2) {
                if (!test.isEmpty() & !standard.isEmpty()) {
                    output += "removeLast()\n";
                    assertEquals(output, test.removeLast(), standard.removeLast());
                }
            } else {
                if (!test.isEmpty() & !standard.isEmpty()) {
                    output += "removeFirst()\n";
                    assertEquals(output, test.removeFirst(), standard.removeFirst());
                }
            }
            i += 1;
            //System.out.print(i);
        }
    }

}
