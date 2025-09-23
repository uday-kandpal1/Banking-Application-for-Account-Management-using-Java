import java.util.Scanner;
class Account {
    // Attributes
    private int accountNumber;
    private String accountHolderName;
    private double balance;
    private String email;
    private String phoneNumber;   
    // Static variable to generate unique account numbers
    private static int nextAccountNumber = 0001;    
    // Constructor
    public Account(String accountHolderName, double initialDeposit, String email, String phoneNumber) {
        this.accountNumber = nextAccountNumber++;
        this.accountHolderName = accountHolderName;
        this.balance = initialDeposit;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }   
    // Deposit method with validation
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposit successful. New balance: " + balance);
        } else {
            System.out.println("Invalid amount. Deposit amount must be positive.");
        }
    }    
    // Withdraw method with validation
    public void withdraw(double amount) {
        if (amount > 0) {
            if (balance >= amount) {
                balance -= amount;
                System.out.println("Withdrawal successful. New balance: " + balance);
            } else {
                System.out.println("Insufficient balance. Current balance: " + balance);
            }
        } else {
            System.out.println("Invalid amount. Withdrawal amount must be positive.");
        }}    
    public void displayAccountDetails() {
        System.out.println("\n--- Account Details ---");
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Account Holder: " + accountHolderName);
        System.out.println("Balance: " + balance);
        System.out.println("Email: " + email);
        System.out.println("Phone Number: " + phoneNumber);
        System.out.println("-----------------------\n");
    }   
    public void updateContactDetails(String email, String phoneNumber) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        System.out.println("Contact details updated successfully.");
    }    
    public int getAccountNumber() {
        return accountNumber;
    }
    public String getAccountHolderName() {
        return accountHolderName;
    }   
    public double getBalance() {
        return balance;
    }   
    public String getEmail() {
        return email;
    }   
    public String getPhoneNumber() {
        return phoneNumber;
    }}
