package com.group9.ongo.presentation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import androidx.annotation.Nullable;

import com.group9.ongo.R;
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
    private final float bottomMargin = 150f;

    // Zoom and Pan variables
    private ScaleGestureDetector scaleGestureDetector;
    private GestureDetector gestureDetector;
    private float scaleFactor = 1.0f;
    private float translateX = 0f;
    private float translateY = 0f;

    public SeatMapView(Context context) {
        super(context);
        init(context);
    }

    public SeatMapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        availablePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        availablePaint.setColor(Color.LTGRAY);

        occupiedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        occupiedPaint.setColor(getContext().getColor(R.color.seat_occupied));

        selectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        selectedPaint.setColor(getContext().getColor(R.color.seat_selected));

        nosePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        nosePaint.setColor(getContext().getColor(R.color.aircraft_nose));
        nosePaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.DKGRAY);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(30f);

        // Initialize Zoom detector
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());

        // Initialize Pan/Tap detector
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    public void setSeatData(List<Seat> seats, int columns) {
        this.seats = seats;
        this.columns = columns;
        invalidate(); // Redraw
    }

    public void setSeatSelectionListener(SeatSelectionListener listener) {
        this.listener = listener;
    }

    /**
     * Helper for programmatic seat selection (e.g. for testing).
     */
    public void selectSeat(int row, String label) {
        for (Seat seat : seats) {
            if (seat.getRow() == row && seat.getLabel().equalsIgnoreCase(label)) {
                handleSeatClick(seat);
                return;
            }
        }
    }

    private float getTotalModelHeight() {
        if (columns <= 0) return 0;
        int rows = (int) Math.ceil((double) seats.size() / columns);
        return noseHeight + (rows * (seatHeight + seatPadding)) + bottomMargin;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if (columns > 0) {
            seatWidth = (width - (columns + 1) * seatPadding) / columns;
            seatHeight = seatWidth;
            setMeasuredDimension(width, MeasureSpec.getSize(heightMeasureSpec));
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (seats == null || columns == 0) return;

        canvas.save();
        
        // Apply Panning and Zooming
        canvas.translate(translateX, translateY);
        canvas.scale(scaleFactor, scaleFactor);

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
            
            // Draw label only if seat is not occupied or selected for cleaner look, or always if requested.
            // Let's keep drawing it but maybe change color if it's selected/occupied.
            canvas.drawText(seat.getLabel(), rect.centerX(), rect.centerY() + 10f, textPaint);
        }

        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(0.5f, Math.min(scaleFactor, 3.0f));
            constrainTranslation();
            invalidate();
            return true;
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            translateX -= distanceX;
            translateY -= distanceY;
            constrainTranslation();
            invalidate();
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            float adjustedX = (e.getX() - translateX) / scaleFactor;
            float adjustedY = (e.getY() - translateY) / scaleFactor;

            for (int i = 0; i < seats.size(); i++) {
                Seat seat = seats.get(i);
                if (seat.getType() == Seat.Type.AISLE) continue;

                int row = i / columns;
                int col = i % columns;

                float left = seatPadding + col * (seatWidth + seatPadding);
                float top = noseHeight + seatPadding + row * (seatHeight + seatPadding);
                RectF rect = new RectF(left, top, left + seatWidth, top + seatHeight);

                if (rect.contains(adjustedX, adjustedY)) {
                    handleSeatClick(seat);
                    return true;
                }
            }
            return false;
        }
    }

    private void constrainTranslation() {
        float scaledContentWidth = getWidth() * scaleFactor;
        float scaledContentHeight = getTotalModelHeight() * scaleFactor;

        // X constraints
        if (scaledContentWidth <= getWidth()) {
            translateX = (getWidth() - scaledContentWidth) / 2;
        } else {
            translateX = Math.min(0, Math.max(translateX, getWidth() - scaledContentWidth));
        }

        // Y constraints
        if (scaledContentHeight <= getHeight()) {
            translateY = 0;
        } else {
            translateY = Math.min(0, Math.max(translateY, getHeight() - scaledContentHeight));
        }
    }

    private void handleSeatClick(Seat clickedSeat) {
        // PREVENT CLICKING OCCUPIED SEATS
        if (clickedSeat.getStatus() == Seat.Status.OCCUPIED) return;

        if (clickedSeat.getStatus() == Seat.Status.SELECTED) {
            clickedSeat.setStatus(Seat.Status.AVAILABLE);
        } else {
            for (Seat seat : seats) {
                if (seat.getStatus() == Seat.Status.SELECTED) {
                    seat.setStatus(Seat.Status.AVAILABLE);
                }
            }
            clickedSeat.setStatus(Seat.Status.SELECTED);
        }

        invalidate();
        if (listener != null) {
            listener.onSeatSelected(clickedSeat);
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
