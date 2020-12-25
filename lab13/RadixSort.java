/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        // TD: Implement LSD Sort
        String[] res = new String[asciis.length];
        // copy the String[] to be non-destructive
        // and also find the longest length of string in asciis
        int l = 0;
        for (int i = 0; i < asciis.length; i += 1) {
            res[i] = asciis[i];
            l = Math.max(l, asciis[i].length());
        }

        sortHelperLSD(res, l);
        return res;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        // Optional LSD helper method for required LSD radix sort
        int R = 256;
        String[] tempRes = new String[asciis.length];

        for (int d = index - 1; d >= 0; d -= 1) {
            // get count array
            int[] count = new int[R];
            int[] startPos = new int[R];
            int c;
            for (String s: asciis) {
                if (s.length() < d + 1) {
                    c = '_';
                } else {
                    c = s.charAt(d);
                }
                count[c] += 1;
            }
            // get position array
            int pos = 0;
            for (int i = 0; i < startPos.length; i += 1) {
                startPos[i] = pos;
                pos += count[i];
            }
            // go through asciis and put sorted array into tempRes
            for (String s: asciis) {
                if (s.length() < d + 1) {
                    c = '_';
                } else {
                    c = s.charAt(d);
                }
                tempRes[startPos[c]] = s;
                startPos[c] += 1;
            }
            // copy back the result from tempRes to asciis to continue the loop
            for (int j = 0; j < tempRes.length; j += 1) {
                asciis[j] = tempRes[j];
            }
        }
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        // base case1: only one String
        if (start >= end - 1) {
            return;
        }

        int R = 256;
        String[] tempRes = new String[end - start];
        String[] partition = new String[end - start];

        // copy the part we want to sort into partition and also get the longest str len
        int l = Integer.MIN_VALUE;
        for (int k = 0; k < partition.length; k += 1) {
            partition[k] = asciis[k + start];
            l = Math.max(l, partition[k].length());
        }
        // base case2: index is larger than the length of the longest string need to be sorted
        // otherwise we will have infinite loop for array with duplicates
        if (index >= l)  {
            return;
        }

        // get count array
        int[] count = new int[R];
        int[] startPos = new int[R];
        int c;
        for (String s: partition) {
            if (s.length() < index + 1) {
                c = '_';
            } else {
                c = s.charAt(index);
            }
            count[c] += 1;
        }
        // get position array
        int pos = 0;
        for (int i = 0; i < startPos.length; i += 1) {
            startPos[i] = pos;
            pos += count[i];
        }
        // go through partition and put sorted array into tempRes
        for (String s: partition) {
            if (s.length() < index + 1) {
                c = '_';
            } else {
                c = s.charAt(index);
            }
            tempRes[startPos[c]] = s;
            startPos[c] += 1;
        }
        // copy back the result from tempRes to asciis[start: end] to continue the loop
        for (int j = 0; j < tempRes.length; j += 1) {
            asciis[j + start] = tempRes[j];
        }

        // recursively MSD sort within each sorted digit group
        for (int j = 1; j < startPos.length; j += 1) {
            sortHelperMSD(asciis, startPos[j - 1], startPos[j], index + 1);
        }
        return;
    }

    /**
     * Use above MSDhelper function to have non-destructive MSD sort.
     * @param asciis
     * @return sorted String[]
     */
    public static String[] sortMSD(String[] asciis) {
        String[] copy = new String[asciis.length];
        for (int i = 0; i < asciis.length; i++) {
            copy[i] = asciis[i];
        }

        sortHelperMSD(copy, 0, copy.length, 0);
        return copy;
    }







    /**
     * Test of LSD radix sort
     */
    public static void main(String[] args) {
        //String[] array = {"2", "100", "ab", "addd", "acdbe"};
        String[] array = {"_", "2", "2__", "_"};
        System.out.println("Sorted results from LSD:");
        for (String s: sort(array)) {
            System.out.println(s);
        }

        System.out.println("Sorted results from MSD:");
        for (String s: sortMSD(array)) {
            System.out.println(s);
        }
    }

}
