package lifedata;

import java.util.*;

public class LifeDataApp {
    private static final String DATA="data/activities.txt";
    private static DataStore store=new DataStore(DATA);
    private static Scanner sc=new Scanner(System.in);

    public static void main(String[] args){
        while(true){
            System.out.println("\n==============================================================");
            System.out.println("       LIFEDATA – PERSONAL DATA ANALYTICS PLATFORM");
            System.out.println("==============================================================");
            System.out.println(" 1. KMP Keyword Search");
            System.out.println(" 2. Z-Function Repeated Pattern");
            System.out.println(" 3. Rabin-Karp Event ID Search");
            System.out.println(" 4. Aho-Corasick Multiple Keywords");
            System.out.println(" 5. Suffix Array Indexing");
            System.out.println(" 6. SA-IS Indexing Demonstration");
            System.out.println(" 7. LCP / Kasai Repeated Phrases");
            System.out.println(" 8. Levenshtein Distance");
            System.out.println(" 9. Damerau-Levenshtein Distance");
            System.out.println("10. Weighted Edit Distance");
            System.out.println("11. Bitmask DP Activity Combinations");
            System.out.println("12. Ford-Fulkerson Data Flow");
            System.out.println("13. Edmonds-Karp Capacity Flow");
            System.out.println("14. Dinic Large Flow");
            System.out.println("15. Bipartite Matching");
            System.out.println("16. Display All Activities");
            System.out.println("17. Add New Activity");
            System.out.println("18. Search Activity by ID");
            System.out.println(" 0. Exit");
            System.out.print("Select option: ");
            String choice=sc.nextLine().trim();
            try{
                switch(choice){
                    case "1": kmp(); break;
                    case "2": z(); break;
                    case "3": rk(); break;
                    case "4": aho(); break;
                    case "5": suffix(false); break;
                    case "6": suffix(true); break;
                    case "7": lcp(); break;
                    case "8": edit(1); break;
                    case "9": edit(2); break;
                    case "10": edit(3); break;
                    case "11": bitmask(); break;
                    case "12": flow(1); break;
                    case "13": flow(2); break;
                    case "14": flow(3); break;
                    case "15": matching(); break;
                    
                    case "16": display(); break;
                    case "17": add(); break;
                    case "18": searchId(); break;
                    case "0": System.out.println("Exiting LifeData. Goodbye!"); return;
                    default: System.out.println("Invalid option.");
                }
            }catch(Exception e){System.out.println("Error: "+e.getMessage());}
            System.out.println("\nPress ENTER to continue...");
            sc.nextLine();
        }
    }

    static List<Activity> data(){return store.load();}

    static void display(){
        System.out.println("\n--- ALL ACTIVITY RECORDS ---");
        for(Activity a:data()) System.out.println(a);
        System.out.println("Total records: "+data().size());
    }

    static void kmp() {

        System.out.print("Enter keyword: ");
        String p = sc.nextLine();

        System.out.println("\nKMP STEPS");

        // Start measuring time
        long startTime = System.nanoTime();

        int totalTextLength = 0;
        int patternLength = p.length();
        int totalMatches = 0;

        for (Activity a : data()) {

            String text = a.searchableText();

            totalTextLength += text.length();

            List<Integer> pos =
                StringAlgorithms.kmpSearch(text, p);

            if (!pos.isEmpty()) {

                totalMatches += pos.size();

                System.out.println(
                    "FOUND in " + a.getId() +
                    " at positions " + pos);
            }
        }

        // Stop measuring time
        long endTime = System.nanoTime();

        // Calculate execution time
        long executionTimeNs = endTime - startTime;

        double executionTimeMs =
            executionTimeNs / 1_000_000.0;

        // Calculate n + m
        int complexityValue =
            totalTextLength + patternLength;

        System.out.println("\n--------------------------------");
        System.out.println("TIME COMPLEXITY");
        System.out.println("--------------------------------");

        System.out.println(
            "Total text length (n) : " +
            totalTextLength);

        System.out.println(
            "Pattern length (m)    : " +
            patternLength);

        System.out.println(
            "n + m                 : " +
            complexityValue);

        System.out.println(
            "Total matches         : " +
            totalMatches);

        System.out.println(
            "Execution time        : " +
            executionTimeMs +
            " ms");

        System.out.println(
            "Execution time        : " +
            executionTimeNs +
            " ns");
    }

