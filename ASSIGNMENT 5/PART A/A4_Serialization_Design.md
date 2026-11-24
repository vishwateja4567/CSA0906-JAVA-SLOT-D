# TASK A4 – WHAT SHOULD BE SERIALIZED?
**Topic:** Java object serialization in a hostel management program

## A4.1 – Decide What to Persist

The `Hostel` object contains a list of `Room` objects. Each `Room` contains a list of `Resident` objects. When the `Hostel` object is serialized, Java follows the references and serializes the reachable serializable objects in the object graph.

### Field decisions

| Field | Decision | Reason |
|---|---|---|
| Hostel room list | Persist | Contains hostel room information |
| Room resident list | Persist | Contains resident information |
| Resident name | Persist | Required hostel record |
| Resident register number | Persist | Identifies the resident |
| Resident password | Do not store in plain text; preferably exclude from this object | Passwords should be stored as securely salted password hashes in an authentication system |
| Database `Connection` | `transient` | It is a live resource and cannot meaningfully be restored through serialization |

Example:

```java
class Resident implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String registerNumber;
    private transient java.sql.Connection connection;
}
```

The `transient` keyword prevents the connection field from being serialized. After deserialization, a transient reference has its default value (`null` for an object reference). A new database connection must be obtained when needed.

All non-transient objects in the object graph must also be serializable; otherwise, serialization can fail with `NotSerializableException`.

## A4.2 – Plan for Change

Suppose a new `phoneNumber` field is added to `Resident` after older files have been saved.

- If the class has a compatible, explicitly declared `serialVersionUID`, an older serialized object can generally be read by the new class.
- The new `phoneNumber` field was not present in the old file, so it receives its default value (`null` for `String`).
- If the class does not declare `serialVersionUID`, Java calculates one. Class changes may produce a different value and cause `InvalidClassException` when reading an older file.
- If the stored and local `serialVersionUID` values are incompatible, deserialization fails.

Example:

```java
private static final long serialVersionUID = 1L;
private String phoneNumber;
```

A stable `serialVersionUID` supports compatible class evolution, but it does not automatically solve every data migration or security issue.

## Alternative Concept – CSV or JSON

| Factor | Java serialization | CSV / JSON |
|---|---|---|
| File size | Often compact for object graphs, but includes serialization metadata | Text representation may be larger |
| Save/load time | O(n) for n residents in a simple full save/load | O(n) to write/read n residents |
| Readability | Not directly human-readable | Human-readable |
| Adding a field | Depends on serialization compatibility and defaults | Can add a field; parser/schema must handle missing values |
| Portability | Java-specific format | More language-independent |

Both approaches require processing the stored data, so a full save or load is generally O(n) in the number of residents, excluding the size of individual fields.

## Result

Only necessary persistent data should be serialized. Live database connections must be transient, and passwords must not be stored as plain text. A declared `serialVersionUID` helps manage compatible changes to serialized classes.
