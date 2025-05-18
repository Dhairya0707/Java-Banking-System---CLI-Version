import java.io.*;
import java.util.*;

class Main {

    static Scanner sc = new Scanner(System.in);
    static ArrayList<Account> accounts;
    static int id = 1001;
    static final int ADMIN_PIN = 1234;

    public static void main(String[] args) {

        loaddata();

        System.out.println("Loaded ID: " + id);
        System.out.println("Loaded accounts: " + accounts.size());

        System.out.println("=================================================");
        System.out.println("      Welcome to Banking System by Dhairya      ");
        System.out.println("=================================================");

        while (true) {

            System.out.println("\n===========================================");
            System.out.println("|           BANKING MANAGEMENT SYSTEM      |");
            System.out.println("===========================================\n");
            System.out.println("  [1]  Create New Account");
            System.out.println("  [2]  Login as User");
            System.out.println("  [3]  Login as Admin");
            System.out.println("  [4]  Exit\n");
            System.out.print(" Enter your choice: ");

            int op = sc.nextInt();

            switch (op) {
                case 1:
                    createaccount();
                    break;
                case 2:

                    login();
                    break;
                case 3:
                    admin();
                    break;
                case 4:
                    savedata();
                    return;

                default:
                    System.out.println("please enter valid option");
                    break;
            }

        }

    }

