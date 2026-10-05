
package com.mycompany.wildlifesa_st10522119;

/*
 Imports ArrayList for the required growable in-memory collection.
 */

import java.util.ArrayList;

/*
 An ArrayList stores all rescue objects in memory (Burd, 2011).
 Loops and String comparisons provide search and Polymorphic calls
 use superclass references (Farrell, 2023)
 */

public class RescueManager 
{
 
/*
 One private growable list holds all three subtypes as RescueCase references.
 final prevents replacing the list reference; addRescueCase can still add elements.
 The list lasts only for this application session. (Burd, 2011)
 */
    private final ArrayList<RescueCase> rescueCases = new ArrayList<RescueCase>();

/*
 This code rejects null and searches all rescue types before allowing a new ID.
 It checks the proposed combined cost for overflow before changing the collection.
 Only a valid complete case is appended. Failures leave existing storage intact.
 */
    
    public void addRescueCase(RescueCase rescueCase) 
    {
        if (rescueCase == null) 
          {
            throw new IllegalArgumentException("A rescue case is required.");
          }
        if (findById(rescueCase.getRescueCaseId()) != null) 
          {
            throw new IllegalArgumentException("Rescue Case ID already exists.");
          }
        double newTotal = getTotalEstimatedCost() + rescueCase.calculateTotalCost();
        Validation.positiveAmount(newTotal, "Total estimated rescue cost");
        rescueCases.add(rescueCase);
    }

/*
 This code returns null immediately for a missing or blank search value.
 It trims a usable ID, then checks each stored case without regard to letter case.
 A match returns the actual stored object which allows status changes to persist
 in memory; reaching the end means no match. (Burd, 2011)
 */
    
    public RescueCase findById(String id) 
    {
        if (id == null || id.trim().length() == 0) 
          {
            return null;
          }
        String searchId = id.trim();
        for (RescueCase rescueCase : rescueCases) 
           {
            if (rescueCase.getRescueCaseId().equalsIgnoreCase(searchId)) 
               {
                return rescueCase;
               }
           }
        return null;
    }

/*
 This code finds the stored object first. A missing ID returns false without adding a case.
 For a match, the validated setter changes that object and this method returns true.
 */
    
    public boolean updateStatus(String id, String status) 
    {
        RescueCase rescueCase = findById(id);
        if (rescueCase == null) 
           {
            return false;
           }
        rescueCase.setRescueStatus(status);
        return true;
    }

/*
 This code returns the number of currently stored objects using ArrayList.size().
 */
    
    public int getCaseCount() { return rescueCases.size(); }

/*
 This code starts at zero and accumulates each case total through a superclass reference.
 Dynamic binding chooses each subclass formula, so a mixed collection totals
 correctly. An empty collection returns zero. (Farrell, 2023)
 */
    
    public double getTotalEstimatedCost() 
    {
        double total = 0;
        for (RescueCase rescueCase : rescueCases) 
           {
            total += rescueCase.calculateTotalCost();
           }
        return total;
    }

/*
 This code returns a friendly message for an empty collection. Otherwise StringBuilder
 collects every full-details view, separated by blank lines, then becomes a String.
 No collection contents are changed by this read-only operation.
 */
    
    public String displayAllCases() 
    {
        if (rescueCases.size() == 0) 
           {
            return "No rescue cases have been recorded.";
           }
        StringBuilder result = new StringBuilder("ALL RESCUE CASES\n\n");
        for (RescueCase rescueCase : rescueCases) 
           {
            result.append(rescueCase.getFullDetails()).append("\n\n");
           }
        return result.toString();
    }

/*
 This code builds a heading, every required case-summary field and each rescue location.
 An empty report still includes the absence message and zero totals.
 The final lines state the case count and sum of all specialised rescue costs.
 */
    
    public String generateReport() 
    {
        StringBuilder report = new StringBuilder("WILDLIFE SA RESCUE REPORT\n\n");
        if (rescueCases.size() == 0) 
           {
            report.append("No rescue cases have been recorded.\n\n");
           }
        for (RescueCase rescueCase : rescueCases) 
           {
            report.append(rescueCase.generateSummary()).append("\nRescue Location: ")
                    .append(rescueCase.getRescueLocation()).append("\n\n");
           }
        report.append("Total number of rescue cases: ").append(rescueCases.size())
                .append("\nTotal estimated rescue cost: ")
                .append(TextFormat.money(getTotalEstimatedCost()));
        return report.toString();
    }
}