    static void z() {

        System.out.print("Enter pattern: ");
        String p = sc.nextLine();

        System.out.println("\nZ-FUNCTION STEPS");

        long startTime = System.nanoTime();

        int totalInputLength = 0;
        int totalMatches = 0;

        for (Activity a : data()) {

            String note = a.getNote();

            // Z algorithm input = pattern + separator + text
            String s = p + "$" + note;

            totalInputLength += s.length();

            int[] z =
                StringAlgorithms.zFunction(s);

            boolean found = false;

            for (int i = p.length() + 1;
                 i < z.length;
                 i++) {

                if (z[i] >= p.length()) {

                    found = true;
                    totalMatches++;
                    break;
                }
            }

            if (found) {

                System.out.println(
                    "Pattern '" + p +
                    "' found in " +
                    a.getId());
            }
        }

        long endTime = System.nanoTime();

        long executionTimeNs =
            endTime - startTime;

        double executionTimeMs =
            executionTimeNs / 1_000_000.0;

        System.out.println("\n--------------------------------");
        System.out.println("TIME COMPLEXITY");
        System.out.println("--------------------------------");

        System.out.println(
            "Pattern length (m)    : " +
            p.length());

        System.out.println(
            "Total Z input length  : " +
            totalInputLength);

        System.out.println(
            "Total matches         : " +
            totalMatches);

        System.out.println(
            "Execution time        : " +
            executionTimeMs +
            " ms");

        System.out.println(
            "Execution time        : " +
            executionTimeNs +
            " ns");
    }

    static void rk() {

        System.out.print(
            "Enter Event ID or ID prefix: ");

        String p = sc.nextLine();

        System.out.println("\nRABIN-KARP STEPS");

        long startTime = System.nanoTime();

        int totalTextLength = 0;
        int patternLength = p.length();
        int totalMatches = 0;
        int totalWindows = 0;

        for (Activity a : data()) {

            String text = a.getId();

            totalTextLength += text.length();

            List<Integer> positions =
                StringAlgorithms.rabinKarp(
                    text, p);

            if (!positions.isEmpty()) {

                totalMatches += positions.size();

                System.out.println(
                    "Rabin-Karp matched: " +
                    a);

                System.out.println(
                    "Positions: " +
                    positions);
            }

            if (text.length() >= patternLength) {

                totalWindows +=
                    text.length() -
                    patternLength + 1;
            }
        }

        long endTime = System.nanoTime();

        long executionTimeNs =
            endTime - startTime;

        double executionTimeMs =
            executionTimeNs / 1_000_000.0;

        System.out.println("\n--------------------------------");
        System.out.println("TIME COMPLEXITY");
        System.out.println("--------------------------------");

        System.out.println(
            "Total text length (n) : " +
            totalTextLength);

        System.out.println(
            "Pattern length (m)    : " +
            patternLength);

        System.out.println(
            "Search windows        : " +
            totalWindows);

        System.out.println(
            "Total matches         : " +
            totalMatches);

        System.out.println(
            "Execution time        : " +
            executionTimeMs +
            " ms");

        System.out.println(
            "Execution time        : " +
            executionTimeNs +
            " ns");
    }

