package com.group9.ongo.models;

import java.time.LocalDate;

public class Passenger {

    private int passengerId;

    private int bookingId;
    private String firstName;

    private String lastName;

    private LocalDate dateOfBirth;

    private String passportNumber;

    public Passenger(int id, int bookingId, String firstName, String lastName, LocalDate dob, String passportNumber) {
        this.passengerId = id;
        this.bookingId = bookingId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dob;
        this.passportNumber = passportNumber;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPassportNumber() {
        return passportNumber;
    }
}