/**
 * Vending Machine Simulation
 * 
 * Description:
 * This Java program simulates a vending machine where users can insert money, 
 * select products from different categories (Candies, Sodas, and Snacks), 
 * make purchases, and receive refunds. It includes basic error handling, 
 * payment verification, and product stock management.
 * 
 * Features:
 * - Product Categories: Candies, Sodas, Snacks
 * - Payment System: Insert money, check balance
 * - Purchase Logic: Select product, specify quantity, check stock, verify balance
 * - Refund Mechanism: Refund remaining balance if the user chooses not to purchase more
 * 
 * Technologies:
 * - Java 8+
 * - Object-Oriented Programming (OOP) Principles
 * - Collections Framework (HashMap)
 * 
 * Author: [Your Name]
 * Modified on: January 2025
 * Version: 1.0
 */

import java.util.*;

// Represents a Product in the vending machine
class Product {
    private String name;
    private double price;
    private int quantity;

    public Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void reduceQuantity(int amount) {
        if (quantity >= amount) {
            quantity -= amount;
        }
    }
}

// Handles the payment process
class Payment {
    private double balance;

    public Payment() {
        balance = 0.0;
    }

    public void addCoins(double amount) {
        balance += amount;
    }

    public boolean hasSufficientBalance(double amount) {
        return balance >= amount;
    }

    public void deduct(double amount) {
        balance -= amount;
    }

    public double getBalance() {
        return balance;
    }

    public void refund() {
        if (balance > 0) {
            System.out.printf("Automatically refunding: $%.2f\n", balance);  // Properly formatted to two decimal places
            balance = 0.0;

        }
    }
}

// Represents the Vending Machine
class VendingMachine {
    private Map<Integer, Product> products;
    private double totalMoney;

    public VendingMachine() {
        products = new HashMap<>();
        totalMoney = 0.0;
        loadProducts();
    }

    private void loadProducts() {
        products.put(101, new Product("Chocolate Bar", 1.50, 5));
        products.put(102, new Product("Gummy Bears", 1.00, 10));
        products.put(103, new Product("Lollipop", 0.75, 15));
        products.put(201, new Product("Coca-Cola", 2.00, 5));
        products.put(202, new Product("Pepsi", 1.75, 5));
        products.put(203, new Product("Sprite", 1.50, 5));
        products.put(301, new Product("Chips", 2.50, 5));
        products.put(302, new Product("Pretzels", 2.00, 5));
        products.put(303, new Product("Cookies", 1.50, 5));
    }

    public void displayProducts() {
        System.out.println("\nAvailable Products:");
        System.out.println("--- Candies ---");
        displayCategory(100, 199);
        System.out.println("--- Sodas ---");
        displayCategory(200, 299);
        System.out.println("--- Snacks ---");
        displayCategory(300, 399);
    }

    private void displayCategory(int min, int max) {
        for (Map.Entry<Integer, Product> entry : products.entrySet()) {
            if (entry.getKey() >= min && entry.getKey() <= max) {
                Product p = entry.getValue();
                System.out.printf("%d - %s: $%.2f (Stock: %d)\n", entry.getKey(), p.getName(), p.getPrice(), p.getQuantity());
            }
        }
    }

    public boolean purchaseProduct(int productCode, int quantity, Payment payment) {
        if (!products.containsKey(productCode)) {
            System.out.println("Invalid selection.");
            return false;
        }

        Product product = products.get(productCode);
        double totalCost = product.getPrice() * quantity;

        if (product.getQuantity() < quantity) {
            System.out.println("Not enough stock available.");
            return false;
        }

        if (payment.hasSufficientBalance(totalCost)) {
            product.reduceQuantity(quantity);
            payment.deduct(totalCost);
            totalMoney += totalCost;
            System.out.printf("Dispensing %d x %s\n", quantity, product.getName());
            return true;
        } else {
            System.out.println("Insufficient funds.");
            return false;
        }
    }
}

// Simulates the vending machine system
public class VendingMachineSimulation {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        VendingMachine vendingMachine = new VendingMachine();
        Payment payment = new Payment();

        System.out.print("Insert money: $");
        double amountInserted = scanner.nextDouble();
        payment.addCoins(amountInserted);
        System.out.printf("Balance: $%.2f\n", payment.getBalance());

        while (true) {
            vendingMachine.displayProducts();
            System.out.print("Enter product code: ");
            int productCode = scanner.nextInt();
            System.out.print("Enter quantity: ");
            int quantity = scanner.nextInt();

            if (vendingMachine.purchaseProduct(productCode, quantity, payment)) {
                System.out.println("Thank you for your purchase!");
            }

            // Ask if user wants to continue purchasing or exit
            System.out.print("Would you like to buy anything else? (yes/no): ");
            String answer = scanner.next().toLowerCase();

            if (answer.equals("no")) {
                // Only refund if the user chooses not to buy more items
                payment.refund();
                System.out.println("Have a great day!");
                break;  // Exits the loop if user chooses 'no'
            } else if (!answer.equals("yes")) {
                System.out.println("Invalid response. Exiting the machine.");
                payment.refund();  // Refund in case of invalid response
                break;  // Exits the loop for invalid input
            }

            // If answer is 'yes', the loop continues for another purchase
            System.out.printf("Remaining balance: $%.2f\n", payment.getBalance());
        }

        scanner.close();
    }
}
