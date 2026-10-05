
package com.mycompany.wildlifesa_st10522119;

/*
 Private fields provide encapsulation; the abstract class holds common data.
 (Farrell, 2023) (Burd, 2011)
 */

public abstract class RescueCase implements RescueOperations 
{

/*
 Shared status constants prevent different spellings across the GUI and model.
 static shares one value per class; final prevents reassignment.
 */
    
    public static final String PENDING = "Pending";
    public static final String IN_PROGRESS = "In Progress";
    public static final String COMPLETED = "Completed";

/*
 Private fields hide shared state. final fixes creation data after construction.
 Only rescueStatus is mutable, through its validated setter.
 */
    
    private final String rescueCaseId;
    private final String animalName;
    private final String species;
    private final String rescueLocation;
    private final String assignedRanger;
    private final int numberOfRescueDays;
    private final double dailyCareCost;
    private String rescueStatus;

/*
 The following code initialises the shared information inherited by all three rescue types.
 Each text field is trimmed and checked. Days and costs must be positive.
 New cases start Pending. Checking the base cost also rejects numeric overflow.
 Only the base calculation is used, subclass fields are not initialised yet.
 (Farrell, 2023)
 */
    
    public RescueCase(String rescueCaseId, String animalName, String species,
            String rescueLocation, String assignedRanger, int numberOfRescueDays,
            double dailyCareCost) 
    {
        this.rescueCaseId = Validation.requiredText(rescueCaseId, "Rescue Case ID");
        this.animalName = Validation.requiredText(animalName, "Animal name");
        this.species = Validation.requiredText(species, "Species");
        this.rescueLocation = Validation.requiredText(rescueLocation, "Rescue location");
        this.assignedRanger = Validation.requiredText(assignedRanger, "Assigned ranger");
        this.numberOfRescueDays = Validation.positiveInteger(numberOfRescueDays, "Rescue days");
        this.dailyCareCost = Validation.positiveAmount(dailyCareCost, "Daily care cost");
        this.rescueStatus = PENDING;
        Validation.positiveAmount(calculateBaseCost(), "Base rescue cost");
    }

/*
 The following code returns the permanent, trimmed case ID used for searching and 
 duplicate checks. There is no ID setter, so a saved case cannot be renamed accidentally.
 */
    
    public String getRescueCaseId() { return rescueCaseId; }
    
/*
 This returns the animal name without allowing direct access to the private field.
 */
    
    public String getAnimalName() { return animalName; }
    
/*
 This returns the recorded species for summaries and detailed displays.
 */
    
    public String getSpecies() { return species; }
    
/*
 Returns the location included in the complete rescue report.
 */
    
    public String getRescueLocation() { return rescueLocation; }
    
/*
 Returns the ranger responsible for this rescue case.
 */
    
    public String getAssignedRanger() { return assignedRanger; }
    
/*
 Returns the validated positive day count used in the base-care calculation.
 */
    
    public int getNumberOfRescueDays() { return numberOfRescueDays; }
    
/*
 Returns the positive daily care amount in rand before display formatting.
 */
    
    public double getDailyCareCost() { return dailyCareCost; }
    
/*
 Returns the current state; staff actions change it through the validated setter.
 */
    
    public String getRescueStatus() { return rescueStatus; }

/*
 Ths code accepts only Pending, In Progress or Completed after checking for blank text.
 The AND expression is true only when the value matches none of these states.
 Invalid values throw an exception before the existing status can be changed.
 (Farrell, 2023)
 */
    public void setRescueStatus(String value) 
    {
        String text = Validation.requiredText(value, "Rescue status");
        if (!text.equals(PENDING) && !text.equals(IN_PROGRESS) && !text.equals(COMPLETED)) 
           {
            throw new IllegalArgumentException("Choose Pending, In Progress or Completed.");
           }
        rescueStatus = text;
    }

/*
 This code multiplies the rescue days by the daily care cost to obtain base care in Rand.
 Protected access lets subclasses reuse this calculation without exposing it
 as a public staff operation. (Farrell, 2023)
 */
    protected double calculateBaseCost() 
    {
        return numberOfRescueDays * dailyCareCost;
    }

/*
 This code requires each concrete subclass to supply its own complete cost formula.
 An abstract declaration has no body; dynamic binding selects the subtype method.
 */
    
    public abstract double calculateTotalCost();
    
/*
 Requires each subclass to return the priority under its documented assumptions.
 */
    
    public abstract String determinePriority();
    
/*
 Requires each subclass to supply the rescue-type label used in outputs.
 */
    
    public abstract String getRescueType();
    
/*
 Requires each subclass to describe its extra fields for the full-details view.
 */
    
    public abstract String getTypeDetails();

/*
 This code implements the shared interface operation by setting the state to In Progress.
 The same inherited behaviour works for every concrete rescue type.
 */
    
    @Override
    public void startRescue() 
    {
        setRescueStatus(IN_PROGRESS);
    }

/*
 This code implements the shared interface operation by setting the state to Completed.
 */
    @Override
    public void completeRescue() 
    {
        setRescueStatus(COMPLETED);
    }

/* 
 Dynamic binding selects each subclass's type, priority and cost methods.
 This code builds the seven fields required as one multiline String.
 It calls to type, priority and cost and uses the actual subclass implementations.
 Newline escapes separate labels; the money helper formats the calculated amount.
 (Farrell, 2023)   
 */
    
    @Override
    public String generateSummary() 
    {
        return "Rescue Case ID: " + rescueCaseId
                + "\nRescue Type: " + getRescueType()
                + "\nSpecies: " + species
                + "\nAssigned Ranger: " + assignedRanger
                + "\nRescue Priority: " + determinePriority()
                + "\nCurrent Status: " + rescueStatus
                + "\nTotal Rescue Cost: " + TextFormat.money(calculateTotalCost());
    }

/*
 This code extends the summary with the remaining common fields and the subtype details.
 This is the full output used after creation, during search and for display-all.
 */
    public String getFullDetails() 
    {
        return generateSummary()
                + "\nAnimal Name: " + animalName
                + "\nRescue Location: " + rescueLocation
                + "\nNumber of Rescue Days: " + numberOfRescueDays
                + "\nDaily Care Cost: " + TextFormat.money(dailyCareCost)
                + "\n" + getTypeDetails();
    }
}

