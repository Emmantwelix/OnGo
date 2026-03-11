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

    // Factory methods for common plane models
    public static SeatMapConfig createBoeing737() {
        return new SeatMapConfig("Boeing 737", 30, "ABC_DEF");
    }

    public static SeatMapConfig createAirbusA320() {
        return new SeatMapConfig("Airbus A320", 28, "ABC_DEF");
    }

    public static SeatMapConfig createBoeing777() {
        return new SeatMapConfig("Boeing 777", 45, "ABC_DEFG_HJK");
    }

    public static SeatMapConfig createBombardierCRJ900() {
        return new SeatMapConfig("Bombardier CRJ900", 20, "AC_DF");
    }

    public static SeatMapConfig createEmbraer190() {
        return new SeatMapConfig("Embraer 190", 25, "AC_DF");
    }
}
