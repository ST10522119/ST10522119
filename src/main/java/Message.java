package com.mycompany.prog5121_poe_st10522119;

/**
 * This class stores message details, validates message data, handles the
 * send/store/disregard decision, and keeps track of the total number of
 * messages sent.
 */
public class Message
{
    /*
     * These constants store repeated messages. Keeping them in one place helps
     * prevent repeated hard-coded text and makes unit tests easier to maintain.
     */
    public static final String MESSAGE_READY = "Message ready to send.";
    public static final String RECIPIENT_SUCCESS = "Cell phone number successfully captured.";
    public static final String RECIPIENT_ERROR = "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    public static final String SENT_SUCCESS = "Message successfully sent.";
    public static final String DISREGARD_MESSAGE = "Press 0 to delete the message.";
    public static final String STORED_SUCCESS = "Message successfully stored.";

    /*
     * These static fields belong to the Message class rather than to one
     * individual object. They keep the running total of sent messages and store
     * report text while the program is running.
     */
    private static int totalMessagesSent = 0;
    private static StringBuilder sentMessages = new StringBuilder("");
    private static StringBuilder storedMessagesJson = new StringBuilder("");

    /*
     * These instance fields describe one message. Each Message object has its
     * own ID, number, recipient, message text, generated hash, and selected
     * action.
     */
    private String messageID;
    private int messageNumber;
    private String recipient;
    private String message;
    private String messageHash;
    private int sendChoice;
    private boolean actionCompleted;

    /*
     * The default constructor creates an empty Message object. The main class
     * fills these fields through setter methods as the user enters data in the
     * console.
     */
    public Message()
    {
        messageID = "";
        messageNumber = 0;
        recipient = "";
        message = "";
        messageHash = "";
        sendChoice = 0;
        actionCompleted = false;
    }

    /*
     * This overloaded constructor creates a Message object with key values that
     * have already been supplied. It is useful for unit tests because test data
     * can be created in one statement (Farrell, 2023).
     */
    public Message(String messageID, int messageNumber, String recipient, String message)
    {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.message = message;
        messageHash = "";
        sendChoice = 0;
        actionCompleted = false;
    }

    /* Setter methods update the message details one value at a time. */
    public void setMessageID(String messageID)
    {
        this.messageID = messageID;
        messageHash = "";
    }

    public void setMessageNumber(int messageNumber)
    {
        this.messageNumber = messageNumber;
        messageHash = "";
    }

    public void setRecipient(String recipient)
    {
        this.recipient = recipient;
    }

    public void setMessage(String message)
    {
        this.message = message;
        messageHash = "";
    }

    public void setSendChoice(int sendChoice)
    {
        this.sendChoice = sendChoice;
    }

    /* Getter methods return field values without exposing private fields.
     * (Farrell, 2023)
    */
    public String getMessageID()
    {
        return messageID;
    }

    public String getRecipient()
    {
        return recipient;
    }

    public String getMessage()
    {
        return message;
    }

    /*
     * This getter returns the message hash. If the hash has not been created
     * yet, it calls createMessageHash() so the latest details are used.
     */
    public String getMessageHash()
    {
        if(messageHash.equals(""))
        {
            messageHash = createMessageHash();
        }
        return messageHash;
    }

    /* This method checks that the message ID is not more than ten characters. */
    public boolean checkMessageID()
    {
        return messageID != null && messageID.length() <= 10;
    }

    /*
     * This method returns the correct message after checking the recipient cell number.
     * It re-uses the same South African +27 pattern used in Part 1.
     */
    public String checkRecipientCell()
    {
        if(isCellNumberValid())
        {
            return RECIPIENT_SUCCESS;
        }
        return RECIPIENT_ERROR;
    }

    /* This private helper performs the actual recipient validation. */
    private boolean isCellNumberValid()
    {
        return recipient != null && recipient.matches("\\+27[0-9]{9}");
    }

    /*
     * This method checks that the message text is not more than 250 characters.
     * If it is too long, the method calculates how many extra characters must be removed.
     */
    public String validateMessageLength()
    {
        int extraCharacters;

        if(message != null && message.length() <= 250)
        {
            return MESSAGE_READY;
        }

        if(message == null)
        {
            extraCharacters = 0;
        }
        else
        {
            extraCharacters = message.length() - 250;
        }

        return "Message exceeds 250 characters by " + extraCharacters + "; please reduce the size.";
    }

