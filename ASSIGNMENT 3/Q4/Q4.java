
import java.io.*;

public class SalesReport {

    static void manualVersion(String[] files) {
        BufferedReader reader = null;

        try {
            FileWriter writer = new FileWriter("manual_report.txt");

            for (String file : files) {
                System.out.println("Reading: " + file);

                reader = new BufferedReader(new FileReader(file));

                String line;
                while ((line = reader.readLine()) != null) {
                    writer.write(line + "\n");
                }

                reader.close();
                reader = null;
            }

            writer.close();
            System.out.println("Manual report completed.");

        } catch (IOException e) {
            System.out.println("Manual error: " + e.getMessage());
            // reader may remain open if an exception occurs
        }
    }

    static void safeVersion(String[] files) {
        try (FileWriter writer =
                 new FileWriter("safe_report.txt")) {

            for (String file : files) {
                System.out.println("Reading safely: " + file);

                try (BufferedReader reader =
                         new BufferedReader(new FileReader(file))) {

                    String line;
                    while ((line = reader.readLine()) != null) {
                        writer.write(line + "\n");
                    }
                }
            }

            System.out.println("Safe report completed.");

        } catch (IOException e) {
            System.out.println("Safe error: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {

        try (FileWriter writer = new FileWriter("store1.txt")) {
            writer.write("Sales: 1000\n");
        }

        try (FileWriter writer = new FileWriter("store3.txt")) {
            writer.write("Sales: 3000\n");
        }

        String[] files = {
            "store1.txt",
            "missing_store.txt",
            "store3.txt"
        };

        System.out.println("Manual version:");
        manualVersion(files);

        System.out.println("\nTry-with-resources version:");
        safeVersion(files);
    }
}
