package com.mycompany.prog6112_assignment1_st10522119;

/*
 The Patient class is the base class for all patient records in the system. 
 It groups related patient data and the methods that work with that data into 
 one class.
 The data fields will be private in order to apply information hiding.
*/

public class Patient 
{
 /*
   These private fields store the information required for one patient.
   Keeping the fields private supports information hiding because outside classes
   must use the public methods below rather than altering the data directly. (Farrell, 2023)
   */
  
 private String patientId;
 private String firstName;
 private String lastName;
 private int age;
 private String gender;
 private String medicalCondition;
 private PatientCategory category;
 
 /*
  The following set of code is a parameterised constructor which will recieve the starting values
  for a patient object and assigns them to the object's fields. (Farrell, 2023)
 */
 
 public Patient(String patientId, String firstName, String lastName, int age, 
                String gender, String medicalCondition, PatientCategory category)
 {
 this.patientId = patientId;
 this.firstName = firstName;
 this.lastName = lastName;
 this.age = age;
 this.gender = gender;
 this.medicalCondition = medicalCondition;
 this.category = category;
 }    
    
 /*
  Accessor methods return private field values to other classes without exposing the fields directly.
  The Patient ID has only a getter because the application treats it as the stable identifier used
  to find a paitent record. (Farrell, 2023)
 */
 
 public String getPatientId()
 {
  return patientId;
 }
    
 /*
  The remaining getter and setter pairs provide controlled read/write access to patient details.
  These public methods are used to work with private data fields while preserving information
  hiding. (Farrell, 2023)
 */
 
 public String getFirstName()
 {
  return firstName;
 }
 
 public void setFirstName(String firstName)
 {
  this.firstName = firstName;
 }
 
 public String getLastName()
 {
  return lastName;
 }
 
 public void setLastName(String lastName)
 {
  this.lastName = lastName;
 }
 
 public int getAge()
 {
  return age;
 }
 
 public void setAge(int age)
 {
  this.age = age;
 }
 
 public String getGender()
 {
  return gender;
 }
 
 public void setGender(String gender)
 {
  this.gender = gender;
 }
 
 public String getMedicalCondition()
 {
  return medicalCondition;
 }
 
 public void setMedicalCondition(String medicalCondition)
 {
  this.medicalCondition = medicalCondition;
 }
 
 public PatientCategory getCategory()
 {
  return category;
 }
 
 public void setCategory(PatientCategory category)
 {
  this.category = category;
 }
 
 /*
  The following set of code is a method which displays the common information that 
  is held by every patient. Placing the display behaviour in the patient class allows
  a subclass to reuse or override it. (Farrell, 2023)
 */
 
 public void displayDetails()
 {
     System.out.println("Patient ID       : " + patientId);
     System.out.println("Name             : " + firstName + " " + lastName);
     System.out.println("Age              : " + age);
     System.out.println("Gender           : " + gender);
     System.out.println("Medical Condition: " + medicalCondition);
     System.out.println("Category         : " + category);
 }
 
 }
