
package com.mycompany.wildlifesa_st10522119;

/*
 * Imports DecimalFormat for two-decimal rand output. (Burd, 2011)
 */
import java.text.DecimalFormat;

/*
 These reusable methods are static, so callers use the class name without
 constructing a helper object. Parameters supply the value for each call.
 */

public class TextFormat 
{

/*
 This code creates a DecimalFormat for each call and fixes both fraction limits at two.
 The R prefix identifies rand; the formatter uses the machine locale for grouping
 and decimal separators. Display rounding does not change the stored double.
 (Burd, 2011)
 */
    
    public static String money(double amount) 
    {
        DecimalFormat format = new DecimalFormat();
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        return "R " + format.format(amount);
    }

/*
 This code converts true to Yes and false to No for readable Boolean display fields.
 */
    
    public static String yesNo(boolean value) 
    {
        if (value) 
           {
            return "Yes";
           }
            return "No";
    }

/*
 This code walks through the text character by character, tracking the current column.
 Characters cause a new line before column 66, then are appended and counted.
 The result keeps dialog lines at most 65 characters wide. (Farrell, 2023)
 */
    public static String wrap(String text) 
    {
        StringBuilder result = new StringBuilder();
        int column = 0;
        for (int i = 0; i < text.length(); i++) 
            {
             char next = text.charAt(i);
             if (next == '\n') 
                {
                result.append(next);
                column = 0;
                } 
                else 
                {
                if (column == 65) 
                   {
                    result.append('\n');
                    column = 0;
                   }
                result.append(next);
                column++;
                }
        }
        return result.toString();
    }
    
}
