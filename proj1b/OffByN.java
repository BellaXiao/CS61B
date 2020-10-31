public class OffByN implements CharacterComparator {
    private int gap;

    public OffByN(int N) {
        gap = N;
    }

    /**
     * equalChars returns true for characters that are different by exactly Gap.
     */
    @Override
    public boolean equalChars(char x, char y) {
        int diff = x - y;
        return Math.abs(diff) == gap;
    }
}
