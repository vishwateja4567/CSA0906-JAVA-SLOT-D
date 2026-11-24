import java.sql.*;
import java.nio.file.*;
import java.io.*;

public class AdversarialInputTest {
    static boolean unsafeLogin(Connection c, String id, String pwd) throws SQLException {
        String sql = "SELECT * FROM users WHERE id='" + id + "' AND pwd='" + pwd + "'";
        try (Statement s = c.createStatement(); ResultSet r = s.executeQuery(sql)) {
            return r.next();
        }
    }
    static boolean safeLogin(Connection c, String id, String pwd) throws SQLException {
        String sql = "SELECT * FROM users WHERE id=? AND pwd=?";
        try (PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, id); p.setString(2, pwd);
            try (ResultSet r = p.executeQuery()) { return r.next(); }
        }
    }
    static void damagedFileTest() throws IOException {
        Path p = Paths.get("damaged_passes.csv");
        Files.writeString(p, "P2001,192425326,Rohith,7,3800.0\n" +
                "P2002,192425327,Meena,7\n" + "P2003,192425328,Arun,7,ABC\n" +
                "\n" + "P2004,192425329,Divya,8,4200.0\n");
        int good = 0, bad = 0;
        for (String line : Files.readAllLines(p)) {
            String[] f = line.split(",", -1);
            try {
                if (f.length != 5 || f[0].isBlank()) throw new IllegalArgumentException();
                Integer.parseInt(f[3]); Double.parseDouble(f[4]); good++;
                System.out.println("Accepted: " + f[0]);
            } catch (RuntimeException ex) {
                bad++; System.out.println("Rejected damaged line: " + line);
            }
        }
        System.out.println("Valid=" + good + ", rejected=" + bad);
    }
    public static void main(String[] args) throws Exception {
        try (Connection c = DBConfig.getConnection()) {
            try (Statement s = c.createStatement()) {
                s.executeUpdate("CREATE TABLE IF NOT EXISTS users (id VARCHAR(50) PRIMARY KEY, pwd VARCHAR(100) NOT NULL)");
                s.executeUpdate("DELETE FROM users");
                s.executeUpdate("INSERT INTO users VALUES ('student1','correct123')");
            }
            String attack = "' OR '1'='1' -- ";
            System.out.println("Unsafe accepts attack: " + unsafeLogin(c, attack, "wrong"));
            System.out.println("PreparedStatement accepts attack: " + safeLogin(c, attack, "wrong"));
            System.out.println("Correct login accepted: " + safeLogin(c, "student1", "correct123"));
        }
        damagedFileTest();
    }
}
