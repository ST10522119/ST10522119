
package com.mycompany.wildlifesa_st10522119;


public class EndangeredSpeciesRescue extends RescueCase 
{

/*
 The assignment fixes specialist assistance at R8,000. Private final fields retain
 the classification, security charge and team requirement for this case.
 */
    
    private static final double SPECIALIST_SURCHARGE = 8000.00;
    private final String conservationClassification;
    private final double securityCost;
    private final boolean specialistTeamRequired;

/*
 This code initialises common data through super. It normalises the accepted classification,
 validates the once-per-case security charge and stores the specialist choice.
 Then it checks the final cost after these subtype fields have been assigned.
 */
    
    public EndangeredSpeciesRescue(String id, String name, String species, String location,
            String ranger, int days, double dailyCost, String classification,
            double securityCost, boolean specialistTeamRequired) 
    {
        super(id, name, species, location, ranger, days, dailyCost);
        this.conservationClassification = Validation.classification(classification);
        this.securityCost = Validation.positiveAmount(securityCost, "Security cost");
        this.specialistTeamRequired = specialistTeamRequired;
        Validation.positiveAmount(calculateTotalCost(), "Total rescue cost");
    }

/*
 This code returns the normalised classification entered for this demonstration case.
 The application does not query a conservation database.
 */
    
    public String getConservationClassification() { return conservationClassification; }
    
/*
 This returns the once-per-case security charge in Rand.
 */
    public double getSecurityCost() { return securityCost; }
    
/*
 This returns whether the case needs a specialist team and its associated surcharge.
 */
    public boolean isSpecialistTeamRequired() { return specialistTeamRequired; }

/*
 This code adds security once to base care. A required specialist team adds exactly R8,000.
 No specialist surcharge is added when the Boolean choice is false.
 */
    
    @Override
    public double calculateTotalCost() 
    {
        double total = calculateBaseCost() + securityCost;
        if (specialistTeamRequired) 
           {
            total += SPECIALIST_SURCHARGE;
           }
        return total;
    }

/*
 This code checks the strongest condition first: specialists OR Critically Endangered
 produce Critical. Otherwise Endangered produces High and Vulnerable produces
 Medium. These are the documented application priority assumptions.
 */
    
    @Override
    public String determinePriority() 
    {
        if (specialistTeamRequired || conservationClassification.equals("Critically Endangered")) 
           {
            return "Critical";
           }
        if (conservationClassification.equals("Endangered")) 
           {
            return "High";
           }
            return "Medium";
    }

/*
 This returns the label identifying this subtype in summaries and reports.
 */
    
    @Override
    public String getRescueType() { return "Endangered Species Rescue"; }

/*
 This creates display lines for classification, security charge and specialist choice.
 */
    
    @Override
    public String getTypeDetails() 
    {
        return "Conservation Classification: " + conservationClassification
                + "\nSecurity Cost: " + TextFormat.money(securityCost)
                + "\nSpecialist Team Required: " + TextFormat.yesNo(specialistTeamRequired);
    }
}

