package lifedata;

public class Activity {
    private String id, date, category, activity, note, tags;

    public Activity(String id, String date, String category, String activity,
                    String note, String tags) {
        this.id = id;
        this.date = date;
        this.category = category;
        this.activity = activity;
        this.note = note;
        this.tags = tags;
    }

    public static Activity fromLine(String line) {
        String[] p = line.split("\\|", -1);
        if (p.length < 6) return null;
        return new Activity(p[0], p[1], p[2], p[3], p[4], p[5]);
    }

    public String toLine() {
        return String.join("|", id, date, category, activity, note, tags);
    }

    public String getId() { return id; }
    public String getDate() { return date; }
    public String getCategory() { return category; }
    public String getActivity() { return activity; }
    public String getNote() { return note; }
    public String getTags() { return tags; }

    public String searchableText() {
        return id + " " + date + " " + category + " " + activity + " " + note + " " + tags;
    }

    @Override
    public String toString() {
        return String.format("%s | %s | %-10s | %-20s | %s | [%s]",
                id, date, category, activity, note, tags);
    }
}
