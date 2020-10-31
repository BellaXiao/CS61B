/** This class outputs all palindromes in the words file in the current directory. */
public class PalindromeFinder {

    /*
    public static void main(String[] args) {
        int minLength = 4;
        In in = new In("../library-sp18/data/words.txt");
        Palindrome palindrome = new Palindrome();

        while (!in.isEmpty()) {
            String word = in.readString();
            if (word.length() >= minLength && palindrome.isPalindrome(word)) {
                System.out.println(word);
            }
        }
    } //Uncomment this class once you've written isPalindrome.
     */

    /*
    // Just for fun for OffByOne
    public static void main(String[] args) {
        int minLength = 4;
        In in = new In("../library-sp18/data/words.txt");
        Palindrome palindrome = new Palindrome();
        CharacterComparator cc = new OffByOne();

        while (!in.isEmpty()) {
            String word = in.readString();
            if (word.length() >= minLength && palindrome.isPalindrome(word, cc)) {
                System.out.println(word);
            }
        }
    }
     */

    /*
    // Just for fun for OffByN
    public static void main(String[] args) {
        int minLength = 4;
        In in = new In("../library-sp18/data/words.txt");
        Palindrome palindrome = new Palindrome();
        CharacterComparator cc = new OffByN(2);

        while (!in.isEmpty()) {
            String word = in.readString();
            if (word.length() >= minLength && palindrome.isPalindrome(word, cc)) {
                System.out.println(word);
            }
        }
    }

     */

    // Just for more fun
    /** For what N are there the most palindromes in English?
     * What is the longest offByN palindrome for any N?
     * */
    public static void main(String[] args) {
        int minLength = 4;
        Palindrome palindrome = new Palindrome();
        int[][] countAndLen = new int[26][2];

        for (int N = 0; N <= 25; N++) {
            int count = 0;
            int maxlen = 0;
            In in = new In("../library-sp18/data/words.txt");
            CharacterComparator cc = new OffByN(N);
            while (!in.isEmpty()) {
                String word = in.readString();
                if (word.length() >= minLength && palindrome.isPalindrome(word, cc)) {
                    count += 1;
                    maxlen = Math.max(maxlen, word.length());
                }
            }
            countAndLen[N][0] = count;
            countAndLen[N][1] = maxlen;
        }

        // print out countAndLen array
        for (int i = 0; i < countAndLen.length; i++) {
            System.out.print(i + " ");
            for (int y : countAndLen[i])
            {
                System.out.print(y + " ");
            }
            System.out.println();
        }

    }

}
