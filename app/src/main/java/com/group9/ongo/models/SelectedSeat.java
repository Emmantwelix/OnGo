package com.group9.ongo.models;
import androidx.annotation.NonNull;

import java.io.Serializable;

public class SelectedSeat implements Serializable {

    private final int seatRow;

    private final String seatColumn;
    public SelectedSeat(int seatRow, String seatColumn)
    {
        this.seatRow  = seatRow;
        this.seatColumn = seatColumn;
    }

    public int getSeatRow() {
        return seatRow;
    }

    public String getSeatColumn() {
        return seatColumn;
    }

    @NonNull
    public String toString(){
        return seatRow + seatColumn;
    }
}
