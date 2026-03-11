package com.group9.ongo.presentation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.group9.ongo.models.Seat;

import java.util.ArrayList;
import java.util.List;

public class SeatMapView extends View {

    public interface SeatSelectionListener {
        void onSeatSelected(Seat seat);
    }

    private List<Seat> seats = new ArrayList<>();
    private int columns = 0;
    private SeatSelectionListener listener;

    private Paint availablePaint;
    private Paint occupiedPaint;
    private Paint selectedPaint;
    private Paint nosePaint;
    private Paint textPaint;

    private float seatWidth;
    private float seatHeight;
    private final float seatPadding = 10f;
    private final float cornerRadius = 15f;
    private final float noseHeight = 150f;

    public SeatMapView(Context context) {
        super(context);
        init();
    }

    public SeatMapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        availablePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        availablePaint.setColor(Color.LTGRAY);

        occupiedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        occupiedPaint.setColor(Color.parseColor("#E57373")); // Soft Red

        selectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectedPaint.setColor(Color.parseColor("#2196F3")); // Material Blue

        nosePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        nosePaint.setColor(Color.parseColor("#EEEEEE"));
        nosePaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.DKGRAY);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(30f);
    }

    public void setSeatData(List<Seat> seats, int columns) {
        this.seats = seats;
        this.columns = columns;
        invalidate(); // Redraw
    }

    public void setSeatSelectionListener(SeatSelectionListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if (columns > 0) {
            seatWidth = (width - (columns + 1) * seatPadding) / columns;
            seatHeight = seatWidth; // Square seats
            int rows = (int) Math.ceil((double) seats.size() / columns);
            int height = (int) (noseHeight + (rows * (seatHeight + seatPadding)) + seatPadding + 100); // 100 for bottom padding
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (seats == null || columns == 0) return;

        drawNose(canvas);

        for (int i = 0; i < seats.size(); i++) {
            Seat seat = seats.get(i);
            if (seat.getType() == Seat.Type.AISLE) continue;

            int row = i / columns;
            int col = i % columns;

            float left = seatPadding + col * (seatWidth + seatPadding);
            float top = noseHeight + seatPadding + row * (seatHeight + seatPadding);
            RectF rect = new RectF(left, top, left + seatWidth, top + seatHeight);

            Paint paint;
            switch (seat.getStatus()) {
                case OCCUPIED:
                    paint = occupiedPaint;
                    break;
                case SELECTED:
                    paint = selectedPaint;
                    break;
                default:
                    paint = availablePaint;
                    break;
            }

            canvas.drawRoundRect(rect, cornerRadius, cornerRadius, paint);
            
            // Draw seat label (e.g., 1A)
            canvas.drawText(seat.getLabel(), rect.centerX(), rect.centerY() + 10f, textPaint);
        }
    }

    private void drawNose(Canvas canvas) {
        Path path = new Path();
        float width = getWidth();
        path.moveTo(width * 0.2f, noseHeight);
        path.quadTo(width / 2, 0, width * 0.8f, noseHeight);
        path.close();
        canvas.drawPath(path, nosePaint);
    }
}
