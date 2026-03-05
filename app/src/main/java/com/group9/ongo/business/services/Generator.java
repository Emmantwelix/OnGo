package com.group9.ongo.business.services;

import java.time.LocalDate;

public interface Generator {
    String generateFlightNum();
    LocalDate generateDate();
    int[] generateSeats(int capacity); //return an array of seats, with some seats being filled

}
