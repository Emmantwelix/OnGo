package com.group9.ongo.business.services.Interfaces;

import com.group9.ongo.models.Aircraft;

public interface AircraftService {
    Aircraft getAircraftById(int aircraftId);

    Aircraft addAircraft(Aircraft aircraft);
}
