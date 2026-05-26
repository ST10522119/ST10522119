

package com.mycompany.login;

import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.Random;

/**
* Login class handles user registration and authentication for the Chat Application.
* This class implements methods for username validation, password complexity checking,
* cell phone number validation, registration, and login functionality.
*/

public class Login {
    
    private String storedUsername = "";
    private String storedPassword = "";
    private String storedPhoneNumber = "";
    private String firstName = "";
    private String lastName = "";
    
/**
* Validates username format.
* Username must contain an underscore (_) and be no more than 5 characters long.
*/

    
    public boolean checkUserName(String username) {
        
    Pattern pattern = Pattern.compile("^[A-Za-z0-9]{1,4}_$");
    return pattern.matcher(username).matches();
    }
    
/**
* Validates password complexity.
* Password must be at least 8 characters, contain a capital letter,
* a number, and a special character.
*/
    public boolean checkPasswordComplexity(String password) {
    if (password.length() < 8) {return false;}
    
    boolean hasCapital = false;
    boolean hasNumber = false;
    boolean hasSpecial = false;
    for (int i = 0; i < password.length(); i++) {
    char c = password.charAt(i);
    if (Character.isUpperCase(c)) {
    hasCapital = true;}
    else if (Character.isDigit(c)) {
    hasNumber = true;}
    else if (!Character.isLetterOrDigit(c)) {
    hasSpecial = true;}
    }
    return hasCapital && hasNumber && hasSpecial;
    }
   
    /**
    * Validates South African cell phone number format.
    * Must contain international country code (+27) followed by a 9-digit number.
    */
    
    public boolean checkCellPhoneNumber(String phoneNumber) {
     Pattern pattern = Pattern.compile("^\\+27[0-9]{9}$");
    return pattern.matcher(phoneNumber).matches();
    }   
        
    }

    public String registerUser(String username, String password, String phoneNumber,
    String first, String last) {
    boolean validUsername = checkUserName(username);
    boolean validPassword = checkPasswordComplexity(password);
    boolean validPhone = checkCellPhoneNumber(phoneNumber);
    
    if (!validUsername) {
return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
}
if (!validPassword) {
return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
}
if (!validPhone) {
return "Cell phone number incorrectly formatted or does not contain international code.";
}

// Store the user's information
    String storedUsername = username;
    String storedPassword = password;
    String storedPhoneNumber = phoneNumber;
    String firstName = first;
    String lastName = last;

return "Username successfully captured.\nPassword successfully captured.\nCell phone number successfully added.\nRegistration successful!";
}

    public boolean loginUser(String username, String password) {
    return storedUsername.equals(username) && storedPassword.equals(password);
}
 //Returns appropriate login status message.
    
    public String returnLoginStatus(boolean isLoginSuccessful) {
if (isLoginSuccessful) {
return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
} else {
return "Username or password incorrect, please try again.";
}
}

 public static void main(String[] args) {
Scanner scanner = new Scanner(System.in);

Login loginSystem = new Login();
System.out.println("=".repeat(50));
System.out.println(" QUICKCHAT REGISTRATION SYSTEM");
System.out.println("=".repeat(50));

System.out.println("\n--- REGISTRATION ---");
System.out.print("Enter first name: ");
String firstName = scanner.nextLine();
System.out.print("Enter last name: ");
String lastName = scanner.nextLine();
String username;
boolean validUsername = false;
do {
System.out.print("Enter username (max 5 chars, must contain '_'): ");
username = scanner.nextLine();
if (loginSystem.checkUserName(username)) {
System.out.println("Username successfully captured.");
validUsername = true;
} else {
System.out.println("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.");
}
} while (!validUsername);
String password;
boolean validPassword = false;
do {
System.out.print("Enter password (8+ chars, capital, number, special char): ");
password = scanner.nextLine();
if (loginSystem.checkPasswordComplexity(password)) {
System.out.println("Password successfully captured.");
validPassword = true;
} else {
System.out.println("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.");
}
} while (!validPassword);
String phoneNumber;
boolean validPhone = false;
do {
System.out.print("Enter cell phone number (+27XXXXXXXXX): ");
phoneNumber = scanner.nextLine();
if (loginSystem.checkCellPhoneNumber(phoneNumber)) {
System.out.println("Cell phone number successfully added.");
validPhone = true;
} else {
System.out.println("Cell phone number incorrectly formatted or does not contain international code.");
}
} while (!validPhone);
String registrationResult = loginSystem.registerUser(username, password, phoneNumber,
firstName, lastName);
System.out.println("\n" + registrationResult);
if (!registrationResult.contains("successful")) {
System.out.println("Registration failed. Please restart the application.");
scanner.close();
return;
}

System.out.println("\n--- LOGIN ---");
boolean loggedIn = false;
int attempts = 0;
String loginUsername, loginPassword;
do {
if (attempts > 0) {
System.out.println("Please try again.");
}
System.out.print("Enter username: ");
loginUsername = scanner.nextLine();
System.out.print("Enter password: ");
loginPassword = scanner.nextLine();
if (loginSystem.loginUser(loginUsername, loginPassword)) {
loggedIn = true;
} else {
System.out.println("Username or password incorrect, please try again.");
}
attempts++;
} while (!loggedIn && attempts < 3);
String loginStatus = loginSystem.returnLoginStatus(loggedIn);
System.out.println(loginStatus);
if (!loggedIn) {
System.out.println("Too many failed attempts. Please restart the application.");
}
scanner.close();
}

    

