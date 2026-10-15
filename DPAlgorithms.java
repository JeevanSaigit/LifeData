package lifedata;

import java.util.*;

public class DPAlgorithms {

    // =========================================================
    // 1. LEVENSHTEIN DISTANCE
    // =========================================================

    public static int levenshtein(String a, String b) {

        a = a.toLowerCase();
        b = b.toLowerCase();

        int m = a.length();
        int n = b.length();

        int[][] dp = new int[m + 1][n + 1];

        // Base cases
        for (int i = 0; i <= m; i++)
            dp[i][0] = i;

        for (int j = 0; j <= n; j++)
            dp[0][j] = j;

        // DP calculation
        for (int i = 1; i <= m; i++) {

            for (int j = 1; j <= n; j++) {

                int cost =
                        a.charAt(i - 1) == b.charAt(j - 1)
                                ? 0
                                : 1;

                dp[i][j] = Math.min(
                        Math.min(
                                dp[i - 1][j] + 1,       // deletion
                                dp[i][j - 1] + 1        // insertion
                        ),
                        dp[i - 1][j - 1] + cost        // replacement
                );
            }
        }

        // Display DP table
        System.out.println();
        System.out.println("LEVENSHTEIN DP TABLE");
        System.out.println("--------------------");

        printDPTable(a, b, dp);

        System.out.println();
        System.out.println("Final Distance = " + dp[m][n]);

        return dp[m][n];
    }


    // =========================================================
    // 2. DAMERAU-LEVENSHTEIN DISTANCE
    // =========================================================

    public static int damerau(String a, String b) {

        a = a.toLowerCase();
        b = b.toLowerCase();

        int m = a.length();
        int n = b.length();

        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++)
            dp[i][0] = i;

        for (int j = 0; j <= n; j++)
            dp[0][j] = j;

        for (int i = 1; i <= m; i++) {

            for (int j = 1; j <= n; j++) {

                int cost =
                        a.charAt(i - 1) ==
                        b.charAt(j - 1)
                                ? 0
                                : 1;

                dp[i][j] = Math.min(
                        Math.min(
                                dp[i - 1][j] + 1,
                                dp[i][j - 1] + 1
                        ),
                        dp[i - 1][j - 1] + cost
                );

                // Transposition
                if (i > 1 &&
                    j > 1 &&
                    a.charAt(i - 1) == b.charAt(j - 2) &&
                    a.charAt(i - 2) == b.charAt(j - 1)) {

                    dp[i][j] = Math.min(
                            dp[i][j],
                            dp[i - 2][j - 2] + 1
                    );
                }
            }
        }

        System.out.println();
        System.out.println("DAMERAU-LEVENSHTEIN DP TABLE");
        System.out.println("-----------------------------");

        printDPTable(a, b, dp);

        System.out.println();
        System.out.println("Final Distance = " + dp[m][n]);

