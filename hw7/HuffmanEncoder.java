import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HuffmanEncoder {
    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
        Map<Character, Integer> resMap = new HashMap<>();
        for (char c: inputSymbols) {
            if (resMap.containsKey(c)) {
                resMap.put(c, resMap.get(c) + 1);
            } else {
                resMap.put(c, 1);
            }
        }
        return resMap;
    }

    public static void main(String[] args) {
        /**
         * 1: Read the file as 8 bit symbols.
         * 2: Build frequency table.
         * 3: Use frequency table to construct a binary decoding trie.
         * 4: Write the binary decoding trie to the .huf file.
         * 5: (optional: write the number of symbols to the .huf file)
         * 6: Use binary trie to create lookup table for encoding.
         * 7: Create a list of bitsequences.
         * 8: For each 8 bit symbol:
         *     Lookup that symbol in the lookup table.
         *     Add the appropriate bit sequence to the list of bitsequences.
         * 9: Assemble all bit sequences into one huge bit sequence.
         * 10: Write the huge bit sequence to the .huf file.
         */

        char[] input = FileUtils.readFile(args[0]);

        Map<Character, Integer> freqTable = buildFrequencyTable(input);

        BinaryTrie decodingTrie = new BinaryTrie(freqTable);

        ObjectWriter ow = new ObjectWriter(args[0] + ".huf");
        ow.writeObject(decodingTrie);
        ow.writeObject(decodingTrie.symbolFreqSum());

        Map<Character, BitSequence> lookupTable = decodingTrie.buildLookupTable();

        List<BitSequence> bitSequenceList = new LinkedList<>();

        for (char c: input) {
            bitSequenceList.add(lookupTable.get(c));
        }

        ow.writeObject(BitSequence.assemble(bitSequenceList));


    }





}
