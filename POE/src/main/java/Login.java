package com.mycompany.prog5121_poe_st10522119;

/**
 * This class stores the registration details.
 * It also validates the username, password, and South African cellphone number.
 */
public class Login
{
    /*
     * These constants store the required registration and login messages in one
     * place. Using constants makes the program easier to maintain because the
     * same wording can be reused in the application and in unit tests
     * (Farrell, 2023; The Independent Institute of Education, 2026).
     */
    public static final String USERNAME_SUCCESS = "Username successfully captured.";
    public static final String USERNAME_ERROR = "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
    public static final String PASSWORD_SUCCESS = "Password successfully captured.";
    public static final String PASSWORD_ERROR = "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
    public static final String CELL_SUCCESS = "Cell number successfully captured.";
    public static final String CELL_ERROR = "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.";
    public static final String REGISTER_SUCCESS = "User registered successfully.";
    public static final String LOGIN_ERROR = "Username or password incorrect, please try again.";

    /*
     * The following private fields hold the user's registration information and
     * attempted login details. The fields are private to apply data hiding,
     * which means other classes must use methods to work with the data
     * (Farrell, 2023).
     */
    private String firstName;
    private String surname;
    private String username;
    private String password;
    private String cellphoneNumber;
    private String loginUsername;
    private String loginPassword;
    private boolean lastLoginSuccessful;

    /*
     * The default constructor gives every field a safe starting value. This
     * helps prevent null values from being used before registration input is
     * added.
     */
    public Login()
    {
        firstName = "";
        surname = "";
        username = "";
        password = "";
        cellphoneNumber = "";
        loginUsername = "";
        loginPassword = "";
        lastLoginSuccessful = false;
    }

    /*
     * This overloaded constructor lets tests and other classes create a complete
     * Login object in one statement. Overloaded constructors keep code simple
     * and testable (Farrell, 2023).
     */
    public Login(String firstName, String surname, String username, String password, String cellphoneNumber)
    {
        this.firstName = firstName;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.cellphoneNumber = cellphoneNumber;
        loginUsername = "";
        loginPassword = "";
        lastLoginSuccessful = false;
    }

    /*
     * Setter methods update one field at a time. App.java / the main class uses
     * these methods when the user enters details through the console.
     */
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public void setSurname(String surname)
    {
        this.surname = surname;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setCellphoneNumber(String cellphoneNumber)
    {
        this.cellphoneNumber = cellphoneNumber;
    }

    
    /*
     *These getter methods will be used by the main application when it needs to
     * display names or use the registered cellphone number as a sender value.
     */
    
    public String getFirstName()
    {
        return firstName;
    }
    
    public String getSurname()
    {
        return surname;
    }
    
    public String getCellphoneNumber()
    {
        return cellphoneNumber;
    }
    
    
    /*
     * This method stores the username and password that the user enters when
     * attempting to log in.
     */
    public void setLoginDetails(String loginUsername, String loginPassword)
    {
        this.loginUsername = loginUsername;
        this.loginPassword = loginPassword;
    }

    /*
     * This method checks the username rule from Part 1. The username must
     * contain an underscore and must be no more than five characters long. It
     * uses String contains() and length() methods (Farrell, 2023).
     */
    public boolean checkUserName()
    {
        return username != null && username.contains("_") && username.length() <= 5;
    }

    /*
     * This method checks password complexity. A for loop reads each character
     * in the password. Character class methods check whether each character is
     * uppercase, numeric, or special (Farrell, 2023).
     */
    public boolean checkPasswordComplexity()
    {
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;
        int count;
        char currentCharacter;

        if(password == null || password.length() < 8)
        {
            return false;
        }

        for(count = 0; count < password.length(); count++)
        {
            currentCharacter = password.charAt(count);

            if(Character.isUpperCase(currentCharacter))
            {
                hasCapital = true;
            }
            else
            {
                if(Character.isDigit(currentCharacter))
                {
                    hasNumber = true;
                }
                else
                {
                    if(!Character.isLetterOrDigit(currentCharacter))
                    {
                        hasSpecial = true;
                    }
                }
            }
        }

        return hasCapital && hasNumber && hasSpecial;
    }

    /*
     * This method checks the South African international cellphone number
     * format. The number must begin with +27 and then contain 9 digits, for
     * example +27838968976. String.matches() applies the regular expression.
     */
    public boolean checkCellPhoneNumber()
    {
        return cellphoneNumber != null && cellphoneNumber.matches("\\+27[0-9]{9}");
    }

    /*
     * The following methods convert Boolean validation results into the required
     * output messages. This keeps each rule testable and easy to display.
     */
    public String getUserNameMessage()
    {
        if(checkUserName())
        {
            return USERNAME_SUCCESS;
        }
        return USERNAME_ERROR;
    }

    public String getPasswordMessage()
    {
        if(checkPasswordComplexity())
        {
            return PASSWORD_SUCCESS;
        }
        return PASSWORD_ERROR;
    }

    public String getCellPhoneMessage()
    {
        if(checkCellPhoneNumber())
        {
            return CELL_SUCCESS;
        }
        return CELL_ERROR;
    }

    /*
     * This method completes the registration decision structure. It returns the
     * first error message if the username, password, or cellphone number is
     * invalid. If all rules pass, it returns a successful registration message.
     */
    public String registerUser()
    {
        if(!checkUserName())
        {
            return USERNAME_ERROR;
        }

        if(!checkPasswordComplexity())
        {
            return PASSWORD_ERROR;
        }

        if(!checkCellPhoneNumber())
        {
            return CELL_ERROR;
        }

        return REGISTER_SUCCESS;
    }

    /*
     * This method compares the stored registration username and password with
     * the login details entered by the user. The result is stored so that
     * returnLoginStatus() can display the correct success or failure message.
     */
    public boolean loginUser()
    {
        lastLoginSuccessful = username != null && password != null &&
                username.equals(loginUsername) && password.equals(loginPassword);
        return lastLoginSuccessful;
    }

    /*
     * This overloaded loginUser() method is useful for unit tests because the
     * entered username and password can be passed directly into the method.
     */
    public boolean loginUser(String enteredUsername, String enteredPassword)
    {
        setLoginDetails(enteredUsername, enteredPassword);
        return loginUser();
    }

    /*
     * This method returns the correct login status message. The welcome message
     * includes the user's first name and surname.
     */
    public String returnLoginStatus()
    {
        if(lastLoginSuccessful)
        {
            return "Welcome " + firstName + ", " + surname + " it is great to see you again.";
        }
        return LOGIN_ERROR;
    }

    /*
     * This overloaded method lets tests set the login result directly before
     * requesting the login status message.
     */
    public String returnLoginStatus(boolean loginSuccessful)
    {
        lastLoginSuccessful = loginSuccessful;
        return returnLoginStatus();
    }
}
