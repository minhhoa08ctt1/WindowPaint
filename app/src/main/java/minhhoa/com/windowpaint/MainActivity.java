package minhhoa.com.windowpaint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
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

       /* shape.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
                String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

                ClipData dragData = new ClipData(v.getTag().toString(), mimeTypes, item);
                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(shape);

                v.startDrag(dragData, myShadow, shape, 0);
                return true;
            }
        });*/

        shape.setOnDragListener(new View.OnDragListener() {
            String msg = "";

            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        layoutParams = (ConstraintLayout.LayoutParams) v.getLayoutParams();
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_STARTED");
                        // Do nothing
                        break;

                    case DragEvent.ACTION_DRAG_ENTERED:
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENTERED");
                        int x_cord = (int) event.getX();
                        int y_cord = (int) event.getY();
                        break;

                    case DragEvent.ACTION_DRAG_EXITED:
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_EXITED");
                        /*x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        layoutParams.leftMargin = x_cord;
                        layoutParams.topMargin = y_cord;
                        v.setLayoutParams(layoutParams);*/

                        break;

                    case DragEvent.ACTION_DRAG_LOCATION:
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_LOCATION");
                        x_cord = (int) event.getX();
                        y_cord = (int) event.getY();
                        break;

                    case DragEvent.ACTION_DRAG_ENDED:
                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENDED");


                        // Do nothing
                        break;

                    case DragEvent.ACTION_DROP:
                        Log.d(msg, "ACTION_DROP event");
                        shape.x = (int) event.getX();
                        shape.y = (int) event.getY();
                        xbefore = shape.x;
                        ybefore = shape.y;
                        shape.invalidate();
                        // Do nothing
                        return false;
                    default:
                        break;
                }
                return true;
            }
        });

        shape.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        if (shape.ellipse.contains((int) event.getX(), (int) event.getY())) {
                            shape.x2 = (int) event.getX();
                            shape.y2 = (int) event.getY();
                            shape.x = shape.x + shape.x2 - shape.x1;
                            shape.y = shape.y + shape.y2 - shape.y1;
                            shape.x1 = shape.x2;
                            shape.y1 = shape.y2;
                            shape.invalidate();
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                    case MotionEvent.ACTION_UP:
                        if (shape.ellipse.contains((int) event.getX(), (int) event.getY())) {
                            shape.boundingRec = null;
                            shape.selectedShape = shape.ellipse;
                            shape.x2 = (int) event.getX();
                            shape.y2 = (int) event.getY();
                            shape.x = shape.x + shape.x2 - shape.x1;
                            shape.y = shape.y + shape.y2 - shape.y1;
                            shape.x1 = shape.x2;
                            shape.y1 = shape.y2;
                            shape.invalidate();
                        }
                        break;
                    case MotionEvent.ACTION_DOWN:
                        //shape.setVisibility(View.INVISIBLE);
                        if (shape.ellipse.contains((int) event.getX(), (int) event.getY())) {
                            shape.selectedShape = shape.ellipse;
                            shape.boundingRec = shape.ellipse;
                        } else {
                            shape.boundingRec = null;
                        }
                        shape.invalidate();
                        shape.x1 = (int) event.getX();
                        shape.y1 = (int) event.getY();
                        return true;
                }
                return true;
            }
        });
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
