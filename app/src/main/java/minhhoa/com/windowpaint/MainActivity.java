package minhhoa.com.windowpaint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {
    private ConstraintLayout.LayoutParams layoutParams;
    int xbefore = 0;
    int ybefore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final RectangeView shape = new RectangeView(MainActivity.this);
        shape.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setContentView(R.layout.activity_main);
        ((ViewGroup) findViewById(R.id.lo_main)).addView(shape);
    }

    private class RectangeView extends View {
        public int x, y, w, h;
        int x1, y1, x2, y2;
        RectF boundingRec;
        public RectF ellipse;
        public RectF selectedShape;
        public Canvas canvas;

        public RectangeView(Context context) {
            super(context);
            x = 20;
            y = 20;
            w = 300;
            h = 225;

        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    if (ellipse.contains((int) event.getX(), (int) event.getY())) {
                        x2 = (int) event.getX();
                        y2 = (int) event.getY();
                        x = x + x2 - x1;
                        y = y + y2 - y1;
                        x1 = x2;
                        y1 = y2;
                        invalidate();
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                    break;
                case MotionEvent.ACTION_UP:
                    if (ellipse.contains((int) event.getX(), (int) event.getY())) {
                        boundingRec = null;
                        selectedShape = ellipse;
                        x2 = (int) event.getX();
                        y2 = (int) event.getY();
                        x = x + x2 - x1;
                        y = y + y2 - y1;
                        x1 = x2;
                        y1 = y2;
                        invalidate();
                    }
                    break;
                case MotionEvent.ACTION_DOWN:
                    //shape.setVisibility(View.INVISIBLE);
                    if (ellipse.contains((int) event.getX(), (int) event.getY())) {
                        selectedShape = ellipse;
                        boundingRec = ellipse;
                    } else {
                        boundingRec = null;
                    }
                    invalidate();
                    x1 = (int) event.getX();
                    y1 = (int) event.getY();
                    return true;
            }
            return true;

        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            //this.canvas = canvas;
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            ellipse = new RectF(
                    x, // Left
                    y, // Top
                    w + x, // Right
                    h + y// Bottom
            );
            canvas.drawOval(ellipse, paint);
            if (boundingRec != null) {
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

            g2D.drawRect((float) x, y, x + 3f, y + 3f, paint);
            g2D.drawRect(x + (float) (w / 2), (float) (y), x + (float) (w / 2) + 3, y + 3, paint);
            g2D.drawRect(x + (float) (w + 7), (float) (y + 7), x + (float) (w + 10), (y + 10), paint);

            g2D.drawRect((float) x, y + h, x + 3f, y + h + 3f, paint);
            g2D.drawRect(x + (float) (w / 2), (float) (y) + h, x + (float) (w / 2) + 3, y + h + 3, paint);
            g2D.drawRect(x + (float) (w + 7), (float) (y + h + 7), x + (float) (w + 10), (y + h + 10), paint);
        }
    }
}
