import java.util.*;

class Account {
    private String userId;
    private String pin;
    private double balance;

    public Account(String userId, String pin, double initialBalance) {
        this.userId = userId;
        this.pin = pin;
        this.balance = initialBalance;
    }

    public String getUserId() {
        return userId;
    }

    public String getPin() {
        return pin;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public boolean withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        return false;
    }

    public boolean transfer(Account toAccount, double amount) {
        if (this.withdraw(amount)) {
            toAccount.deposit(amount);
            return true;
        }
        return false;
    }
}

class Transaction {
    private Date date;
    private String type;
    private double amount;

    public Transaction(String type, double amount) {
        this.date = new Date();
        this.type = type;
        this.amount = amount;
    }

    public String getDetails() {
        return date.toString() + " | " + type + " | " + amount;
    }
}

class ATMOperations {
    private Account currentAccount;
    private List<Transaction> transactionHistory;

    public ATMOperations(Account account) {
        this.currentAccount = account;
        this.transactionHistory = new ArrayList<>();
    }

    public void showTransactionHistory() {
        if (transactionHistory.isEmpty()) {
            System.out.println("No transaction history available.");
        } else {
            System.out.println("Transaction History: ");
            for (Transaction transaction : transactionHistory) {
                System.out.println(transaction.getDetails());
            }
        }
    }

    public void withdraw(double amount) {
        if (currentAccount.withdraw(amount)) {
            transactionHistory.add(new Transaction("Withdraw", amount));
            System.out.println("Withdrawal of " + amount + " successful.");
        } else {
            System.out.println("Insufficient funds for withdrawal.");
        }
    }

    public void deposit(double amount) {
        currentAccount.deposit(amount);
        transactionHistory.add(new Transaction("Deposit", amount));
        System.out.println("Deposit of " + amount + " successful.");
    }

    public void transfer(Account toAccount, double amount) {
        if (currentAccount.transfer(toAccount, amount)) {
            transactionHistory.add(new Transaction("Transfer", amount));
            System.out.println("Transfer of " + amount + " successful.");
        } else {
            System.out.println("Insufficient funds for transfer.");
        }
    }

    public void showBalance() {
        System.out.println("Current Balance: " + currentAccount.getBalance());
    }
}

public class ATM {
    private static Scanner scanner = new Scanner(System.in);
    private static Map<String, Account> accounts = new HashMap<>();
    private static ATMOperations atmOperations;

    public static void main(String[] args) {
        // Initialize Accounts
        accounts.put("user1", new Account("user1", "1234", 1000.00));
        accounts.put("user2", new Account("user2", "5678", 5000.00));

        System.out.println("Welcome to the ATM system");

        // Start User Authentication
        while (true) {
            System.out.print("Enter User ID: ");
            String userId = scanner.nextLine();
            System.out.print("Enter PIN: ");
            String pin = scanner.nextLine();

            Account account = accounts.get(userId);
            if (account != null && account.getPin().equals(pin)) {
                atmOperations = new ATMOperations(account);
                System.out.println("Login Successful");
                showMenu();
                break;
            } else {
                System.out.println("Invalid User ID or PIN. Try again.");
            }
        }
    }

    private static void showMenu() {
        while (true) {
            System.out.println("\nATM Menu:");
            System.out.println("1. Transaction History");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Quit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    atmOperations.showTransactionHistory();
                    break;
                case 2:
                    System.out.print("Enter amount to withdraw: ");
                    double withdrawAmount = scanner.nextDouble();
                    atmOperations.withdraw(withdrawAmount);
                    break;
                case 3:
                    System.out.print("Enter amount to deposit: ");
                    double depositAmount = scanner.nextDouble();
                    atmOperations.deposit(depositAmount);
                    break;
                case 4:
                    System.out.print("Enter account ID to transfer to: ");
                    String transferToUser = scanner.nextLine();
                    Account toAccount = accounts.get(transferToUser);
                    if (toAccount != null) {
                        System.out.print("Enter amount to transfer: ");
                        double transferAmount = scanner.nextDouble();
                        atmOperations.transfer(toAccount, transferAmount);
                    } else {
                        System.out.println("Invalid account ID.");
                    }
                    break;
                case 5:
                    System.out.println("Goodbye! Exiting ATM.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
