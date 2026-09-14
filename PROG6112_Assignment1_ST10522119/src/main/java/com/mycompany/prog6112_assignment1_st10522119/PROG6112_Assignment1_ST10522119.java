package com.mycompany.prog6112_assignment1_st10522119;

import java.util.Scanner;

public class PROG6112_Assignment1_ST10522119 {
    private static Scanner input = new Scanner(System.in);
    private static HospitalSystem hospital = new HospitalSystem();
    
    /*
     The main method is the entry point of the application. It is the place where execution
     of a Java application begins. (Burd, 2011)
    
     A do...while loop will keep the menu running until the user chooses 0. The switch 
     statement selects the operation that corresponds to the user's menu choice. (Farrell, 2023)
     */
    
    public static void main(String[] args) {
        int choice;
        
        do
        {
            displayMenu();
            choice = readInteger("Select an option");
            
            /*
             Each case will delegate a specific menu task to either a helper method in this class
             or a method in HospitalSystem. Breaking the program into methods keeps each task
             focused and reusable (Farell, 2023)
             */
            
            switch (choice) 
            {
                case 1:
                    registerPatient();
                    break;
                case 2:
                    searchPatient();
                    break;
                case 3:
                    updatePatient();
                    break;
                case 4:
                    deletePatient();
                    break;
                case 5:
                    hospital.displayAllPatients();
                    break;
                case 6:
                    allocateBed();
                    break;
                case 7:
                    releaseBed();
                    break;
                case 8:
                    hospital.displayWardLayout();
                    break;
                case 9:
                    hospital.displayAvailableBeds();
                    break;
                case 10:
                    hospital.displayOccupiedBeds();
                    break;
                case 11:
                    displayReports();
                    break;
                case 12:
                    hospital.displaySortedPatientsBySurname();
                    break;
                case 13:
                    hospital.displaySortedPatientsById();
                    break;
                case 0:
                    System.out.println("Exiiting Hoospital Patient Admission System.");
                    break;
                default:
                    System.out.println("Invalid option. Please select a menu option from 0 to 13");
            }
        } while (choice != 0);
    }
         
    /*
     The following set of code prints the menu options to the console. System.out.println() is used for
     console output. (Farrell, 2023)
    */
                    
    private static void displayMenu()
    {
        System.out.println("\n=====================================================");
        System.out.println("       MEDICARE HOSPITAL PATIENT ADMISSION SYSTEM");
        System.out.println("=====================================================");
        System.out.println("1.  Register patient");
        System.out.println("2.  Search and display patient");
        System.out.println("3.  Update patient details");
        System.out.println("4.  Delete patient");
        System.out.println("5.  Display all registered patients");
        System.out.println("6.  Allocate bed");
        System.out.println("7.  Release bed");
        System.out.println("8.  Display complete ward layout");
        System.out.println("9.  Display available beds");
        System.out.println("10. Display occupied beds");
        System.out.println("11. Display ward reports");
        System.out.println("12. Sort patients by surname");
        System.out.println("13. Sort patients by PatientID");
        System.out.println("0.  Exit");
        System.out.println("=======================================================");
    }
    
    /*
     The following set of code will collect the information that is needed to create a new
     patient. Before the rest of the details are read, the Patient ID is searched in order 
     to prevent a duplicate record. 
    */
    
    private static void registerPatient()
    {
        System.out.println("\n--- REGISTER PATIENT ---");
        String id = readNonEmpty("Patient ID: ");
        
        if (hospital.searchPatient(id) != null)
        {
            System.out.println("Registration failed: Patient ID already exists.");
            return;
        }
        
        String firstName = readNonEmpty("First name: ");
        String lastName = readNonEmpty("Last name: ");
        int age = readNonNegativeInteger("Age: ");
        String gender = readNonEmpty("Gender: ");
        String condition = readNonEmpty("Medical Condition: ");
        PatientCategory category = readCategory();
        
        Patient patient;
        if (category == PatientCategory.INPATIENT)
        {
            patient = new Inpatient(id, firstName, lastName, age, gender, condition,
            category, "Ward 1", "");
        }
        else
        {
        patient = new Patient(id, firstName, lastName, age, gender, condition, category);
        }
        
        if(hospital.registerPatient(patient))
        {
            System.out.println("Patient registered successfully.");
        }
        else
        {
            System.out.println("Patient could not be registered.");
        }
     }
    
    /*
     The following set of code will read a Patient ID, ask HospitalSystem to search for it and
     display the returned object when one if found. Since displayDetails() can be overridden,
     the correct Patient or Inpatient version is used for the runtime object (Farrell,2023)
    */
    
    private static void searchPatient()
    {
        System.out.println("\n--- SEARCH PATIENT---");
        String id = readNonEmpty("Enter Patient ID: ");
        Patient patient = hospital.searchPatient(id);
        
        if (patient == null)
        {
            System.out.println("Patient not found.");
        }
        else
        {
            System.out.println("\nPatient found:");
            patient.displayDetails();
        }
    }
    
    /*
     The following set of code will collect the replacement values for an existing patient and 
     pass them to HospitalSystem.updatePatient(). This separates user inpit from the data management
     logic and uses methods in order to divide a larger task into smaller operations . (Farrell, 2023)
    */
    
