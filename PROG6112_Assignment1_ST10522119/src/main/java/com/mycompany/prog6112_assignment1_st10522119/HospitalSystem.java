
package com.mycompany.prog6112_assignment1_st10522119;

import java.util.ArrayList;

/*
 This Class contains the main program logic for storing patients, managing beds, producing reports
 and storing patient records. It uses an ArrayList for a changeable collection of patients,
 a two-dimentional array for the ward, nested loops to process rows and columns, inheritance,
 information hiding as well as bubble sorting. (Farrell, 2023)
*/
public class HospitalSystem 
{
 /*
   Named constants describe the fixed ward dimensions. The ward contains four rows, 
   five columns and therefore twenty beds. Using constants keeps these fixed values 
   in one place rather than repeating unexplained numbers throughout the program (Farrell, 2023).
  */

 private static final int ROWS = 4;
 private static final int COLUMNS = 5;
 private static final int TOTAL_BEDS = 20;

 /*
   patients is an ArrayList whose generic type is Patient. Specifying the element
   type with generics allows the collection to hold the intended type of object.(Burd, 2011)
   The beds field is a two-dimensional String array where each row/column position represents
   one bed. A two-dimensional array uses a row subscript and a column subscript to identify an element.
   (Farrell, 2023) patientCount records how many Patient objects are currently registered.
  */
 
    private ArrayList<Patient> patients;
    private String[][] beds;
    private int patientCount;

 /*
  The constructor establishes a new HospitalSystem by creating an empty
  ArrayList, creating the 4 x 5 bed array and setting the patient count to
  zero. Constructors establish objects and can perform the initialisation needed when 
  an object is created. (Farrell, 2023)
 */
    public HospitalSystem()
    {
        patients = new ArrayList<Patient>();
        beds = new String[ROWS][COLUMNS];
        patientCount = 0;
    }
    
  /*
   The following set of code registers a patient only when the supplied object and Patient ID are
   valid and when no patient with the same ID already exists. If validation
   succeeds, add() appends the object to the ArrayList and the counter is increased.
   add() is the operation used to place a new value into an ArrayList. (Burd, 2011)
   */
    
    public boolean registerPatient(Patient patient)
    {
        if (patient == null || patient.getPatientId() == null ||
                patient.getPatientId().equals("") ||
                searchPatient(patient.getPatientId()) != null) {
            return false;
        }

        patients.add(patient);
        patientCount++;
        return true;
    }

   /*
    The next set of code searches for a patient by Patient ID. The enhanced for loop 
    processes each Patient stored in the ArrayList. The enhanced for loop is a 
    convenient way to work through each value in a collection. (Burd, 2011)
    equalsIgnoreCase() is used so that different letter case does not make
    an otherwise matching String unequal. Using this method compares
    String contents while ignoring case. (Farrell, 2023)
    */
    
    public Patient searchPatient(String patientId)
    {
        if (patientId == null) {
            return null;
        }

        for (Patient patient : patients) {
            if (patient.getPatientId().equalsIgnoreCase(patientId)) {
                return patient;
            }
        }
        return null;
    }

  /*
    The next set of code finds the numeric position of a patient in the ArrayList.
    Returning -1 indicates that no matching patient was found.
  */
    
    private int findPatientIndex(String patientId)
    {
        int index = 0;
        for (Patient patient : patients) {
            if (patient.getPatientId().equalsIgnoreCase(patientId)) 
            {
                return index;
            }
            index++;
        }
        return -1;
    }

  /*
    The following code updates a patient's details after validating the incoming values.
    The method also handles a change of category between a general Patient and an
    Inpatient object. When the required runtime type changes, the old ArrayList
    element is removed and a correctly typed replacement object is inserted at
    the same position. (Burd, 2011)
    If the runtime type does not need to change, the existing object is updated
    through setter methods, preserving information hiding (Farrell, 2023).
  */
    
