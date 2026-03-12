package com.group9.ongo.models;
public class SeatMapConfig {
    private final String layout;
    private final int capacity;
    private final int rows;

    public SeatMapConfig(String layout, int capacity, int rows) {
        this.layout = layout;
        this.capacity = capacity;
        this.rows = rows;
    }

    public String getLayout() { return layout; }
    public int getCapacity() { return capacity; }
    public int getRows() { return rows; }
}