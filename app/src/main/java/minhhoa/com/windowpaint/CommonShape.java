package minhhoa.com.windowpaint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by DELL on 10/11/2017.
 */

public class CommonShape extends ImageView {
    int x;
    int y;
    int w;
    int h;
    public RectF ellipse;
    private boolean isSelected;
    private String hexColor="#80000000";
    public String getHexColor() {
        return hexColor;
    }

    public void setHexColor(String hexColor) {
        this.hexColor = hexColor;
    }



    public CommonShape(Context context) {
        this(context, null, 0);
        x = 20;
        y = 20;
        w = 300;
        h = 225;
    }

    public CommonShape(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        x = 20;
        y = 20;
        w = 300;
        h = 225;
    }

    public CommonShape(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        x = 20;
        y = 20;
        w = 300;
        h = 225;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (ellipse.contains(event.getX(), event.getY())) {
                    isSelected = true;
                } else {
                    isSelected = false;
                }
                invalidate();
                break;
        }
        return true;
    }

    Paint paint;

    public void setSolidColor() {
        paint = new Paint();
        paint.setColor(Color.parseColor(hexColor));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setSolidColor();
        ellipse = new RectF(
                x, // Left
                y, // Top
                getWidth() - 20, // Right
                getHeight() - 20// Bottom
        );
        canvas.drawOval(ellipse, paint);
        if (isSelected) {
            drawHighlightSquares(canvas, ellipse);
        }
    }

    public void drawHighlightSquares(Canvas g2D, RectF r) {
        float x = r.left;
        float y = r.top;
        float w = r.width();
        float h = r.height();
        Paint paint = new Paint();
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.parseColor("#ff0000"));

        RectF point1 = new RectF((float) x, y, x + 3f, y + 3f);
        //points.add(point1);
        g2D.drawRect(point1, paint);
        RectF point2 = new RectF(x + (float) (w / 2), (float) (y), x + (float) (w / 2) + 3, y + 3);
        //points.add(point2);
        g2D.drawRect(point2, paint);

        RectF point3 = new RectF(x + (float) (w + 7), (float) (y + 7), x + (float) (w + 10), (y + 10));
        //points.add(point3);
        g2D.drawRect(point3, paint);

        RectF point4 = new RectF((float) x, y + h, x + 3f, y + h + 3f);
        //points.add(point4);
        g2D.drawRect(point4, paint);

        RectF point5 = new RectF(x + (float) (w / 2), (float) (y) + h, x + (float) (w / 2) + 3, y + h + 3);
        //points.add(point5);
        g2D.drawRect(point5, paint);


        RectF point6 = new RectF(x + (float) (w + 7), (float) (y + h + 7), x + (float) (w + 10), (y + h + 10));
        //points.add(point6);
        g2D.drawRect(point6, paint);
    }
}
