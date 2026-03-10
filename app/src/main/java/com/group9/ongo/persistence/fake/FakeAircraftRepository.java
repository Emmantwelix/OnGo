package com.group9.ongo.persistence.fake;

import static com.group9.ongo.business.constants.FlightConstants.A320_DETAILS;
import static com.group9.ongo.business.constants.FlightConstants.A380_DETAILS;
import static com.group9.ongo.business.constants.FlightConstants.B737_DETAILS;
import static com.group9.ongo.business.constants.FlightConstants.B787_DETAILS;

import com.group9.ongo.models.Aircraft;
import com.group9.ongo.persistence.AircraftRepository;

import java.util.ArrayList;
import java.util.List;

public class FakeAircraftRepository implements AircraftRepository {

    private List<Aircraft> aircrafts;

    private int nextId = 1;
    public FakeAircraftRepository(){
        aircrafts = new ArrayList<>();
        populateWithSampleData();
    }

    @Override
    public Aircraft getAircraftById(int aircraftId)
    {
        for(Aircraft aircraft : aircrafts)
        {
            if(aircraft.getAircraftId() == aircraftId){
                return aircraft;
            }
        }

        return null;
    }

    @Override
    public Aircraft addAircraft(Aircraft aircraft)
    {
        Aircraft addedAircraft = new Aircraft(nextId, aircraft);
        aircrafts.add(addedAircraft);
        nextId++;
        return addedAircraft;
    }
    private void populateWithSampleData()
    {
        addAircraft(A320_DETAILS);
        addAircraft(B737_DETAILS);
        addAircraft(B787_DETAILS);
        addAircraft(A380_DETAILS);
    }
}
