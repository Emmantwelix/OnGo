package com.group9.ongo.models;

import java.util.ArrayList;
import java.util.List;

/**
 * SeatMapConfig handles different plane layouts and generates a list of Seat objects.
 * Following SOLID principles, it uses a layout string to define seat and aisle positions.
 */
public class SeatMapConfig {
    private final String modelName;
    private final int rows;
    private final String layout; // e.g., "ABC_DEF" where '_' is the aisle

    public SeatMapConfig(String modelName, int rows, String layout) {
        this.modelName = modelName;
        this.rows = rows;
        this.layout = layout;
    }

    /**
     * Dynamically creates a configuration based on aircraft capacity.
     */
    public static SeatMapConfig createFromCapacity(String modelName, int capacity) {
        String layout;
        int seatsPerRow;

        if (capacity >= 250) {
            layout = "ABC_DEFG_HJK"; // Wide-body (10 seats/row)
            seatsPerRow = 10;
        } else if (capacity >= 100) {
            layout = "ABC_DEF";      // Narrow-body (6 seats/row)
            seatsPerRow = 6;
        } else {
            layout = "AC_DF";        // Regional (4 seats/row)
            seatsPerRow = 4;
        }

        int rows = (int) Math.ceil((double) capacity / seatsPerRow);
        return new SeatMapConfig(modelName, rows, layout);
    }

    /**
     * Generates a list of Seat objects based on the configuration.
     * @return List of generated seats.
     */
    public List<Seat> generateSeats() {
        List<Seat> seats = new ArrayList<>();
        for (int r = 1; r <= rows; r++) {
            for (int i = 0; i < layout.length(); i++) {
                char c = layout.charAt(i);
                if (c == '_') {
                    // Aisle is represented as a Seat with Type.AISLE
                    seats.add(new Seat(r, "", Seat.Type.AISLE, Seat.Status.OCCUPIED));
                } else {
                    // Normal seat
                    seats.add(new Seat(r, String.valueOf(c), Seat.Type.SEAT, Seat.Status.AVAILABLE));
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

    // Factory methods for project-specific plane models
    public static SeatMapConfig createAirbusA320() {
        return new SeatMapConfig("Airbus A320", 25, "ABC_DEF");
    }

    public static SeatMapConfig createBoeing737() {
        return new SeatMapConfig("Boeing 737", 27, "ABC_DEF");
    }

    public static SeatMapConfig createBoeing787() {
        return new SeatMapConfig("Boeing 787 Dreamliner", 25, "ABC_DEFG_HJK");
    }

    public static SeatMapConfig createAirbusA380() {
        return new SeatMapConfig("Airbus A380", 50, "ABC_DEFG_HJK");
    }
}
