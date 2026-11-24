import java.sql.*;
import java.util.*;

public class PassLoaderBenchmark {
    static void setup(Connection c) throws SQLException {
        try (Statement s = c.createStatement()) {
            s.executeUpdate("CREATE TABLE IF NOT EXISTS bus_pass (pass_id VARCHAR(30) PRIMARY KEY, reg_no VARCHAR(30), student_name VARCHAR(60), route_no INT, amount DOUBLE)");
        }
    }
    static void clear(Connection c) throws SQLException {
        try (Statement s = c.createStatement()) { s.executeUpdate("DELETE FROM bus_pass"); }
    }
    static List<String[]> rows(int n, String prefix) {
        List<String[]> a = new ArrayList<>();
        for (int i=1; i<=n; i++) a.add(new String[]{prefix+i,"REG"+i,"Student"+i,"7","3800.0"});
        return a;
    }
    static long statement(Connection c, List<String[]> a) throws SQLException {
        clear(c); long t=System.nanoTime();
        try (Statement s=c.createStatement()) {
            for (String[] p:a) s.executeUpdate("INSERT INTO bus_pass VALUES ('"+p[0]+"','"+p[1]+"','"+p[2]+"',"+p[3]+","+p[4]+")");
        }
        return (System.nanoTime()-t)/1_000_000;
    }
    static long prepared(Connection c, List<String[]> a) throws SQLException {
        clear(c); long t=System.nanoTime();
        try (PreparedStatement p=c.prepareStatement("INSERT INTO bus_pass VALUES (?,?,?,?,?)")) {
            for (String[] r:a) { for(int i=0;i<5;i++) p.setString(i+1,r[i]); p.executeUpdate(); }
        }
        return (System.nanoTime()-t)/1_000_000;
    }
    static long batch(Connection c, List<String[]> a) throws SQLException {
        clear(c); long t=System.nanoTime();
        try (PreparedStatement p=c.prepareStatement("INSERT INTO bus_pass VALUES (?,?,?,?,?)")) {
            for (String[] r:a) { for(int i=0;i<5;i++) p.setString(i+1,r[i]); p.addBatch(); }
            p.executeBatch();
        }
        return (System.nanoTime()-t)/1_000_000;
    }
    static void failureTest(Connection c, boolean transaction) throws SQLException {
        clear(c); c.setAutoCommit(!transaction);
        try (PreparedStatement p=c.prepareStatement("INSERT INTO bus_pass VALUES (?,?,?,?,?)")) {
            for(int i=1;i<=6;i++) {
                p.setString(1, i==4 ? "F3" : "F"+i); // duplicate key at item 4
                p.setString(2,"R"+i); p.setString(3,"Test"+i); p.setInt(4,7); p.setDouble(5,3800);
                p.executeUpdate();
            }
            if(transaction) c.commit();
        } catch(SQLException ex) {
            if(transaction) c.rollback();
            System.out.println((transaction?"Transaction":"Auto-commit")+" error: "+ex.getMessage());
        } finally { c.setAutoCommit(true); }
        try(Statement s=c.createStatement(); ResultSet r=s.executeQuery("SELECT COUNT(*) FROM bus_pass")) {
            r.next(); System.out.println((transaction?"Transaction":"Auto-commit")+" rows remaining: "+r.getInt(1));
        }
    }
    static void metadata(Connection c) throws SQLException {
        try(Statement s=c.createStatement(); ResultSet r=s.executeQuery("SELECT * FROM bus_pass LIMIT 1")) {
            ResultSetMetaData m=r.getMetaData();
            for(int i=1;i<=m.getColumnCount();i++)
                System.out.println(m.getColumnName(i)+" : "+m.getColumnTypeName(i));
        }
    }
    public static void main(String[] args) throws Exception {
        try(Connection c=DBConfig.getConnection()) {
            setup(c);
            for(int n:new int[]{10,1000,10000,100000}) {
                List<String[]> a=rows(n,"N"+n+"P");
                System.out.println("\\nRecords: "+n);
                System.out.println("Statement: "+statement(c,a)+" ms");
                System.out.println("PreparedStatement: "+prepared(c,a)+" ms");
                System.out.println("Batch: "+batch(c,a)+" ms");
            }
            System.out.println("\\nColumn metadata:");
            metadata(c);
            System.out.println("\\nForced failure:");
            failureTest(c,false); failureTest(c,true);
        }
    }
}
