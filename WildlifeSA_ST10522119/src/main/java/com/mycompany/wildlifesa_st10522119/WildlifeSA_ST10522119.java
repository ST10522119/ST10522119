
package com.mycompany.wildlifesa_st10522119;

/*
 This code Imports JOptionPane for every graphical input, confirmation and output dialog.
 (Farrell, 2023)
 */

import javax.swing.JOptionPane;

public class WildlifeSA_ST10522119 
{
    
/*
 This application owns one manager, so all menu actions share the same in-memory
 case list. Other code cannot replace the manager reference.
 */
    
private final RescueManager manager = new RescueManager();

/*
 This set of code creates one application object and calls its menu loop.
 */
    
public static void main(String[] args) 
    {
    WildlifeSA_ST10522119 application = new WildlifeSA_ST10522119();
    application.run();
    }
    
/*
 This code repeats the main menu while running is true. Cancel (0) and Exit (9) open
 an exit confirmation. Only Yes will stop the loop. The switch dispatches choices
 1-8 to the corresponding action. Action-level catches show cancellation or
 validation messages and return control to the menu instead of terminating.
 (Farrell, 2023)
 */
    
 public void run() 
 {
    boolean running = true;
    while (running)
        {
        int choice = readMainMenu();
        if (choice == 0 || choice == 9)
            {
            int answer = JOptionPane.showConfirmDialog(null,
                    "Exit WildLife SA?\nCases are held in memory and will be lost when you exit.",
                    "Exit application", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (answer == JOptionPane.YES_OPTION)
                {
                running = false;
                }
            }
            else 
             {
              try
                 {
                 switch (choice)
                 {
                   case 1: createCase(); break;
                   case 2: searchCase(); break;
                   case 3: updateStatus(); break;
                   case 4: showPagedMessage("All rescue cases", manager.displayAllCases()); break;
                   case 5: performOperation(true); break;
                   case 6: performOperation(false); break;
                   case 8: showPagedMessage("Rescue report", manager.generateReport()); break;
                   default: showError("Choose a menu number frooom 1 to 9.");
                 }
                 }
            catch (InputCancelledException ex)
            {
                showMessage("Action cancelled.");
            }
            catch (IllegalArgumentException ex)
            {
                showError(ex.getMessage());
            }
        }
        }
    }
    
/*
 The following code shows the nine actions and current case count in an input dialog.
 Null means Cancel or close and returns the exit sentinel 0. Other input is
 trimmed and parsed; only 1-9 can return. Both malformed and out-of-range input
 reach the same error message, and the loop prompts again.
 */
    
    private int readMainMenu()
    {
        while (true)
    {
        String input = JOptionPane.showInputDialog(null,
                "WILDLIFE SA RESCUE OPERATIONS\n"
                 + "Recorded cases: " + manager.getCaseCount() + "\n\n"
                 + "1. Create a rescue case\n2. Search by Rescue Case ID\n"
                 + "3. Update rescue status\n4. Display all rescue cases\n"
                 + "5. Start a rescue operation\n6. Complete a rescue operation\n"
                 + "7. Generate a rescue summary\n8. Generate rescue report\n"
                 + "9. Exit\n\nEnter a menu number:",
                   "WildLife SA rescue menu", JOptionPane.QUESTION_MESSAGE);
        if (input == null)
        {
            return 0;
        }
        try
        {
            int value = Integer.parseInt(input.trim());
            if (value >= 1 && value <= 9)
            {
                return value;
            }
        }
        catch (NumberFormatException ex){}
        showError("Enter a whole menu number from 1 to 9.");
    }    
    }
    
/*
 The following code collects a valid rescue type and all common fields before constructing a case.
 The if/else branches create the appropriate subclass through a RescueCase reference.
 Endangered classifications use a three-element array, while subtracting 1 converts a menu number to its index.    
 The manager receives the case only after all input and constructor checks pass.
 No partial records will be saved should cancellation occur before that point.
 */
    
    private void createCase() 
    {
        int type = readSelection("Rescue type\n1. Injured Animal Rescue\n"
                + "2. Orphaned Animal Rescue\n3. Endangered Species Rescue", 3);
        String id = readUniqueId();
        String name = readText("Animal name");
        String species = readText("Species");
        String location = readText("Rescue location");
        String ranger = readText("Assigned ranger");
        int days = readPositiveInteger("Number of rescue days");
        double dailyCost = readPositiveAmount("Daily care cost in rand");
        RescueCase rescueCase;

        if (type == 1) 
        {
            String injury = readText("Injury description");
            double veterinaryCost = readPositiveAmount("Veterinary treatment cost in rand (once per case)");
            boolean surgery = readYesNo("Is surgery required? (Adds R 5 000.)");
            rescueCase = new InjuredAnimalRescue(id, name, species, location, ranger,
                    days, dailyCost, injury, veterinaryCost, surgery);
        } 
        else if (type == 2) 
        {
            int age = readPositiveInteger("Estimated age in whole months");
            double feedingCost = readPositiveAmount("Feeding cost in rand (once per case)");
            boolean foster = readYesNo("Is foster care required? (Adds R 2 500.)");
            rescueCase = new OrphanedAnimalRescue(id, name, species, location, ranger,
                    days, dailyCost, age, feedingCost, foster);
        } 
        else 
        {
            int classification = readSelection("Conservation classification\n1. Vulnerable\n"
                    + "2. Endangered\n3. Critically Endangered", 3);
            String[] classifications = {"Vulnerable", "Endangered", "Critically Endangered"};
            double securityCost = readPositiveAmount("Security cost in rand (once per case)");
            boolean specialist = readYesNo("Is a specialist team required? (Adds R 8 000.)");
            rescueCase = new EndangeredSpeciesRescue(id, name, species, location, ranger,
                    days, dailyCost, classifications[classification - 1], securityCost, specialist);
        }

        
        manager.addRescueCase(rescueCase);
        showPagedMessage("Rescue case created", rescueCase.getFullDetails());
    }

/*
 The next code repeats the ID prompt until the manager finds no existing match
 Blank text will be rejected by readText; and matching IDs produce a retry message.
 */
    
    private String readUniqueId() 
    {
        while (true) 
        {
            String id = readText("Rescue Case ID (must be unique)");
            if (manager.findById(id) == null) 
               {
                return id;
               }
            showError("That Rescue Case ID already exists. Enter another ID.");
        }
    }

/*
 This code will read one valid ID and retrieve its stored object. 
 it shows a not-found message for null and returns the result so each caller can safely check it.
 */ 
    
    private RescueCase selectExistingCase() 
    {
        String id = readText("Enter the Rescue Case ID");
        RescueCase rescueCase = manager.findById(id);
        if (rescueCase == null) 
        {
            showMessage("No rescue case was found for ID " + id + ".");
        }
        return rescueCase;
    }

/*
 This code will display full common and specialised details only when the selected ID exists.
 */
    
    private void searchCase() 
    {
        RescueCase rescueCase = selectExistingCase();
        if (rescueCase != null) 
        {
            showPagedMessage("Search result", rescueCase.getFullDetails());
        }
    }

/*
 For an existing case, this code will show the current state and three permitted new states.
 A validated choice is mapped to its array element using choice minus 1.
 The manager updates the stored case and the resulting summary will be displayed.
 */
    
    private void updateStatus() 
    {
        RescueCase rescueCase = selectExistingCase();
        if (rescueCase != null) 
        {
            int choice = readSelection("Current status: " + rescueCase.getRescueStatus()
                    + "\nSelect the new status\n1. Pending\n2. In Progress\n3. Completed", 3);
            String[] statuses = {RescueCase.PENDING, RescueCase.IN_PROGRESS, RescueCase.COMPLETED};
            manager.updateStatus(rescueCase.getRescueCaseId(), statuses[choice - 1]);
            showPagedMessage("Status updated", rescueCase.generateSummary());
        }
    }

/* 
 The interface reference can refer to any of the three rescue subclasses. (Farrell, 2023)
 This set of code uses an interface reference for any concrete rescue type. The start parameter
 selects startRescue for true and completeRescue for false. Both operations
 update the shared status, then the updated polymorphic summary is displayed.(Farrell, 2023
 */
    
    private void performOperation(boolean start) 
    {
        RescueCase rescueCase = selectExistingCase();
        if (rescueCase != null) 
        {
            RescueOperations operation = rescueCase;
            if (start) 
            {
                operation.startRescue();
            } 
            else 
            {
                operation.completeRescue();
            }
            showPagedMessage("Rescue operation updated", operation.generateSummary());
        }
    }
    
/*
 This set of code will Display one existing case through the RescueOperations contract.
 No subtype-specific selection is needed for its priority or cost calculation. 
 */  

    private void generateSummary() 
    {
        RescueCase rescueCase = selectExistingCase();
        if (rescueCase != null) 
        {
            RescueOperations operation = rescueCase;
            showPagedMessage("Rescue summary", operation.generateSummary());
        }
    }

/*
 This set of code centralises text-dialog input. A cancelled or closed dialog returns null from
 JOptionPane, so a custom exception exits the current action. Otherwise the raw
 String is returned to a helper that validates the requested kind of input.
 */
    
    private String readInput(String prompt) 
    {
        String value = JOptionPane.showInputDialog(null, prompt,
                "WildLife SA input", JOptionPane.QUESTION_MESSAGE);
        if (value == null) 
        {
            throw new InputCancelledException();
        }
        return value;
    }
    
/*
 This set of code retries required-text validation until a non-blank trimmed value is returned.
 The validation error is displayed inside the loop. Cancellation is thrown by
 readInput outside the try block and therefore reaches the action-level catch. 
 */  

    private String readText(String prompt) 
    {
        while (true) 
        {
            String value = readInput(prompt);
            try 
            {
                return Validation.requiredText(value, prompt);
            } catch (IllegalArgumentException ex) 
            {
                showError(ex.getMessage());
            }
        }
    }

/*
 This code will repeat numeric entry until parsing and positivity checks both succeed.
 The more specific NumberFormatException catch handles malformed or overflowing
 integer text before the IllegalArgumentException catch handles nonpositive input.
 */
    
    private int readPositiveInteger(String prompt) 
    {
        while (true) 
        {
            String input = readInput(prompt);
            try 
                {
                return Validation.parsePositiveInteger(input, prompt);
                } 
            catch (NumberFormatException ex) 
                {
                showError("Enter a whole number greater than zero, within the int range.");
                } 
            catch (IllegalArgumentException ex) 
                {
                showError(ex.getMessage());
                }
        }
    }

    private double readPositiveAmount(String prompt) 
    {
        while (true) 
        {
            String input = readInput(prompt + "\nUse a decimal point, for example 150.50.");
            try 
            {
                return Validation.parsePositiveAmount(input, prompt);
            } 
            catch (NumberFormatException ex) 
            {
                showError("Enter a numeric amount greater than zero, for example 150.50.");
            } catch (IllegalArgumentException ex) 
            {
                showError(ex.getMessage());
            }
        }
    }

    private int readSelection(String prompt, int maximum) 
    {
        while (true) 
        {
            int choice = readPositiveInteger(prompt);
            if (choice <= maximum) 
            {
                return choice;
            }
            showError("Choose a number from 1 to " + maximum + ".");
        }
    }

    private boolean readYesNo(String prompt) 
    {
        int result = JOptionPane.showConfirmDialog(null, prompt, "WildLife SA decision",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) 
        {
            throw new InputCancelledException();
        }
        return result == JOptionPane.YES_OPTION;
    }

    private void showMessage(String message) 
    {
        JOptionPane.showMessageDialog(null, message, "WildLife SA",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) 
    {
        JOptionPane.showMessageDialog(null, message, "Please correct the input",
                JOptionPane.ERROR_MESSAGE);
    }

/* 
 Wrapping and 20-line pages keep every case readable in message dialogs.
 Only String methods, StringBuilder, decisions and loops are used. (Farrell, 2023)
 */
    
    private void showPagedMessage(String title, String message) 
    {
        String wrapped = TextFormat.wrap(message);
        int start = 0;
        int lines = 0;
        int page = 1;
        for (int i = 0; i < wrapped.length(); i++) 
        {
            if (wrapped.charAt(i) == '\n') 
            {
                lines++;
            }
            if (lines == 20 || i == wrapped.length() - 1) 
            {
                JOptionPane.showMessageDialog(null, wrapped.substring(start, i + 1),
                        title + " - Page " + page, JOptionPane.INFORMATION_MESSAGE);
                start = i + 1;
                lines = 0;
                page++;
            }
        }
    }

  
    private static class InputCancelledException extends RuntimeException {
        private static final long serialVersionUID = 1L;
    }
}