    static void aho() {

        System.out.print(
            "Enter keywords separated by comma: ");

        String[] ps =
            sc.nextLine().split(",");

        List<String> patterns =
            new ArrayList<String>();

        for (String p : ps) {

            patterns.add(p.trim());
        }

        System.out.println("\nAHO-CORASICK STEPS");

        long startTime = System.nanoTime();

        int totalTextLength = 0;
        int totalPatternLength = 0;
        int totalFoundPatterns = 0;

        for (Activity a : data()) {

            String text =
                a.searchableText();

            totalTextLength +=
                text.length();

            Set<String> found =
                StringAlgorithms.ahoCorasickSimple(
                    text,
                    patterns);

            if (!found.isEmpty()) {

                totalFoundPatterns +=
                    found.size();

                System.out.println(
                    a.getId() +
                    " -> " +
                    found);
            }
        }

        for (String p : patterns) {

            totalPatternLength +=
                p.length();
        }

        long endTime = System.nanoTime();

        long executionTimeNs =
            endTime - startTime;

        double executionTimeMs =
            executionTimeNs / 1_000_000.0;

        int numberOfPatterns =
            patterns.size();

        System.out.println("\n--------------------------------");
        System.out.println("TIME COMPLEXITY");
        System.out.println("--------------------------------");

        System.out.println(
            "Number of patterns (k): " +
            numberOfPatterns);

        System.out.println(
            "Total text length (n) : " +
            totalTextLength);

        System.out.println(
            "Total pattern length  : " +
            totalPatternLength);

        System.out.println(
            "Patterns found        : " +
            totalFoundPatterns);

        System.out.println(
            "Execution time        : " +
            executionTimeMs +
            " ms");

        System.out.println(
            "Execution time        : " +
            executionTimeNs +
            " ns");
    }

    static String corpus(){
        StringBuilder b=new StringBuilder();
        for(Activity a:data())b.append(a.getNote()).append(" ");
        return b.toString().trim();
    }

    static void suffix(boolean sais) {

        String s = corpus();

        if (s.length() > 180)
            s = s.substring(0, 180);

        System.out.println(
            "\nInput text length (n): " +
            s.length());

        // Start timer
        long startTime = System.nanoTime();

        int[] sa;

        if (sais)
            sa = SuffixAlgorithms.sais(s);
        else
            sa = SuffixAlgorithms.suffixArray(s);

        // Stop timer
        long endTime = System.nanoTime();

        long executionTimeNs =
            endTime - startTime;

        double executionTimeMs =
            executionTimeNs / 1_000_000.0;

        System.out.println("\nInput text:");
        System.out.println(s);

        System.out.println(
            "\nSuffix Array (first 30 entries):");

        for (int i = 0;
             i < Math.min(30, sa.length);
             i++) {

            System.out.println(
                i +
                ": position " +
                sa[i] +
                " -> " +
                s.substring(
                    sa[i],
                    Math.min(
                        sa[i] + 45,
                        s.length()
                    )
                )
            );
        }

        System.out.println(
            sais
            ? "SA-IS indexing demonstration complete."
            : "Suffix Array indexing complete."
        );


        // =====================================================
        // TIME COMPLEXITY
        // =====================================================

        System.out.println("\n--------------------------------");
        System.out.println("TIME COMPLEXITY");
        System.out.println("--------------------------------");

        System.out.println(
            "Input size (n)       : " +
            s.length());

        System.out.println(
            "Number of suffixes   : " +
            sa.length);

        if (sais) {

            System.out.println(
                "Algorithm             : SA-IS demonstration");

            System.out.println(
                "Theoretical complexity of this code:");
                
            System.out.println(
                "O(n^2 log n)");
        }
        else {

            System.out.println(
                "Algorithm             : Suffix Array");

            System.out.println(
                "Theoretical complexity:");
                
            System.out.println(
                "O(n^2 log n)");
        }

        System.out.println(
            "Execution time        : " +
            executionTimeMs +
            " ms");

        System.out.println(
            "Execution time        : " +
            executionTimeNs +
            " ns");
    }