    public boolean updatePatient(String patientId, String firstName, String lastName,
                                 int age, String gender, String medicalCondition,
                                 PatientCategory newCategory) 
    {
        if (patientId == null || firstName == null || lastName == null ||
                gender == null || medicalCondition == null || newCategory == null ||
                firstName.equals("") || lastName.equals("") || gender.equals("") ||
                medicalCondition.equals("") || age < 0) 
        {
            return false;
        }

        int index = findPatientIndex(patientId);
        if (index == -1) 
        {
            return false;
        }

        Patient existing = patients.get(index);
    
     /*
       If an Inpatient becomes an Outpatient or Emergency patient, any bed
       must first be released because the replacement object will be a base
       Patient. The instanceof test determines whether the current object is
       an instance of the Inpatient subclass.
     */
     
        if (existing instanceof Inpatient && newCategory != PatientCategory.INPATIENT) 
        {
            Inpatient oldInpatient = (Inpatient) existing;
            if (!oldInpatient.getBedNumber().equals("")) 
            {
                releaseBed(patientId);
            }
            Patient replacement = new Patient(patientId, firstName, lastName, age,
                    gender, medicalCondition, newCategory);
            patients.remove(index);
            patients.add(index, replacement);
        }
        
     /*
       If a general Patient becomes an Inpatient, a new Inpatient object is
       constructed so the record can also hold ward and bed information.
      */ 
        
        else if (!(existing instanceof Inpatient) && newCategory == PatientCategory.INPATIENT) 
        {
            Inpatient replacement = new Inpatient(patientId, firstName, lastName, age,
                    gender, medicalCondition, PatientCategory.INPATIENT, "Ward 1", "");
            patients.remove(index);
            patients.add(index, replacement);
        }

     /*
       If the object's class does not change, its private data is updated
       through setter methods rather than by direct field access.
     */ 
        
        else 
        {
            existing.setFirstName(firstName);
            existing.setLastName(lastName);
            existing.setAge(age);
            existing.setGender(gender);
            existing.setMedicalCondition(medicalCondition);
            existing.setCategory(newCategory);
        }

        return true;
    }
     
   /*
     The next set of code deletes a patient by locating the ArrayList index 
     and removing that element. An allocated bed is released first when the 
     deleted record is an Inpatient. (Burd, 2011)
     */
    
    public boolean deletePatient(String patientId)
    {
        int index = findPatientIndex(patientId);
        if (index == -1)
        {
            return false;
        }

        Patient patient = patients.get(index);
        if (patient instanceof Inpatient)
        {
            Inpatient inpatient = (Inpatient) patient;
            if (!inpatient.getBedNumber().equals(""))
            {
                releaseBed(patientId);
            }
        }

        patients.remove(index);
        patientCount--;
        return true;
    }

  /*
    This set of code displays all registered patients. The enhanced for loop visits
    every Patient object and calls displayDetails(). Because Inpatient overrides
    that method, an Inpatient object automatically displays its specialised version.
    This combines collection iteration with inherited/overridden behaviour 
    (Farrell, 2023).
   */
    
    public void displayAllPatients() 
    {
        if (patientCount == 0)
        {
            System.out.println("No patients are registered.");
            return;
        }
        
  System.out.println("\n================ REGISTERED PATIENTS ================");
        for (Patient patient : patients)
        {
            patient.displayDetails();
            System.out.println("-----------------------------------------------------");
        }
    }

  /*
    This set of code allocates a requested bed to a registered Inpatient.
    The preliminary conditions ensure that only an Inpatient without an existing
    bed can be allocated and that the ward is not already full.
   
    The nested loops process the two-dimensional beds array row by row and
    column by column. A two-dimensional array uses two subscripts and nested
    loops are appropriate when working across rows and columns. (Farrell, 2023)
    A null array element represents an available bed.
  */
    
    public boolean allocateBed(String patientId, String requestedBed)
    {
        Patient patient = searchPatient(patientId);
        if (!(patient instanceof Inpatient))
        {
            return false;
        }

        Inpatient inpatient = (Inpatient) patient;
        if (!inpatient.getBedNumber().equals(""))
        {
            return false;
        }

        if (getOccupiedBedCount() >= TOTAL_BEDS)
        {
            return false;
        }

        for (int row = 0; row < beds.length; row++)
            {
            for (int column = 0; column < beds[row].length; column++)
                {
                    if (getBedCode(row, column).equalsIgnoreCase(requestedBed))
                    {
                    if (beds[row][column] != null)
                    {
                        return false;
                    }
                    beds[row][column] = patientId;
                    inpatient.setBedNumber(getBedCode(row, column));
                    return true;
                }
            }
        }

        return false;
    }
    
