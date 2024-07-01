package org.example;


import java.io.*;
import java.util.*;

public class UniqueElementsFinder {

    public static void main(String[] args) {
        String fileA = "/home/administrator/compare/files/corescript.txt";
        String fileB = "/home/administrator/compare/files/railcorescript.txt";

        Set<String> uniqueElements = findUniqueElements(fileB, fileA);

        System.out.println("Unique elements in " + fileB + " compared to " + fileA + ":");
        for (String element : uniqueElements) {
            System.out.println(element);
        }
    }

    public static Set<String> findUniqueElements(String fileA, String fileB) {
        Set<String> elementsA = readElementsFromFile(fileA);
        Set<String> elementsB = readElementsFromFile(fileB);

        elementsA.removeAll(elementsB); // Remove all elements from A that are also in B
        return elementsA;
    }

    public static Set<String> readElementsFromFile(String fileName) {
        Set<String> elements = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                elements.add(line.trim());
            }
        } catch (IOException e) {
            System.err.println("Error reading file " + fileName + ": " + e.getMessage());
        }
        return elements;
    }
}

