package com.mycompany.prog5121_poe_st10522119;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/*
 * Unit tests for the Message class.
 *
 * These tests check the Part 2 message-length, recipient-cell, message-hash, message-ID, send, disregard,
   store, and print-message requirements. 
 * The test data follows the POE tables , (IIE, 2026) and JUnit assertions are used to confirm expected results.
 * I have added tests for the array, search, delete and report features required by Part 3 of the POE.
 */

public class MessageTest
{
    /*
     * This method runs before each test. It clears static values so that one test does not affect another test.
     */
    
    @BeforeEach
    public void resetData()
    {
        Message.resetMessageData();
    }

    /*
     * This test confirms that a message below 250 characters is ready to send.
     */
    @Test
    public void testMessageLengthSuccess()
    {
        Message message = new Message("0012345678", 0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals(Message.MESSAGE_READY, message.validateMessageLength());
        assertEquals(message.MESSAGE_SENT_TEXT, message.getMessageLengthDisplayMessage());
    }

    /*
     * This test builds a 251-character message using a loop and StringBuilder.
     * It confirms that the program reports one extra character and rejects it.
     */
    @Test
    public void testMessageLengthFailure()
    {
        StringBuilder longMessage = new StringBuilder("");
        int count;

        for(count = 0; count < 251; count++)
        {
            longMessage.append("a");
        }

        Message message = new Message("0012345678", 0, "+27718693002", longMessage.toString());
        assertEquals("Message exceeds 250 characters by 1; please reduce the size.", message.validateMessageLength());
        assertEquals(Message.MESSAGE_TOO_LONG, message.getMessageLengthDisplayMessage());
    }

    /*
     * This test confirms that the recipient number is correctly formatted when it contains the +27 international code.
     */
    
    @Test
    public void testRecipientNumberCorrectlyFormatted()
    {
        Message message = new Message("0012345678", 0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals(Message.RECIPIENT_SUCCESS, message.checkRecipientCell());
    }

    /*
     * This test confirms that the recipient number fails when it does not use the required international code format.
     */
    
    @Test
    public void testRecipientNumberIncorrectlyFormatted()
    {
        Message message = new Message("0012345678", 0, "08575975889", "Hi Keegan, did you receive the payment?");
        assertEquals(Message.RECIPIENT_ERROR, message.checkRecipientCell());
    }
    
    /*
     * This test confirms that the message hash is created correctly,and that it uses the first two message-ID digits,
     * the message number, and the first and last message words.
     */
    
    @Test
    public void testMessageHashIsCorrect()
    {
        Message message = new Message("0012345678", 0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals(Message.RECIPIENT_ERROR, message.createMessageHash());
    }

    /*
     * This test confirms that a message ID with ten or fewer characters passes the ID check.
     */
    
    @Test
    public void testMessageIDCorrectLength()
    {
        Message message = new Message("0012345678", 0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertTrue(message.checkMessageID());
    }

    /*
     * This test confirms that a message ID with more than ten characters fails the ID check.
     */
    
    @Test
    public void testMessageIDTooLong()
    {
        Message message = new Message("00123456789", 0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertFalse(message.checkMessageID());
    }

    /*
     * This test confirms that the message hash uses the first two message-ID digits, the message number, and the first
       and last message words.
     */
    
  
    /*
     * This test confirms that the generated message ID is ten characters long.
     */
    
    @Test
    public void testMessageIDIsCreated()
    {
        String messageID = Message.generateMessageID(0);
        assertEquals(10, messageID.length());
    }

    /*
     * This test confirms that choosing Send Message returns the correct message and increases the total number of sent messages.
     */
    
        @Test
    public void testSendMessageChoice()
    {
        Message message = new Message("0012345678", 0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        message.setSendChoice(1);
        assertEquals(Message.SENT_SUCCESS, message.SentMessage());
        assertEquals(1, message.returnTotalMessages());
    }

    
    /*
     * This test confirms that a message can be found by its message ID.
     */
    
    @Test
    public void testSearchForMessageID()
    {
        Message report = new Message();
        populateFinalPoeTestData();
        assertEquals("Is it dinner time!", report.getMessageByID("0838884567"));
        assertTrue(report.searchMessageByID("0838884567").contains("It is dinner time!"));
    }
 
    /*
     * This test confirms that choosing Disregard Message returns the correct message and does not increase the sent-message total.
     */
    
    @Test
    public void testDisregardMessageChoice()
    {
        Message message = new Message("1112345678", 1, "08575975889", "Hi Keegan, did you receive the payment?");
        message.setSendChoice(2);
        assertEquals(Message.DISREGARD_MESSAGE, message.SentMessage());
        assertEquals(0, message.returnTotalMessages());
    }

    /*
     * This test confirms that choosing Store Message returns the correct message and places JSON-formatted text in memory.
     */
    
    @Test
    public void testStoreMessageChoice()
    {
        Message message = new Message("2212345678", 2, "+27718693002", "Please store this message.");
        message.setSendChoice(3);
        assertEquals(Message.STORED_SUCCESS, message.SentMessage());
        assertTrue(message.getStoredMessagesJson().contains("Please store this message."));
    }

    /*
     * This test confirms that printMessages() returns the details of a sent message captured while the program is running.
     */
    
    @Test
    public void testPrintMessages()
    {
        Message message = new Message("0012345678", 0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        message.setSendChoice(1);
        message.SentMessage();
        assertTrue(message.printMessages().contains("Hi Mike, can you join us for dinner tonight?"));
    }

    /*
     * This test confirms that no sent-message report is printed when no message has been sent yet.
     */
    
    @Test
    public void testPrintMessagesWhenNoneSent()
    {
        Message message = new Message();
        assertEquals("No sent messages.", message.printMessages());
    }
    
    /*
     * This test confirms that sent message arrays are populated correctly
     */
    
    @Test
    public void tetsSentMessagesArrayCorrectlyPopulated()
    {
        Message report = new Message();
        populateFinalPoeTestData();
        assertEquals("Did you get the cake? It is dinner time!", report.getSentMessagesArrayText());
    }
    
    /*
     * This test confirms that the longest message can be found from arrays.
     */
    
    @Test
    public void testDisplayLongestMessage()
    {
        Message report = new Message();
        populateFinalPoeTestData();
        assertEquals("Where are you? You are Late! I have asked you to be on time.", report.displayLongestMessage());
    }
    
    /*
     * This test confirms that all messages for a recipient can be found.
     */
    
    @Test
    public void testSearchMessagesByRecipient()
    {
    Message report = new Message();
    String result;
    
    populateFinalPoeTestData();
    result = report.searchMessagesByRecipient("+27838884567");
    assertTrue(result.contains("Where are you? You are late! I have asked you to be on time."));
    assertTrue(result.contains("Ok, I am leaving without you."));
    }
    
    /*
     * This test confirms that deletion by message hash, marks a message deleted.
     */
    
    @Test
    public void testDeleteMessageByHash()
    {
        Message report = new Message();
        Message messageTwo = new Message("2000000002", 1, "+27838884567", "Where are you? You are late! I have asked you to be on time.");
        String result;
        
        populateFinalPoeTestData();
        result = report.deletedMessageByHash(messageTwo.createMessageHash());
        assertEquals("Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.", result);
        assertFalse(report.searchMessagesByRecipient("+27838884567").contains("Where are you? You are late! I have asked you to be on time."));
        
    }
    
    /*
     * This test confirms that the report includes hash, recipient and message.
     */
    
    @Test
    public void tetsDisplayReport()
    {
        Message report = new Message();
        String result;
        
        populateFinalPoeTestData();
        result = report.displayReport();
        assertTrue(result.contains("Message Hash"));
        assertTrue(result.contains("Recipient"));
        assertTrue(result.contains("Message"));
        assertTrue(result.contains("Did you get the cake?"));
        assertTrue(result.contains("Is it dinner time!"));
    }
    
    /*
     * This helper uses the message test data to populate the arrays which is provided
     * in the final part of the POE.
     */
    
    private void populateFinalPoeTestData()
    {
        Message.prepareMessageArrays(5);
        Message.addMessageToArrays("1000000001", 0, "Developer", "+27834557896", "Did you get the cake?", Message.STATUS_SENT);
        Message.addMessageToArrays("2000000002", 1, "Developer", "+27838884567", "Where are you? You are late! I have asked you to be on time.", Message.STATUS_STORED);
        Message.addMessageToArrays("3000000003", 2, "Developer", "+27834484567", "Yohoooo, I am at your gate.", Message.STATUS_DISREGARDED);
        Message.addMessageToArrays("0838884567", 3, "Developer", "0838884567", "It is dinner time!", Message.STATUS_SENT);
        Message.addMessageToArrays("5000000005", 4, "Developer", "+27838884567", "Ok, I am leaving without you.", Message.STATUS_STORED);
        
    }
}
