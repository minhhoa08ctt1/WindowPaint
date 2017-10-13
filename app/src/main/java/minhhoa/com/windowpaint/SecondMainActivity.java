package minhhoa.com.windowpaint;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class SecondMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RectangeView shape = new RectangeView(SecondMainActivity.this);
        shape.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setContentView(R.layout.activity_main);
        ((ViewGroup) findViewById(R.id.lo_main)).addView(shape);
    }

    private class RectangeView extends View {
        int x, y, w, h;
        int x1, y1, x2, y2;
        RectF boundingRec;
        RectF ellipse;
        RectF selectedShape;
        Canvas canvas;

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
                    h + y // Bottom
            );
            canvas.drawOval(ellipse, paint);
            if (boundingRec != null) {
                drawHighlightSquares(canvas, ellipse);
            }
        }

        @Override
        public boolean onDragEvent(DragEvent e) {
            super.onDragEvent(e);
            if (e.getAction() == DragEvent.ACTION_DROP) {
                //if (ellipse.contains(e.getX(), e.getY())) {
                boundingRec = null;
                selectedShape = ellipse;
                x = (int) e.getX();
                y = (int) e.getY();
                /*rawX = (int) e.getX();
                rawY = (int) e.getY();
                x = x + rawX - lastX;
                y = y + rawY - lastY;
                lastX = rawX;
                lastY = rawY;
                */
                //invalidate();
                //}
            }
            return true;
        }

        @Override
        public boolean onTouchEvent(MotionEvent e) {
            super.onTouchEvent(e);

            if (e.getAction() == MotionEvent.ACTION_DOWN) {
                if (ellipse.contains(e.getX(), e.getY())) {
                    selectedShape = ellipse;
                    if (boundingRec != null)
                        boundingRec = ellipse;
                } else {
                    boundingRec = null;
                }
                x1 = (int) e.getX();
                y1 = (int) e.getY();

                ClipData dragData = ClipData.newPlainText("", "");
                DragShadowBuilder myShadow = new DragShadowBuilder(RectangeView.this);

                this.startDrag(dragData, myShadow, RectangeView.this, 0);
                return true;
            } else {
                return false;
            }
        }
    }


    public void drawHighlightSquares(Canvas g2D, RectF r) {
           /* Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(3);
            g2D.drawRect(130, 130, 180, 180, paint);
            paint.setStrokeWidth(0);
            paint.setColor(Color.CYAN);
            g2D.drawRect(133, 160, 177, 177, paint );
            paint.setColor(Color.YELLOW);
            g2D.drawRect(133, 133, 177, 160, paint );*/

        float x = r.left;
        float y = r.top;
        float w = r.width();
        float h = r.height();
        Paint paint = new Paint();
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.parseColor("#ff0000"));
        //g2D.drawRect(x,y, w,h, paint);


        //paint.setColor(Color.YELLOW);
        //g2D.drawRect(133, 133, 177, 160, paint );

        //paint.setColor(Color.RED);
        //g2D.drawRect(133, 133, 145, 150, paint);// (left,top) start point,(right,bottom)=(width,height)

        g2D.drawRect((float) x, y, x + 3f, y + 3f, paint);
        g2D.drawRect((float) (w * 0.5), (float) (y), (float) (w * 0.5) + 3, y + 3, paint);
        g2D.drawRect((float) (w + 7), (float) (y + 7), (float) (w + 10), (y + 10), paint);
           /*g2D.drawRect(new RectF((float) (x + w - 3.0), (float) (y - 3.0), 6.0f, 6.0f), paint);
            g2D.drawRect(new RectF((float) (x - 3.0), (float) (y + h * 0.5 - 3.0),  6.0f, 6.0f), paint);
            g2D.drawRect(new RectF((float) (x + w - 3.0), (float) (y + h * 0.5 - 3.0), 6.0f, 6.0f), paint);
            g2D.drawRect(new RectF((float) (x - 3.0), (float) (y + h - 3.0),  6.0f, 6.0f), paint);
            g2D.drawRect(new RectF((float) (x + w * 0.5 - 3.0), (float) (y + h - 3.0), 6.0f, 6.0f), paint);
            g2D.drawRect(new RectF((float) (x + w - 3.0), (float) (y + h - 3.0),  6.0f, 6.0f), paint);*/
    }

}
