
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

class InsufficientBalanceException extends Exception {
    InsufficientBalanceException(String msg) {
        super(msg);
    }
}

public class ATMWithdrawal {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            System.out.print("Enter account number: ");
            String account = sc.nextLine();

            System.out.print("Enter withdrawal amount: ");
            int amount = Integer.parseInt(sc.nextLine());

            if (amount <= 0) {
                throw new IllegalArgumentException(
                    "Amount must be positive");
            }

            if (amount > 5000) {
                throw new InsufficientBalanceException(
                    "Insufficient balance");
            }

            System.out.println("Account: " + account);
            System.out.println("Withdrawal successful");
            System.out.println("Amount: Rs." + amount);

        } catch (NumberFormatException e) {
          System.out.println("Invalid number: " + e.getMessage());

        } catch (IllegalArgumentException e) {
          System.out.println("Invalid input: " + e.getMessage());

        } catch (InsufficientBalanceException e) {
            System.out.println("Error: " + e.getMessage());

        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());

        } finally {
            System.out.println("ATM transaction completed.");
            sc.close();
        }
    }
}
