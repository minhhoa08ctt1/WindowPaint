package minhhoa.com.windowpaint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

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
        findViewById(R.id.tv_zoom_mode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shape.mode == shape.ZOOM) {
                    shape.mode = shape.DRAG;
                    Toast.makeText(MainActivity.this, "DRAG", Toast.LENGTH_LONG).show();
                } else {
                    shape.mode = shape.ZOOM;
                    Toast.makeText(MainActivity.this, "ZOOM", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    private class RectangeView extends View {
        public static final int DRAG = 1;
        public static final int ZOOM = 2;
        public static final int NONE = 3;
        public int mode = ZOOM;
        public int x, y, w, h;
        int lastX, lastY, rawX, rawY;
        RectF boundingRec = new RectF();
        public RectF ellipse;
        public RectF selectedShape;
        private List<RectF> points = new ArrayList<>();
        Point pushPoint;
        int moveX;
        int moveY;
        int beforeMoveX;
        int beforeMoveY;
        List<RectF> history = new ArrayList<>();
        Canvas canvas;
        boolean callOnDraw = true;
        int lastImgWidth;
        int lastImgHeight;
        int lastImgLeft;
        int lastImgTop;
        Point mViewCenter;
        int xExtra = 300;
        int yExtra = 225;
        int newWidth;
        int newHeight;

        public RectangeView(Context context) {
            super(context);
            x = 20;
            y = 20;
            w = 300;
            h = 225;

        }

        private float getDistance(Point a, Point b) {
            float v = ((a.x - b.x) * (a.x - b.x)) + ((a.y - b.y) * (a.y - b.y));
            return ((int) (Math.sqrt(v) * 100)) / 100f;
        }

        private void refreshImageCenter() {
            int x = (int) (ellipse.left + ellipse.width() / 2);
            int y = (int) (ellipse.right + ellipse.height() / 2);
            mViewCenter = new Point(x, y);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    if (ellipse.contains((int) event.getX(), (int) event.getY()) && mode == ZOOM) {
                        rawX = (int) event.getX();
                        rawY = (int) event.getY();
                        //moveX = rawX - pushPoint.x;
                        //moveY = rawY - pushPoint.y;
                        if (lastX != -1) {
                            if (Math.abs(rawX - lastX) < 5 && Math.abs(rawY - lastY) < 5) {
                                callOnDraw = false;
                                return false;
                            } else {
                                callOnDraw = true;
                            }
                        }

                        Point O = mViewCenter, A = pushPoint, B = new Point(rawX, rawY);
                        float dOA = getDistance(O, A);
                        float dOB = getDistance(O, B);
                        float f = dOB / dOA;

                        int newWidth = (int) (lastImgWidth * f);
                        int newHeight = (int) (lastImgHeight * f);

                        //x = lastImgLeft - ((newWidth - lastImgWidth) / 2);
                        //y = lastImgTop - ((newHeight - lastImgHeight) / 2);

                        xExtra = newWidth;
                        yExtra = newHeight;

                        lastX = rawX;
                        lastY = rawY;
                        invalidate();
                    }
                    if (ellipse.contains((int) event.getX(), (int) event.getY()) && mode == DRAG) {
                        callOnDraw = true;
                        rawX = (int) event.getX();
                        rawY = (int) event.getY();
                        //moveX = rawX - pushPoint.x;
                        //moveY = rawY - pushPoint.y;
                        if (lastX != -1) {
                            if (Math.abs(rawX - lastX) < 5 && Math.abs(rawY - lastY) < 5) {
                                callOnDraw = false;
                                return false;
                            } else {
                                callOnDraw = true;
                            }
                        }
                        Point O = mViewCenter, A = pushPoint, B = new Point(rawX, rawY);
                        float dOA = getDistance(O, A);
                        float dOB = getDistance(O, B);
                        float f = dOB / dOA;

                        int newWidth = (int) (lastImgWidth * f);
                        int newHeight = (int) (lastImgHeight * f);

                        x = lastImgLeft - ((newWidth - lastImgWidth) / 2);
                        y = lastImgTop - ((newHeight - lastImgHeight) / 2);

                        //x = x + rawX - lastX;
                        //y = y + rawY - lastY;
                        //xExtra = x + (int) ellipse.width();
                        //yExtra = y + (int) ellipse.height();
                        xExtra = newWidth;
                        yExtra = newHeight;
                        lastX = rawX;
                        lastY = rawY;
                        invalidate();
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                case MotionEvent.ACTION_DOWN:
                    if (ellipse.contains((int) event.getX(), (int) event.getY())) {
                        selectedShape = ellipse;
                        boundingRec = ellipse;
                    } else {
                        selectedShape = null;
                        boundingRec = null;
                    }
                    lastX = (int) event.getX();
                    lastY = (int) event.getY();
                    pushPoint = new Point(lastX, lastY);
                    //if (mode == ZOOM) {
                    lastImgWidth = (int) ellipse.width();
                    lastImgHeight = (int) ellipse.height();
                    lastImgLeft = (int) ellipse.left;
                    lastImgTop = (int) ellipse.top;
                    //}
                    refreshImageCenter();
            }
            return true;

        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            this.canvas = canvas;
            //this.canvas = canvas;
            Paint paint = new Paint();

            if (callOnDraw == true) {
                ellipse = new RectF(
                        x, // Left
                        y, // Top
                        xExtra, // Right
                        yExtra// Bottom
                );
            }
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

            RectF point1 = new RectF((float) x, y, x + 3f, y + 3f);
            points.add(point1);
            g2D.drawRect(point1, paint);
            RectF point2 = new RectF(x + (float) (w / 2), (float) (y), x + (float) (w / 2) + 3, y + 3);
            points.add(point2);
            g2D.drawRect(point2, paint);

            RectF point3 = new RectF(x + (float) (w + 7), (float) (y + 7), x + (float) (w + 10), (y + 10));
            points.add(point3);
            g2D.drawRect(point3, paint);

            RectF point4 = new RectF((float) x, y + h, x + 3f, y + h + 3f);
            points.add(point4);
            g2D.drawRect(point4, paint);

            RectF point5 = new RectF(x + (float) (w / 2), (float) (y) + h, x + (float) (w / 2) + 3, y + h + 3);
            points.add(point5);
            g2D.drawRect(point5, paint);


            RectF point6 = new RectF(x + (float) (w + 7), (float) (y + h + 7), x + (float) (w + 10), (y + h + 10));
            points.add(point6);
            g2D.drawRect(point6, paint);
        }
    }
}
