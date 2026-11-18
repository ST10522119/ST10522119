
package com.mycompany.mattressshop;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class ProductCalculationsTest {
    
   

    @org.junit.jupiter.api.Test
    public void testGetVat_WithValidData_ReturnsCorrectValue() 
    {
        ProductCalculations calculations = new ProductCalculations(3500.00);
         assertEquals(525, calculations.GetVat(), 0.001);
    }


    @org.junit.jupiter.api.Test
    public void testGetTotal_WithValidData_ReturnsCorrectValue()
    {
        ProductCalculations calculations = new ProductCalculations(3500.00);
        assertEquals(4200, calculations.GetTotal(), 0.001);
    }
    
}
