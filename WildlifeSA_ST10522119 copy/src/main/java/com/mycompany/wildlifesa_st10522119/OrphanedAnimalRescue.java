
package com.mycompany.wildlifesa_st10522119;


public class OrphanedAnimalRescue extends RescueCase 
{
    
/*
 The assignment fixes foster care at R2,500. The private final fields store the
 positive whole-month age, feeding charge and foster-care Boolean choice.
 */
    
    private static final double FOSTER_SURCHARGE = 2500.00;
    private final int estimatedAgeMonths;
    private final double feedingCost;
    private final boolean fosterCareRequired;

/*
 The following code initialises common data with super, then validates whole positive age months
 and the once-per-case feeding charge. It stores the foster-care choice and checks
 the final calculated cost for overflow after all fields are assigned.
 */
    
    public OrphanedAnimalRescue(String id, String name, String species, String location,
            String ranger, int days, double dailyCost, int estimatedAgeMonths,
            double feedingCost, boolean fosterCareRequired) 
    {
        super(id, name, species, location, ranger, days, dailyCost);
        this.estimatedAgeMonths = Validation.positiveInteger(estimatedAgeMonths, "Estimated age");
        this.feedingCost = Validation.positiveAmount(feedingCost, "Feeding cost");
        this.fosterCareRequired = fosterCareRequired;
        Validation.positiveAmount(calculateTotalCost(), "Total rescue cost");
    }

/*
 This code returns the positive whole-month age used in the assumed priority rule.
*/
    
    public int getEstimatedAgeMonths() { return estimatedAgeMonths; }
    
/*
 This code returns the once-per-case feeding charge in rand.
 */
    
    public double getFeedingCost() { return feedingCost; }
    
/*
 This code returns the Boolean foster-care requirement used by the cost and priority rules.
 */
    
    public boolean isFosterCareRequired() { return fosterCareRequired; }

/*
 This code adds feeding once to the base care amount. Foster care adds R2,500 only when
 its Boolean field is true. It also returns the unformatted numeric total.
 */
    
    @Override
    public double calculateTotalCost() 
    {
        double total = calculateBaseCost() + feedingCost;
        if (fosterCareRequired) 
           {
            total += FOSTER_SURCHARGE;
           }
        return total;
    }

/*
 This code returns High if foster care is needed OR the animal is younger than six months.
 Returns Medium only when both conditions are false. Six months is the boundary
 of the documented assumption. (Farrell, 2023)
 */
    
    @Override
    public String determinePriority() 
    {
        if (fosterCareRequired || estimatedAgeMonths < 6) 
           {
            return "High";
           }
            return "Medium";
    }

/*
 This code returns the orphaned rescue label through an overridden superclass method.
 */
    
    @Override
    public String getRescueType() { return "Orphaned Animal Rescue"; }

/*
 This code returns the age, feeding charge and foster requirement as separate display lines.
 */
    
    @Override
    public String getTypeDetails() 
    {
        return "Estimated Age (Months): " + estimatedAgeMonths
                + "\nFeeding Cost: " + TextFormat.money(feedingCost)
                + "\nFoster Care Required: " + TextFormat.yesNo(fosterCareRequired);
    }
}

