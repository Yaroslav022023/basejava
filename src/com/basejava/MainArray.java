package com.basejava;

import java.util.Arrays;
import java.util.Scanner;

public class MainArray {
    public static void main(String[] args) {
        ArrayStorage arrayStorage = new ArrayStorage();
        Scanner scanner = new Scanner(System.in);
        boolean quit = false;

        while (!quit) {
            if (!selectAction(arrayStorage, scanner)) {
                quit = true;
            }
        }
    }

    private static boolean selectAction(ArrayStorage arrayStorage, Scanner scanner) {
        int action = inputNumber(scanner);

        switch (action) {
            case 1 -> save(arrayStorage, scanner);
            case 2 -> get(arrayStorage, scanner);
            case 3 -> getAll(arrayStorage);
            case 4 -> delete(arrayStorage, scanner);
            case 5 -> System.out.println(arrayStorage.getSize());
            case 6 -> clear(arrayStorage);
            case 7 -> {
                return false;
            }
        }
        return true;
    }

    private static int inputNumber(Scanner scanner) {
        int amountNumbersMenu = 7;
        int action = 0;

        while (action < 1 || action > amountNumbersMenu) {
            System.out.print(printMenu() + "\n" + "Select the number from the menu (1-7): ");
            try {
                action = scanner.nextInt();
            } catch (RuntimeException ex) {
                System.out.println("Enter only numbers.");
            }
            scanner.nextLine();
        }
        return action;
    }

    private static String printMenu() {
        return """
                
                Menu:
                1. save
                2. get Resume
                3. get all Resumes
                4. delete Resume
                5. get size of database
                6. clear all database of resumes
                7. quit
                """;
    }

    private static void save(ArrayStorage arrayStorage, Scanner scanner) {
        System.out.print("\nEnter a new resume: ");
        arrayStorage.save(new Resume(scanner.nextLine()));
        System.out.println("The resume was successfully added.");
    }

    private static void get(ArrayStorage arrayStorage, Scanner scanner) {
        System.out.print("\nEnter a resume to search: ");
        String resume = scanner.nextLine();
        if (arrayStorage.get(resume) != null) {
            System.out.println(arrayStorage.get(resume));
        } else {
            System.out.println("The entered resume was not found.");
        }
    }

    private static void getAll(ArrayStorage arrayStorage) {
        if (arrayStorage.getSize() > 0) {
            System.out.println(Arrays.toString(arrayStorage.getAll())
                    .replaceAll("^\\[|\\]$", ""));
        } else {
            System.out.println("The resume database is empty.");
        }
    }

    private static void delete(ArrayStorage arrayStorage, Scanner scanner) {
        System.out.print("\nEnter a resume to delete: ");
        arrayStorage.delete(scanner.nextLine());
    }

    private static void clear(ArrayStorage arrayStorage) {
        arrayStorage.clear();
        System.out.println("The database was successfully cleaned up.");
    }
}