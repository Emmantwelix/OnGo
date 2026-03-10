package com.group9.ongo.models;

import java.io.Serializable;

public class Aircraft implements Serializable {

    private int aircraftId;
    private final String modelName;
    private final int capacity;
    private final boolean hasWifi;

    public Aircraft(int aircraftId, String modelName, int capacity, boolean hasWifi) {
        this.aircraftId = aircraftId;
        this.modelName = modelName;
        this.capacity = capacity;
        this.hasWifi = hasWifi;
    }

    public Aircraft(int aircraftId, Aircraft aircraft)
    {
        this.aircraftId = aircraftId;
        this.modelName = aircraft.modelName;
        this.capacity = aircraft.capacity;
        this.hasWifi = aircraft.hasWifi;
    }

    public String getModelName() { return modelName; }
    public int getCapacity() { return capacity; }
    public boolean hasWifi() { return hasWifi; }

    public int getAircraftId() { return aircraftId; }
    
    // Helper to get seat string
    public String getCapacityString() {
        return capacity + " seats";
    }
}