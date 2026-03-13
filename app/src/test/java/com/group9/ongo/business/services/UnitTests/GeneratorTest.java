package com.group9.ongo.business.services.UnitTests;

import static com.group9.ongo.business.constants.FlightConstants.DATE_RANGE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
                assertTrue(flightNum.charAt(i) >= 'A' && flightNum.charAt(i) <= 'Z');
            }
            else
            {
                assertTrue(flightNum.charAt(i) >= '0' && flightNum.charAt(i) <= '9');
            }
        }
        assertEquals(6, flightNum.length());
    }

    @Test
    public void testGenerateLocalDate() {
        LocalDate today = LocalDate.now();
        LocalDate date = generator.generateDate();
        assertFalse(date.isBefore(today));
        assertTrue(date.isBefore(today.plusDays(DATE_RANGE + 1)));
    }


}
