
package com.mycompany.mattress_salesreport;


public class Product implements IProduct {
private final String[] productNames;
private final int[][] monthlySales;

    public Product (String[] productNames, int[] monthlySales)
    {
        if (productNames == null || monthlySales == null
                )
        {
            throw new IllegalArgumentException("Product names and sales must match.");
        }
    }
    
    @Override
    public int GetTotalProductsSold() 
    {
        int totalSales = 0;
        for(int product = 0; product < monthlySales.length; product++)
        {
            for (int month = 0; month < monthlySales[product].length; month++)
            {
                totalSales += monthlySales[product][month];
            }
        }
        return totalSales;
    }

    @Override
    public double GetAverageProductSold() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public String GetMostPopularProduct() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
}
