package lifedata;

import java.util.*;

public class FlowAlgorithms {

    // =========================================================
    // SAMPLE GRAPH
    // =========================================================
    static int[][] sampleGraph() {

        return new int[][]{

            {0, 10, 8, 0, 0, 0},
            {0, 0, 2, 4, 0, 0},
            {0, 0, 0, 0, 5, 0},
            {0, 0, 0, 0, 2, 6},
            {0, 0, 0, 0, 0, 7},
            {0, 0, 0, 0, 0, 0}

        };
    }


    // =========================================================
    // FORD-FULKERSON
    //
    // This implementation uses BFS to find augmenting paths.
    //
    // Time Complexity:
    // O(V * E^2) for the BFS-based implementation
    //
    // With adjacency matrix:
    // E <= V^2
    // Worst case becomes O(V^5)
    //
    // Space Complexity: O(V^2)
    // =========================================================
    public static int fordFulkerson(
            int[][] capacity,
            int source,
            int sink) {

        int n = capacity.length;
        int max = 0;

        int[][] r = new int[n][n];

        for (int i = 0; i < n; i++)
            r[i] = capacity[i].clone();

        while (true) {

            int[] parent = new int[n];
            Arrays.fill(parent, -1);

            boolean[] seen = new boolean[n];

            Queue<Integer> q =
                new LinkedList<Integer>();

            q.add(source);
            seen[source] = true;

            while (!q.isEmpty()) {

                int u = q.remove();

                for (int v = 0; v < n; v++) {

                    if (!seen[v] && r[u][v] > 0) {

                        parent[v] = u;
                        seen[v] = true;
                        q.add(v);
                    }
                }
            }

            if (!seen[sink])
                break;

            int path =
                Integer.MAX_VALUE;

            for (int v = sink;
                 v != source;
                 v = parent[v]) {

                path = Math.min(
                    path,
                    r[parent[v]][v]
                );
            }

            for (int v = sink;
                 v != source;
                 v = parent[v]) {

                int u = parent[v];

                r[u][v] -= path;
                r[v][u] += path;
            }

            max += path;

            System.out.println(
                "  Augmenting path flow = " +
                path +
                ", total = " +
                max
            );
        }

        return max;
    }


    // =========================================================
    // EDMONDS-KARP
    //
    // Current implementation calls Ford-Fulkerson above,
    // which itself uses BFS.
    //
    // Time Complexity: O(V * E^2)
    //
    // With adjacency matrix: O(V^5) worst case
    //
    // Space Complexity: O(V^2)
    // =========================================================
    public static int edmondsKarp(
            int[][] capacity,
            int s,
            int t) {

        return fordFulkerson(
            capacity,
            s,
            t
        );
    }


    // =========================================================
    // DINIC
    //
    // Standard Dinic:
    // O(V^2 E)
    //
    // With adjacency matrix E <= V^2:
    // O(V^4) worst case
    //
    // Space Complexity: O(V^2)
    // =========================================================
    public static int dinic(
            int[][] capacity,
            int s,
            int t) {

        int n = capacity.length;
        int max = 0;

        int[][] r = new int[n][n];

        for (int i = 0; i < n; i++)
            r[i] = capacity[i].clone();

        while (true) {

            int[] level = new int[n];
            Arrays.fill(level, -1);

            Queue<Integer> q =
                new LinkedList<Integer>();

            q.add(s);
            level[s] = 0;

            while (!q.isEmpty()) {

                int u = q.remove();

                for (int v = 0; v < n; v++) {

                    if (level[v] < 0 &&
                        r[u][v] > 0) {

                        level[v] =
                            level[u] + 1;

                        q.add(v);
                    }
                }
            }

            if (level[t] < 0)
                break;

            while (true) {

                int f = sendFlow(
                    r,
                    level,
                    s,
                    t,
                    Integer.MAX_VALUE
                );

                if (f == 0)
                    break;

                max += f;

                System.out.println(
                    "  Blocking-flow increment = " +
                    f +
                    ", total = " +
                    max
                );
            }
        }

        return max;
    }


    // =========================================================
    // SEND FLOW
    // =========================================================
    private static int sendFlow(
            int[][] r,
            int[] level,
            int u,
            int t,
            int f) {

        if (u == t)
            return f;

        for (int v = 0;
             v < r.length;
             v++) {

            if (level[v] == level[u] + 1 &&
                r[u][v] > 0) {

                int got = sendFlow(
                    r,
                    level,
                    v,
                    t,
                    Math.min(f, r[u][v])
                );

                if (got > 0) {

                    r[u][v] -= got;
                    r[v][u] += got;

                    return got;
                }
            }
        }

        return 0;
    }


    // =========================================================
    // MIN CUT
    //
    // Runs max-flow first and then performs BFS.
    //
    // Time Complexity:
    // Same dominant complexity as Ford-Fulkerson
    //
    // O(V * E^2)
    //
    // Matrix worst case: O(V^5)
    //
    // Space Complexity: O(V^2)
    // =========================================================
    public static List<String> minCut(
            int[][] capacity,
            int s,
            int t) {

        int n = capacity.length;

        int[][] r =
            new int[n][n];

        for (int i = 0; i < n; i++)
            r[i] = capacity[i].clone();

        fordFulkerson(r, s, t);

        boolean[] seen =
            new boolean[n];

        Queue<Integer> q =
            new LinkedList<Integer>();

        q.add(s);
        seen[s] = true;

        while (!q.isEmpty()) {

            int u = q.remove();

            for (int v = 0; v < n; v++) {

                if (!seen[v] &&
                    r[u][v] > 0) {

                    seen[v] = true;
                    q.add(v);
                }
            }
        }

        List<String> cut =
            new ArrayList<String>();

        for (int u = 0; u < n; u++) {

            if (seen[u]) {

                for (int v = 0; v < n; v++) {

                    if (!seen[v] &&
                        capacity[u][v] > 0) {

                        cut.add(
                            u +
                            " -> " +
                            v +
                            " (capacity " +
                            capacity[u][v] +
                            ")"
                        );
                    }
                }
            }
        }

        return cut;
    }
}