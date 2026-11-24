import java.io.*;
import java.nio.file.*;
import java.util.*;

public class LineCounterBenchmark {
    static long countUnbuffered(Path p) throws IOException {
        long lines = 0;
        try (InputStream in = new FileInputStream(p.toFile())) {
            int x;
            while ((x = in.read()) != -1) if (x == '\n') lines++;
        }
        return lines;
    }

    static long countBuffered(Path p) throws IOException {
        long lines = 0;
        byte[] buffer = new byte[8192];
        try (InputStream in = new BufferedInputStream(new FileInputStream(p.toFile()))) {
            int n;
            while ((n = in.read(buffer)) != -1)
                for (int i = 0; i < n; i++) if (buffer[i] == '\n') lines++;
        }
        return lines;
    }

    static void createFile(Path p, int mb) throws IOException {
        byte[] block = "attendance-record-192425326\n".getBytes();
        long target = mb * 1024L * 1024L;
        try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(p))) {
            long written = 0;
            while (written + block.length <= target) {
                out.write(block); written += block.length;
            }
            if (written < target) out.write(new byte[(int)(target - written)]);
        }
    }

    static void runSize(int mb) throws IOException {
        Path p = Paths.get("line_test_" + mb + "MB.txt");
        createFile(p, mb);
        countBuffered(p); // warm-up
        long t = System.nanoTime(); long a = countUnbuffered(p);
        long unbuf = (System.nanoTime() - t) / 1_000_000;
        t = System.nanoTime(); long b = countBuffered(p);
        long buf = (System.nanoTime() - t) / 1_000_000;
        System.out.println(mb + " MB | unbuffered " + unbuf + " ms (" + a +
                " lines) | buffered " + buf + " ms (" + b + " lines)");
        Files.deleteIfExists(p);
    }

    static void searchGrowth() throws IOException {
        Path p = Paths.get("growth_passes.csv");
        for (int n : new int[]{1000, 10000, 50000, 100000}) {
            try (BufferedWriter w = Files.newBufferedWriter(p)) {
                for (int i = 1; i <= n; i++)
                    w.write("P" + i + ",R" + i + ",Student" + i + ",7,3800.0\n");
            }
            long t = System.nanoTime();
            new PassStore(p).find("P" + n);
            System.out.println(n + " records | last record search: " +
                    (System.nanoTime() - t) / 1_000_000 + " ms");
        }
        Files.deleteIfExists(p);
    }

    public static void main(String[] args) throws Exception {
        for (int mb : new int[]{1, 10, 50}) runSize(mb);
        System.out.println("Search growth:");
        searchGrowth();
    }
}
