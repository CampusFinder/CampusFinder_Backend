package com.example.campusfinder.email.utils;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class EmailFormatValidator {

    private final String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    public boolean isValid(String email) {
        return Pattern.matches(emailRegex, email);
    }
}