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

public class RescueCostTest 
{
    
/*
 JUnit compares calculated doubles with this small tolerance for representation
 rounding. It does not permit a different surcharge or cost formula.
 */
    
    private static final double DELTA = 0.000001;

/*
 This test checks the basic injured-animal formula without an optional charge.
 */
    
    @Test
    public void injuredWithoutSurgeryIncludesDailyAndVeterinaryCosts() 
    {
        RescueCase rescue = new InjuredAnimalRescue("I01", "Leo", "Lion", "Kruger",
                "Ranger Dlamini", 5, 200, "Leg injury", 1200, false);
        assertEquals(2200.00, rescue.calculateTotalCost(), DELTA);
    }

/*
 This test checks that the surgery branch adds the prescribed fixed amount.
 5 x R200 + R1,200 + R5,000 = R7,200 when surgery is true.
 */
    
    @Test
    public void injuredWithSurgeryAddsExactlyFiveThousand() 
    {
        RescueCase rescue = new InjuredAnimalRescue("I02", "Leo", "Lion", "Kruger",
                "Ranger Dlamini", 5, 200, "Leg injury", 1200, true);
        assertEquals(7200.00, rescue.calculateTotalCost(), DELTA);
    }

/*
 This checks base care plus a once-per-case feeding amount.
 7 x R150 + R800 = R1,850; feeding is added once and foster care is false.
 */
    
    @Test
    public void orphanWithoutFosterCareIncludesFeedingOnce() 
    {
        RescueCase rescue = new OrphanedAnimalRescue("O01", "Nala", "Elephant", "Addo",
                "Ranger Mokoena", 7, 150, 8, 800, false);
        assertEquals(1850.00, rescue.calculateTotalCost(), DELTA);
    }

/*
 This test checks the additional foster-care charge.
 Checks 7 x R150 + R800 + R2,500 = R4,350 when foster care is required.
 */
    
    @Test
    public void orphanWithFosterCareAddsExactlyTwoThousandFiveHundred() 
    {
        RescueCase rescue = new OrphanedAnimalRescue("O02", "Nala", "Elephant", "Addo",
                "Ranger Mokoena", 7, 150, 4, 800, true);
        assertEquals(4350.00, rescue.calculateTotalCost(), DELTA);
    }

/*
 This test Checks that security is included once with base care.
 Checks 10 x R300 + R2,000 = R5,000 without a specialist surcharge.
 */
    
    @Test
    public void endangeredWithoutSpecialistsIncludesSecurityOnce() 
    {
        RescueCase rescue = new EndangeredSpeciesRescue("E01", "Thandi", "Rhino", "Hluhluwe",
                "Ranger Naidoo", 10, 300, "Endangered", 2000, false);
        assertEquals(5000.00, rescue.calculateTotalCost(), DELTA);
    }

/*
 This test Checks the specialist-team surcharge.
 Checks 10 x R300 + R2,000 + R8,000 = R13,000 with specialists.
 */
    
    @Test
    public void endangeredWithSpecialistsAddsExactlyEightThousand() 
    {
        RescueCase rescue = new EndangeredSpeciesRescue("E02", "Thandi", "Rhino", "Hluhluwe",
                "Ranger Naidoo", 10, 300, "Critically Endangered", 2000, true);
        assertEquals(13000.00, rescue.calculateTotalCost(), DELTA);
    }

/*
 This test Checks arithmetic with fractional monetary values rather than whole rand only.
 Checks 3 x R125.50 + R100.25 = R476.75, preserving fractional rand amounts.
 */
    
    @Test
    public void fractionalRandAmountsAreIncludedInTheCalculation() 
    {
        RescueCase rescue = new InjuredAnimalRescue("I03", "Leo", "Lion", "Kruger",
                "Ranger Dlamini", 3, 125.50, "Leg injury", 100.25, false);
        assertEquals(476.75, rescue.calculateTotalCost(), DELTA);
    }
    
}