    static void lcp() {

        String s = corpus();

        if (s.length() > 180)
            s = s.substring(0, 180);

        System.out.println(
            "\nInput text length (n): " +
            s.length());

        // Build suffix array first
        int[] sa =
            SuffixAlgorithms.suffixArray(s);

        // Start timing ONLY Kasai
        long startTime = System.nanoTime();

        int[] lcp =
            SuffixAlgorithms.lcpKasai(s, sa);

        long endTime = System.nanoTime();

        long executionTimeNs =
            endTime - startTime;

        double executionTimeMs =
            executionTimeNs / 1_000_000.0;


        System.out.println("\nLCP/KASAI TABLE");

        for (int i = 0;
             i < Math.min(25, sa.length);
             i++) {

            System.out.println(
                "SA[" + i + "]=" +
                sa[i] +
                "  LCP=" +
                lcp[i] +
                "  " +
                s.substring(
                    sa[i],
                    Math.min(
                        sa[i] + 35,
                        s.length()
                    )
                )
            );
        }


        // =====================================================
        // TIME COMPLEXITY
        // =====================================================

        System.out.println("\n--------------------------------");
        System.out.println("TIME COMPLEXITY");
        System.out.println("--------------------------------");

        System.out.println(
            "Input size (n)       : " +
            s.length());

        System.out.println(
            "LCP array size       : " +
            lcp.length);

        System.out.println(
            "Algorithm             : Kasai LCP");

        System.out.println(
            "Theoretical complexity: O(n)");

        System.out.println(
            "Execution time        : " +
            executionTimeMs +
            " ms");

        System.out.println(
            "Execution time        : " +
            executionTimeNs +
            " ns");
    }
    static void edit(int type) {

        System.out.print("Enter activity to search: ");
        String query = sc.nextLine();

        List<Activity> activities = data();

        System.out.println();
        System.out.println("========================================");
        System.out.println("      LIFEDATA FUZZY ACTIVITY SEARCH");
        System.out.println("========================================");

        System.out.println("User Input : " + query);

        long startTime = System.nanoTime();

        int bestDistance = Integer.MAX_VALUE;
        Activity bestActivity = null;

        for (Activity activity : activities) {

            String activityName =
                    activity.getActivity();

            int distance;

            if (type == 1) {

                distance =
                        DPAlgorithms.levenshtein(
                                query,
                                activityName
                        );

            }
            else if (type == 2) {

                distance =
                        DPAlgorithms.damerau(
                                query,
                                activityName
                        );

            }
            else {

                distance =
                        DPAlgorithms.weightedEdit(
                                query,
                                activityName
                        );
            }

            System.out.println(
                    String.format(
                            "%-25s -> Distance = %d",
                            activityName,
                            distance
                    )
            );

            if (distance < bestDistance) {

                bestDistance = distance;
                bestActivity = activity;
            }
        }

        long endTime = System.nanoTime();

        double executionTime =
                (endTime - startTime) / 1_000_000.0;

        System.out.println();
        System.out.println("========================================");
        System.out.println("              BEST MATCH");
        System.out.println("========================================");

        if (bestActivity != null) {

            System.out.println(
                    "Input Activity    : " + query
            );

            System.out.println(
                    "Matched Activity  : "
                    + bestActivity.getActivity()
            );

            System.out.println(
                    "Activity ID       : "
                    + bestActivity.getId()
            );

            System.out.println(
                    "Category          : "
                    + bestActivity.getCategory()
            );

            System.out.println(
                    "Distance          : "
                    + bestDistance
            );

            System.out.println(
                    "Execution Time    : "
                    + executionTime + " ms"
            );
        }
        else {

            System.out.println(
                    "No activities found."
            );
        }
    }

    static void bitmask(){
        List<String> items=new ArrayList<String>();
        for(Activity a:data())items.add(a.getActivity());
        System.out.println("\nBITMASK DP – combinations of first 4 activities:");
        for(String x:DPAlgorithms.bitmaskCombinations(items))System.out.println(x);
    }

    static int[][] graph(){return FlowAlgorithms.sampleGraph();}

