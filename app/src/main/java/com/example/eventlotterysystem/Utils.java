package com.example.eventlotterysystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Utils {

    // Method to draw n unique random numbers from [0, m] where m > n and return them in descending order
    public static List<Integer> drawRandomNumbers(int n, int m) {
        Set<Integer> resultSet = new HashSet<>();
        Random random = new Random();

        // Keep drawing until we have n unique numbers
        while (resultSet.size() < n) {
            int randomNumber = random.nextInt(m + 1); // Generate a number between 0 and m
            resultSet.add(randomNumber); // Add to the set (duplicates automatically handled)
        }

        // Convert the set to a list and sort it in descending order
        List<Integer> resultList = new ArrayList<>(resultSet);
        resultList.sort(Collections.reverseOrder());

        return resultList;
    }
}
