package com.group9.ongo.business.services;
import static com.group9.ongo.business.constants.FlightConstants.ALPHABET;
import static com.group9.ongo.business.constants.FlightConstants.DATE_RANGE;
import static com.group9.ongo.business.constants.FlightConstants.NUMBERS;

import java.time.LocalDate;
import java.util.Random;
public class FlightDetailGen implements Generator {
    private Random rand;

    public FlightDetailGen(Random rand){
        this.rand = rand;
    }

    @Override
    public String generateFlightNum() {
        StringBuilder flightNum = new StringBuilder();

        //2 random letters
        for (int i = 0; i < 2; i++) {
            int index = rand.nextInt(ALPHABET.length);
            flightNum.append(ALPHABET[index]);
        }

        //4 random digits
        for (int i = 0; i < 4; i++) {
            int index = rand.nextInt(NUMBERS.length);
            flightNum.append(NUMBERS[index]);
        }
        return flightNum.toString();
    }

    @Override
    public LocalDate generateDate() {
            //date from current date up to DATE_RANGE days
            LocalDate today = LocalDate.now();
            return today.plusDays(rand.nextInt(DATE_RANGE + 1));
    }

    @Override
    public int[] generateSeats(int capacity) {
        //generate seats up to 95% occupied
        int[] seats = new int[capacity];

        int maxOccupied = (int) Math.floor(capacity * 0.95);
        int occupied = rand.nextInt(maxOccupied + 1);

        int count = 0;
        while (count < occupied) {
            int index = rand.nextInt(capacity);
            if (seats[index] == 0) {
                seats[index] = 1;
                count++;
            }
        }
        return seats;
    }
}
