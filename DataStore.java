package lifedata;

import java.io.*;
import java.util.*;

public class DataStore {
    private final File file;

    public DataStore(String path) {
        file = new File(path);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) parent.mkdirs();
    }

    public List<Activity> load() {
        List<Activity> list = new ArrayList<Activity>();
        if (!file.exists()) return list;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                Activity a = Activity.fromLine(line);
                if (a != null) list.add(a);
            }
        } catch (IOException e) {
            System.out.println("Could not read data: " + e.getMessage());
        }
        return list;
    }

    public void append(Activity a) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.write(a.toLine());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Could not save activity: " + e.getMessage());
        }
    }
}
