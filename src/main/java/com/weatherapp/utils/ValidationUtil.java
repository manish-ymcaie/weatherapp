package com.weatherapp.utils;
public class ValidationUtil {
    public static void validatePostalCode(String postalCode) {
        if (!postalCode.matches("\\d{5}")) {
            throw new IllegalArgumentException("Invalid US postal code.");
        }
    }
}
