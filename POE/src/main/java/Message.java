package com.mycompany.prog5121_poe_st10522119;

/**
 * This class stores message details, validates message data, handles the
 * send/store/disregard decision, and keeps track of the total number of
 * messages sent as well as the arrays used for searching, deleting and reporting
 * messages as required by Part 3
 * (Farrell, 2023).
 */
public class Message
{
    /*
     * These constants store repeated messages as well as the menu status values.
     * Keeping them in one place helps to prevent repeating hard-coded text. 
     * It also makes unit tests easier to maintain.
     */
    public static final String MESSAGE_READY = "Message ready to send.";
    public static final String MESSAGE_SENT_TEXT = "Message sent";
    public static final String MESSAGE_TOO_LONG = "Please enter a message of less than 250 characters.";
    public static final String RECIPIENT_SUCCESS = "Cell phone number successfully captured.";
    public static final String RECIPIENT_ERROR = "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    public static final String SENT_SUCCESS = "Message successfully sent.";
    public static final String DISREGARD_MESSAGE = "Press 0 to delete the message.";
    public static final String STORED_SUCCESS = "Message successfully stored.";
    public static final String NO_MESSAGES = "No messages available.";
    public static final int STATUS_SENT = 1;
    public static final int STATUS_DISREGARDED = 2;
    public static final int STATUS_STORED = 3;

    /*
     * The default array size is used before the applicationn knows how many 
     * messages the user plans to send. The array can also be expanded manually
     * using loops, which follows the array principles 
     * (Farrell, 2023)
     */
    
    private static final int DEFAULT_ARRAY_SIZE = 100;
    private static final String DEFAULT_SENDER = "QuickChat User";
    
    /*
     * These static fields belong to the Message class rather than to one
     * individual object. They keep the running total of sent messages as well as store
     * and report text while the program is running.
     * In addition, it will store all the arrays required by part 3 while the program is running.
     */
    
    private static int totalMessagesSent;
    private static int totalRecords;
    private static int sentCount;
    private static int disregardedCount;
    private static int storedCount;
    private static StringBuilder sentMessages;
    private static StringBuilder storedMessagesJson;
    private static String[] sentMessagesArray;
    private static String[] disregardedMessagesArray;
    private static String[] storedMessagesArray;
    private static String[] messageHashArray;
    private static String[] messageIDArray;
    private static String[] allSendersArray;
    private static String[] allRecipientsArray;
    private static String[] allMessagesArray;
    private static int [] allStatusArray;
    private static boolean [] deletedArray;
    
    /*
     * Static initialisation prepares arrays before any Message object is made.
     * This prevents null array references during tests or menu reports.
     */
    
    static
    {
        prepareMessageArrays(DEFAULT_ARRAY_SIZE);
    }

