import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Your implementations of various string searching algorithms.
 *
 * @author Depei Yu
 * @version 1.0
 * @userid dyu79
 * @GTID 903312858
 *
 * Collaborators: LIST ALL COLLABORATORS YOU WORKED WITH HERE
 *
 * Resources: LIST ALL NON-COURSE RESOURCES YOU CONSULTED HERE
 */
public class PatternMatching {

    /**
     * Knuth-Morris-Pratt (KMP) algorithm relies on the failure table (also
     * called failure function). Works better with small alphabets.
     *
     * Make sure to implement the buildFailureTable() method before implementing
     * this method.
     *
     * @param pattern    the pattern you are searching for in a body of text
     * @param text       the body of text where you search for pattern
     * @param comparator you MUST use this to check if characters are equal
     * @return list containing the starting index for each match found
     * @throws java.lang.IllegalArgumentException if the pattern is null or has
     *                                            length 0
     * @throws java.lang.IllegalArgumentException if text or comparator is null
     */
    public static List<Integer> kmp(CharSequence pattern, CharSequence text,
                                    CharacterComparator comparator) {
        if (pattern == null) {
            throw new java.lang.IllegalArgumentException("Pattern cannot be null.");
        }

        if (pattern.length() == 0) {
            throw new java.lang.IllegalArgumentException("Pattern cannot have length of 0.");
        }

        if (text == null) {
            throw new java.lang.IllegalArgumentException("Text cannot be null.");
        }

        if (comparator == null) {
            throw new java.lang.IllegalArgumentException("Comparator cannot be null.");
        }

        List<Integer> matches = new ArrayList<>();

        if (pattern.length() > text.length()) {
            return matches;
        }

        int[] failureTable = buildFailureTable(pattern, comparator);

        if (pattern.length() > text.length()) {
            return matches;
        }

        int i = 0;
        int j = 0;

        while (i <= (text.length() - pattern.length())) {
            while ((j < pattern.length()) && (comparator.compare(text.charAt(i + j), pattern.charAt(j)) == 0)) {
                j += 1;
            }
            if (j == 0) {
                i += 1;
            } else {
                if (j == pattern.length()) {
                    matches.add(i);
                }
                int nextAlignment = failureTable[j - 1];
                i = i + j - nextAlignment;
                j = nextAlignment;
            }
        }
        return matches;
    }

    /**
     * Builds failure table that will be used to run the Knuth-Morris-Pratt
     * (KMP) algorithm.
     *
     * The table built should be the length of the input pattern.
     *
     * Note that a given index i will contain the length of the largest prefix
     * of the pattern indices [0..i] that is also a suffix of the pattern
     * indices [1..i]. This means that index 0 of the returned table will always
     * be equal to 0
     *
     * Ex. pattern = ababac
     *
     * table[0] = 0
     * table[1] = 0
     * table[2] = 1
     * table[3] = 2
     * table[4] = 3
     * table[5] = 0
     *
     * If the pattern is empty, return an empty array.
     *
     * @param pattern    a pattern you're building a failure table for
     * @param comparator you MUST use this to check if characters are equal
     * @return integer array holding your failure table
     * @throws java.lang.IllegalArgumentException if the pattern or comparator
     *                                            is null
     */
    public static int[] buildFailureTable(CharSequence pattern,
                                          CharacterComparator comparator) {
        if (pattern == null) {
            throw new java.lang.IllegalArgumentException("Pattern cannot be null.");
        }

        if (comparator == null) {
            throw new java.lang.IllegalArgumentException("Comparator cannot be null.");
        }

        if (pattern.length() == 0) {
            return new int[0];
        }

        int iPointer = 0;
        int jPointer = 1;
        int[] table = (new int[pattern.length()]);
        table[0] = 0;
        while (jPointer < pattern.length()) {
            if (comparator.compare(pattern.charAt(iPointer), pattern.charAt(jPointer)) == 0) {
                iPointer += 1;
                table[jPointer] = iPointer;
                jPointer += 1;
            } else {
                if (iPointer != 0) {
                    iPointer = table[iPointer - 1];
                } else if (iPointer == 0) {
                    table[jPointer] = iPointer;
                    jPointer += 1;
                }
            }
        }
        return table;
    }

