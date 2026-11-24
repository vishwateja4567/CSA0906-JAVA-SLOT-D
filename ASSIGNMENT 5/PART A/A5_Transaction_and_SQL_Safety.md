# TASK A5 – DESIGN FOR CORRECTNESS AND SAFETY
**Topic:** JDBC transaction and SQL injection prevention

## A5.1 – Transaction Design

A student transfers ₹500 from a hostel wallet to a canteen wallet. Both updates must succeed together. If either update fails, the transaction must be rolled back.

### Pseudocode

```text
ALGORITHM TransferMoney
INPUT: hostelWalletId, canteenWalletId, amount = 500
BEGIN
    OBTAIN JDBC connection
    SET autoCommit = FALSE

    TRY
        UPDATE hostel wallet
            SET balance = balance - amount
            WHERE wallet_id = hostelWalletId
              AND balance >= amount

        IF number of updated rows is not 1 THEN
            RAISE transfer failure
        END IF

        UPDATE canteen wallet
            SET balance = balance + amount
            WHERE wallet_id = canteenWalletId

        IF number of updated rows is not 1 THEN
            RAISE transfer failure
        END IF

        COMMIT transaction
        DISPLAY "Transfer successful"

    CATCH database or validation error
        ROLLBACK transaction
        DISPLAY "Transfer failed; changes rolled back"

    FINALLY
        CLOSE database resources
END
```

**Explanation:** `setAutoCommit(false)` groups the two updates into one transaction. `commit()` makes both changes permanent. `rollback()` cancels the transaction if an error occurs. The source wallet balance is checked to prevent transferring more money than available.

## A5.2 – Injection Analysis

### Unsafe query

```java
String sql = "SELECT * FROM users WHERE id='" + id
           + "' AND pwd='" + pwd + "'";
```

If the input is concatenated directly into SQL, a crafted value can change the meaning of the query.

Example test input:

```text
ID: ' OR '1'='1' --
Password: anything
```

Depending on the SQL dialect and query formatting, the injected condition may make the WHERE clause true and bypass the intended password check.

### Safe redesign using PreparedStatement

```java
String sql = "SELECT * FROM users WHERE id = ? AND pwd = ?";

PreparedStatement ps = con.prepareStatement(sql);
ps.setString(1, id);
ps.setString(2, pwd);

ResultSet rs = ps.executeQuery();

if (rs.next()) {
    System.out.println("Login successful");
} else {
    System.out.println("Invalid login");
}
```

The placeholders keep user input as parameter values rather than executable SQL syntax. In a real application, passwords should be stored as salted password hashes and verified using a password-hashing library, not compared as plain text.

## Alternative Concept – Two Independent Auto-Commit Updates

If each wallet update is executed as a separate auto-commit statement:

1. The hostel wallet may be debited successfully.
2. The program may fail before crediting the canteen wallet.
3. The money is then lost from the application’s records because only the first update was committed.

With a transaction, both updates commit together or are rolled back together.

**Time complexity:** Both designs perform two database updates, so the basic operation count is O(1) with respect to the number of wallets. A transaction has additional coordination and commit/rollback overhead, but it protects correctness. Two independent auto-commit updates may have less transaction-management overhead but are unsafe for this transfer.

## Result

JDBC transactions protect related updates from partial completion. `PreparedStatement` prevents user input from being interpreted as SQL code and is safer than string concatenation.
