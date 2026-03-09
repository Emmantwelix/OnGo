package com.group9.ongo.business.services;

import static com.group9.ongo.business.constants.FlightConstants.DATE_RANGE;
import static com.group9.ongo.business.constants.FlightConstants.MEDIUM_CAPACITY;

import com.group9.ongo.business.services.Implementations.FlightDetailGen;
import com.group9.ongo.business.services.Interfaces.Generator;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Random;

public class GeneratorTest {
    Random rand;
    Generator generator;

    @Before
    public void setup(){
        rand = new Random();
        generator = new FlightDetailGen(rand);
    }

    @Test
    public void testGenerateFlightNum(){
        String flightNum = generator.generateFlightNum();
        for (int i = 0; i < 6; i++)
        {
            if (i == 0 || i == 1)
            {
                assert(flightNum.charAt(i) >= 'A' && flightNum.charAt(i) <= 'Z');
            }
            else
            {
                assert(flightNum.charAt(i) >= '0' && flightNum.charAt(i) <= '9');
            }
        }
        assert(flightNum.length() == 6);
    }

    @Test
    public void testGenerateLocalDate() {
        LocalDate today = LocalDate.now();
        LocalDate date = generator.generateDate();
        assert(!date.isBefore(today));
        assert(date.isBefore(today.plusDays(DATE_RANGE + 1)));
    }


}