        return dp[m][n];
    }


    // =========================================================
    // 3. WEIGHTED EDIT DISTANCE
    // =========================================================

    public static int weightedEdit(String a, String b) {

        a = a.toLowerCase();
        b = b.toLowerCase();

        int m = a.length();
        int n = b.length();

        /*
         * Costs used in LifeData:
         *
         * Insertion  = 2
         * Deletion   = 2
         * Replacement = 3
         */

        int INSERT_COST = 2;
        int DELETE_COST = 2;
        int REPLACE_COST = 3;

        int[][] dp = new int[m + 1][n + 1];

        // Base cases
        for (int i = 0; i <= m; i++)
            dp[i][0] = i * DELETE_COST;

        for (int j = 0; j <= n; j++)
            dp[0][j] = j * INSERT_COST;

        // DP
        for (int i = 1; i <= m; i++) {

            for (int j = 1; j <= n; j++) {

                int replacementCost =
                        a.charAt(i - 1) ==
                        b.charAt(j - 1)
                                ? 0
                                : REPLACE_COST;

                int deletion =
                        dp[i - 1][j] + DELETE_COST;

                int insertion =
                        dp[i][j - 1] + INSERT_COST;

                int replacement =
                        dp[i - 1][j - 1]
                        + replacementCost;

                dp[i][j] = Math.min(
                        Math.min(
                                deletion,
                                insertion
                        ),
                        replacement
                );
            }
        }

        System.out.println();
        System.out.println("WEIGHTED EDIT DISTANCE DP TABLE");
        System.out.println("-------------------------------");

        printDPTable(a, b, dp);

        System.out.println();
        System.out.println("Insertion Cost  : " + INSERT_COST);
        System.out.println("Deletion Cost   : " + DELETE_COST);
        System.out.println("Replacement Cost: " + REPLACE_COST);

        System.out.println();
        System.out.println(
                "Final Weighted Distance = "
                + dp[m][n]
        );

        return dp[m][n];
    }


    // =========================================================
    // PRINT DP TABLE
    // =========================================================

    private static void printDPTable(
            String a,
            String b,
            int[][] dp) {

        System.out.print("    ");

        for (int j = 0; j < b.length(); j++) {

            System.out.printf(
                    "%4c",
                    b.charAt(j)
            );
        }

        System.out.println();

        for (int i = 0; i <= a.length(); i++) {

            if (i == 0)
                System.out.print(" ");

            else
                System.out.print(a.charAt(i - 1));

            for (int j = 0; j <= b.length(); j++) {

                System.out.printf(
                        "%4d",
                        dp[i][j]
                );
            }

            System.out.println();
        }
    }


    // =========================================================
    // 4. BITMASK DP FOR LIFEDATA ACTIVITIES
    // =========================================================

    public static List<String> bitmaskCombinations(
            List<String> items) {

        List<String> result =
                new ArrayList<>();

        /*
         * To keep the output manageable,
         * use maximum 4 activities.
         */

        int n = Math.min(items.size(), 4);

        System.out.println();
        System.out.println(
                "BITMASK ACTIVITY COMBINATIONS"
        );

        System.out.println(
                "-----------------------------"
        );

        System.out.println(
                "Number of activities used = " + n
        );

        System.out.println();

        /*
         * Total subsets = 2^n
         */

        int total =
                1 << n;

        System.out.println(
                "Total possible subsets = "
                + total
        );

        System.out.println();

        for (int mask = 1;
             mask < total;
             mask++) {

            StringBuilder combination =
                    new StringBuilder();

            System.out.printf(
                    "Mask %" + n + "s -> ",
                    Integer.toBinaryString(mask)
            );

            for (int i = 0;
                 i < n;
                 i++) {

                if ((mask & (1 << i)) != 0) {

                    if (combination.length() > 0)
                        combination.append(", ");

                    combination.append(
                            items.get(i)
                    );
                }
            }

            String output =
                    "{" + combination + "}";

            result.add(output);

            System.out.println(output);
        }

        return result;
    }


    // =========================================================
    // 5. FIND CLOSEST ACTIVITY
    // =========================================================

    public static void findClosestActivity(
            List<Activity> activities,
            String query) {

        System.out.println();
        System.out.println(
                "CLOSEST ACTIVITY SEARCH"
        );

        System.out.println(
                "======================="
        );

        System.out.println(
                "User Input : " + query
        );

        int bestDistance =
                Integer.MAX_VALUE;

        Activity bestActivity = null;

        for (Activity activity : activities) {

            String activityName =
                    activity.getActivity();

            int distance =
                    damerau(
                            query,
                            activityName
                    );

            System.out.println(
                    activityName +
                    " -> Distance = " +
                    distance
            );

            if (distance < bestDistance) {

                bestDistance = distance;
                bestActivity = activity;
            }
        }

        System.out.println();
        System.out.println(
                "========== BEST MATCH =========="
        );

        if (bestActivity != null) {

            System.out.println(
                    "Activity : "
                    + bestActivity.getActivity()
            );

            System.out.println(
                    "Distance : "
                    + bestDistance
            );

            System.out.println(
                    "Record   : "
                    + bestActivity
            );
        }
    }
}