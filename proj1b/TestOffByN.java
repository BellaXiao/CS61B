import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByN {
    static CharacterComparator offByN = new OffByN(2);

    @Test
    public void testEqualChars() {
        assertTrue(offByN.equalChars('a', 'c'));
        assertTrue(offByN.equalChars('d', 'b'));

        assertFalse(offByN.equalChars('a', 'a'));
        assertFalse(offByN.equalChars('D', 'b'));
        assertFalse(offByN.equalChars('a', 'b'));
    }
}
