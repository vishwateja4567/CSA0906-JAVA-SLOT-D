# B1 Results – Fill after execution

## Validation
| Test | Expected | Actual |
|---|---|---|
| Find P1003 | Matching record | |
| Find P9999 | null | |
| Damaged P1002 line | Skipped; search continues | |
| Route 7 | Matching records listed | |

## Buffering
| File size | Unbuffered ms | Buffered ms |
|---|---:|---:|
| 1 MB | | |
| 10 MB | | |
| 50 MB | | |

## Search growth
| Records | Search time ms |
|---:|---:|
| 1,000 | |
| 10,000 | |
| 50,000 | |
| 100,000 | |

Interpret the trend using O(n) for a sequential search. Paste actual console output/screenshots.