    /*
     * These instance fields describe one message. Each Message object has its
     * own ID, number, sender, recipient, message text, generated hash, and selected
     * action.
     */
    private String messageID;
    private int messageNumber;
    private String sender;
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
        sender = DEFAULT_SENDER;
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
        this.sender = DEFAULT_SENDER;
        this.recipient = recipient;
        this.message = message;
        messageHash = "";
        sendChoice = 0;
        actionCompleted = false;
    }
    
    /*
     *This constructor adds the sender field required when the stored-message menu
     * which will display sender and recipient details.
     */
    
    public Message(String messageID, int messageNNumber, String sender, String recipient, String message)
    {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.sender = sender;
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
    
    public void setSender(String sender)
    {
        this.sender = sender;
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
    
    public int getMessageNumber()
    {
        return messageNumber;
    }

    public String getSender()
    {
        return sender;
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
     * This method returns the wordinng provided in the POE that is used when validating the message in the
     * console. I have kept it separate from validateMessageLength() soo that previous
     * unit tests can still use MESSAGE_READY.
     */
    public String getMessageLengthDisplayMessage()
    {
        if(message != null && message.length() <= 250)
        {
            return MESSAGE_SENT_TEXT;
        }
        return MESSAGE_TOO_LONG;
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

        if(sendChoice == STATUS_SENT)
        {
            if(!actionCompleted)
            {
                recordMessage(STATUS_SENT);
            }
            result = SENT_SUCCESS;
        }
        else
        {
            if(sendChoice == STATUS_DISREGARDED)
            {
                if(!actionCompleted)
                {
                    recordMessage(STATUS_DISREGARDED);
                }
                result = DISREGARD_MESSAGE;
            }
            else
            {
                if(sendChoice == STATUS_STORED)
                {
                    if(!actionCompleted)
                {
                    recordMessage(STATUS_STORED);
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
    
    /*
     * This private method adds the current message to the general arrays and to
     * the selected status array. Parallel arrays are used so that the value in the
     * same subscript position belongs to the same message record. (Farrell, 2023)
     */
    
    private void recordMessage(int status)
    {
        int recordPosition;
        
        ensureAllCapacity();
        recordPosition = totalRecords;
        
        messageIDArray[recordPosition] = safeText(messageID);
        messageHashArray[recordPosition] = getMessageHash();
        allSendersArray[recordPosition] = safeText(sender);
        allRecipientsArray[recordPosition] = safeText(recipient);
        allMessagesArray[recordPosition] = safeText(message);
        allStatusArray[recordPosition] = status;
        deletedArray[recordPosition] = false;
        totalRecords++;
        
        if(status == STATUS_SENT)
        {
            ensureSentCapacity();
            sentMessagesArray[sentCount] = safeText(message);
            sentCount++;
            totalMessagesSent++;
            sentMessages.append(getFullDetails()).append("\n");
        }
        else
        {
            if(status == STATUS_DISREGARDED)
            {
                ensureDisregardedCapacity();
                disregardedMessagesArray[disregardedCount] = safeText(message);
                disregardedCount++;
            }
            else
            {
                if(status == STATUS_STORED)
                {
                  ensureStoredCapacity();
                  storedMessagesArray[storedCount] = safeText(message);
                  storedCount++;
                  appendJsonToStorage(toJsonText());
                }
            }
        
        }
        actionCompleted = true;
    }

    
    /*
     *This helper avoids null text from being stored in arrays and reports.
     */
    
    private String safeText(String value)
    {
        if(value == null)
        {
            return"";
        }
        return value;
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
        appendJsonToStorage(jsonMessage);
        return jsonMessage;
    }      
    /*
     * This helper appends oone JSON-style message to the in-memory JSON text
     */
    
    private void appendJsonToStorage(String jsonMessage)
    {
        if(storedMessagesJson.length() > 0)
        {
            storedMessagesJson.append(",\n");
        }
        storedMessagesJson.append(jsonMessage);
    }

    /* This method wraps stored JSON-formatted messages in square brackets. */
    public String getStoredMessagesJson()
    {
        return "[\n" + storedMessagesJson.toString() + "\n]";
    }

    /* This method builds one JSON-formatted message object using StringBuilder.
     * StringBuilder is used because the string content changes repratedly while
     * the JSON text is being built. (Farrell, 2023)
     */
 
    public String toJsonText()
    {
        StringBuilder json = new StringBuilder("");

        json.append("  {\n");
        json.append("    \"messageID\": \"").append(escapeJson(messageID)).append("\",\n");
        json.append("    \"messageHash\": \"").append(escapeJson(getMessageHash())).append("\",\n");
        json.append("    \"sender\": \"").append(escapeJson(sender)).append("\",\n");
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

    /*
     * This method prepares new arrays with size based on the number of records the user
     * plans to enter. It demonstrates declaring and creating arrays with square brackets.
     * (Farrell, 2023)
     */
    
    public static void prepareMessageArrays(int maximumMessages)
    {
        if(maximumMessages < 1)
        {
            maximumMessages = DEFAULT_ARRAY_SIZE;
        }
        
        totalMessagesSent = 0;
        totalRecords = 0;
        sentCount = 0;
        disregardedCount = 0;
        storedCount = 0;
        sentMessages = new StringBuilder("");
        storedMessagesJson = new StringBuilder("");
        sentMessagesArray = new String[maximumMessages];
        disregardedMessagesArray = new String[maximumMessages];
        storedMessagesArray = new String[maximumMessages];
        messageHashArray = new String[maximumMessages];
        messageIDArray = new String[maximumMessages];
        allSendersArray = new String[maximumMessages];
        allRecipientsArray = new String[maximumMessages];
        allMessagesArray = new String[maximumMessages];
        allStatusArray = new int[maximumMessages];
        deletedArray = new boolean[maximumMessages];
    }
    
    /* This method clears static message data before each unit test. */
    public static void resetMessageData()
    {
        prepareMessageArrays(DEFAULT_ARRAY_SIZE);
    }
    
    /*
     * This helper lets unit tests or future code add a complete message to the arrays
     * without using keyboard input.
     */
    
    public static String addMessageToArrays(String messageID, int messageNumber, String sender, String recipient, String messageText, int status)
    {
        Message messageObject = new Message(messageID, messageNumber, sender, recipient, messageText);
        messageObject.setSendChoice(status);
        return messageObject.SentMessage();
    }
    
    /*
     * This overload uses the default sender to keep older tests working.
     */
    
    public static String addMessageToArrays(String messageID, int messageNumber, String recipient, String messageText, int status)
    {
        return addMessageToArrays(messageID, messageNumber, DEFAULT_SENDER, recipient, messageText, status);
    }
    
    /*
     * The following capacity helpers manually expand arrays when needed.
     */
    
    private void ensureAllCapacity()
    {
        if(totalRecords >= messageIDArray.length)
        {
            expandAllArrays();
        }
    }
    
    private void ensureSentCapacity()
    {
        if(sentCount >= sentMessagesArray.length)
        {
            sentMessagesArray = expandStringArray(sentMessagesArray);
        }
    }
    
    private void ensureDisregardedCapacity()
    {
        if(disregardedCount >= disregardedMessagesArray.length)
        {
            disregardedMessagesArray = expandStringArray(disregardedMessagesArray);
        }
    }
    
    private void ensureStoredCapacity()
    {
        if(storedCount >= storedMessagesArray.length)
        {
            storedMessagesArray = expandStringArray(storedMessagesArray);
        }
    }
    
    private void expandAllArrays()
    {
        messageHashArray = expandStringArray(messageHashArray);
        messageIDArray = expandStringArray(messageIDArray);
        allSendersArray = expandStringArray(allSendersArray);
        allRecipientsArray = expandStringArray(allRecipientsArray);
        allMessagesArray = expandStringArray(allMessagesArray);
        allStatusArray = expandIntArray(allStatusArray);
        deletedArray = expandBooleanArray(deletedArray);
    }
    
    private String[] expandStringArray(String[] oldArray)
    {
        String[] newArray = new String[oldArray.length + DEFAULT_ARRAY_SIZE];
        int count;
        
        for(count = 0; count < oldArray.length; count++)
        {
            newArray[count] = oldArray[count];
        }
        return newArray;
    } 
    
    private int[] expandIntArray(int[] oldArray)
    {
        int[] newArray = new int[oldArray.length + DEFAULT_ARRAY_SIZE];
        int count;
        
        for(count = 0; count < oldArray.length; count++)
        {
            newArray[count] = oldArray[count];
        }
        return newArray;
    }
    
    private boolean[] expandBooleanArray(boolean[] oldArray)
    {
        boolean[] newArray = new boolean[oldArray.length + DEFAULT_ARRAY_SIZE];
        int count;
        
        for(count = 0; count < oldArray.length; count++)
        {
            newArray[count] = oldArray[count];
        }
        return newArray;
    }
    
    /*
     * These array-copy methods return only populated elements to callers.
     */
    
    public String[] getSentMessagesArray()
    {
        return copyStringArray(sentMessagesArray, sentCount);
    }
    
    public String[] getDisregardedMessagesArray()
    {
        return copyStringArray(disregardedMessagesArray, disregardedCount);
    }
    
    public String[] getStoredMessagesArray()
    {
        return copyStringArray(storedMessagesArray, storedCount);
    }
    
    public String[] getMessageHashArray()
    {
        return copyStringArray(messageHashArray, totalRecords);
    }
    
    public String[] getMessageIDArray()
    {
        return copyStringArray(messageIDArray, totalRecords);
    }
    
    public String[] readStoredMessagesJsonIntoArray()
    {
        return getStoredMessagesArray();
    }
    
    private String[] copyStringArray(String[] sourceArray, int numberOfItems)
    {
        String[] copiedArray = new String[numberOfItems];
        int count;
        
        for(count = 0; count < numberOfItems; count++)
        {
            copiedArray[count] = sourceArray[count];
        }
        return copiedArray;
    }
    
    /*
     * This method returns sent messages as a comma-separated list as required for part 3 of the POE
     */
    
    public String getSentMessagesArrayText()
    {
        return joinArrayValues(sentMessagesArray, sentCount);
    }
    
    /*
     * This method returns stored messages as a comma-separated list.
     */
    
    public String getStoredMessagesArrayText()
    {
        return joinArrayValues(storedMessagesArray, storedCount);
    }
    
    private String joinArrayValues(String[] values, int numberOfItems)
    {
        StringBuilder result = new StringBuilder("");
        int count;
        int included = 0;
        
        for(count = 0; count < numberOfItems; count++)
        {
            if(values[count] != null && values[count].length() > 0)
            {
                if(included > 0)
                {
                    result.append(", ");
                }    
                result.append(values[count]);
                included++;
            }
        }
        
        if(result.length() == 0)
        {
            return NO_MESSAGES;
        }
        return result.toString();
    }
    
    /*
     * This method searches the parallel arrays and returns the longest message.
     * It uses a loop that starts at subscript 0 and ends before the array length,
     * matching the (Farrell, 2023) guidance for processing arrays.
     */

    public String displayLongestMessage()
    {
        int count;
        int longestIndex = -1;
        
        for(count = 0; count < totalRecords; count++)
        {
            if(!deletedArray[count] && allMessagesArray[count] != null)
            {
                if(longestIndex == -1 || allMessagesArray[count].length() > allMessagesArray[longestIndex].length())
                {
                    longestIndex = count;
                }
            }
        }
        
        if(longestIndex == -1)
        {
            return NO_MESSAGES;
        }
        return allMessagesArray[longestIndex];
    }
    
    /*
     * This method displays sender and recipient for all stored messages.
     */
    
    public String displayStoredSenderRecipient()
    {
        StringBuilder result = new StringBuilder("");
        int count;
        
        for(count = 0; count < totalRecords; count++)
        {
            if(!deletedArray[count] && allStatusArray[count] == STATUS_STORED)
            {
                result.append("Sender: ").append(allSendersArray[count]).append("\n");
                result.append("Recipient: ").append(allRecipientsArray[count]).append("\n\n");
            }
        }
        
        if(result.length() == 0)
        {
            return "No Stored messages.";
        }
        return result.toString();
    }
    
    /*
     * This method returns only the message text for a matching message ID
     */
    
    public String getMessageByID(String searchMessageID)
    {
        int index = findIndexByMessageID(searchMessageID);
        
        if(index == -1)
        {
            return "Message ID not found.";
        }
        return allMessagesArray[index];
    }
    
    /*
     * This method returns the matching recipient and message for the menu.
     */
    
    public String searchMessageByID(String searchMessageID)
    {
        int index = findIndexByMessageID(searchMessageID);
        
        if(index == -1)
        {
            return "Message ID not found.";
        }
        return "Recipient: " + allRecipientsArray[index] + "\nMessage: " + allMessagesArray[index];
    }
    
    private int findIndexByMessageID(String searchMessageID)
    {
        int count;
        int foundIndex = -1;
        
        for(count = 0; count < totalRecords; count++)
        {
            if(!deletedArray[count] && messageIDArray[count] != null && messageIDArray[count].equals(searchMessageID))
            {
                foundIndex = count;
                count = totalRecords;
            }
        }
        
        return foundIndex;
    }
    
    /*
     * This method searches foor all the sent or stored messages for a particular
     * recipient. However, both disregarded and deleted messages are excluded.
     */
    
    public String searchMessagesByRecipient(String searchRecipient)
    {
        StringBuilder result = new StringBuilder("");
        int count;
        int matches = 0;
        
        for(count = 0; count < totalRecords; count++)
        {
            if(!deletedArray[count] && allRecipientsArray[count] != null && allRecipientsArray[count].equals(searchRecipient) && 
                    (allStatusArray[count] == STATUS_SENT || allStatusArray[count] == STATUS_STORED))
            {
                if(matches > 0)
                {
                    result.append("\n");
                }
                result.append(allMessagesArray[count]);
                matches++;
            }
        }
        
        if(matches == 0)
        {
            return "No messages found for recipient.";
        }
        return result.toString();
    }
    
    /*
     * This method searches by message hash and marks the matching record deleted.
     */
    
    public String deletedMessageByHash(String searchHash)
    {
        int count;
        String deletedMessage = "";
        boolean deleted = false;
        
        for(count =0; count < totalRecords; count++)
        {
            if(!deletedArray[count] && messageHashArray[count] != null && messageHashArray[count].equals(searchHash))
            {
                deletedArray[count] = true;
                deletedMessage = allMessagesArray[count];
                deleted = true;
                count = totalRecords;
            }
        }
        
        if(deleted)
        {
            rebuildCategoryArrays();
            return "Message: \"" + deletedMessage + "\" successfully deleted.";
        }
        return "Message hash not found.";
    }
    
    /*
     * This helper rebuilds category arrays after a delete opration.
     */
    
    private void rebuildCategoryArrays()
    {
        int count;
        
        sentMessagesArray = new String[messageIDArray.length];
        disregardedMessagesArray = new String[messageIDArray.length];
        storedMessagesArray = new String[messageIDArray.length];
        sentMessages = new StringBuilder("");
        storedMessagesJson = new StringBuilder("");
        totalMessagesSent = 0;
        sentCount = 0;
        disregardedCount = 0;
        storedCount = 0;
        
        for(count = 0; count < totalRecords; count++)
        {
            if(!deletedArray[count])
            {
                if(allStatusArray[count] == STATUS_SENT)
                {
                    sentMessagesArray[sentCount] = allMessagesArray[count];
                    sentCount++;
                    totalMessagesSent++;
                    sentMessages.append(detailsFromArrays(count)).append("\n");
                }
                else
                {
                    if(allStatusArray[count] == STATUS_DISREGARDED)
                    {
                        disregardedMessagesArray[disregardedCount] = allMessagesArray[count];
                        disregardedCount++;
                    }
                    else
                    {
                        if(allStatusArray[count] == STATUS_STORED)
                        {
                            storedMessagesArray[storedCount] = allMessagesArray[count];
                            storedCount++;
                            appendJsonToStorage(jsonFromArrays(count));
                        }
                    }
                }
            }
        }
    }
    
    /*
     * This report lists the full details for all messages that have been sent and stored,
     * but have not been deleted.
     */
    
    public String displayReport()
    {
        StringBuilder report = new StringBuilder("");
        int count;
        
        for(count = 0; count < totalRecords; count++)
        {
            if(!deletedArray[count] && (allStatusArray[count] == STATUS_SENT || allStatusArray[count] == STATUS_STORED))
            {
            report.append("Message Hash: ").append(messageHashArray[count]).append("\n");
            report.append("Recipient: ").append(allRecipientsArray[count]).append("\n");
            report.append("Message: ").append(allMessagesArray[count]).append("\n\n");
            }
        }
        
        if(report.length() == 0)
        {
            return NO_MESSAGES;
        }
        return report.toString();
    }
    
    /*
     * This report lists only stored messages for the stored message menu options.
     */
    
    public String displayStoredMessagesReport()
    {
        StringBuilder report = new StringBuilder("");
        int count;
        
        for(count = 0; count < totalRecords; count++)
        {
            if(!deletedArray[count] && allStatusArray[count] == STATUS_STORED)
            {
                report.append("Message Hash: ").append(messageHashArray[count]).append("\n");
                report.append("Sender: ").append(allSendersArray[count]).append("\n");
                report.append("Recipient: ").append(allRecipientsArray[count]).append("\n");
                report.append("Message: ").append(allMessagesArray[count]).append("\n\n");
            }
        }
        
        if(report.length() == 0)
        {
            return "No Stored messages.";
        }
        return report.toString();
    }
    
    /*
     * This helper builds detail text from the parallel arrays.
     */
    
    private String detailsFromArrays(int index)
    {
        return "Message ID: " + messageIDArray[index] + "\n"+
               "Message Hash: " + messageHashArray[index] + "\n"+
               "Recipient: " + allRecipientsArray[index] + "\n"+
               "Message: " + allMessagesArray[index];
    }
    
    /*
     * This helper creates JSON-style from a record already stored in arrays.
     */
    
    private String jsonFromArrays(int index)
    {
        StringBuilder json = new StringBuilder("");
        
        json.append("  {\n");
        json.append("    \"MessageID\": \"").append(escapeJson(messageIDArray[index])).append("\",\n");
        json.append("    \"messageHash\": \"").append(escapeJson(messageHashArray[index])).append("\",\n");
        json.append("    \"sender\": \"").append(escapeJson(allSendersArray[index])).append("\",\n");
        json.append("    \"recipient\": \"").append(escapeJson(allRecipientsArray[index])).append("\",\n");
        json.append("    \"message\": \"").append(escapeJson(allMessagesArray[index])).append("\"\n");
        json.append("  }");
        
        return json.toString();
    }
    
    /*
     * These count methods support unit tests and menu output.
     */
    
    public int getTotalRecords()
    {
        return totalRecords;
    }
    
    public int getStoredCount()
    {
        return storedCount;
    }
    
    public int getSentCount()
    {
        return sentCount;
    }
    
    public int getDisregardedCount()
    {
        return disregardedCount;
    }
   
}
