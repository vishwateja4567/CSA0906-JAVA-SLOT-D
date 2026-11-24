# B3 – JDBC Comparison
Run `database/create_database.sql`, edit `DBConfig.java`, add Connector/J to classpath, then:
```bash
javac -cp ".;mysql-connector-j-<version>.jar" DBConfig.java PassLoaderBenchmark.java
java -cp ".;mysql-connector-j-<version>.jar" PassLoaderBenchmark
```
Tests 10, 1,000, 10,000 and 100,000 rows; prints ResultSetMetaData and compares auto-commit with rollback after a forced duplicate-key failure.
