package com.homework.two;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import com.homework.two.InvalidUserInputException;

public class HomeworkTwo {

    private List<List<String>> tableList = new ArrayList<>();
    private String fileName = "myFile.txt";

    public void setTableList(List<List<String>> tableList) {
        this.tableList = tableList;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<List<String>> getTableList() {
        return this.tableList;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void editTable(List<List<String>> tableList) {

        Scanner sc = new Scanner(System.in);
        String[] holder;
        String newVal;
        String option;
        int x, y;
        try {
            System.out.println("\n===================");
            System.out.println("Edit Table");
            System.out.println("Array index: ");
            System.out.print("x: ");
            x = sc.nextInt();
            System.out.print("y: ");
            y = sc.nextInt();
            System.out.format("User Input: Index %dx%d\n", x, y);
            System.out.println("Key or Value?");
            System.out.println("Input: ");
            option = sc.next().toUpperCase();
            System.out.print("New Value: ");
            newVal = sc.next();
            holder = tableList.get(x).get(y).split("=");

            switch (option) {
                case "KEY":
                    holder[0] = newVal;
                    tableList.get(x).set(y, holder[0] + "=" + holder[1]);
                    System.out.println("Edit successful.");
                    saveToFile(getFileName(), getTableList());
                    break;
                case "VALUE":
                    holder[1] = newVal;
                    tableList.get(x).set(y, holder[0] + "=" + holder[1]);
                    System.out.println("Edit successful.");
                    saveToFile(getFileName(), getTableList());
                    break;
                default:
                    System.out.println("Please choose Key or Value only.");
                    System.out.println("No changes made.");
                    break;
            }

        } catch (IndexOutOfBoundsException IOBE) {
            System.out.println("Invalid Table Index.");
            System.out.println("No changes made.");
        } catch (InputMismatchException IME) {
            System.out.println("Check your input.");
            System.out.println("No changes made.");
        }
    }

    //Loads the table from an existing path
    public void loadTableFromFile(String path) {
        try {
            Scanner reader = new Scanner(new File(path));
            List<List<String>> outerList = new ArrayList<>();
            List<String> innerList;
            int columns;

            while (reader.hasNextLine()) {
                innerList = new ArrayList<>();
                String[] line = reader.nextLine().trim().split(",");
                for (int j = 0; j < line.length; j++) {
                    innerList.add(line[j]);
                }
                outerList.add(innerList);
            }

            setTableList(outerList);
            setFileName(path);
        } catch (FileNotFoundException FNFE) {
            System.out.println("File not found.");
            System.out.println("Create new array.");
            initializeTable();
        } catch (Exception e) {
            System.out.println("An error occured.");
            initializeTable();
        }

    }

    //Starts the application by loading/creating new table
    public void initialPrompt() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter 'y/Y' to read file.");
        System.out.println("Enter any key to create new table.");
        System.out.print("Input: ");
        String choice = sc.next();
        if (choice.toUpperCase().equals("Y")) {
            System.out.print("File path: ");
            String path = sc.next();
            loadTableFromFile(path);
        } else {
            initializeTable();
        }
    }

    //Prints the contents of the table
    public void printTable(List<List<String>> table) {
        for (List<String> inner : table) {
            for (String data : inner) {
                System.out.print(data.replace("=", ",") + "   ");
            }
            System.out.println("");
        }
    }
    
    
    
    //Creates and populates a table  of NxM size
    public void initializeTable() {

        Scanner sc = new Scanner(System.in);
        Random r = new Random();
        List<String> innerList;
        List<List<String>> outerList = new ArrayList<>();
        String generatedStringK = "";
        String generatedStringV = "";
        int x;
        int y;
        final String entries = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+?/.><[]";

        System.out.println("\n===================");
        System.out.println("Reset Table");

        try {
            //ask user for size of new array
            System.out.println("Array size: ");
            System.out.print("x: ");
            x = sc.nextInt();
            System.out.print("y: ");
            y = sc.nextInt();
            System.out.format("User Input: %dx%d\n", x, y);
            checkZeroInput(x);
            checkZeroInput(y);

            //populate table
            for (int i = 0; i < x; i++) {
                innerList = new ArrayList<>();
                for (int j = 0; j < y; j++) {
                    for (int k = 0; k < 3; k++) {
                        generatedStringK += entries.charAt(r.nextInt(entries.length()));
                        generatedStringV += entries.charAt(r.nextInt(entries.length()));
                    }
                    innerList.add(generatedStringK + "=" + generatedStringV);
                    generatedStringK = "";
                    generatedStringV = "";
                }
                outerList.add(innerList);
            }
            System.out.println("Table Created.");
            setTableList(outerList);
            saveToFile(getFileName(), getTableList());
        } catch (InvalidUserInputException e) {
            System.out.println("Check your input.");
            initializeTable();
        } catch (InputMismatchException IME) {
            System.out.println("Check your input.");
            initializeTable();
        }
    }

    //Check input for possible exception
    public void checkZeroInput(int n) throws InvalidUserInputException {
        if (n <= 0) {
            throw new InvalidUserInputException("Invalid input.");
        }
    }

    //Saves table to a text file
    public void saveToFile(String filename, List<List<String>> table) {
        try {
            List<String> row;
            BufferedWriter outputWriter = new BufferedWriter(new FileWriter(filename));
            for (int i = 0; i < table.size(); i++) {
                row = new ArrayList<>();
                row = table.get(i);
                for (int j = 0; j < row.size(); j++) {
                    if (j == row.size() - 1) {
                        outputWriter.write(row.get(j));
                    } else {
                        outputWriter.write(row.get(j) + ",");
                    }
                }
                outputWriter.newLine();
            }
            outputWriter.flush();
            outputWriter.close();
            System.out.println("File upated.");
        } catch (IOException IOE) {
            System.out.println("Error saving to file.");
        }
    }

    public void searchTable(List<List<String>> table) {
        //search a string in the array
        Scanner sc = new Scanner(System.in);
        String subString;
        int row = table.size();
        int col;
        int occuranceK;
        int fromIndexK;
        int occuranceV;
        int fromIndexV;
        System.out.println("\n===================");
        System.out.println("Search Table");
        try {
            System.out.print("Search: ");
            subString = sc.next();
            for (int i = 0; i < row; i++) {
                col = table.get(i).size();
                for (int j = 0; j < col; j++) {
                    //drives inner loop for arr
                    String result = table.get(i).get(j);
                    String[] searchFrom = result.split("=");
                    occuranceK = 0;
                    fromIndexK = 0;
                    occuranceV = 0;
                    fromIndexV = 0;
                    //return -1 if value cannot  be found (Loop stop mechanism) 
                    //search in KEY
                    while ((fromIndexK = searchFrom[0].indexOf(subString, fromIndexK)) != -1) {
                        occuranceK++;
                        fromIndexK++;
                    }
                    if (occuranceK > 0) {
                        System.out.format("Output: [%d,%d] - %d Occurance in Key field\n", i, j, occuranceK);
                    }
                    //return -1 if value cannot  be found (Loop stop mechanism)
                    //search in VALUE
                    while ((fromIndexV = searchFrom[1].indexOf(subString, fromIndexV)) != -1) {
                        occuranceV++;
                        fromIndexV++;
                    }
                    if (occuranceV > 0) {
                        System.out.format("Output: [%d,%d] - %d Occurance in Value field\n", i, j, occuranceV);
                    }
                }//end  for loop
            }
        } catch (ArrayIndexOutOfBoundsException AIOBE) {
            System.out.println("Invalid Table Index.");
        } catch (IndexOutOfBoundsException AIOBE) {
            System.out.println("Invalid Table Index.");
        }
    }

    //sorts table rows in ascending/descending order
    public void sortTable(List<List<String>> table) {

        System.out.println("\n===================");
        System.out.println("Sort Array:");
        System.out.println("A=Ascending D=Descending");
        System.out.print("Input: ");
        Scanner sc = new Scanner(System.in);
        String option = sc.next();
        try {
            switch (option.toUpperCase()) {
                case "A":
                    for (List<String> list1 : table) {
                        Collections.sort(list1);
                    }
                    saveToFile(getFileName(), getTableList());
                    System.out.println("Table sorted in ascending order.");
                    break;
                case "D":
                    for (List<String> list1 : table) {
                        Collections.sort(list1, Collections.reverseOrder());
                    }
                    System.out.println("Table sorted in descending order.");
                    saveToFile(getFileName(), getTableList());
                    break;
                default:
                    System.out.println("Please choose A or D only.");
                    System.out.println("No changes made.");
                    break;
            }
        } catch (Exception E) {
            System.out.println("An error occured.");
            System.out.println("No changes made.");
        }

    }

    //Adds a column to a specified row
    public void addColumn(List<List<String>> table) {

        Scanner sc = new Scanner(System.in);
        String value;
        String key;
        int r;

        System.out.println("\n===================");
        System.out.println("Add to Table");
        System.out.print("Add in row: ");
        r = sc.nextInt();
        System.out.print("Key: ");
        key = sc.next();
        System.out.print("Value: ");
        value = sc.next();

        try {
            table.get(r).add(key + "=" + value);
            System.out.println("Table Updated.");
            saveToFile(getFileName(), getTableList());

        } catch (NullPointerException NPE) {
            System.out.println("An error occured.");
            System.out.println("No changes made.");
        } catch (IndexOutOfBoundsException IOBE) {
            System.out.println("Input exceeds array size.");
            System.out.println("No changes made.");
        }

    }

    //Stops process
    public void endSession() {
        System.out.println("Process terminated.");
        System.exit(0);
    }

}
