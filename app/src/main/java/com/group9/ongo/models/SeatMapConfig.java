package com.group9.ongo.models;

import java.util.ArrayList;
import java.util.List;

public class SeatMapConfig {
    private final String modelName;
    private final String layout;
    private final int capacity;
    private final int rows;

    public SeatMapConfig(String modelName, String layout, int capacity) {
        this.modelName = modelName;
        this.layout = layout;
        this.capacity = capacity;
        
        // Calculate rows based on how many physical seats (non-aisle) are in the layout
        int physicalSeatsPerRow = layout.replace("_", "").length();
        this.rows = (int) Math.ceil((double) capacity / physicalSeatsPerRow);
    }

    public static SeatMapConfig createFromCapacity(String modelName, int capacity) {
        String layout;
        if (capacity >= 250) {
            layout = "ABC_DEFG_HJK"; // Wide-body
        } else if (capacity >= 100) {
            layout = "ABC_DEF";      // Narrow-body
        } else {
            layout = "AC_DF";        // Regional
        }
        return new SeatMapConfig(modelName, layout, capacity);
    }

    public List<Seat> generateSeats() {
        List<Seat> seats = new ArrayList<>();
        int physicalSeatCount = 0;

        for (int r = 1; r <= rows; r++) {
            for (int i = 0; i < layout.length(); i++) {
                char c = layout.charAt(i);
                if (c == '_') {
                    // Physical aisle
                    seats.add(new Seat(r, "", Seat.Type.AISLE, Seat.Status.OCCUPIED));
                } else {
                    // Check if we still have physical seats left to place
                    if (physicalSeatCount < capacity) {
                        seats.add(new Seat(r, String.valueOf(c), Seat.Type.SEAT, Seat.Status.AVAILABLE));
                        physicalSeatCount++;
                    } else {
                        // Empty space in the tail or last row where no seat exists
                        seats.add(new Seat(r, "", Seat.Type.AISLE, Seat.Status.OCCUPIED));
                    }
                }
            }
        }
        return seats;
    }

    public String getModelName() {
        return modelName;
    }

    public String getLayout() {
        return layout;
    }

    public int getRows() {
        return rows;
    }

    public int getCapacity() {
        return capacity;
    }

    // Specific factory methods matching project constants
    public static SeatMapConfig createAirbusA320() {
        return new SeatMapConfig("Airbus A320", "ABC_DEF", 150);
    }

    public static SeatMapConfig createBoeing737() {
        return new SeatMapConfig("Boeing 737", "ABC_DEF", 160);
    }

    public static SeatMapConfig createBoeing787() {
        return new SeatMapConfig("Boeing 787 Dreamliner", "ABC_DEFG_HJK", 250);
    }

    public static SeatMapConfig createAirbusA380() {
        return new SeatMapConfig("Airbus A380", "ABC_DEFG_HJK", 500);
    }
}