   /*
     This set of code releases the bed assigned to an Inpatient. The allocated
     bed code is located by traversing the same two-dimensional array. 
     Setting the array element back to null makes the position available again, 
     and the Inpatient object's bed field is cleared.
    */
    
    public boolean releaseBed(String patientId)
    {
        Patient patient = searchPatient(patientId);
        if (!(patient instanceof Inpatient))
        {
            return false;
        }

        Inpatient inpatient = (Inpatient) patient;
        if (inpatient.getBedNumber().equals(""))
        {
            return false;
        }

        String allocatedBed = inpatient.getBedNumber();
        for (int row = 0; row < beds.length; row++)
        {
            for (int column = 0; column < beds[row].length; column++)
            {
                if (getBedCode(row, column).equalsIgnoreCase(allocatedBed))
                {
                    beds[row][column] = null;
                    inpatient.setBedNumber("");
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * Checks whether a specific bed contains a Patient ID. A non-null value in
     * the two-dimensional array means that the bed is occupied. The nested-loop
     * structure is consistent with Farrell's (2023) treatment of processing
     * two-dimensional arrays.
     */
    public boolean isBedOccupied(String bedCode) {
        for (int row = 0; row < beds.length; row++) {
            for (int column = 0; column < beds[row].length; column++) {
                if (getBedCode(row, column).equalsIgnoreCase(bedCode)) {
                    return beds[row][column] != null;
                }
            }
        }
        return false;
    }

   /*
     This set of code displays the complete ward as a 4 x 5 layout.
     The outer loop controls rows and the inner loop controls columns,
     which demonstrates the nested loop pattern used with multidimensional arrays.
     (Farrell, 2023)
     */
    
    public void displayWardLayout() 
    {
        System.out.println("\n==================== WARD LAYOUT ====================");
        for (int row = 0; row < beds.length; row++)
        {
            for (int column = 0; column < beds[row].length; column++)
            {
                String bedCode = getBedCode(row, column);
                if (beds[row][column] == null)
                {
                    System.out.print("[" + bedCode + ": FREE] ");
                } else
                {
                    System.out.print("[" + bedCode + ": " + beds[row][column] + "] ");
                }
            }
            System.out.println();
        }
    }

    /*
      This set of code displays only unoccupied beds. A boolean flag records 
      whether at least one available bed was found so that the method can display
      "None" when every array element is occupied. The nested loops inspect
      every row and column of the two-dimensional array. (Farrell, 2023)
    */
    
    public void displayAvailableBeds()
    {
        System.out.println("\nAvailable beds:");
        boolean found = false;
        for (int row = 0; row < beds.length; row++)
        {
            for (int column = 0; column < beds[row].length; column++)
            {
                if (beds[row][column] == null)
                {
                    System.out.print(getBedCode(row, column) + " ");
                    found = true;
                }
            }
        }
        
        if (!found)
        {
            System.out.print("None");
        }
        System.out.println();
    }

    /*
     This set of code displays only occupied beds. For each non-null array element,
     the method prints the bed code and the Patient ID stored in that position. 
     This is another application of nested loops over a two-dimensional array.
     (Farrell, 2023)
    */
    
    public void displayOccupiedBeds()
    {
        System.out.println("\nOccupied beds:");
        boolean found = false;
        for (int row = 0; row < beds.length; row++)
        {
            for (int column = 0; column < beds[row].length; column++)
            {
                if (beds[row][column] != null)
                {
                    System.out.println(getBedCode(row, column) + " - Patient ID: " +
                            beds[row][column]);
                    found = true;
                }
            }
        }
        
        if (!found)
        {
            System.out.println("None");
        }
    }

    /*
      These small methods return report values. Methods are used to separate
      reusable tasks from the menu code, which makes the program easier to
      organise and avoids repeating the same calculations. (Farrell, 2023)
    */
    
    public int getPatientCount()
    {
        return patientCount;
    }

    public int getOccupiedBedCount()
    {
        int count = 0;
        for (int row = 0; row < beds.length; row++)
        {
            for (int column = 0; column < beds[row].length; column++)
            {
                if (beds[row][column] != null)
                {
                    count++;
                }
            }
        }
        return count;
    }

    public int getAvailableBedCount()
    {
        return TOTAL_BEDS - getOccupiedBedCount();
    }

    public double getOccupancyPercentage()
    {
        return (getOccupiedBedCount() * 100.0) / TOTAL_BEDS;
    }

    /*
      This set of code combines the report methods above to present a concise
      ward summary. The occupancy percentage is calculated from occupied beds 
      divided by the fixed total number of beds.
     */
    
    public void displayWardReport()
    {
        System.out.println("\n==================== WARD REPORT ====================");
        System.out.println("Total registered patients : " + getPatientCount());
        System.out.println("Total occupied beds       : " + getOccupiedBedCount());
        System.out.println("Total available beds      : " + getAvailableBedCount());
        System.out.println("Ward occupancy percentage : " + getOccupancyPercentage() + "%");
        System.out.println("=====================================================");
    }

    /*
     This set of code returns a Patient array sorted alphabetically by surname. 
     The program first copies the ArrayList into an array and then applies 
     bubble sort. Bubble sort repeatedly compares adjacent elements and swaps
     them when they are out of order. (Farrell, 2023) The temporary Patient
     variable allows complete Patient object references to be exchanged.
     
     compareTo() returns an integer that indicates the ordering relationship
     between two Strings; converting both surnames to uppercase gives the
     comparison a consistent case (Farrell, 2023).
    */
    
    public Patient[] getPatientsSortedBySurname()
    {
        Patient[] sorted = copyPatientsToArray();
        int highSubscript = sorted.length - 1;

        for (int a = 0; a < highSubscript; a++)
        {
            for (int b = 0; b < highSubscript - a; b++)
            {
                String surname1 = sorted[b].getLastName();
                String surname2 = sorted[b + 1].getLastName();

                if (surname1.toUpperCase().compareTo(surname2.toUpperCase()) > 0)
                {
                    Patient temp = sorted[b];
                    sorted[b] = sorted[b + 1];
                    sorted[b + 1] = temp;
                }
            }
        }
        return sorted;
    }

    /*
     * This method uses the same bubble-sort process but compares Patient IDs
     * instead of surnames. The repeated adjacent comparison and swap follows
     * the bubble-sort algorithm. (Farrell, 2023)
     */
    
    public Patient[] getPatientsSortedById()
    {
        Patient[] sorted = copyPatientsToArray();
        int highSubscript = sorted.length - 1;

        for (int a = 0; a < highSubscript; a++)
        {
            for (int b = 0; b < highSubscript - a; b++)
            {
                if (sorted[b].getPatientId().toUpperCase().compareTo(
                        sorted[b + 1].getPatientId().toUpperCase()) > 0)
                {
                    Patient temp = sorted[b];
                    sorted[b] = sorted[b + 1];
                    sorted[b + 1] = temp;
                }
            }
        }
        return sorted;
    }

   /*
     The following two methods obtain a sorted array and pass it to one shared display
     method.
   */
    public void displaySortedPatientsBySurname() 
    {
        displayPatientArray(getPatientsSortedBySurname(), "PATIENTS SORTED BY SURNAME");
    }

    public void displaySortedPatientsById()
    {
        displayPatientArray(getPatientsSortedById(), "PATIENTS SORTED BY PATIENT ID");
    }

   /*
     This set of code copies each Patient reference from the ArrayList into 
     fixed-size Patient array. The enhanced for loop processes each collection element,
     and the resulting array is then suitable for the bubble-sort technique
     (Farrell, 2023).
    */
    
    private Patient[] copyPatientsToArray()
    {
        Patient[] copy = new Patient[patientCount];
        int index = 0;
        for (Patient patient : patients)
        {
            copy[index] = patient;
            index++;
        }
        return copy;
    }

   /*
     This set of code displays every Patient contained in an array supplied as a parameter.
    */
    
    private void displayPatientArray(Patient[] array, String heading)
    {
        System.out.println("\n================ " + heading + " ================");
        if (array.length == 0)
        {
            System.out.println("No patients are registered.");
            return;
        }

        for (Patient patient : array)
        {
            patient.displayDetails();
            System.out.println("-----------------------------------------------------");
        }
    }

   /*
     This set of code converts a row and column position into the visible
     bed labels B01-B20. The row is multiplied by the number of columns,
     the column offset is added, and one is added because Java array subscripts 
     start at zero while the displayed bed numbering starts at one (Farrell, 2023).
     */
    
    private String getBedCode(int row, int column)
    {
        int number = row * COLUMNS + column + 1;
        if (number < 10)
        {
            return "B0" + number;
        }
        return "B" + number;
    }
        
}
