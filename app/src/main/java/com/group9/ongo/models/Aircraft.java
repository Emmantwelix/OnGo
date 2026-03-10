package com.group9.ongo.models;

import java.io.Serializable;

public class Aircraft implements Serializable {
    private final String modelName;
    private final int capacity;
    private final boolean hasWifi;

    public Aircraft(String modelName, int capacity, boolean hasWifi) {
        this.modelName = modelName;
        this.capacity = capacity;
        this.hasWifi = hasWifi;
    }

    public String getModelName() { return modelName; }
    public int getCapacity() { return capacity; }
    public boolean hasWifi() { return hasWifi; }
    
    // Helper to get seat string
    public String getCapacityString() {
        return capacity + " seats";
    }
}