    private static void updatePatient()
    {
        System.out.println("\n--- UPDATE PATIENT ---");
        String id = readNonEmpty("Enter Patient ID to update: ");
        Patient patient = hospital.searchPatient(id);
        
        if(patient == null)
        {
            System.out.println("Patient not found.");
            return;
        }
        
        String firstName = readNonEmpty("New first name: ");
        String lastName = readNonEmpty("New last name: ");
        int age = readNonNegativeInteger("New age: ");
        String gender = readNonEmpty("New gender: ");
        String condition = readNonEmpty("New medical condition: ");
        PatientCategory category = readCategory();
        
        if (hospital.updatePatient(id, firstName, lastName, age, gender, condition, category))
        {
            System.out.println("Patient details updated successfully.");
        }
        else
        {
        System.out.println("Patient details could nnot be updated. ");        
        }
    }
    
    /*
     The following set of code reads the Patient ID to remove and delegates deletion
     and to HospitalSystem. The returned boolean will indicate whether the deletion 
     was successfull or not.
    */
    
    private static void deletePatient()
    {
        System.out.println("\n--- DELETE PATIENT ---");
        String id = readNonEmpty("Enter Patient ID to delete: ");
        
        if(hospital.deletePatient(id))
        {
            System.out.println("Patient deleted successfully.");
        }
        else
        {
            System.out.println("Patient not found.");
        }
    }
    
    /*
     The next set of code will ensure that the selected record is an Inpatient before
     asking for a bed number. The instanceof operator is used to test the object's
     runtime relationship to the Inpatient subclass. The requested bed is then converted
     to uppercase before being passed to the allocation logic. (Farrell, 2023)
    */
    
    private static void allocateBed()
    {
        System.out.println("\nn--- ALLOCATE BED ---");
        String id = readNonEmpty("Enter inpatient Patient ID: ");
        Patient patient = hospital.searchPatient(id);
        
        if(!(patient instanceof Inpatient))
        {
            System.out.println("Bed allocatiion failed: only registered inpatients may receive beds.");
            return;
        }
        
        hospital.displayAvailableBeds();
        String bed = readNonEmpty("Enter bed number (B01-B20): ").toUpperCase();
        
        if(hospital.allocateBed(id, bed))
        {
            System.out.println("Bed " + bed + "allocated successfully.");
           
        }
        else
        {
            System.out.println("Bed allocation failed. The bed man be invalid, occupied, or no bed may be available.");
        }
        
    }
    
    /*
     The next set of code reads an Inpatient ID and delegates the release operation to HospitalSystem.
     The returned boolean is used to display a clear success or failure message.
    */
    
    private static void releaseBed()
    {
        System.out.println("\n--- RELEASE BED ---");
        String id = readNonEmpty("Enter inpatient Patient ID: ");
        
        if(hospital.releaseBed(id))
        {
            System.out.println("Bed released successfully.");
        }
        else
        {
            System.out.println("Bed release failed. Check the patient and bed allocation.");
        }
    }
    
    /*
     The following set of code produces the combined report by calling the separate patient, bed and 
     ward-summary methods. By reusing methods, I can prevent the report option from duplicating logic
     that already exists elsewhere. (Farrell, 2023)
    */
    private static void displayReports()
    {
        System.out.println("\n--- PATIENT REPORT ---");
        hospital.displayAllPatients();
        hospital.displayAvailableBeds();
        hospital.displayOccupiedBeds();
        hospital.displayWardReport();
    }
    
    /*
     If valueOf() receives an invalid category, an IllegalArgumentException can occur. The try/catch 
     structure handles that condition and allows the user to enter another value. (Farrell, 2023)
    */
    
    private static PatientCategory readCategory()
    {
        while (true)
        {
            System.out.println("Patient categories:");
            for (PatientCategory category : PatientCategory.values())
            {
                System.out.println("- "+ category);
            }
            
            System.out.println("Enter category: ");
            String entry = input.nextLine().trim().toUpperCase();
            
            try
            {
                return PatientCategory.valueOf(entry);
            }
            catch(IllegalArgumentException exception)
            {
                System.out.println("Invalid category, Enter INPATIENT, OUTPATIENT, or EMERGENCY.");
            }
        }
    }
    
   /*
     The next set of code reads keyboard input as a String and converts it to an int using
     Integer.parseInt().. This converts a String into its integer equivalent. A NumberFormatException
     is handled with try/catch so that an invalid entry does not terminate the program.
    */
    
    private static int readInteger(String prompt)
    {
     while (true)
     {
        System.out.print(prompt);
        String entry = input.nextLine();
        try
        {
         return Integer.parseInt(entry);
        }
        catch(NumberFormatException exception)
        {
            System.out.println("Invalid number. Please enter a whole number.");
        }
     }
    }
    
   /*
     The next set of code reuses readInteger() and appplies a do...while validation loop so that a negative
     value is rejected. The method returns only after a value of zero or greater has been entered. I will 
     use a do...while loop as a repetition structure that executes the body before testing the condition.
     (Farrell, 2023)
    */
    
    private static int readNonNegativeInteger(String prompt)
    {
     int value;
     do
     {
      value = readInteger(prompt);
      if(value < 0)
      {
          System.out.println("Value may not be negative.");
      }
     }
      while (value < 0);
      {
       return value;
      }
    }
    
    /*
     The following set of code repeats keyboard input until the trimmed String is not empty. String
     comparison is performed with equals(), which is a method that compares the contents of String
     objects. (Farrell, 2023)
    */
    
    private static String readNonEmpty(String prompt)
    {
     String value;
     do
      {
       System.out.print(prompt);
       value = input.nextLine().trim();
       if(value.equals(""))
        {
            System.out.println("This value many not be empty.");
        }
      }
       while (value.equals(""));
       return value; 
    }
    
    }
        
    

