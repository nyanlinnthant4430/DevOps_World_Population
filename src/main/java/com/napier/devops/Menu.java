package com.napier.devops;

import java.util.Scanner;

/**
 * Handles the main interactive menu for the application.
 * Allows users to select which report to generate.
 */
public class Menu {

    /**
     * Displays a simple text-based menu and returns the user's selection.
     *
     * @return integer representing the user's choice
     */
    public static int showMenu() {
        System.out.println("\n========================================");
        System.out.println("   🌍 WORLD POPULATION REPORT MENU");
        System.out.println("========================================");
        System.out.println("1. Population of each Continent");
        System.out.println("2. Population of each Region");
        System.out.println("3. Population of each Country");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");

        Scanner sc = new Scanner(System.in);
        try {
            return Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            return -1; // invalid input
        }
    }
}


