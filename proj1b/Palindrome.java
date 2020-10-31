public class Palindrome {
    /**
     * Given a String, wordToDeque should return a Deque where the characters
     * appear in the same order as in the String.
     * For example, if the word is “persiflage”, then the returned
     * Deque should have ‘p’ at the front, followed by ‘e’, and so forth.
     * @param word
     * @return
     */
    public Deque<Character> wordToDeque(String word) {
        Deque<Character> deque = new LinkedListDeque();
        for (int i = 0; i < word.length(); i++) {
            deque.addLast(word.charAt(i));
        }
        return deque;
    }

    /**
     * The isPalindrome method should return true if the given word is a palindrome,
     * and false otherwise. A palindrome is defined as a word that is the same whether
     * it is read forwards or backwards. For example “a”, “racecar”, and “noon” are all palindromes.
     * “horse”, “rancor”, and “aaaaab” are not palindromes.
     * Any word of length 1 or 0 is a palindrome.
     */
    public boolean isPalindrome(String word) {
        Deque d = wordToDeque(word);
        return isPalindromehelp(d);

    }

    private boolean isPalindromehelp(Deque d) {
        if (d.size() == 1 | d.size() == 0) {
            return true;
        } else {
            return (d.removeFirst() == d.removeLast() & isPalindromehelp(d));
        }
    }

    /** Add a new method that overloads isPalindrome.*/
    public boolean isPalindrome(String word, CharacterComparator cc) {
        Deque<Character> d = wordToDeque(word);
        return isPalindromehelp(d, cc);
    }

    private boolean isPalindromehelp(Deque d, CharacterComparator cc) {
        if (d.size() == 1 | d.size() == 0) {
            return true;
        } else {
            char x = (char) d.removeFirst();
            char y = (char) d.removeLast();
            return (cc.equalChars(x, y) & isPalindromehelp(d, cc));
        }
    }



}
