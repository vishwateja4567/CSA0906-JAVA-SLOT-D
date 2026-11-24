# B4 – Migration and Metadata
Run `database/create_database.sql`, edit `DBConfig.java`, put `migration_input.csv` in this folder and add Connector/J.
```bash
javac -cp ".;mysql-connector-j-<version>.jar" DBConfig.java PassMigration.java
java -cp ".;mysql-connector-j-<version>.jar" PassMigration migration_input.csv
```
The program inserts valid records in batches within a transaction, writes rejected rows to `migration_failed.csv`, and prints database and driver metadata.
