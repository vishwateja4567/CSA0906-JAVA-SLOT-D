# TASK A1 – DESIGN A FILE FORMAT
**Course:** Programming in Java – Assignment 5 (CO5)  
**Topic:** Transport Office Bus-Pass Records

## A1.1 – Represent the Record

Each bus-pass record contains: pass ID, student register number, name, route number, boarding stop, validity date and amount paid.

### (a) Delimited text-file layout

**Field order:**

`passId,registerNo,name,routeNo,boardingStop,validityDate,amountPaid`

**Example record:**

```text
P1001,192425326,Rohith,12,Central Stop,2026-12-31,3800.00
```

- Delimiter: comma (`,`)
- Date format: `YYYY-MM-DD`
- Amount: decimal number with two digits after the decimal point.
- If a field contains a comma, quote the field and escape any quotation mark inside it by doubling it. Example: `"Anna Nagar, East"`.
- A CSV-aware parser should be used; simple `split(",")` is not sufficient when quoted fields can contain commas.

### (b) Binary-file layout using `DataOutputStream`

Write fields in this exact order:

| Order | Field | Data type / representation |
|---|---|---|
| 1 | passId | `writeUTF()` |
| 2 | registerNo | `writeUTF()` |
| 3 | name | `writeUTF()` |
| 4 | routeNo | `writeInt()` |
| 5 | boardingStop | `writeUTF()` |
| 6 | validityDate | `writeUTF()` using `YYYY-MM-DD` |
| 7 | amountPaid | `writeDouble()` |

`writeUTF()` stores a length-prefixed modified UTF-8 string, so the string length is stored with the value. Numeric fields use their corresponding binary representation.

## A1.2 – Design the Operations

### 1. Add a new pass

```text
ALGORITHM AddPass
INPUT: pass details, fileName
BEGIN
    OPEN fileName using FileWriter in append mode
    WRAP FileWriter with BufferedWriter
    VALIDATE all required fields
    CONVERT each field to the defined text representation
    WRITE the delimited record
    WRITE a new line
    FLUSH the BufferedWriter
    CLOSE the writer
    OUTPUT "Pass added successfully"
END
```

**Stream choice:** `FileWriter` writes characters to a text file. `BufferedWriter` reduces frequent write operations and provides `newLine()`.

### 2. Find a pass by pass ID

```text
ALGORITHM FindPass
INPUT: searchPassId, fileName
BEGIN
    SET found = FALSE
    OPEN fileName using FileReader
    WRAP FileReader with BufferedReader

    WHILE a line can be read
        PARSE the line using the defined delimited format

        IF the line is valid THEN
            IF record.passId = searchPassId THEN
                DISPLAY the record
                SET found = TRUE
                EXIT LOOP
            END IF
        ELSE
            SKIP the damaged line and record its line number
        END IF
    END WHILE

    CLOSE the reader
    IF found = FALSE THEN
        DISPLAY "Pass not found"
    END IF
END
```

**Stream choice:** `FileReader` reads characters and `BufferedReader` reads complete lines efficiently.

### 3. List passes on a route

```text
ALGORITHM ListPassesByRoute
INPUT: requiredRoute, fileName
BEGIN
    SET found = FALSE
    OPEN fileName using FileReader
    WRAP FileReader with BufferedReader

    WHILE a line can be read
        PARSE the line

        IF the record is valid AND record.routeNo = requiredRoute THEN
            DISPLAY the record
            SET found = TRUE
        ELSE IF the line is damaged THEN
            RECORD the damaged line and continue
        END IF
    END WHILE

    CLOSE the reader
    IF found = FALSE THEN
        DISPLAY "No passes found for this route"
    END IF
END
```

### Stream summary

| Operation | Classes | Reason |
|---|---|---|
| Add | `FileWriter`, `BufferedWriter` | Character-based append and efficient buffered writing |
| Find | `FileReader`, `BufferedReader` | Read and inspect one line at a time |
| List by route | `FileReader`, `BufferedReader` | Scan records sequentially without loading the whole file |

`FileInputStream` and `FileOutputStream` are byte streams. They are suitable for binary data, but the selected text-file design uses character streams.

## A1.3 – Design for Damaged Data

1. **Damaged line:** Check the number of fields, required fields, date format, route number and amount. Skip invalid lines, record the line number and continue processing.
2. **Missing file:** Check whether the file exists. For a search or list operation, report that the file is missing. For a first add operation, create it.
3. **Incomplete write / power failure:** Write new data to a temporary file, flush and close it, then replace the original file using an atomic move where supported. Keep a backup until replacement succeeds. This reduces the risk of leaving a partially updated file.

## Alternative Concept – RandomAccessFile

With fixed-length records, record `i` can be accessed at:

`offset = i × recordSize`

The program can seek directly to that offset using `RandomAccessFile.seek()`.

- A direct seek to a known record position takes **O(1)** time.
- Finding a pass by ID still takes **O(n)** if the file has no separate index, because records must be scanned.
- Fixed-length fields are padded to their maximum sizes. This makes access predictable but wastes space when names or boarding stops are short.
- A separate index from pass ID to record number would be needed for efficient ID lookup.

## Result

The text format is simple and readable, while the binary format is more compact and structured. Buffered character streams are suitable for the required text-file operations. Damaged records should be isolated so that one invalid line does not stop the complete operation.
