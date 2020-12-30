public class HuffmanDecoder {
    public static void main(String[] args) {

        ObjectReader or = new ObjectReader(args[0]);
        /* Read object from the file in the same order as in the HuffmanEncoder. */
        BinaryTrie decodingTrie = (BinaryTrie) or.readObject();
        int symbolNum = (int) or.readObject();
        BitSequence bitSequence = (BitSequence) or.readObject();

        char[] originContext = new char[symbolNum];

        int i = 0;
        while (i < symbolNum) {
            Match m = decodingTrie.longestPrefixMatch(bitSequence);
            originContext[i] = m.getSymbol();
            bitSequence = bitSequence.allButFirstNBits(m.getSequence().length());
            i += 1;
        }

        FileUtils.writeCharArray(args[1], originContext);
    }

}
