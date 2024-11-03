/**
 * The {@code Utils} class provides utility methods for common operations in the event lottery system.
 */
package com.example.eventlotterysystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Utils {

    /**
     * Draws {@code n} unique random numbers from the range [0, {@code m}], where {@code m > n},
     * and returns them in descending order.
     * <p>
     * This method ensures that all drawn numbers are unique by using a {@code Set} to store
     * them, then converts the {@code Set} to a {@code List} and sorts it in descending order.
     *
     * @param n the number of unique random numbers to draw
     * @param m the upper bound (inclusive) for the random numbers
     * @return a {@code List<Integer>} containing {@code n} unique random numbers in descending order
     * @throws IllegalArgumentException if {@code m <= n}, as it would be impossible to draw {@code n} unique numbers
     */
    public static List<Integer> drawRandomNumbers(int n, int m) {
        if (m <= n) {
            throw new IllegalArgumentException("Upper bound m must be greater than n for unique draws.");
        }
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
