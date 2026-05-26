package com.mycompany.prog5121_poe_st10522119;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/*
 * Unit tests for the Login class.
 *
 * These tests check the Part 1 username, password, cellphone, registration, and login requirements from the POE. 
 * The tests use JUnit assertTrue(), assertFalse(), and assertEquals() so that each method can be tested
   separately, as required for a testable program (IIE, 2026). 
 */
public class LoginTest
{
    /*
     * This test confirms that a username with an underscore and five or fewer characters passes validation.
     */
    @Test
    public void testUsernameCorrectlyFormatted()
    {
        Login login = new Login("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.checkUserName());
        assertEquals(Login.USERNAME_SUCCESS, login.getUserNameMessage());
    }

    /*
     * This test confirms that a username without the correct format fails and returns the required error message.
     */
    @Test
    public void testUsernameIncorrectlyFormatted()
    {
        Login login = new Login("Kyle", "Smith", "kyle!!!!!!!", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(login.checkUserName());
        assertEquals(Login.USERNAME_ERROR, login.getUserNameMessage());
    }

    /*
     * This test confirms that a password with enough length, a capital letter, a number, and a special character passes validation.
     */
    
    @Test
    public void testPasswordMeetsComplexityRequirements()
    {
        Login login = new Login("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.checkPasswordComplexity());
        assertEquals(Login.PASSWORD_SUCCESS, login.getPasswordMessage());
    }

    /*
     * This test confirms that a password without the required complexity fails and returns the required error message.
     */
    
    @Test
    public void testPasswordDoesNotMeetComplexityRequirements()
    {
        Login login = new Login("Kyle", "Smith", "kyl_1", "password", "+27838968976");
        assertFalse(login.checkPasswordComplexity());
        assertEquals(Login.PASSWORD_ERROR, login.getPasswordMessage());
    }

    /*
     * This test confirms that a South African cell number with the +27 international code passes validation.
     */
    
    @Test
    public void testCellPhoneCorrectlyFormatted()
    {
        Login login = new Login("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.checkCellPhoneNumber());
        assertEquals(Login.CELL_SUCCESS, login.getCellPhoneMessage());
    }

    /*
     * This test confirms that a cell number without the correct international code fails validation.
     */
    
    @Test
    public void testCellPhoneIncorrectlyFormatted()
    {
        Login login = new Login("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "08966553");
        assertFalse(login.checkCellPhoneNumber());
        assertEquals(Login.CELL_ERROR, login.getCellPhoneMessage());
    }

    /*
     * This test confirms that a valid username, password, and cellphone number return the successful registration message.
     */
    
    @Test
    public void testRegisterUserSuccessful()
    {
        Login login = new Login("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals(Login.REGISTER_SUCCESS, login.registerUser());
    }

    /*
     * This test confirms that registration returns the username error first when the username is invalid.
     */
    
    @Test
    public void testRegisterUserFailsForBadUsername()
    {
        Login login = new Login("Kyle", "Smith", "kyle!!!!!!!", "Ch&&sec@ke99!", "+27838968976");
        assertEquals(Login.USERNAME_ERROR, login.registerUser());
    }

    /*
     * This test confirms that login succeeds when the entered username and password match the registered username and password.
     */
    
    @Test
    public void testLoginSuccessful()
    {
        Login login = new Login("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
        assertEquals("Welcome Kyle, Smith it is great to see you again.", login.returnLoginStatus());
    }

    /*
     * This test confirms that login fails when the entered password does not match the registered password.
     */
    
    @Test
    public void testLoginFailed()
    {
        Login login = new Login("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(login.loginUser("kyl_1", "wrongPassword"));
        assertEquals(Login.LOGIN_ERROR, login.returnLoginStatus());
    }
}