public class BankingInterface {
    // Attributes
    private Account[] accounts;
    private int accountCount;
    private Scanner scanner;   
    // Constructor
    public BankingInterface() {
        accounts = new Account[10]; // Initial capacity of 10 accounts
        accountCount = 0;
        scanner = new Scanner(System.in);
    }   
    public void createAccount() {
        if (accountCount >= accounts.length) {
            // The array can be resized if needed
            Account[] newAccounts = new Account[accounts.length * 2];
            System.arraycopy(accounts, 0, newAccounts, 0, accounts.length);
            accounts = newAccounts;
        }        
        System.out.println("\n--- Create New Account ---");       
        System.out.print("Enter account holder name: ");
        String name = scanner.nextLine();        
        // Get initial deposit with validation
        double initialDeposit = 0;
        boolean validDeposit = false;
        while (!validDeposit) {
            System.out.print("Enter initial deposit amount: ");
            if (scanner.hasNextDouble()) {
                initialDeposit = scanner.nextDouble();
                scanner.nextLine(); // Consume newline
                if (initialDeposit >= 0) {
                    validDeposit = true;
                } else {
                    System.out.println("Invalid amount. Initial deposit must be non-negative.");
                }}
                 else {
                System.out.println("Invalid input. Please enter a numeric value.");
                scanner.nextLine(); // Clears invalid input
            }}       
        System.out.print("Enter email address: ");
        String email = scanner.nextLine();        
        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine();        
        Account newAccount = new Account(name, initialDeposit, email, phone);
        accounts[accountCount++] = newAccount;        
        System.out.println("Account created successfully with Account Number: " + newAccount.getAccountNumber());
    }    
    public void performDeposit() {
        if (accountCount == 0) {
            System.out.println("No accounts exist. Please create an account first.");
            return;
        }       
        System.out.println("\n--- Deposit Money ---");       
        int accountNumber = getAccountNumberInput();       
        Account account = findAccount(accountNumber);
        if (account == null) {
            System.out.println("Account not found with account number: " + accountNumber);
            return;
        }       
        // Gets deposit amount with validation
        double amount = 0;
        boolean validAmount = false;
        while (!validAmount) {
            System.out.print("Enter amount to deposit: ");
            if (scanner.hasNextDouble()) {
                amount = scanner.nextDouble();
                scanner.nextLine(); // Consumes newline
                if (amount > 0) {
                    validAmount = true;
                } else {
                    System.out.println("Invalid amount. Deposit amount must be positive.");
                }}
                 else {
                System.out.println("Invalid input. Please enter a numeric value.");
                scanner.nextLine(); // Clear invalid input
            }
        }account.deposit(amount);
    }   
    public void performWithdrawal() {
        if (accountCount == 0) {
            System.out.println("No accounts exist. Please create an account first.");
            return;
        }     
        System.out.println("\n--- Withdraw Money ---");       
        int accountNumber = getAccountNumberInput();       
        Account account = findAccount(accountNumber);
        if (account == null) {
            System.out.println("Account not found with account number: " + accountNumber);
            return;
        }       
        double amount = 0;
        boolean validAmount = false;
        while (!validAmount) {
            System.out.print("Enter amount to withdraw: ");
            if (scanner.hasNextDouble()) {
                amount = scanner.nextDouble();
                scanner.nextLine(); // Consumes newline
                if (amount > 0) {
                    validAmount = true;
                } else {
                    System.out.println("Invalid amount. Withdrawal amount must be positive.");
                }
            } else {
                System.out.println("Invalid input. Please enter a numeric value.");
                scanner.nextLine(); // Clears invalid input
            }
        }account.withdraw(amount);
    }    
    public void showAccountDetails() {
        if (accountCount == 0) {
            System.out.println("No accounts exist. Please create an account first.");
            return;
        }     
        System.out.println("\n--- View Account Details ---");       
        int accountNumber = getAccountNumberInput();
        Account account = findAccount(accountNumber);
        if (account == null) {
            System.out.println("Account not found with account number: " + accountNumber);
            return;
        }
        account.displayAccountDetails();
    }   
    public void updateContact() {
        if (accountCount == 0) {
            System.out.println("No accounts exist. Please create an account first.");
            return;
        }       
        System.out.println("\n--- Update Contact Details ---");
        int accountNumber = getAccountNumberInput();      
        Account account = findAccount(accountNumber);
        if (account == null) {
            System.out.println("Account not found with account number: " + accountNumber);
            return;
        }      
        System.out.print("Enter new email address: ");
        String email = scanner.nextLine();        
        System.out.print("Enter new phone number: ");
        String phone = scanner.nextLine();       
        account.updateContactDetails(email, phone);
    }   
    private int getAccountNumberInput() {
        int accountNumber = 0;
        boolean validInput = false;
        while (!validInput) {
            System.out.print("Enter account number: ");
            if (scanner.hasNextInt()) {
                accountNumber = scanner.nextInt();
                scanner.nextLine(); // Consumes newline
                validInput = true;
            } else {
                System.out.println("Invalid input. Please enter a numeric account number.");
                scanner.nextLine(); // Clears invalid input
            }}        
        return accountNumber;
    }    
    // Helper method is used to find account by account number
    private Account findAccount(int accountNumber) {
        for (int i = 0; i < accountCount; i++) {
            if (accounts[i].getAccountNumber() == accountNumber) {
                return accounts[i];
            }
        }return null;
    }    
    public void mainMenu() {
        int choice = 0;        
        do {
            System.out.println("\nWelcome to the Banking Application!");
            System.out.println("1. Create a new account");
            System.out.println("2. Deposit money");
            System.out.println("3. Withdraw money");
            System.out.println("4. View account details");
            System.out.println("5. Update contact details");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");           
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consumes newline              
                switch (choice) {
                    case 1:
                        createAccount();
                        break;
                    case 2:
                        performDeposit();
                        break;
                    case 3:
                        performWithdrawal();
                        break;
                    case 4:
                        showAccountDetails();
                        break;
                    case 5:
                        updateContact();
                        break;
                    case 6:
                        System.out.println("Thank you for using the Banking Application. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 6.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number between 1 and 6.");
                scanner.nextLine(); // Clears invalid input
            }
        } while (choice != 6);
    }   
    // Main method
    public static void main(String[] args) {
        BankingInterface obj = new BankingInterface();
        obj.mainMenu();
    }
}