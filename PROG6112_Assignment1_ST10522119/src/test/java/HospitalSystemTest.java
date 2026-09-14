
import com.mycompany.prog6112_assignment1_st10522119.HospitalSystem;
import com.mycompany.prog6112_assignment1_st10522119.Inpatient;
import com.mycompany.prog6112_assignment1_st10522119.Patient;
import com.mycompany.prog6112_assignment1_st10522119.PatientCategory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class HospitalSystemTest
{

/*
  This set of code contains a helper method that will be used by several tests
  to create a normal Patient object. A constructor creates and initialises the 
  object from the supplied values. (Farrell, 2023)
*/    
   
 private Patient patient(String id, String first, String last)
 {
     return new Patient(id, first, last, 30, "Female", "Check-up",
                PatientCategory.OUTPATIENT);
 }
 
 /*
  This set of code contains a Helper method used by bed-management tests to
  create an Inpatient object. Inpatient inherits from Patient and its constructor
  calls the superclass constructor, following inheritance concepts (Farrell, 2023)
 */
 
    private Inpatient inpatient(String id, String first, String last)
    {
        return new Inpatient(id, first, last, 40, "Male", "Observation",
                PatientCategory.INPATIENT, "Ward 1", "");
    }
 
 /*
   This test verifies that registering a valid patient succeeds and that
   the stored patient count increases to one. 
 */

 @Test
    public void testRegisterPatient() 
    {
        HospitalSystem system = new HospitalSystem();
        assertTrue(system.registerPatient(patient("P001", "Lebo", "Mokoena")));
        assertEquals(1, system.getPatientCount());
    }    
    
 /*
   This test verifies that a patient can be found by Patient ID and that 
   the returned object contains the expected surname. The search logic uses an 
   enhanced for loop and String comparison as explained in the main source comments.
 */
    
 @Test
    public void testSearchPatient()
    {
        HospitalSystem system = new HospitalSystem();
        system.registerPatient(patient("P002", "Anele", "Dlamini"));
        Patient found = system.searchPatient("P002");
        assertNotNull(found);
        assertEquals("Dlamini", found.getLastName());
    }
    
 /*
   This test verifies that an existing patient's mutable details can be changed 
   and then retrieved through accessor methods. This supports the use of public
   getters/setters with private fields (Farrell, 2023).
 */
    @Test
    public void testUpdatePatientDetails()
    {
        HospitalSystem system = new HospitalSystem();
        system.registerPatient(patient("P003", "Nandi", "Khumalo"));

        assertTrue(system.updatePatient("P003", "Nandi", "Zulu", 31,
                "Female", "Recovered", PatientCategory.OUTPATIENT));
        assertEquals("Zulu", system.searchPatient("P003").getLastName());
        assertEquals(31, system.searchPatient("P003").getAge());
    }
    
 /*
   This test verifies that deleting a patient removes the record and decreases 
   the patient count.
  */
    
    @Test
    public void testDeletePatient() 
    {
        HospitalSystem system = new HospitalSystem();
        system.registerPatient(patient("P004", "Sipho", "Nkosi"));

        assertTrue(system.deletePatient("P004"));
        assertNull(system.searchPatient("P004"));
        assertEquals(0, system.getPatientCount());
    }
    
 /*
   This test verifies successful bed allocation for an Inpatient, including the ward
   array state and the bed number stored in the subclass object. The ward is
   represented by a two-dimensional array.
  */
    
    @Test
    public void testAllocateBed() 
    {
        HospitalSystem system = new HospitalSystem();
        system.registerPatient(inpatient("P005", "Thabo", "Molefe"));

        assertTrue(system.allocateBed("P005", "B01"));
        assertTrue(system.isBedOccupied("B01"));
        assertEquals("B01", ((Inpatient) system.searchPatient("P005")).getBedNumber());
    }

 /*
   This test verifies that a previously allocated bed can be released and 
   that both the array position and the Inpatient object's bed field reflect the
   released state.
  */
    
    @Test
    public void testReleaseBed() 
    {
        HospitalSystem system = new HospitalSystem();
        system.registerPatient(inpatient("P006", "Zanele", "Ndlovu"));
        system.allocateBed("P006", "B02");

        assertTrue(system.releaseBed("P006"));
        assertFalse(system.isBedOccupied("B02"));
        assertEquals("", ((Inpatient) system.searchPatient("P006")).getBedNumber());
    }

 /*
   Boundary/validation test: verifies that a second Patient object cannot be
   registered with an ID that is already stored.
  */
    
    @Test
    public void testDuplicatePatientIdIsPrevented() 
    {
        HospitalSystem system = new HospitalSystem();
        assertTrue(system.registerPatient(patient("P007", "Mpho", "Mokoena")));
        assertFalse(system.registerPatient(patient("P007", "Lerato", "Maseko")));
        assertEquals(1, system.getPatientCount());
    }

 /*
   Boundary/validation test: verifies that a bed already containing one
   inpatient's Patient ID cannot be allocated to another inpatient.
  */
    
    @Test
    public void testOccupiedBedCannotBeAllocatedTwice() 
    {
        HospitalSystem system = new HospitalSystem();
        system.registerPatient(inpatient("P008", "Neo", "Mthembu"));
        system.registerPatient(inpatient("P009", "Kagiso", "Mahlangu"));

        assertTrue(system.allocateBed("P008", "B03"));
        assertFalse(system.allocateBed("P009", "B03"));
    }

 /*
   Boundary test for a full ward. A for loop creates twenty inpatients and
   allocates all B01-B20 positions. The final allocation must fail once all
   twenty array positions are occupied.
  */
    
    @Test
    public void testAllocationFailsWhenAllBedsAreOccupied() 
    {
        HospitalSystem system = new HospitalSystem();

        for (int i = 1; i <= 20; i++)
        {
            String id;
            String bed;
            if (i < 10) 
            {
                id = "P0" + i;
                bed = "B0" + i;
            } 
               else 
            {
                id = "P" + i;
                bed = "B" + i;
            }

            system.registerPatient(inpatient(id, "Patient", "Number" + i));
            assertTrue(system.allocateBed(id, bed));
        }

        system.registerPatient(inpatient("P021", "Extra", "Patient"));
        assertFalse(system.allocateBed("P021", "B01"));
        assertEquals(20, system.getOccupiedBedCount());
    }

 /*
   This test verifies both sorting options. Bubble sort is a repeated comparison
   of neighbouring array elements followed by a swap when the elements are out 
   of order. (Farrell, 2023) The assertions confirm that the resulting Patient 
   arrays are ordered first by surname and then by ID.
  */
    
    @Test
    public void testSortPatientsBySurnameAndId() 
    {
        HospitalSystem system = new HospitalSystem();
        system.registerPatient(patient("P020", "A", "Zulu"));
        system.registerPatient(patient("P003", "B", "Dlamini"));
        system.registerPatient(patient("P011", "C", "Mokoena"));

        Patient[] bySurname = system.getPatientsSortedBySurname();
        assertEquals("Dlamini", bySurname[0].getLastName());
        assertEquals("Mokoena", bySurname[1].getLastName());
        assertEquals("Zulu", bySurname[2].getLastName());

        Patient[] byId = system.getPatientsSortedById();
        assertEquals("P003", byId[0].getPatientId());
        assertEquals("P011", byId[1].getPatientId());
        assertEquals("P020", byId[2].getPatientId());
    } 
     
}
