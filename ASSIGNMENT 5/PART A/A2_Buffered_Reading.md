# TASK A2 – DON'T READ WHAT YOU DON'T NEED
**Topic:** Counting lines in a 50 MB attendance log

## A2.1 – Predict the Cost

If the program reads the file one byte at a time using `FileInputStream.read()`, it makes approximately one read call per byte.

For a 50 MB file (using 1 MB = 1,000,000 bytes):

- Approximate number of read calls: **50,000,000**
- Each call has method-call and I/O overhead.
- Repeated small reads are inefficient compared with reading a block of bytes at once.

The exact number of operating-system reads may differ because the JVM and operating system can use internal buffering, but the Java program still performs approximately one `read()` call per byte.

## A2.2 – Redesign Using Buffering

Use `BufferedInputStream` or a byte array buffer to read blocks of data.

### Pseudocode

```text
ALGORITHM CountLinesBuffered
INPUT: fileName
BEGIN
    SET lineCount = 0
    OPEN file using FileInputStream
    WRAP it with BufferedInputStream
    SET buffer to a byte array of size 8192

    WHILE read bytes into buffer
        FOR each byte that was read
            IF byte is newline character THEN
                INCREMENT lineCount
            END IF
        END FOR
    END WHILE

    CLOSE the stream
    DISPLAY lineCount
END
```

An 8 KB buffer reads many bytes per operation. For a 50 MB file, the number of buffer-fill operations is approximately:

`50,000,000 / 8,192 ≈ 6,104`

This is far fewer than 50 million single-byte Java read calls. The actual number of operating-system calls is implementation-dependent.

## A2.3 – When Buffering Does Not Help

Buffering provides little benefit when the data is already available in memory, or when the program reads only a very small amount of data once. The extra buffer layer may not noticeably improve performance in these cases.

## Alternative Concept – Files.readAllLines()

The program could use `Files.readAllLines(path)` to load all lines into a list and then count them.

```text
ALGORITHM CountLinesAllAtOnce
INPUT: filePath
BEGIN
    READ all lines using Files.readAllLines
    SET lineCount = number of elements in the returned list
    DISPLAY lineCount
END
```

| Approach | Time complexity | Additional memory |
|---|---|---|
| Buffered stream | O(n) | O(b), where b is buffer size |
| `Files.readAllLines()` | O(n) | O(n), proportional to file contents and line objects |

Both approaches must process the file, so both take O(n) time. A buffered stream is preferable for a large 50 MB file when memory should remain low. `Files.readAllLines()` is convenient for small files that comfortably fit in memory.

## Result

Buffered reading reduces the number of small read operations and uses memory proportional to the buffer size. Reading all lines at once is simpler but requires memory proportional to the complete file.