    public static void loaddata() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("id.txt"));

            String line = br.readLine();
            if (line != null) {
                id = Integer.parseInt(line);
            }

            ObjectInputStream op = new ObjectInputStream(new FileInputStream("accounts.dat"));
            accounts = (ArrayList<Account>) op.readObject();

            br.close();
            op.close();

        } catch (Exception e) {
            id = 1001;
            accounts = new ArrayList<>();
        }
    }

    public static void savedata() {
        try {
            BufferedWriter br = new BufferedWriter(new FileWriter("id.txt"));
            br.write(Integer.toString(id));

            ObjectOutputStream op = new ObjectOutputStream(new FileOutputStream("accounts.dat"));
            op.writeObject(accounts);

            op.close();
            br.close();

        } catch (Exception e) {
            System.out.println("\nError saving data: " + e.getMessage());
        }

    }

    public static void createaccount() {
        System.out.println("\n\nCreate your account, fill the deatils :");

        System.out.print("enter your name :");
        String name = sc.next();

        sc.nextLine();

        System.out.print("enter your age :");
        int age = sc.nextInt();

        System.out.print("enter your pin :");
        int pin = sc.nextInt();
        int accountNumber = id++;

        Account acc = new Account(name, accountNumber, age, 0, pin);
        accounts.add(acc);

        System.out.println("\nYour account is created succesfull with id : " + accountNumber);

    }

    public static void login() {
        System.out.println("enter your account number :");
        int accno = sc.nextInt();
        sc.nextLine();

        System.out.println("enter your pin :");
        int pin = sc.nextInt();

        Account loggedAccount = null;
        int found = 0;

        for (Account account : accounts) {
            if (account.accountNumber == accno) {
                if (account.pin == pin) {
                    loggedAccount = account;
                    found = 1;
                } else {
                    System.out.println("\nIncorrect PIN. Please try again.");;
                }
                break;
            }
        }

        if (found == 1) {
            System.out.println("\nLogin successful! Welcome, " + loggedAccount.name + "!");
            accountmenu(loggedAccount);

        } else {
            System.out.println("\nAccount not found. Please check your account number.");
        }
    }

    public static void accountmenu(Account acc) {
        while (true) {
            System.out.println("\nWhat would you like to do?");
            System.out.println("1. Deposit Money");
            System.out.println("2. Withdraw Money");
            System.out.println("3. Check Balance");
            System.out.println("4. Account Information");
            System.out.println("5. Transfer Money");
            System.out.println("6. Chnage pin");
            System.out.println("7. Transaction History");
            System.out.println("8. Logout");
            System.out.println("9. Delete Account");

            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    deposit(acc);
                    break;
                case 2:
                    withdraw(acc);
                    break;
                case 3:
                    checkBalance(acc);
                    break;
                case 4:
                    accountinfo(acc);
                    break;
                case 5:
                    transfer(acc);
                    break;
                case 6:
                    changepin(acc);
                    break;
                case 7:
                    printTransactionHistory(acc);
                    break;
                case 8:
                    System.out.println("Logging out...");
                    return;
                case 9:
                    accounts.remove(acc);
                    System.out.println("Account deleted successfully.");

                    break;
                default:
                    System.out.println("Invalid choice! Try again.");
            }

        }

    }

    public static void deposit(Account acc) {
        System.out.println("enter the amount to deposit :");
        double amount = sc.nextDouble();
        if (amount > 0) {
            acc.balance += amount;
            acc.transactions.add("Deposited Rs." + amount + " on " + new Date());
            System.out.println(amount + " deposited successfully.");
        } else {
            System.out.println("Invalid amount.");
        }
    }

    public static void withdraw(Account acc) {
        System.out.println("enter the amount to withdraw :");
        double amount = sc.nextInt();
        if (amount > 0 && acc.balance >= amount) {
            acc.balance -= amount;
            acc.transactions.add("Withdrew Rs." + amount + " on " + new Date());
            System.out.println(amount + " withdrawn successfully.");
        } else {
            System.out.println(" Insufficient balance or invalid amount.");
        }
    }

    public static void checkBalance(Account acc) {
        System.out.println("Current Balance: " + acc.balance);
    }

    public static void accountinfo(Account acc) {
        System.out.println("\n===========================================");
        System.out.println("|           USER ACCOUNT DETAILS           |");
        System.out.println("===========================================");
        System.out.printf("| %-20s : %-15s |\n", "Name", acc.name);
        System.out.printf("| %-20s : %-15d |\n", "Account Number", acc.accountNumber);
        System.out.printf("| %-20s : %-15d |\n", "Age", acc.age);
        System.out.printf("| %-20s : ₹%-14.2f |\n", "Current Balance", acc.balance);
        System.out.println("===========================================\n");
    }

    public static void transfer(Account acc) {
        System.out.println("\n======== MONEY TRANSFER ========");
        System.out.print("Enter recipient account number: ");
        int otherid = sc.nextInt();
        int found = 0;

        Account other = null;

        for (Account elem : accounts) {
            if (elem.accountNumber == otherid) {
                found = 1;
                other = elem;
                System.out.println("account founded id: " + elem.accountNumber + " & name: " + elem.name);
                break;
            }

        }

        if (found == 0) {
            System.out.println("account not found !!");
            return;
        }

        System.out.print("\nenter the amount for transfer :");
        double amount = sc.nextDouble();

        if (amount <= acc.balance && amount > 0) {
            acc.balance -= amount;
            other.balance += amount;
            other.transactions.add("Received Rs." + amount + " from " + acc.name + " on " + new Date());
            acc.transactions.add("Transferred Rs." + amount + " to " + other.name + " on " + new Date());
            System.out.println("Rs." + amount + " transferred successfully to " + other.name + " !");
        } else {
            System.out.println("Transfer failed due to insufficient balance or invalid amount.");
        }
    }

    public static void changepin(Account acc) {
        System.out.print("Enter current PIN: ");
        int current = sc.nextInt();

        if (current != acc.pin) {
            System.out.println("Wrong PIN.");
            return;
        }

        System.out.print("Enter new PIN: ");
        int newPin = sc.nextInt();
        acc.pin = newPin;
        System.out.println("PIN changed successfully!");
    }

    public static void admin() {
        System.out.println("enter the pin for admin login :");
        int pin = sc.nextInt();

        if (pin == ADMIN_PIN) {
            System.out.println("Admin login successful!\n");
            adminmenu();
        } else {
            System.out.println("Invalid admin pin.");
        }

    }

    public static void adminmenu() {
        while (true) {
            System.out.println("\n===========================================");
            System.out.println("|              ADMIN DASHBOARD             |");
            System.out.println("===========================================\n");
            System.out.println("1. Show All Accounts");
            System.out.println("2. Show Total Bank Balance");
            System.out.println("3. Show Total Users");
            System.out.println("4. Search User by Name");
            System.out.println("5. Logout");

            System.out.print("Enter your choice: ");
            int ch = sc.nextInt();

            switch (ch) {
                case 1:
                    showAllAccounts();
                    break;
                case 2:
                    showTotalBalance();
                    break;
                case 3:
                    System.out.println("Total Users: " + accounts.size());
                    break;
                case 4:
                    searchUserByName();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    public static void showAllAccounts() {
        System.out.println("\nList of Accounts:");
        System.out.println("------------------------------------------------------------");
        System.out.println("Name\t\tAccount No\tAge\tBalance");
        System.out.println("------------------------------------------------------------");

        for (Account acc : accounts) {
            System.out.printf("%-15s%-15d%-8d%.2f\n", acc.name, acc.accountNumber, acc.age, acc.balance);
        }
    }

    public static void showTotalBalance() {
        double total = 0;
        for (Account acc : accounts) {
            total += acc.balance;
        }
        System.out.println("Total Bank Balance: " + total);
    }

    public static void printTransactionHistory(Account acc) {
        if (acc.transactions.isEmpty()) {
            System.out.println("\n  Transaction History: No transactions yet.");
            return;
        }

        System.out.println("\n  Transaction History for: " + acc.name);
        System.out.println("--------------------------------------------------");
        System.out.println("No.\tDate & Time\t\t\tDetails");
        System.out.println("--------------------------------------------------");

        int count = 1;
        for (String t : acc.transactions) {
            System.out.println(count + "\t" + t);
            count++;
        }

        System.out.println("--------------------------------------------------\n");
    }

    public static void searchUserByName() {
        System.out.print("Enter the name or part of the name to search: ");
        sc.nextLine();
        String searchName = sc.nextLine().toLowerCase();

        ArrayList<Account> foundAccounts = new ArrayList<>();

        for (Account acc : accounts) {
            if (acc.name.toLowerCase().contains(searchName)) {
                foundAccounts.add(acc);
            }
        }

        if (foundAccounts.isEmpty()) {
            System.out.println("No users found with name containing: " + searchName);
        } else {
            System.out.println("\nFound " + foundAccounts.size() + " user(s):");
            System.out.println("------------------------------------------------------------");
            System.out.println("Name\t\tAccount No\tAge\tBalance");
            System.out.println("------------------------------------------------------------");

            for (Account acc : foundAccounts) {
                System.out.printf("%-15s%-15d%-8d%.2f\n", acc.name, acc.accountNumber, acc.age, acc.balance);
            }
        }
    }

}

class Account implements Serializable {

    public String name;
    public int accountNumber;
    public int age;
    public double balance;
    public int pin;
    ArrayList<String> transactions = new ArrayList<>();

    Account(String name, int accountNumber, int age, double balance, int pin) {
        this.name = name;
        this.accountNumber = accountNumber;
        this.age = age;
        this.balance = balance;
        this.pin = pin;
    }

}
