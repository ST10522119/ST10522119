
package com.mycompany.prog6112_assignment1_st10522119;

/*
 This Inpatient class extends Patient, which makes it a more specialised subclass of
 the Patient superclass. The extends keyword creates an inheritance relationship
 in which the subclass receives the superclass fields as well as its methods and can
 add more specialised members. (Farrell, 2023)
*/


public class Inpatient extends Patient {
    
/*
 These fields are specific to inpatients and therefore are declared in the subclass
 rather than the general Patient class. They remain private so that they are accessed
 through methods, which supports information hiding. (Farrell, 2023)
*/
    
private String wardNumber;
private String bedNumber;

/*
 The Inpatient constructor will first call the Patient constructor with super(...)
 A subclass constructor uses super to pass required arguments to its superclass
 constructor and this call must be the first executable statement in the constructor.
 The constructor will then initiate the two fields that belong specifically to the 
 Inpatient subclass. (Farrell, 2023)
*/

public Inpatient(String patientId, String firstName, String lastName, int age,
                 String gender, String medicalCondition, PatientCategory category,
                 String wardNumber, String bedNumber)
{
    super(patientId, firstName, lastName, age, gender, medicalCondition, category);
    this.wardNumber = wardNumber;
    this.bedNumber = bedNumber;
}
/*
 The following getter and setter methods provide controlled access to the
 private fields which are specific to the subclass
*/

public String getWardNumber()
{
 return wardNumber;
}

public void setWardNumbber(String wardNumber)
{
 this.wardNumber = wardNumber;
}

public String getBedNumber()
{
 return bedNumber;
}

public void setBedNumber(String bedNumber)
{
 this.bedNumber = bedNumber;
}

/*
 This method overrides Patient.displayDetails(). Overriding occurs when 
 a subclass supplies a method with the same signature as one in its superclass.
 super.displayDetails() deliberately reuses the superclass version first, 
 after which the inpatient-specific ward and bed information is displayed. (Farrell, 2023)
*/

@Override
public void displayDetails()
{
 super.displayDetails();
    System.out.println("Ward Number       : " + wardNumber);
    System.out.println("Bed Number        : " + (bedNumber.equals("") ? "Not allocated" : bedNumber));
}

}