    static void flow(int type) {

        int[][] g = graph();

        System.out.println(
            "\nNodes: 0=Input, 1=Notes, 2=Sensor, " +
            "3=Activity, 4=Analytics, 5=Output"
        );

        // Calculate V
        int vertices = g.length;

        // Calculate E
        int edges = 0;

        for (int i = 0; i < g.length; i++) {

            for (int j = 0; j < g[i].length; j++) {

                if (g[i][j] > 0)
                    edges++;
            }
        }

        // Start timer
        long startTime = System.nanoTime();

        int result;

        if (type == 1) {

            result = FlowAlgorithms.fordFulkerson(
                g, 0, 5
            );

        } else if (type == 2) {

            result = FlowAlgorithms.edmondsKarp(
                g, 0, 5
            );

        } else {

            result = FlowAlgorithms.dinic(
                g, 0, 5
            );
        }

        // Stop timer
        long endTime = System.nanoTime();

        long executionTimeNs =
            endTime - startTime;

        double executionTimeMs =
            executionTimeNs / 1_000_000.0;


        System.out.println(
            "\nMaximum flow = " + result
        );


        // ==========================================
        // TIME COMPLEXITY
        // ==========================================

        System.out.println(
            "\n--------------------------------"
        );

        System.out.println(
            "TIME COMPLEXITY"
        );

        System.out.println(
            "--------------------------------"
        );

        System.out.println(
            "Number of vertices (V) : " +
            vertices
        );

        System.out.println(
            "Number of edges (E)    : " +
            edges
        );


        if (type == 1) {

            System.out.println(
                "Algorithm              : Ford-Fulkerson"
            );

            System.out.println(
                "Theoretical complexity : O(V * E^2)"
            );

        } else if (type == 2) {

            System.out.println(
                "Algorithm              : Edmonds-Karp"
            );

            System.out.println(
                "Theoretical complexity : O(V * E^2)"
            );

        } else {

            System.out.println(
                "Algorithm              : Dinic"
            );

            System.out.println(
                "Theoretical complexity : O(V^2 * E)"
            );
        }


        System.out.println(
            "Execution time         : " +
            executionTimeMs +
            " ms"
        );

        System.out.println(
            "Execution time         : " +
            executionTimeNs +
            " ns"
        );
    }

    static void matching(){
        Map<String,List<String>> g=new LinkedHashMap<String,List<String>>();
        g.put("Text Search",Arrays.asList("CPU-1","CPU-2"));
        g.put("Sensor Analytics",Arrays.asList("CPU-2","CPU-3"));
        g.put("Similarity",Arrays.asList("CPU-1","CPU-3"));
        g.put("Flow Analytics",Arrays.asList("CPU-3","CPU-4"));
        Map<String,String> m=Matching.match(g);
        System.out.println("\nBIPARTITE MATCHING");
        for(Map.Entry<String,String> e:m.entrySet())System.out.println(e.getKey()+" -> "+e.getValue());
    }

    static void mincut() {

        int[][] g = graph();

        // Number of vertices
        int vertices = g.length;

        // Number of edges
        int edges = 0;

        for (int i = 0; i < g.length; i++) {

            for (int j = 0; j < g[i].length; j++) {

                if (g[i][j] > 0)
                    edges++;
            }
        }


        // Start timer
        long startTime = System.nanoTime();

        List<String> cut =
            FlowAlgorithms.minCut(
                g, 0, 5
            );

        // Stop timer
        long endTime = System.nanoTime();

        long executionTimeNs =
            endTime - startTime;

        double executionTimeMs =
            executionTimeNs / 1_000_000.0;


        System.out.println(
            "\nMIN-CUT BOTTLENECK EDGES"
        );

        for (String e : cut)
            System.out.println(e);


        // ==========================================
        // TIME COMPLEXITY
        // ==========================================

        System.out.println(
            "\n--------------------------------"
        );

        System.out.println(
            "TIME COMPLEXITY"
        );

        System.out.println(
            "--------------------------------"
        );

        System.out.println(
            "Number of vertices (V) : " +
            vertices
        );

        System.out.println(
            "Number of edges (E)    : " +
            edges
        );

        System.out.println(
            "Algorithm              : Min-Cut"
        );

        System.out.println(
            "Theoretical complexity : O(V * E^2)"
        );

        System.out.println(
            "Execution time         : " +
            executionTimeMs +
            " ms"
        );

        System.out.println(
            "Execution time         : " +
            executionTimeNs +
            " ns"
        );
    }

    static void add(){
        System.out.print("ID: ");String id=sc.nextLine();
        System.out.print("Date (YYYY-MM-DD): ");String date=sc.nextLine();
        System.out.print("Category: ");String cat=sc.nextLine();
        System.out.print("Activity: ");String act=sc.nextLine();
        System.out.print("Note: ");String note=sc.nextLine();
        System.out.print("Tags (comma separated): ");String tags=sc.nextLine();
        store.append(new Activity(id,date,cat,act,note,tags));
        System.out.println("Activity saved to "+DATA);
    }

    static void searchId(){
        System.out.print("Enter ID: ");String id=sc.nextLine();
        boolean found=false;
        for(Activity a:data())if(a.getId().equalsIgnoreCase(id)){System.out.println(a);found=true;}
        if(!found)System.out.println("No record found.");
    }
}
