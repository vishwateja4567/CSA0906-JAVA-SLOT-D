# B3 Results – Fill actual measurements
| Records | Statement ms | PreparedStatement ms | Batch ms |
|---:|---:|---:|---:|
| 10 | | | |
| 1,000 | | | |
| 10,000 | | | |
| 100,000 | | | |

Paste column metadata output.

| Failure mode | Expected result | Actual rows remaining |
|---|---|---:|
| Auto-commit | Earlier successful inserts remain | |
| Single transaction | Rollback removes all inserts | |

Choose the suitable approach for a 100,000-row nightly import and a single counter transaction, based on actual timings.
