package com.mycompany.mattressshop;


public class ProductCalculations implements IProductCalculations
{
    private final double productPrice;
    
    public ProductCalculations(double productPrice)
    {
        if (Double.isNaN(productPrice) || Double.isInfinite(productPrice)
                || productPrice < 0)
            {
                throw new IllegalArgumentException("Enter a valid price from zero upwards.");
            }
        this.productPrice = productPrice;
    }
    @Override
    public double GetVat() 
    {
        return productPrice * 0.15;
    }

    @Override
    public double GetCommission() {
        return productPrice * 0.05; 
    }

    @Override
    public double GetTotal() {
        return productPrice + GetVat() + GetCommission(); 
    }
    
}
