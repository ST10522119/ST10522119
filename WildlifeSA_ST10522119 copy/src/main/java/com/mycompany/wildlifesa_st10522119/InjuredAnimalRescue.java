
package com.mycompany.wildlifesa_st10522119;

public class InjuredAnimalRescue extends RescueCase 
{

/*
 R5,000 is the fixed surgery surcharge from the assignment. The three private final
 fields store injury-specific creation data; no setters can later bypass validation.
 */
    
    private static final double SURGERY_SURCHARGE = 5000.00;
    private final String injuryDescription;
    private final double veterinaryTreatmentCost;
    private final boolean surgeryRequired;

/*
 This code calls the superclass constructor to initialise the eight common fields.
 It then validates the injury description and veterinary charge and stores surgery.
 Lastly, it checks the completed formula only after all subtype fields have been assigned.
 */
    
    public InjuredAnimalRescue(String id, String name, String species, String location,
            String ranger, int days, double dailyCost, String injuryDescription,
            double veterinaryTreatmentCost, boolean surgeryRequired) 
    {
        super(id, name, species, location, ranger, days, dailyCost);
        this.injuryDescription = Validation.requiredText(injuryDescription, "Injury description");
        this.veterinaryTreatmentCost = Validation.positiveAmount(veterinaryTreatmentCost,
                "Veterinary treatment cost");
        this.surgeryRequired = surgeryRequired;
        Validation.positiveAmount(calculateTotalCost(), "Total rescue cost");
    }

/*
 This code returns the recorded injury description through a public accessor.
 */
    
    public String getInjuryDescription() { return injuryDescription; }
    
/*
 This returns the once-per-case veterinary charge in Rand.
 */
    
    public double getVeterinaryTreatmentCost() { return veterinaryTreatmentCost; }
    
/*
 This returns the Boolean surgery choice used by both the cost and priority rules.
 */
    
    public boolean isSurgeryRequired() { return surgeryRequired; }

/*
 This code adds the once-per-case veterinary charge to days multiplied by daily care.
 The if branch adds exactly R5,000 when surgery is required; otherwise it adds
 no surgery charge. Then it returns the numeric total before formatting.
 */
    
    @Override
    public double calculateTotalCost() 
    {
        double total = calculateBaseCost() + veterinaryTreatmentCost;
        if (surgeryRequired) 
           {
            total += SURGERY_SURCHARGE;
           }
        return total;
    }

/*
 This code applies the declared demonstration policy: surgery means Critical; otherwise
 an injured rescue is High. 
 */
    
    @Override
    public String determinePriority() 
    {
        if (surgeryRequired) 
           {
            return "Critical";
           }
            return "High";
    }

/*
 This code overrides the abstract label method with the injured rescue type name.
 */
    
    @Override
    public String getRescueType() { return "Injured Animal Rescue"; }

/*
 This code builds the injury-specific display lines. Money and Boolean helpers provide
 two-decimal Rand amounts and readable Yes/No values.
 */
    
    @Override
    public String getTypeDetails() 
    {
        return "Injury Description: " + injuryDescription
                + "\nVeterinary Treatment Cost: " + TextFormat.money(veterinaryTreatmentCost)
                + "\nSurgery Required: " + TextFormat.yesNo(surgeryRequired);
    }
}

