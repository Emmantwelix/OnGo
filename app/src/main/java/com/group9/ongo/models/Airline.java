package com.group9.ongo.models;

import com.group9.ongo.R;

public enum Airline {
    AIR_CANADA("Air Canada", R.drawable.logo_aircanada),
    WESTJET("Westjet", R.drawable.logo_westjet),
    PORTER_AIRLINES("Porter Airlines", R.drawable.logo_porter),
    AIR_TRANSAT("Air Transat", R.drawable.logo_airtransat),
    UNKNOWN("Unknown", R.drawable.baseline_flight_24);

    private final String displayName;
    private final int logoResId;

    Airline(String displayName, int logoResId) {
        this.displayName = displayName;
        this.logoResId = logoResId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getLogoResId() {
        return logoResId;
    }

    public static Airline fromName(String name) {
        for (Airline airline : Airline.values()) {
            if (airline.displayName.equalsIgnoreCase(name)) {
                return airline;
            }
        }
        return UNKNOWN;
    }
}
