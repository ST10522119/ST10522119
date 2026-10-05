
package com.mycompany.wildlifesa_st10522119;

/*
 This interface declares a common contract for every rescue type.
 */

public interface RescueOperations 
{
    
/*
 This code declares the public operation to set a rescue to In Progress. 
 RescueCase provides the implementation inherited by all three concrete types.
 */
    
   void startRescue();
 /*
  Declares the public operation to set a rescue to Completed.
  */
   void completeRescue();
 /*
  Declares the public operation returning the seven required fields as a String.
  */
    String generateSummary();
}
