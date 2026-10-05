package com.mycompany.wildlifesa_st10522119;


import com.mycompany.wildlifesa_st10522119.EndangeredSpeciesRescue;
import com.mycompany.wildlifesa_st10522119.InjuredAnimalRescue;
import com.mycompany.wildlifesa_st10522119.OrphanedAnimalRescue;
import com.mycompany.wildlifesa_st10522119.RescueCase;
import com.mycompany.wildlifesa_st10522119.RescueManager;
import com.mycompany.wildlifesa_st10522119.RescueOperations;
import com.mycompany.wildlifesa_st10522119.TextFormat;
import com.mycompany.wildlifesa_st10522119.Validation;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


public class ValidationTest 
{

/*
 The following code runs a test which checks that surrounding spaces are removed
 from both integer and double input.
 */
    
    @Test
    public void validNumericInputIsParsedAndTrimmed() 
    {
        assertEquals(5, Validation.parsePositiveInteger(" 5 ", "Days"));
        assertEquals(125.50, Validation.parsePositiveAmount(" 125.50 ", "Cost"), 0.000001);
    }

/*
 This test loops over zero, negative, malformed, fractional, overflowing and blank inputs.
 Each must throw; fail flags accidental acceptance and the catch checks feedback.
 */
    
    @Test
    public void nonpositiveAndOutOfRangeIntegerInputIsRejected() 
    {
        for (String input : new String[]{"0", "-1", "abc", "2.5", "2147483648", " "}) 
        {
            try 
                {
                Validation.parsePositiveInteger(input, "Days");
                fail("Invalid integer accepted: " + input);
                } 
            catch (IllegalArgumentException ex) 
            {
                assertNotNull(ex.getMessage());
            }
        }
    }

/*
 This test loops over zero, negative, malformed, NaN, infinity, overflow and blank amounts.
 Each must be rejected with an explanatory exception message.
 */
    
    @Test
    public void invalidAndNonfiniteAmountInputIsRejected() 
    {
        for (String input : new String[]{"0", "-1", "abc", "NaN", "Infinity", "1e309", " "}) 
        {
            try 
            {
                Validation.parsePositiveAmount(input, "Cost");
                fail("Invalid amount accepted: " + input);
            } 
            catch (IllegalArgumentException ex) 
            {
                assertNotNull(ex.getMessage());
            }
        }
    }

/*
 This test creates a fresh five-field array on each iteration and blanks one field.
 Construction must reject each common text field independently with a blank error.
 */
    
    @Test
    public void eachRequiredCommonTextFieldIsValidatedAtConstruction() 
    {
        for (int blankField = 0; blankField < 5; blankField++) 
            {
            String[] values = {"R001", "Leo", "Lion", "Kruger", "Dlamini"};
            values[blankField] = " ";
            try {
                new InjuredAnimalRescue(values[0], values[1], values[2], values[3], values[4],
                        5, 200, "Leg injury", 1200, false);
                fail("Blank required field accepted: " + blankField);
                } 
            catch (IllegalArgumentException ex) 
                {
                assertTrue(ex.getMessage().contains("must not be blank"));
                }
            }
    }

/*
 This test checks that null text is rejected safely rather than causing a null dereference.
 */
    
    @Test
    public void nullRequiredTextIsRejected() 
    {
        try 
           {
            Validation.requiredText(null, "Species");
            
            fail("Expected IllegalArgumentException for invalid input.");
           } 
        catch (IllegalArgumentException ex) {}
    }

/*
 This test checks constructor validation of the common positive day-count rule.
 */
    
    @Test
    public void zeroRescueDaysAreRejected() 
    {
        try 
        {
            new InjuredAnimalRescue("I01", "Leo", "Lion", "Kruger", "Dlamini",
                    0, 200, "Leg injury", 1200, false);
            
            fail("Expected IllegalArgumentException for invalid input.");
        } 
        catch (IllegalArgumentException ex) {}
    }

/*
 This test checks that a negative daily care amount is rejected during construction.
 */
    
    @Test
    public void negativeDailyCareCostIsRejected() 
    {
        try 
            {
            new InjuredAnimalRescue("I01", "Leo", "Lion", "Kruger", "Dlamini",
                    5, -200, "Leg injury", 1200, false);
            
            fail("Expected IllegalArgumentException for invalid input.");
            } 
        catch (IllegalArgumentException ex) {}
    }

/*
 This test checks validation of the injured subtype required text field.
 */
    
    @Test
    public void blankInjuryDescriptionIsRejected() {
        try 
            {
            new InjuredAnimalRescue("I01", "Leo", "Lion", "Kruger", "Dlamini",
                    5, 200, " ", 1200, false);
            /* Reaching this line means the invalid input was accepted. */
            fail("Expected IllegalArgumentException for invalid input.");
            } 
        catch (IllegalArgumentException ex) {}
    }

/*
 This test checks the positive veterinary-cost rule at construction.
 */
    
    @Test
    public void zeroVeterinaryTreatmentCostIsRejected() 
    {
        try 
            {
            new InjuredAnimalRescue("I01", "Leo", "Lion", "Kruger", "Dlamini",
                    5, 200, "Leg injury", 0, false);
            /* Reaching this line means the invalid input was accepted. */
            fail("Expected IllegalArgumentException for invalid input.");
            } 
        catch (IllegalArgumentException ex) {}
    }

/*
 This test checks the orphan subtype positive whole-month age rule.
 */
    
    @Test
    public void zeroEstimatedAgeIsRejected() 
    {
        try 
        {
            new OrphanedAnimalRescue("O01", "Nala", "Elephant", "Addo", "Mokoena",
                    7, 150, 0, 800, true);
            
            fail("Expected IllegalArgumentException for invalid input.");
        } 
        catch (IllegalArgumentException ex) {}
    }

/*
 This test checks the orphan subtype positive feeding-cost rule.
 */
    
    @Test
    public void zeroFeedingCostIsRejected() 
    {
        try 
            {
            new OrphanedAnimalRescue("O01", "Nala", "Elephant", "Addo", "Mokoena",
                    7, 150, 4, 0, true);
            /* Reaching this line means the invalid input was accepted. */
            fail("Expected IllegalArgumentException for invalid input.");
            } 
        catch (IllegalArgumentException ex) {}
    }

/*
 This test checks that an unknown endangered classification cannot be stored.
 */
    
    @Test
    public void unsupportedClassificationIsRejected() 
    {
        try 
            {
            new EndangeredSpeciesRescue("E01", "Thandi", "Rhino", "Hluhluwe", "Naidoo",
                    10, 300, "Unknown", 2000, true);
            
            fail("Expected IllegalArgumentException for invalid input.");
            } 
        catch (IllegalArgumentException ex) {}
    }

/*
 This test checks the endangered subtype positive security-cost rule.
 */
    
    @Test
    public void zeroSecurityCostIsRejected() 
    {
        try 
            {
            new EndangeredSpeciesRescue("E01", "Thandi", "Rhino", "Hluhluwe", "Naidoo",
                    10, 300, "Endangered", 0, true);
            
            fail("Expected IllegalArgumentException for invalid input.");
            } 
        catch (IllegalArgumentException ex) {}
    }
}