    /*
     * This method creates the POE message hash. The hash contains the first two digits of the message ID, 
       the message number, and the first and last words of the message in uppercase. 
     * It uses String methods such as substring(), indexOf(), lastIndexOf(), and toUpperCase() (Farrell, 2023).
     */
    public String createMessageHash()
    {
        String firstTwoNumbers;
        String firstWord;
        String lastWord;
        String cleanMessage;
        int firstSpace;
        int lastSpace;

        if(messageID == null || messageID.length() < 2)
        {
            firstTwoNumbers = "00";
        }
        else
        {
            firstTwoNumbers = messageID.substring(0, 2);
        }

        if(message == null || message.length() == 0)
        {
            firstWord = "";
            lastWord = "";
        }
        else
        {
            cleanMessage = trimMessage(message);
            firstSpace = cleanMessage.indexOf(' ');
            lastSpace = cleanMessage.lastIndexOf(' ');

            if(firstSpace == -1)
            {
                firstWord = cleanWord(cleanMessage);
                lastWord = cleanWord(cleanMessage);
            }
            else
            {
                firstWord = cleanWord(cleanMessage.substring(0, firstSpace));
                lastWord = cleanWord(cleanMessage.substring(lastSpace + 1));
            }
        }

        messageHash = (firstTwoNumbers + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
        return messageHash;
    }

    /*
     * This private helper removes spaces from the start and end of a message.
     * It uses while loops and charAt() (Farrell, 2023).
     */
    private String trimMessage(String value)
    {
        int start = 0;
        int end;
        String result = "";

        if(value != null)
        {
            end = value.length() - 1;

            while(start <= end && value.charAt(start) == ' ')
            {
                start++;
            }

            while(end >= start && value.charAt(end) == ' ')
            {
                end--;
            }

            if(start <= end)
            {
                result = value.substring(start, end + 1);
            }
        }

        return result;
    }

    /* This private helper removes punctuation from the start and end of a word. */
    private String cleanWord(String word)
    {
        int start = 0;
        int end;
        String clean = "";

        if(word != null && word.length() > 0)
        {
            end = word.length() - 1;

            while(start <= end && !Character.isLetterOrDigit(word.charAt(start)))
            {
                start++;
            }

            while(end >= start && !Character.isLetterOrDigit(word.charAt(end)))
            {
                end--;
            }

            if(start <= end)
            {
                clean = word.substring(start, end + 1);
            }
        }

        return clean;
    }

    /*
     * This method performs the user's selected message action. Choice 1 sends the message and increments the total. 
     * Choice 2 disregards the message.
     * Choice 3 stores the JSON-formatted message text.
     */
    public String SentMessage()
    {
        String result;

        if(sendChoice == 1)
        {
            if(!actionCompleted)
            {
                totalMessagesSent++;
                sentMessages.append(getFullDetails()).append("\n");
                actionCompleted = true;
            }
            result = SENT_SUCCESS;
        }
        else
        {
            if(sendChoice == 2)
            {
                actionCompleted = true;
                result = DISREGARD_MESSAGE;
            }
            else
            {
                if(sendChoice == 3)
                {
                    if(!actionCompleted)
                    {
                        storeMessage();
                        actionCompleted = true;
                    }
                    result = STORED_SUCCESS;
                }
                else
                {
                    result = "Invalid option selected.";
                }
            }
        }

        return result;
    }

    /* Lowercase alias supplied for normal Java naming style. */
    public String sentMessage()
    {
        return SentMessage();
    }

    /* This method returns all sent messages captured while the program runs. */
    public String printMessages()
    {
        if(sentMessages.length() == 0)
        {
            return "No sent messages.";
        }
        return sentMessages.toString();
    }

    /* This method returns the total number of messages that were sent. */
    public int returnTotalMessages()
    {
        return totalMessagesSent;
    }

    
    public int returnTotalMessagess()
    {
        return returnTotalMessages();
    }

    /*
     * This method stores the current message as JSON-formatted text in memory.
     * StringBuilder is used instead of advanced JSON libraries.
     */
    public String storeMessage()
    {
        String jsonMessage = toJsonText();

        if(storedMessagesJson.length() > 0)
        {
            storedMessagesJson.append(",\n");
        }
        storedMessagesJson.append(jsonMessage);

        return jsonMessage;
    }

    /* This method wraps stored JSON-formatted messages in square brackets. */
    public String getStoredMessagesJson()
    {
        return "[\n" + storedMessagesJson.toString() + "\n]";
    }

    /* This method builds one JSON-formatted message object. */
    public String toJsonText()
    {
        StringBuilder json = new StringBuilder("");

        json.append("  {\n");
        json.append("    \"messageID\": \"").append(escapeJson(messageID)).append("\",\n");
        json.append("    \"messageHash\": \"").append(escapeJson(getMessageHash())).append("\",\n");
        json.append("    \"recipient\": \"").append(escapeJson(recipient)).append("\",\n");
        json.append("    \"message\": \"").append(escapeJson(message)).append("\"\n");
        json.append("  }");

        return json.toString();
    }

    /* This private helper escapes quotation marks and backslashes for JSON text.
    */
    private String escapeJson(String value)
    {
        StringBuilder escaped = new StringBuilder("");
        int count;

        if(value != null)
        {
            for(count = 0; count < value.length(); count++)
            {
                if(value.charAt(count) == '"')
                {
                    escaped.append("\\\"");
                }
                else
                {
                    if(value.charAt(count) == '\\')
                    {
                        escaped.append("\\\\");
                    }
                    else
                    {
                        escaped.append(value.charAt(count));
                    }
                }
            }
        }

        return escaped.toString();
    }

    /* This method returns the full message details in the required order. */
    public String getFullDetails()
    {
        return "Message ID: " + messageID + "\n" +
               "Message Hash: " + getMessageHash() + "\n" +
               "Recipient: " + recipient + "\n" +
               "Message: " + message;
    }

    /*
     * This static method generates a ten-digit message ID. Math.random() is used to generate each digit.
     * The number is stored as a String so that a leading zero is not lost.
     */
    public static String generateMessageID(int messageNumber)
    {
        String id = "";
        int count;
        int digit;

        for(count = 0; count < 10; count++)
        {
            digit = (int)(Math.random() * 10);
            id = id + digit;
        }

        return id;
    }

    /* This method returns the generated ID inside a display message. */
    public static String getGeneratedMessageIDMessage(int messageNumber)
    {
        return "Message ID generated: " + generateMessageID(messageNumber);
    }

    /* This method clears static message data before each unit test. */
    public static void resetMessageData()
    {
        totalMessagesSent = 0;
        sentMessages = new StringBuilder("");
        storedMessagesJson = new StringBuilder("");
    }
}
