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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


public class RescueManagerTest 
{

/*
 Creates a standard injured fixture with a caller-selected ID and R7,200 cost.
 */
    
    private RescueCase injured(String id) 
    {
        return new InjuredAnimalRescue(id, "Leo", "Lion", "Kruger", "Dlamini",
                5, 200, "Leg injury", 1200, true);
    }

/*
 This test checks object identity, not merely equivalent values.
 */
    
    @Test
    public void searchReturnsTheActualStoredObject() 
    {
        RescueManager manager = new RescueManager();
        RescueCase rescue = injured("R001");
        manager.addRescueCase(rescue);
        assertSame(rescue, manager.findById("R001"));
    }

/*
 This test checks trimming on creation and onfirms spaces and letter case do not create a different identifier.
 */
    
    @Test
    public void searchIgnoresSurroundingSpacesAndLetterCase() 
    {
        RescueManager manager = new RescueManager();
        RescueCase rescue = injured(" R001 ");
        manager.addRescueCase(rescue);
        assertEquals("R001", rescue.getRescueCaseId());
        assertSame(rescue, manager.findById(" r001 "));
    }

/*
 Checks an unknown ID, whitespace and null; each lookup must return null.
 This test distinguishes no match from a valid case and protects callers from treating
 missing input as a record.
 */
    
    @Test
    public void missingOrBlankSearchReturnsNull() 
    {
        RescueManager manager = new RescueManager();
        manager.addRescueCase(injured("R001"));
        assertNull(manager.findById("R999"));
        assertNull(manager.findById(" "));
        assertNull(manager.findById(null));
    }

/*
 This test attempts an orphan case with the injured case ID in different case and spacing.
 fail makes unexpected acceptance fail the test. The catch checks that count,
 original object identity and R7,200 total are unchanged after rejection.
 */
    
    @Test
    public void duplicatesAcrossDifferentTypesAreRejectedWithoutChangingStorage() 
    {
        RescueManager manager = new RescueManager();
        RescueCase original = injured("R001");
        manager.addRescueCase(original);
        try 
           {
            manager.addRescueCase(new OrphanedAnimalRescue(" r001 ", "Nala", "Elephant",
                    "Addo", "Mokoena", 7, 150, 4, 800, true));
            fail("A duplicate ID must be rejected.");
           } 
        catch (IllegalArgumentException ex) 
            {
            assertEquals(1, manager.getCaseCount());
            assertSame(original, manager.findById("R001"));
            assertEquals(7200.00, manager.getTotalEstimatedCost(), 0.000001);
            }
    }

/*
 This test checks both Completed and Pending updates, including the true return 
 value and the actual stored object status after each change.
 */
    
    @Test
    public void statusUpdateChangesTheStoredCase() 
    {
        RescueManager manager = new RescueManager();
        manager.addRescueCase(injured("R001"));
        assertTrue(manager.updateStatus("R001", RescueCase.COMPLETED));
        assertEquals(RescueCase.COMPLETED, manager.findById("R001").getRescueStatus());
        assertTrue(manager.updateStatus("R001", RescueCase.PENDING));
        assertEquals(RescueCase.PENDING, manager.findById("R001").getRescueStatus());
    }

/*
 This test confirms that missing ID must return false and leave the empty 
 collection at zero cases.
 */
    
    @Test
    public void updatingAMissingCaseReturnsFalseAndDoesNotAddACase() 
    {
        RescueManager manager = new RescueManager();
        assertFalse(manager.updateStatus("R999", RescueCase.IN_PROGRESS));
        assertEquals(0, manager.getCaseCount());
    }

/*
 This test creates three different rescue types costing R7,200, R4,350 and R13,000.
 Checks count three and total R24,550, then loops over IDs and required labels.
 Each label must occur three times so a field missing from one case cannot pass.
 Final checks verify report totals and specialised display-all information.
 */
    
    @Test
    public void mixedReportContainsAllCasesAllRequiredFieldsAndCorrectTotals() 
    {
        RescueManager manager = new RescueManager();
        manager.addRescueCase(injured("R001"));
        manager.addRescueCase(new OrphanedAnimalRescue("R002", "Nala", "Elephant", "Addo",
                "Mokoena", 7, 150, 4, 800, true));
        manager.addRescueCase(new EndangeredSpeciesRescue("R003", "Thandi", "Rhino", "Hluhluwe",
                "Naidoo", 10, 300, "Critically Endangered", 2000, true));
        assertEquals(3, manager.getCaseCount());
        assertEquals(24550.00, manager.getTotalEstimatedCost(), 0.000001);
        String report = manager.generateReport();
        for (String id : new String[]{"R001", "R002", "R003"}) 
           {
            assertTrue(report.contains("Rescue Case ID: " + id));
           }
        for (String label : new String[]{"Rescue Type:", "Species:", "Rescue Location:",
                "Assigned Ranger:", "Rescue Priority:", "Current Status:", "Total Rescue Cost:"}) 
           {
            assertEquals(3, countOccurrences(report, label));
           }
        assertTrue(report.contains("Total number of rescue cases: 3"));
        assertTrue(report.contains("Total estimated rescue cost: " + TextFormat.money(24550)));
        assertTrue(manager.displayAllCases().contains("Specialist Team Required: Yes"));
    }

/*
 This test checks the zero-case message, zero count and zero cost before any cases are added.
 */
    
    @Test
    public void emptyReportStillDisplaysZeroTotals() 
    {
        RescueManager manager = new RescueManager();
        assertEquals(0, manager.getCaseCount());
        assertEquals(0.0, manager.getTotalEstimatedCost(), 0.000001);
        assertTrue(manager.generateReport().contains("No rescue cases have been recorded."));
        assertTrue(manager.generateReport().contains("Total number of rescue cases: 0"));
        assertTrue(manager.generateReport().contains(TextFormat.money(0)));
    }

/*
 Try/catch confirms that null cannot enter storage. If the call returns normally,
 fail marks the test as failed.
 */
    
    @Test
    public void nullCaseIsRejected() 
    {
        try 
            {
            new RescueManager().addRescueCase(null);
            
            fail("Expected IllegalArgumentException for invalid input.");
            } 
        catch (IllegalArgumentException ex) 
        {}
    }
    
/*
 This test counts non-overlapping label matches. indexOf returns -1 when no match remains.
 Advancing by label.length avoids counting the same occurrence repeatedly.
 */
    private int countOccurrences(String text, String label) 
    {
        int count = 0;
        int position = text.indexOf(label);
        while (position >= 0) 
            {
            count++;
            position = text.indexOf(label, position + label.length());
            }
        return count;
    }

}
