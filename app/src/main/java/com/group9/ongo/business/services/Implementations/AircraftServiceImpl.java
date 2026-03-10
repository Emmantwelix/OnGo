package com.group9.ongo.business.services.Implementations;

import com.group9.ongo.business.services.Interfaces.AircraftService;
import com.group9.ongo.models.Aircraft;
import com.group9.ongo.persistence.AircraftRepository;

public class AircraftServiceImpl implements AircraftService {

    private AircraftRepository aircraftRepo;

    public AircraftServiceImpl(AircraftRepository aircraftRepo)
    {
        this.aircraftRepo = aircraftRepo;
    }

    @Override
    public Aircraft getAircraftById(int aircraftId)
    {
        return aircraftRepo.getAircraftById(aircraftId);
    }

    @Override
    public Aircraft addAircraft(Aircraft aircraft)
    {
        //validation here
        return aircraftRepo.addAircraft(aircraft);
    }

}
