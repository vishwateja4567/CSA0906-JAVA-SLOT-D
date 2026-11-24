# TASK A3 – DISCOVER THE DATA-ACCESS DESIGN PATTERN
**Topic:** Generic Data Access Object (DAO)

## A3.1 – Identify the Common Pattern

The three classes `StudentDB`, `CourseDB` and `MarksDB` repeat these steps:

1. Open a database connection.
2. Prepare or build an SQL statement.
3. Execute the SQL statement.
4. Read the `ResultSet`.
5. Convert database rows into Java objects.
6. Close the result set, statement and connection.

**Parts that differ:** table name, column names, object type, primary key, SQL parameters and object-mapping logic.

The repeated database operations can be moved into a common DAO design. Each specific DAO supplies the SQL and mapping details for its entity.

## A3.2 – Design a Generic Data-Access Interface

### Generic interface

```java
public interface Dao<T, K> {
    void insert(T object);
    T findById(K id);
    List<T> findAll();
    void update(T object);
    void delete(K id);
}
```

### Student class

```java
public class Student {
    private int studentId;
    private String studentName;
    private String email;

    public Student(int studentId, String studentName, String email) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.email = email;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getEmail() {
        return email;
    }
}
```

### StudentDao implementation outline

```java
public class StudentDao implements Dao<Student, Integer> {
    // Store or receive a JDBC Connection.

    @Override
    public void insert(Student student) {
        // Use PreparedStatement to insert a student.
    }

    @Override
    public Student findById(Integer id) {
        // Select one student by primary key and map the row.
        return null;
    }

    @Override
    public List<Student> findAll() {
        // Select all students and return mapped objects.
        return new ArrayList<>();
    }

    @Override
    public void update(Student student) {
        // Update student details using PreparedStatement.
    }

    @Override
    public void delete(Integer id) {
        // Delete the student using PreparedStatement.
    }
}
```

The methods contain implementation outlines; the purpose of this Part A task is to show the generic interface and how `StudentDao` implements it.

## A3.3 – Map Objects to Tables

Example mapping:

| Java `Student` field | Database column | Role |
|---|---|---|
| `studentId` | `student_id` | Primary key |
| `studentName` | `student_name` | Student name |
| `email` | `email` | Email address |

The `student_id` column uniquely identifies each student and is the primary key.

## Alternative Concept – SQL Inside Service or GUI Classes

If SQL is written directly inside service or GUI classes:

- SQL statements and column names may be repeated in multiple places.
- Renaming a table column may require changes in several classes.
- Database logic becomes tightly coupled to the user interface or business logic.

With DAO, SQL and row mapping are mainly kept in the DAO implementation. A column rename is generally handled in fewer places.

**Complexity comparison:**

- DAO and direct-SQL designs usually have the same database-operation time complexity for the same SQL query and database indexes.
- DAO introduces a small fixed method-call and abstraction overhead, which does not change the Big-O complexity.
- DAO uses some additional object/code structure, but the database result storage is determined mainly by the query and returned rows.

## Result

The DAO pattern separates database access from application logic, reduces repeated code and makes database changes easier to maintain.
