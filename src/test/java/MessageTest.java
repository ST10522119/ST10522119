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
    }

    /*
     * This test builds a 251-character message using a loop and StringBuilder.
     * It confirms that the program reports one extra character.
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
    
    @Test
    public void testMessageHashIsCorrect()
    {
        Message message = new Message("0012345678", 0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("00:0:HITONIGHT", message.createMessageHash());
    }

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
}
