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

public class FourMain extends AppCompatActivity {
    private ConstraintLayout.LayoutParams layoutParams;
    int xbefore = 0;
    int ybefore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final RectangeView shape = new RectangeView(FourMain.this);
        shape.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setContentView(R.layout.activity_main);
        ((ViewGroup) findViewById(R.id.lo_main)).addView(shape);
        findViewById(R.id.tv_zoom_mode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shape.mode == shape.ZOOM) {
                    shape.mode = shape.DRAG;
                    Toast.makeText(FourMain.this, "DRAG", Toast.LENGTH_LONG).show();
                } else {
                    shape.mode = shape.ZOOM;
                    Toast.makeText(FourMain.this, "ZOOM", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    private class RectangeView extends View {
        int loadTime = 1;
        public static final int DRAG = 1;
        public static final int ZOOM = 2;
        public static final int NONE = 3;
        public int mode = ZOOM;
        public int x, y, w, h;
        int x1, y1, x2, y2;
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
                    if (ellipse.contains((int) event.getX(), (int) event.getY()) && mode == ZOOM) {
                        x2 = (int) event.getX();
                        y2 = (int) event.getY();
                        moveX = x2 - pushPoint.x;
                        moveY = y2 - pushPoint.y;
                        if (moveX > 0 && moveY > 0) {
                            moveX = moveX+beforeMoveX;
                            moveY = moveY+beforeMoveY;
                            callOnDraw = true;
                            invalidate();
                        } else if (moveX <= 0 && moveY <= 0) {
                            moveX = beforeMoveX+moveX;
                            moveY = beforeMoveY+moveY;
                            callOnDraw = false;
                            invalidate();
                        }
                    } else if (ellipse.contains((int) event.getX(), (int) event.getY()) && mode == DRAG) {
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
                    if (ellipse.contains((int) event.getX(), (int) event.getY()) && mode == ZOOM) {
                        x2 = (int) event.getX();
                        y2 = (int) event.getY();
                        moveX = x2 - pushPoint.x;
                        moveY = y2 - pushPoint.y;
                        if (moveX > 0 && moveY > 0) {
                            beforeMoveX = moveX+beforeMoveX;
                            beforeMoveY = moveY+beforeMoveY;
                            callOnDraw = true;
                            invalidate();
                        } else if (moveX <= 0 && moveY <= 0) {
                            moveX = beforeMoveX+moveX;
                            moveY = beforeMoveY+moveY;
                            callOnDraw = false;
                            invalidate();
                        }
                    } else if (ellipse.contains((int) event.getX(), (int) event.getY()) && mode == DRAG) {
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
                    loadTime++;
                    if (ellipse.contains((int) event.getX(), (int) event.getY())) {
                        selectedShape = ellipse;
                        boundingRec = ellipse;
                    } else {
                        selectedShape = null;
                        boundingRec = null;
                    }
                    x1 = (int) event.getX();
                    y1 = (int) event.getY();
                    if (mode == ZOOM) {
                        pushPoint = new Point(x1, y1);
                    }
            }
            return true;

        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            this.canvas = canvas;
            //this.canvas = canvas;
            Paint paint = new Paint();
            int xExtra = 0;
            int yExtra = 0;
            //if (mode == ZOOM) {
            xExtra = x + moveX;
            yExtra = y + moveY;
            //} else {
            //xExtra = x;
            //yExtra = y;
            //}
            if (callOnDraw == true) {
                ellipse = new RectF(
                        x, // Left
                        y, // Top
                        w + xExtra, // Right
                        h + yExtra// Bottom
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
