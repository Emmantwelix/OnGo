package com.group9.ongo.persistence;

import com.group9.ongo.models.Aircraft;

public interface AircraftRepository {
    Aircraft getAircraftById(int aircraftId);

    Aircraft addAircraft(Aircraft aircraft);

}
