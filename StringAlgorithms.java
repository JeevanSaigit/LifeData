package lifedata;

import java.util.*;

public class StringAlgorithms {

    // =========================================================
    // KMP STRING SEARCH
    // Time Complexity: O(n + m)
    // Space Complexity: O(m)
    // =========================================================
    public static List<Integer> kmpSearch(String text, String pattern) {

        List<Integer> ans = new ArrayList<Integer>();

        if (pattern.length() == 0)
            return ans;

        int[] lps = new int[pattern.length()];

        // Build LPS array
        for (int i = 1, len = 0; i < pattern.length();) {

            if (Character.toLowerCase(pattern.charAt(i)) ==
                Character.toLowerCase(pattern.charAt(len))) {

                lps[i++] = ++len;

            } else if (len > 0) {

                len = lps[len - 1];

            } else {

                lps[i++] = 0;
            }
        }

        // Search
        for (int i = 0, j = 0; i < text.length();) {

            if (Character.toLowerCase(text.charAt(i)) ==
                Character.toLowerCase(pattern.charAt(j))) {

                i++;
                j++;

                if (j == pattern.length()) {

                    ans.add(i - j);
                    j = lps[j - 1];
                }

            } else if (j > 0) {

                j = lps[j - 1];

            } else {

                i++;
            }
        }

        return ans;
    }


    // =========================================================
    // Z-FUNCTION
    // Time Complexity: O(n)
    // Space Complexity: O(n)
    // =========================================================
    public static int[] zFunction(String s) {

        int n = s.length();
        int l = 0, r = 0;

        int[] z = new int[n];

        for (int i = 1; i < n; i++) {

            if (i <= r)
                z[i] = Math.min(r - i + 1, z[i - l]);

            while (i + z[i] < n &&
                   s.charAt(z[i]) == s.charAt(i + z[i])) {

                z[i]++;
            }

            if (i + z[i] - 1 > r) {

                l = i;
                r = i + z[i] - 1;
            }
        }

        return z;
    }


    // =========================================================
    // RABIN-KARP
    // Average Time: O(n + m)
    // Worst-case Time: O(nm)
    // Space Complexity: O(1)
    // =========================================================
    public static List<Integer> rabinKarp(String text, String pat) {

        List<Integer> out = new ArrayList<Integer>();

        if (pat.length() == 0 || pat.length() > text.length())
            return out;

        long mod = 1000000007L;
        long base = 256;

        long ph = 0;
        long th = 0;
        long pow = 1;

        for (int i = 0; i < pat.length(); i++) {

            ph = (ph * base + pat.charAt(i)) % mod;

            th = (th * base + text.charAt(i)) % mod;

            if (i < pat.length() - 1)
                pow = pow * base % mod;
        }

        for (int i = 0;
             i <= text.length() - pat.length();
             i++) {

            if (ph == th &&
                text.regionMatches(
                    true,
                    i,
                    pat,
                    0,
                    pat.length())) {

                out.add(i);
            }

            if (i < text.length() - pat.length()) {

                th = (th - text.charAt(i) * pow) % mod;

                if (th < 0)
                    th += mod;

                th = (th * base +
                      text.charAt(i + pat.length())) % mod;
            }
        }

        return out;
    }


    // =========================================================
    // AHO-CORASICK SIMPLE VERSION
    //
    // NOTE:
    // This implementation uses KMP separately for every pattern.
    //
    // Time Complexity: O(k(n + m))
    // Space Complexity: O(m)
    //
    // k = number of patterns
    // n = text length
    // m = pattern length
    // =========================================================
    public static Set<String> ahoCorasickSimple(
            String text,
            List<String> patterns) {

        Set<String> found =
            new LinkedHashSet<String>();

        for (String p : patterns) {

            if (!kmpSearch(text, p).isEmpty())
                found.add(p);
        }

        return found;
    }


    // =========================================================
    // TIME COMPLEXITY DISPLAY METHODS
    // =========================================================

    public static String kmpComplexity() {

        return "KMP Search -> Time: O(n + m), Space: O(m)";
    }

    public static String zComplexity() {

        return "Z-Function -> Time: O(n), Space: O(n)";
    }

    public static String rabinKarpComplexity() {

        return "Rabin-Karp -> Average Time: O(n + m), " +
               "Worst Time: O(nm), Space: O(1)";
    }

    public static String ahoCorasickComplexity() {

        return "Aho-Corasick (current simple implementation) -> " +
               "Time: O(k(n + m)), Space: O(m)";
    }
}