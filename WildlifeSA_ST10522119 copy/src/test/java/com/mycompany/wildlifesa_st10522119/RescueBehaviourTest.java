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

/*
 These tests check the declared priority assumptions and the common interface.
 */

public class RescueBehaviourTest 
{
    
/*
 This test creates a fresh injured fixture with the chosen surgery Boolean. Other inputs
 remain fixed so priority tests isolate that one decision.
 */
    private RescueCase injured(boolean surgery) 
    {
        return new InjuredAnimalRescue("I01", "Leo", "Lion", "Kruger", "Dlamini",
                5, 200, "Leg injury", 1200, surgery);
    }

/*
 This test creates a fresh orphan fixture with controlled age and foster choices.
 */
    
    private RescueCase orphan(int age, boolean foster) 
    {
        return new OrphanedAnimalRescue("O01", "Nala", "Elephant", "Addo", "Mokoena",
                7, 150, age, 800, foster);
    }

/*
 This test creates a fresh endangered fixture with controlled classification and team choices.
 */
    
    private RescueCase endangered(String classification, boolean specialist) {
        return new EndangeredSpeciesRescue("E01", "Thandi", "Rhino", "Hluhluwe", "Naidoo",
                10, 300, classification, 2000, specialist);
    }

/*
 This test checks the surgery branch of the declared injured priority policy.
 */
    
    @Test
    public void surgeryMakesAnInjuredRescueCritical() 
    {
        assertEquals("Critical", injured(true).determinePriority());
    }

/*
 This test checks the alternative injured branch when surgery is false.
 */
    
    @Test
    public void injuryWithoutSurgeryHasHighPriority() 
    {
        assertEquals("High", injured(false).determinePriority());
    }

/*
 Age five with foster false isolates the below-six-month High condition.
 */
    
    @Test
    public void orphanYoungerThanSixMonthsHasHighPriorityWithoutFosterCare() 
    {
        assertEquals("High", orphan(5, false).determinePriority());
    }

/*
 This test checks which side of the age threshold includes six months.
 */
    
    @Test
    public void sixMonthsIsTheMediumPriorityBoundaryWithoutFosterCare() 
    {
        assertEquals("Medium", orphan(6, false).determinePriority());
    }

/*
 This test isolates the foster condition rather than the young-age condition.
 Age eight would be Medium alone, so foster true must override it to High.
 */
    
    @Test
    public void fosterCareMakesAnOlderOrphanHighPriority() 
    {
        assertEquals("High", orphan(8, true).determinePriority());
    }

/*
 This test checks classification without the specialist condition.
 It checks that the strongest classification alone produces Critical.
 */
    
    @Test
    public void criticallyEndangeredClassificationIsCriticalWithoutSpecialists() 
    {
        assertEquals("Critical", endangered("Critically Endangered", false).determinePriority());
    }

/*
 This test checks the specialist condition against a lower classification.
 */
    
    @Test
    public void specialistsMakeAVulnerableRescueCritical() 
    {
        assertEquals("Critical", endangered("Vulnerable", true).determinePriority());
    }

/*
 This test checks the middle classification branch without specialists.
 */
    
    @Test
    public void endangeredClassificationHasHighPriorityWithoutSpecialists() 
    {
        assertEquals("High", endangered("Endangered", false).determinePriority());
    }

/*
 This test checks the remaining classification branch without specialists.
 */
    
    @Test
    public void vulnerableClassificationHasMediumPriorityWithoutSpecialists() 
    {
        assertEquals("Medium", endangered("Vulnerable", false).determinePriority());
    }

/*
 An array holds one object of each subtype. The loop checks initial Pending,
 then calls the common interface methods and checks In Progress and Completed.
 This verifies inherited operations through the interface for every rescue type.
 */
    
    @Test
    public void allThreeTypesStartPendingThenStartAndCompleteThroughTheInterface() 
    {
        RescueCase[] cases = {injured(true), orphan(4, true), endangered("Endangered", true)};
        for (RescueCase rescue : cases) 
           {
            assertEquals(RescueCase.PENDING, rescue.getRescueStatus());
            RescueOperations operation = rescue;
            operation.startRescue();
            assertEquals(RescueCase.IN_PROGRESS, rescue.getRescueStatus());
            operation.completeRescue();
            assertEquals(RescueCase.COMPLETED, rescue.getRescueStatus());
           }
    }

/*
 Obtains a summary through the interface and checks each of its seven fields.
 The R7,200 expected amount confirms the injured subtype formula was selected.
 */
    
    @Test
    public void summaryContainsEveryRequiredFieldAndSpecialisedCost() 
    {
        RescueOperations operation = injured(true);
        String summary = operation.generateSummary();
        assertTrue(summary.contains("Rescue Case ID: I01"));
        assertTrue(summary.contains("Rescue Type: Injured Animal Rescue"));
        assertTrue(summary.contains("Species: Lion"));
        assertTrue(summary.contains("Assigned Ranger: Dlamini"));
        assertTrue(summary.contains("Rescue Priority: Critical"));
        assertTrue(summary.contains("Current Status: Pending"));
        assertTrue(summary.contains("Total Rescue Cost: " + TextFormat.money(7200)));
    }

/*
 Checks the extra common display fields and the three specialised fields for each subtype.  
 */
    
    @Test
    public void completeDetailsIncludeCommonAndTypeSpecificInformation() 
    {
        String injury = injured(true).getFullDetails();
        assertTrue(injury.contains("Animal Name: Leo"));
        assertTrue(injury.contains("Rescue Location: Kruger"));
        assertTrue(injury.contains("Number of Rescue Days: 5"));
        assertTrue(injury.contains("Daily Care Cost: " + TextFormat.money(200)));
        assertTrue(injury.contains("Injury Description: Leg injury"));
        assertTrue(injury.contains("Veterinary Treatment Cost: " + TextFormat.money(1200)));
        assertTrue(injury.contains("Surgery Required: Yes"));
        assertTrue(orphan(4, true).getFullDetails().contains("Estimated Age (Months): 4"));
        assertTrue(orphan(4, true).getFullDetails().contains("Feeding Cost: " + TextFormat.money(800)));
        assertTrue(orphan(4, true).getFullDetails().contains("Foster Care Required: Yes"));
        assertTrue(endangered("Endangered", true).getFullDetails().contains("Conservation Classification: Endangered"));
        assertTrue(endangered("Endangered", true).getFullDetails().contains("Security Cost: " + TextFormat.money(2000)));
        assertTrue(endangered("Endangered", true).getFullDetails().contains("Specialist Team Required: Yes"));
    }

/*
 This test checks the setter rather than accepting arbitrary status text.
 An unsupported status must throw IllegalArgumentException.
 */
    
    @Test
    public void unknownStatusIsRejected() 
    {
        try 
           {
            injured(false).setRescueStatus("Unknown");
            
            fail("Expected IllegalArgumentException for invalid input.");
           } 
        catch (IllegalArgumentException ex) 
            {
            
            }
    }
}