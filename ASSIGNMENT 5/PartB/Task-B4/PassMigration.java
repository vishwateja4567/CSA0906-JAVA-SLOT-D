import java.io.*;
import java.nio.file.*;
import java.sql.*;

public class PassMigration {
    static boolean valid(String[] f) {
        if(f.length!=5 || f[0].isBlank() || f[1].isBlank() || f[2].isBlank()) return false;
        Integer.parseInt(f[3]); Double.parseDouble(f[4]); return true;
    }
    public static void main(String[] args) throws Exception {
        Path input=Paths.get(args.length>0?args[0]:"migration_input.csv");
        Path rejected=Paths.get("migration_failed.csv");
        int good=0,bad=0;
        try(Connection c=DBConfig.getConnection()) {
            try(Statement s=c.createStatement()) {
                s.executeUpdate("CREATE TABLE IF NOT EXISTS bus_pass (pass_id VARCHAR(30) PRIMARY KEY, reg_no VARCHAR(30), student_name VARCHAR(60), route_no INT, amount DOUBLE)");
            }
            c.setAutoCommit(false);
            try(BufferedReader r=Files.newBufferedReader(input);
                BufferedWriter w=Files.newBufferedWriter(rejected);
                PreparedStatement p=c.prepareStatement("INSERT INTO bus_pass VALUES (?,?,?,?,?)")) {
                String line;
                while((line=r.readLine())!=null) {
                    String[] f=line.split(",",-1);
                    try {
                        if(!valid(f)) throw new IllegalArgumentException("Invalid fields");
                        p.setString(1,f[0]); p.setString(2,f[1]); p.setString(3,f[2]);
                        p.setInt(4,Integer.parseInt(f[3])); p.setDouble(5,Double.parseDouble(f[4]));
                        p.addBatch(); good++;
                        if(good%500==0) p.executeBatch();
                    } catch(RuntimeException ex) {
                        w.write(line+" <-- "+ex.getMessage()); w.newLine(); bad++;
                    }
                }
                p.executeBatch(); c.commit();
            } catch(Exception ex) { c.rollback(); throw ex; }
            finally { c.setAutoCommit(true); }

            System.out.println("Migration completed");
            System.out.println("Valid rows submitted: "+good);
            System.out.println("Rejected rows: "+bad);
            System.out.println("Failure report: "+rejected.toAbsolutePath());
            DatabaseMetaData m=c.getMetaData();
            System.out.println("Database: "+m.getDatabaseProductName());
            System.out.println("Driver: "+m.getDriverName());
            System.out.println("Driver version: "+m.getDriverVersion());
            try(ResultSet t=m.getTables(c.getCatalog(),null,"%",new String[]{"TABLE"})) {
                System.out.println("Tables:");
                while(t.next()) System.out.println("- "+t.getString("TABLE_NAME"));
            }
        }
    }
}
