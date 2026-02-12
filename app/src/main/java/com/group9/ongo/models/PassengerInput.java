package com.group9.ongo.models;

/**
 * Represents the data collected from the UI for a passenger booking.
 * This is a Data Transfer Object (DTO).
 */
public class PassengerInput {
    private final String firstName;
    private final String lastName;
    private final String dateOfBirth;
    private final String passportNumber;

    public PassengerInput(String firstName, String lastName, String dateOfBirth, String passportNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.passportNumber = passportNumber;
    }

    // Getters only (No Setters) makes the data immutable (unchangeable)
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getDateOfBirth() { return dateOfBirth; }
    public String getPassportNumber() { return passportNumber; }
}