    /**
     * Boyer Moore algorithm that relies on last occurrence table. Works better
     * with large alphabets.
     *
     * Make sure to implement the buildLastTable() method before implementing
     * this method. Do NOT implement the Galil Rule in this method.
     *
     * Note: You may find the getOrDefault() method from Java's Map class
     * useful.
     *
     * @param pattern    the pattern you are searching for in a body of text
     * @param text       the body of text where you search for the pattern
     * @param comparator you MUST use this to check if characters are equal
     * @return list containing the starting index for each match found
     * @throws java.lang.IllegalArgumentException if the pattern is null or has
     *                                            length 0
     * @throws java.lang.IllegalArgumentException if text or comparator is null
     */
    public static List<Integer> boyerMoore(CharSequence pattern,
                                           CharSequence text,
                                           CharacterComparator comparator) {
        if (pattern == null) {
            throw new java.lang.IllegalArgumentException("Pattern cannot be null.");
        }

        if (pattern.length() == 0) {
            throw new java.lang.IllegalArgumentException("Pattern cannot have length of 0.");
        }

        if (text == null) {
            throw new java.lang.IllegalArgumentException("Text cannot be null.");
        }

        if (comparator == null) {
            throw new java.lang.IllegalArgumentException("Pattern cannot be null.");
        }

        List<Integer> matches = new ArrayList<>();
        Map<Character, Integer> lastTable = buildLastTable(pattern);
        int i = 0;
        while (i <= (text.length() - pattern.length())) {
            int j = pattern.length() - 1;
            while ((j >= 0) && (comparator.compare(text.charAt(i + j), pattern.charAt(j)) == 0)) {
                j -= 1;
            }
            if (j == -1) {
                matches.add(i);
                i += 1;
            } else {
                int shiftedIndex = lastTable.getOrDefault(text.charAt(i + j), -1);
                if (shiftedIndex < j) {
                    i = i + (j - shiftedIndex);
                } else {
                    i += 1;
                }
            }
        }
        return matches;
    }

    /**
     * Builds last occurrence table that will be used to run the Boyer Moore
     * algorithm.
     *
     * Note that each char x will have an entry at table.get(x).
     * Each entry should be the last index of x where x is a particular
     * character in your pattern.
     * If x is not in the pattern, then the table will not contain the key x,
     * and you will have to check for that in your Boyer Moore implementation.
     *
     * Ex. pattern = octocat
     *
     * table.get(o) = 3
     * table.get(c) = 4
     * table.get(t) = 6
     * table.get(a) = 5
     * table.get(everything else) = null, which you will interpret in
     * Boyer-Moore as -1
     *
     * If the pattern is empty, return an empty map.
     *
     * @param pattern a pattern you are building last table for
     * @return a Map with keys of all of the characters in the pattern mapping
     * to their last occurrence in the pattern
     * @throws java.lang.IllegalArgumentException if the pattern is null
     */
    public static Map<Character, Integer> buildLastTable(CharSequence pattern) {
        if (pattern == null) {
            throw new java.lang.IllegalArgumentException("Pattern cannot be null.");
        }
        Map<Character, Integer> lastTable = new HashMap<>();
        for (int i = 0; i < pattern.length(); i++) {
            lastTable.put(pattern.charAt(i), i);
        }
        return lastTable;
    }

    /**
     * Prime base used for Rabin-Karp hashing.
     * DO NOT EDIT!
     */
    private static final int BASE = 113;

