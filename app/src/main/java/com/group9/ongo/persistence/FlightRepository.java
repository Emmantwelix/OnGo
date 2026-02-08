package com.group9.ongo.persistence;

import com.group9.ongo.models.FlightClass;

import java.util.List;

public interface FlightRepository {
    List<FlightClass> getAll();
}
