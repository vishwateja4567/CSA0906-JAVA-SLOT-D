# B2 – Adversarial Inputs
1. Run `database/create_database.sql` in MySQL Workbench.
2. Set the local password in `DBConfig.java`.
3. Add MySQL Connector/J to the VS Code Java classpath.
4. Compile and run:
```bash
javac -cp ".;mysql-connector-j-<version>.jar" DBConfig.java AdversarialInputTest.java
java -cp ".;mysql-connector-j-<version>.jar" AdversarialInputTest
```
On Linux/macOS use `:` instead of `;`. This deliberately vulnerable example is for your local test database only.
