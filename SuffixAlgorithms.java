package lifedata;

import java.util.*;

public class SuffixAlgorithms {

    // =========================================================
    // SUFFIX ARRAY
    //
    // Current implementation:
    // Creates all suffixes using substring()
    // and sorts them using Arrays.sort()
    //
    // Time Complexity: O(n^2 log n)
    // Space Complexity: O(n^2) approximately due to substring
    // allocations during comparisons
    // =========================================================
    public static int[] suffixArray(String s) {

        Integer[] sa = new Integer[s.length()];

        for (int i = 0; i < s.length(); i++)
            sa[i] = i;

        final String text = s;

        Arrays.sort(
            sa,
            (a, b) ->
                text.substring(a)
                    .compareTo(text.substring(b))
        );

        int[] out = new int[sa.length];

        for (int i = 0; i < sa.length; i++)
            out[i] = sa[i];

        return out;
    }


    // =========================================================
    // LCP / KASAI
    //
    // Time Complexity: O(n)
    // Space Complexity: O(n)
    // =========================================================
    public static int[] lcpKasai(
            String s,
            int[] sa) {

        int n = s.length();
        int k = 0;

        int[] rank = new int[n];
        int[] lcp = new int[n];

        for (int i = 0; i < n; i++)
            rank[sa[i]] = i;

        for (int i = 0; i < n; i++) {

            if (rank[i] == n - 1) {
                k = 0;
                continue;
            }

            int j = sa[rank[i] + 1];

            while (
                i + k < n &&
                j + k < n &&
                s.charAt(i + k) ==
                s.charAt(j + k)
            ) {
                k++;
            }

            lcp[rank[i]] = k;

            if (k > 0)
                k--;
        }

        return lcp;
    }


    // =========================================================
    // SA-IS
    //
    // IMPORTANT:
    // This is currently only a classroom demonstration.
    // It calls suffixArray(), so its actual complexity is the
    // same as suffixArray().
    //
    // Time Complexity: O(n^2 log n)
    // =========================================================
    public static int[] sais(String s) {

        return suffixArray(s);
    }


    // =========================================================
    // COMPLEXITY INFORMATION
    // =========================================================

    public static String suffixArrayComplexity() {

        return "Suffix Array -> Time: O(n^2 log n), " +
               "Space: O(n^2)";
    }


    public static String lcpComplexity() {

        return "LCP / Kasai -> Time: O(n), Space: O(n)";
    }


    public static String saisComplexity() {

        return "SA-IS (current implementation) -> " +
               "Time: O(n^2 log n), Space: O(n^2)";
    }
}