
package com.mycompany.wildlifesa_st10522119;

/*
 Shared checks use String methods, decisions and exceptions.
 These reusable methods are static, so callers use the class name without
 constructing a helper object. Parameters supply the value for each call.
 (Farrell, 2023)
 */

public class Validation 
{

 /*
  The following code rejects null before calling trim.
  It also rejects whitespace-only input, then returns the trimmed valid text.
  fieldName gives the caller a useful field-specific error message.
  */
    
    public static String requiredText(String value, String fieldName) 
    {
        if (value == null || value.trim().length() == 0) 
           {
            throw new IllegalArgumentException(fieldName + " must not be blank.");
           }
            return value.trim();
    }

/*
 The following code rejects zero and negative integers. Otherwise returns the unchanged valid value.
 This shared rule applies to rescue days and estimated whole-month age.
 */
    
    public static int positiveInteger(int value, String fieldName) 
    {
        if (value <= 0) 
           {
            throw new IllegalArgumentException(fieldName + " must be greater than zero.");
           }
        return value;
    }

/*
 This code rejects NaN, positive or negative infinity, zero and negative values.
 Checking nonfinite values also catches calculations that overflow double.
 A valid amount is returned unchanged for storage and arithmetic.
 */
    
    public static double positiveAmount(double value, String fieldName) 
    {
        if (Double.isNaN(value) || Double.isInfinite(value) || value <= 0) 
           {
            throw new IllegalArgumentException(fieldName + " must be a finite number greater than zero.");
           }
        return value;
    }

/*
 This code checks and trims text, converts it with Integer.parseInt, then checks positivity.
 Malformed text, decimals and values outside int range cause NumberFormatException.
 Valid integers that are not positive cause IllegalArgumentException. 
 The GUI catches these errors and retries. (Farrell, 2023)
 */
    
    public static int parsePositiveInteger(String text, String fieldName) 
    {
        return positiveInteger(Integer.parseInt(requiredText(text, fieldName)), fieldName);
    }

/*
 This code checks and trims text, parses a double, then rejects non-positive or non-finite
 results. Parsing errors propagate to the GUI input-recovery loop.
 */
    
    public static double parsePositiveAmount(String text, String fieldName) 
    {
        return positiveAmount(Double.parseDouble(requiredText(text, fieldName)), fieldName);
    }

/*
 This code normalises case-insensitive input to one of the three permitted labels.
 Each matching branch returns the canonical spelling. An unsupported label will throw
 an exception instead of storing inconsistent classification text.
 */
    
    public static String classification(String value) 
    {
        String text = requiredText(value, "Conservation classification");
        if (text.equalsIgnoreCase("Vulnerable")) 
           {
            return "Vulnerable";
           }
        if (text.equalsIgnoreCase("Endangered")) 
           {
            return "Endangered";
           }
        if (text.equalsIgnoreCase("Critically Endangered")) 
           {
            return "Critically Endangered";
           }
        throw new IllegalArgumentException("Choose Vulnerable, Endangered or Critically Endangered.");
    }
    
}
