package com.mycompany.prog5121_poe_st10522119;

import java.util.Scanner;

/**
 * PROG5121w POE Parts 1 and 2.
 * Author: Lebohang Modiko
 * Student number: ST10522119
 *
 * This class runs the program in the console without using a GUI or
 * JOptionPane, as required by the POE. Scanner is used to accept keyboard input
 * (Farrell, 2023; The IIE, 2026).
 */
public class PROG5121_POE_ST10522119
{
    /*
     * The main() method controls the order of the application. It first captures
     * the user's registration details. It then requires a successful login, and
     * only after login does it display the QuickChat menu.
     */
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        Login login;
        String firstName;
        String surname;
        String username;
        String password;
        String cellNumber;
        String enteredUsername;
        String enteredPassword;
        boolean registered = false;
        boolean loggedIn = false;
        int numberOfMessages;
        int menuChoice = 0;
        int messagesEntered = 0;
        Message reportMessage = new Message();

        /*
         * This first section collects the user's first name and surname. These
         * values are used later in the successful login welcome message.
         */
        System.out.println("QuickChat Registration");

        System.out.println("Enter your first name: ");
        firstName = input.nextLine();

        System.out.println("Enter your surname: ");
        surname = input.nextLine();

        login = new Login();
        login.setFirstName(firstName);
        login.setSurname(surname);

        /*
         * This loop repeats registration until the username, password, and
         * cellphone number all pass their validation checks. A while loop is
         * suitable because the number of attempts is unknown (Farrell, 2023).
         */
        while(!registered)
        {
            System.out.println("Enter username: ");
            username = input.nextLine();
            login.setUsername(username);
            System.out.println(login.getUserNameMessage());

            System.out.println("Enter password: ");
            password = input.nextLine();
            login.setPassword(password);
            System.out.println(login.getPasswordMessage());

            System.out.println("Enter your South African cellphone number with the international code, for example +27813249913: ");
            cellNumber = input.nextLine();
            login.setCellphoneNumber(cellNumber);
            System.out.println(login.getCellPhoneMessage());

            if(login.registerUser().equals(Login.REGISTER_SUCCESS))
            {
                registered = true;
                System.out.println(Login.REGISTER_SUCCESS);
            }
            else
            {
                System.out.println("Please re-enter your registration details.\n");
            }
        }

        /*
         * This login loop continues until the user enters the same username and
         * password that were stored during registration.
         */
        while(!loggedIn)
        {
            System.out.println("\nLogin");
            System.out.println("Enter your username: ");
            enteredUsername = input.nextLine();

            System.out.println("Enter your password: ");
            enteredPassword = input.nextLine();

            loggedIn = login.loginUser(enteredUsername, enteredPassword);
            System.out.println(login.returnLoginStatus());
        }

        /*
         * After login, the program asks how many messages the user would like to
         * enter. The program will not allow more than this set number of message
         * entries.
         */
        System.out.println("\nWelcome to QuickChat!");
        System.out.println("How many messages would you like to enter? ");
        numberOfMessages = readNonNegativeInteger(input);

        /*
         * This menu loop continues until the user selects option 3 to quit.
         * Option 1 sends or stores messages. Option 2 displays the required
         * Coming Soon message. Option 3 ends the application.
         */
        while(menuChoice != 3)
        {
            System.out.println("\nPlease choose an option:");
            System.out.println("1.) Send Messages");
            System.out.println("2.) Show recently sent messages");
            System.out.println("3.) Quit");
            menuChoice = readNonNegativeInteger(input);

            if(menuChoice == 1)
            {
                if(messagesEntered >= numberOfMessages)
                {
                    System.out.println("You have already entered the set number of messages.");
                }
                else
                {
                    /*
                     * A for loop is suitable here because the number of message
                     * entries is controlled by the user-selected limit
                     * (Farrell, 2023).
                     */
                    for(; messagesEntered < numberOfMessages; messagesEntered++)
                    {
                        captureMessage(input, messagesEntered + 1);
                    }
                }
            }
            else
            {
                if(menuChoice == 2)
                {
                    System.out.println("Coming Soon.");
                }
                else
                {
                    if(menuChoice == 3)
                    {
                        System.out.println("Total messages sent: " + reportMessage.returnTotalMessages());
                        System.out.println("Goodbye.");
                    }
                    else
                    {
                        System.out.println("Invalid option selected.");
                    }
                }
            }
        }

        input.close();
    }

    /*
     * This helper method reads a whole number from the console. It avoids
     * crashing on non-numeric input by checking each character before converting
     * the String to an int.
     */
    private static int readNonNegativeInteger(Scanner input)
    {
        String value;
        boolean valid = false;
        int number = 0;

        while(!valid)
        {
            value = input.nextLine();

            if(isWholeNumber(value))
            {
                number = Integer.parseInt(value);
                valid = true;
            }
            else
            {
                System.out.println("Please enter a valid whole number: ");
            }
        }

        return number;
    }

    /* This helper checks whether a String contains only digit characters. */
    private static boolean isWholeNumber(String value)
    {
        boolean valid = true;
        int count;

        if(value == null || value.length() == 0)
        {
            valid = false;
        }
        else
        {
            for(count = 0; count < value.length(); count++)
            {
                if(!Character.isDigit(value.charAt(count)))
                {
                    valid = false;
                }
            }
        }

        return valid;
    }

    /*
     * This method captures one message. Separating this work from main() makes
     * the program easier to read and maintain through methods (Farrell, 2023).
     */
    private static void captureMessage(Scanner input, int messageNumber)
    {
        String messageID;
        String recipient;
        String messageText;
        int sendChoice;
        Message message;
        boolean messageReady = false;
        boolean recipientReady = false;

        /*
         * The message ID is generated before the rest of the message details are
         * captured because it is needed for the message hash.
         */
        messageID = Message.generateMessageID(messageNumber);
        System.out.println("Message ID generated: " + messageID);

        message = new Message();
        message.setMessageID(messageID);
        message.setMessageNumber(messageNumber);

        /* This loop repeats until the recipient cellphone number is valid. */
        while(!recipientReady)
        {
            System.out.println("Enter recipient cellphone number with international code: ");
            recipient = input.nextLine();
            message.setRecipient(recipient);
            System.out.println(message.checkRecipientCell());

            if(message.checkRecipientCell().equals(Message.RECIPIENT_SUCCESS))
            {
                recipientReady = true;
            }
        }

        /* This loop repeats until the message is 250 characters or fewer. */
        while(!messageReady)
        {
            System.out.println("Enter message of less than 250 characters: ");
            messageText = input.nextLine();
            message.setMessage(messageText);
            System.out.println(message.validateMessageLength());

            if(message.validateMessageLength().equals(Message.MESSAGE_READY))
            {
                messageReady = true;
            }
        }

        /*
         * This section lets the user send, disregard, or store the message. The
         * selected option is passed to the Message object.
         */
        System.out.println("Choose what to do with this message");
        System.out.println("1.) Send the Message");
        System.out.println("2.) Disregard the Message");
        System.out.println("3.) Store the Message to send later");
        System.out.println("Selection: ");
        sendChoice = readNonNegativeInteger(input);
        message.setSendChoice(sendChoice);

        System.out.println(message.SentMessage());

        /*
         * Sent messages display all details. Stored messages display the
         * JSON-formatted text produced by toJsonText().
         */
        if(sendChoice == 1)
        {
            System.out.println(message.getFullDetails());
        }
        else
        {
            if(sendChoice == 3)
            {
                System.out.println("Stored JSON text:");
                System.out.println(message.toJsonText());
            }
        }
    }
}