    /**
     * Runs the Rabin-Karp algorithm. This algorithms generates hashes for the
     * pattern and compares this hash to substrings of the text before doing
     * character by character comparisons.
     *
     * When the hashes are equal and you do character comparisons, compare
     * starting from the beginning of the pattern to the end, not from the end
     * to the beginning.
     *
     * You must use the Rabin-Karp Rolling Hash for this implementation. The
     * formula for it is:
     *
     * sum of: c * BASE ^ (pattern.length - 1 - i)
     *   c is the integer value of the current character, and
     *   i is the index of the character
     *
     * We recommend building the hash for the pattern and the first m characters
     * of the text by starting at index (m - 1) to efficiently exponentiate the
     * BASE. This allows you to avoid using Math.pow().
     *
     * Note that if you were dealing with very large numbers here, your hash
     * will likely overflow; you will not need to handle this case.
     * You may assume that all powers and calculations CAN be done without
     * overflow. However, be careful with how you carry out your calculations.
     * For example, if BASE^(m - 1) is a number that fits into an int, it's
     * possible for BASE^m will overflow. So, you would not want to do
     * BASE^m / BASE to calculate BASE^(m - 1).
     *
     * Ex. Hashing "bunn" as a substring of "bunny" with base 113
     * = (b * 113 ^ 3) + (u * 113 ^ 2) + (n * 113 ^ 1) + (n * 113 ^ 0)
     * = (98 * 113 ^ 3) + (117 * 113 ^ 2) + (110 * 113 ^ 1) + (110 * 113 ^ 0)
     * = 142910419
     *
     * To update the hash, subtract the oldChar times BASE raised to the length - 1, multiply by
     * BASE, and add the newChar as shown by this formula:
     * (oldHash - oldChar * BASE ^ (pattern.length - 1)) * BASE + newChar
     *
     * Ex. Shifting from "bunn" to "unny" in "bunny" with base 113
     * hash("unny") = (hash("bunn") - b * 113 ^ 3) * 113 + y
     *              = (142910419 - 98 * 113 ^ 3) * 113 + 121
     *              = 170236090
     *
     * @param pattern    a string you're searching for in a body of text
     * @param text       the body of text where you search for pattern
     * @param comparator you MUST use this to check if characters are equal
     * @return list containing the starting index for each match found
     * @throws java.lang.IllegalArgumentException if the pattern is null or has
     *                                            length 0
     * @throws java.lang.IllegalArgumentException if text or comparator is null
     */
    public static List<Integer> rabinKarp(CharSequence pattern,
                                          CharSequence text,
                                          CharacterComparator comparator) {
        if (pattern == null) {
            throw new java.lang.IllegalArgumentException("Pattern cannot be null.");
        }

        if (pattern.length() == 0) {
            throw new java.lang.IllegalArgumentException("Pattern cannot have length of 0.");
        }

        if (text == null) {
            throw new java.lang.IllegalArgumentException("Text cannot be null.");
        }

        if (comparator == null) {
            throw new java.lang.IllegalArgumentException("Comparator cannot be null.");
        }

        List<Integer> matches = new ArrayList<>();

        if (pattern.length() > text.length()) {
            return matches;
        }

        int patternHash = 0;
        int patternHashMultiplier = 1;
        for (int j = pattern.length() - 1; j >= 0; j--) {
            patternHash += pattern.charAt(j) * patternHashMultiplier;
            if ((patternHashMultiplier * BASE) < Integer.MAX_VALUE) {
                patternHashMultiplier = patternHashMultiplier * BASE;
            }
        }

        int rollingTextHash = 0;
        int rollingTextHashMultiplier = 1;
        int temp = 1;
        for (int k = pattern.length() - 1; k >= 0; k--) {
            rollingTextHash += text.charAt(k) * rollingTextHashMultiplier;
            if (k == 0) {
                temp = rollingTextHashMultiplier;
            }
            rollingTextHashMultiplier = rollingTextHashMultiplier * BASE;
        }

        int i = 0;
        while (i <= (text.length() - pattern.length())) {
            if (patternHash == rollingTextHash) {
                int j = 0;
                while ((j < pattern.length()) && (comparator.compare(text.charAt(i + j), pattern.charAt(j)) == 0)) {
                    j += 1;
                }
                if (j == pattern.length()) {
                    matches.add(i);
                }
            }
            i += 1;
            if (i <= (text.length() - pattern.length())) {
                rollingTextHash = (rollingTextHash - (text.charAt(i - 1) * temp))
                        * BASE + text.charAt(i - 1 + pattern.length());
            }
        }
        return matches;
    }

    /**
     * This method is OPTIONAL and for extra credit only.
     *
     * The Galil Rule is an addition to Boyer Moore that optimizes how we shift the pattern
     * after a full match. Please read Extra Credit: Galil Rule section in the homework pdf for details.
     *
     * Make sure to implement the buildLastTable() method and buildFailureTable() method
     * before implementing this method.
     *
     * @param pattern    the pattern you are searching for in a body of text
     * @param text       the body of text where you search for the pattern
     * @param comparator you MUST use this to check if characters are equal
     * @return list containing the starting index for each match found
     * @throws java.lang.IllegalArgumentException if the pattern is null or has
     *                                            length 0
     * @throws java.lang.IllegalArgumentException if text or comparator is null
     */
    public static List<Integer> boyerMooreGalilRule(CharSequence pattern,
                                          CharSequence text,
                                          CharacterComparator comparator) {
        return null; // if you are not implementing this method, do NOT modify this line
    }
}
