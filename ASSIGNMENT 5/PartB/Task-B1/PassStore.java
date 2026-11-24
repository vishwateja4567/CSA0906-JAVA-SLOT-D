import java.io.*;
import java.nio.file.*;
import java.util.*;

public class PassStore {
    private final Path file;
    public PassStore(Path file) { this.file = file; }

    // Five fields as shown in the assignment's supplied code.
    public void add(String passId, String regNo, String name, int route, double amount)
            throws IOException {
        try (BufferedWriter w = Files.newBufferedWriter(file,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            w.write(String.join(",", passId, regNo, name,
                    String.valueOf(route), String.valueOf(amount)));
            w.newLine();
        }
    }

    public String[] find(String passId) throws IOException {
        if (!Files.exists(file)) return null;
        try (BufferedReader r = Files.newBufferedReader(file)) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] f = line.split(",", -1);
                if (f.length != 5) continue;
                try {
                    Integer.parseInt(f[3]);
                    Double.parseDouble(f[4]);
                } catch (NumberFormatException ex) { continue; }
                if (f[0].equals(passId)) return f;
            }
        }
        return null;
    }

    public List<String[]> listByRoute(int route) throws IOException {
        List<String[]> result = new ArrayList<>();
        if (!Files.exists(file)) return result;
        try (BufferedReader r = Files.newBufferedReader(file)) {
            String line;
            while ((line = r.readLine()) != null) {
                String[] f = line.split(",", -1);
                if (f.length != 5) continue;
                try {
                    if (Integer.parseInt(f[3]) == route) {
                        Double.parseDouble(f[4]);
                        result.add(f);
                    }
                } catch (NumberFormatException ignored) { }
            }
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        Path p = Paths.get("passes.csv");
        Files.deleteIfExists(p);
        PassStore store = new PassStore(p);
        store.add("P1001", "192311001", "Arun", 12, 3800);
        Files.writeString(p, "P1002,DAMAGED LINE\n", StandardOpenOption.APPEND);
        store.add("P1003", "192311003", "Divya", 7, 3800);
        System.out.println("Find P1003: " + Arrays.toString(store.find("P1003")));
        System.out.println("Find P9999: " + Arrays.toString(store.find("P9999")));
        System.out.println("Route 7 count: " + store.listByRoute(7).size());
        System.out.println("Damaged P1002 skipped: " + (store.find("P1002") == null));
    }
}
