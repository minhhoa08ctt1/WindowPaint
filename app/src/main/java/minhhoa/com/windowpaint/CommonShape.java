package minhhoa.com.windowpaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 10/11/2017.
 */

public class CommonShape extends ImageView {
    // Enumeration for Mode
    public enum Mode {
        DRAW,
        TEXT,
        ERASER;
    }

    // Enumeration for Drawer
    public enum Drawer {
        PEN,
        LINE,
        RECTANGLE,
        CIRCLE,
        ELLIPSE,
        QUADRATIC_BEZIER,
        QUBIC_BEZIER,
        CLOUD;
    }

    int x;
    int y;
    int w;
    int h;
    public RectF ellipse;
    private boolean isSelected;
    private String hexColor = "#80000000";
    private Paint paint;


    private Context context = null;
    private Canvas canvas = null;
    private Bitmap bitmap = null;

    private List<Path> pathLists = new ArrayList<Path>();
    private List<Paint> paintLists = new ArrayList<Paint>();

    // for Eraser
    private int baseColor = Color.TRANSPARENT; //Color.WHITE;

    // for Undo, Redo
    private int historyPointer = 0;

    // Flags
    private Mode mode = Mode.DRAW;
    private Drawer drawer = Drawer.PEN;
    private boolean isDown = false;

    // for Paint
    private Paint.Style paintStyle = Paint.Style.STROKE;
    private int paintStrokeColor = Color.BLACK;
    private int paintFillColor = Color.BLACK;
    private float paintStrokeWidth = 3F;
    private int opacity = 255;
    private float blur = 0F;
    private Paint.Cap lineCap = Paint.Cap.ROUND;

    // for Text
    private String text = "";
    private Typeface fontFamily = Typeface.DEFAULT;
    private float fontSize = 32F;
    private Paint.Align textAlign = Paint.Align.RIGHT;  // fixed
    private Paint textPaint = new Paint();
    private float textX = 0F;
    private float textY = 0F;

    // for Drawer
    private float startX = 0F;
    private float startY = 0F;
    private float controlX = 0F;
    private float controlY = 0F;

    /**
     * This method creates the instance of Paint.
     * In addition, this method sets styles for Paint.
     *
     * @return paint This is returned as the instance of Paint
     */
    private Paint createPaint() {
        Paint paint = new Paint();

        paint.setAntiAlias(true);
        paint.setStyle(this.paintStyle);
        paint.setStrokeWidth(this.paintStrokeWidth);
        paint.setStrokeCap(this.lineCap);
        paint.setStrokeJoin(Paint.Join.MITER);  // fixed

        // for Text
        if (this.mode == Mode.TEXT) {
            paint.setTypeface(this.fontFamily);
            paint.setTextSize(this.fontSize);
            paint.setTextAlign(this.textAlign);
            paint.setStrokeWidth(0F);
        }

        if (this.mode == Mode.ERASER) {
            // Eraser
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            paint.setARGB(0, 0, 0, 0);

            // paint.setColor(this.baseColor);
            // paint.setShadowLayer(this.blur, 0F, 0F, this.baseColor);
        } else {
            // Otherwise
            paint.setColor(this.paintStrokeColor);
            paint.setShadowLayer(this.blur, 0F, 0F, this.paintStrokeColor);
            paint.setAlpha(this.opacity);
        }

        return paint;
    }

    /**
     * Common initialization.
     *
     * @param context
     */
    private void setup(Context context) {
        this.context = context;

        this.pathLists.add(new Path());
        this.paintLists.add(this.createPaint());
        this.historyPointer++;

        this.textPaint.setARGB(0, 255, 255, 255);
    }

    /**
     * This method initialize Path.
     * Namely, this method creates the instance of Path,
     * and moves current position.
     *
     * @param event This is argument of onTouchEvent method
     * @return path This is returned as the instance of Path
     */
    private Path createPath(MotionEvent event) {
        Path path = new Path();

        // Save for ACTION_MOVE
        this.startX = event.getX();
        this.startY = event.getY();

        path.moveTo(this.startX, this.startY);

        return path;
    }

    /**
     * This method updates the lists for the instance of Path and Paint.
     * "Undo" and "Redo" are enabled by this method.
     *
     * @param path   the instance of Path
     * @param @paint the instance of Paint
     */
    private void updateHistory(Path path) {
        if (this.historyPointer == this.pathLists.size()) {
            this.pathLists.add(path);
            this.paintLists.add(this.createPaint());
            this.historyPointer++;
        } else {
            // On the way of Undo or Redo
            this.pathLists.set(this.historyPointer, path);
            this.paintLists.set(this.historyPointer, this.createPaint());
            this.historyPointer++;

            for (int i = this.historyPointer, size = this.paintLists.size(); i < size; i++) {
                this.pathLists.remove(this.historyPointer);
                this.paintLists.remove(this.historyPointer);
            }
        }
    }

    /**
     * This method gets the instance of Path that pointer indicates.
     *
     * @return the instance of Path
     */
    private Path getCurrentPath() {
        return this.pathLists.get(this.historyPointer - 1);
    }

    /**
     * This method set event listener for drawing.
     *
     * @param event the instance of MotionEvent
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.onActionDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                //this.onActionMove(event);
                break;
            case MotionEvent.ACTION_UP:
                this.onActionUp(event);
                break;
            default:
                break;
        }

        // Re draw
        this.invalidate();

        return true;
    }

    /**
     * This method defines processes on MotionEvent.ACTION_DOWN
     *
     * @param event This is argument of onTouchEvent method
     */
    private void onActionDown(MotionEvent event) {
        switch (this.mode) {
            case DRAW:
            case ERASER:
                /*if (ellipse.contains(event.getX(), event.getY())) {
                    isSelected = true;
                } else {
                    isSelected = false;
                }
                invalidate();
                break;*/
                if ((this.drawer != Drawer.QUADRATIC_BEZIER) && (this.drawer != Drawer.QUBIC_BEZIER)) {
                    // Oherwise
                    this.updateHistory(this.createPath(event));
                    this.isDown = true;
                } else {
                    // Bezier
                    if ((this.startX == 0F) && (this.startY == 0F)) {
                        // The 1st tap
                        this.updateHistory(this.createPath(event));
                    } else {
                        // The 2nd tap
                        this.controlX = event.getX();
                        this.controlY = event.getY();

                        this.isDown = true;
                    }
                }

                break;
            case TEXT:
                this.startX = event.getX();
                this.startY = event.getY();

                break;
            default:
                break;
        }
    }

    /**
     * This method defines processes on MotionEvent.ACTION_MOVE
     *
     * @param event This is argument of onTouchEvent method
     */
    private void onActionMove(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (this.mode) {
            case DRAW:
            case ERASER:

                if ((this.drawer != Drawer.QUADRATIC_BEZIER) && (this.drawer != Drawer.QUBIC_BEZIER)) {
                    if (!isDown) {
                        return;
                    }

                    Path path = this.getCurrentPath();

                    switch (this.drawer) {
                        case PEN:
                            path.lineTo(x, y + getHeight());
                            break;
                        case LINE:
                            path.reset();
                            path.moveTo(this.startX, this.startY);
                            path.lineTo(x, y);
                            break;
                        case RECTANGLE:
                            path.reset();
                            path.addRect(this.startX, this.startY, x, y, Path.Direction.CCW);
                            break;
                        case CIRCLE:
                            double distanceX = Math.abs((double) (this.startX - x));
                            double distanceY = Math.abs((double) (this.startY - y));
                            double radius = Math.sqrt(Math.pow(distanceX, 2.0) + Math.pow(distanceY, 2.0));

                            path.reset();
                            path.addCircle(this.startX, this.startY, (float) radius, Path.Direction.CCW);
                            break;
                        case ELLIPSE:
                            RectF rect = new RectF(this.startX, this.startY, x, y);

                            path.reset();
                            path.addOval(rect, Path.Direction.CCW);
                            break;
                        case CLOUD:
                            int dividend = 1;
                            path = this.getCurrentPath();
                            path.cubicTo((startX - 40) / dividend, (startY + 20) / dividend, (startX - 40) / dividend, (startY + 70) / dividend, (startX + 60) / dividend, (startY + 70) / dividend);
                            path.cubicTo((startX + 80) / dividend, (startY + 100) / dividend, (startX + 150) / dividend, (startY + 100) / dividend, (startX + 170) / dividend, (startY + 70) / dividend);
                            path.cubicTo((startX + 250) / dividend, (startY + 70) / dividend, (startX + 250) / dividend, (startY + 40) / dividend, (startX + 220) / dividend, (startY + 20) / dividend);
                            path.cubicTo((startX + 260) / dividend, (startY - 40) / dividend, (startX + 200) / dividend, (startY - 50) / dividend, (startX + 170) / dividend, (startY - 30) / dividend);
                            path.cubicTo((startX + 150) / dividend, (startY - 75) / dividend, (startX + 80) / dividend, (startY - 60) / dividend, (startX + 80) / dividend, (startY - 30) / dividend);
                            path.cubicTo((startX + 30) / dividend, (startY - 75) / dividend, (startX - 20) / dividend, (startY - 60) / dividend, (startX) / dividend, (startY) / dividend);
                            int dx = 0;
                            int dy = 0;
                            //if (event.getX() > startX) {
                            dx = (int) (event.getX() / startX);
                            //} else {
                            // dx = (int) (startX / event.getX());
                            //}
                            //if (event.getY() > startY) {
                            dy = (int) (event.getY() / startY);
                            //} else {
                            //dy = (int) (startY / event.getY());
                            //}
                            Matrix matrix = new Matrix();
                            matrix.setScale(dx / 1.5f, dy / 1.5f);
                            path.transform(matrix);

                            path.moveTo(this.startX, this.startY);
                            break;
                        default:
                            break;
                    }
                } else {
                    if (!isDown) {
                        return;
                    }

                    Path path = this.getCurrentPath();

                    path.reset();
                    path.moveTo(this.startX, this.startY);
                    path.quadTo(this.controlX, this.controlY, x, y);
                }

                break;
            case TEXT:
                this.startX = x;
                this.startY = y;

                break;
            default:
                break;
        }
    }

    /**
     * This method defines processes on MotionEvent.ACTION_DOWN
     *
     * @param event This is argument of onTouchEvent method
     */
    private void onActionUp(MotionEvent event) {
        if (isDown) {


            this.startX = 0F;
            this.startY = 0F;
            this.isDown = false;
        }
    }

    public String getHexColor() {
        return hexColor;
    }

    public void setHexColor(String hexColor) {
        this.hexColor = hexColor;
    }


    public CommonShape(Context context) {
        this(context, null, 0);
        setup(context);
        x = 20;
        y = 20;
        w = 300;
        h = 225;
    }

    public CommonShape(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        setup(context);
        x = 20;
        y = 20;
        w = 300;
        h = 225;
    }

    public CommonShape(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup(context);
        x = 20;
        y = 20;
        w = 300;
        h = 225;
    }

    /**
     * This method draws text.
     *
     * @param canvas the instance of Canvas
     */
    private void drawText(Canvas canvas) {
        if (this.text.length() <= 0) {
            return;
        }

        if (this.mode == Mode.TEXT) {
            this.textX = this.startX;
            this.textY = this.startY;

            this.textPaint = this.createPaint();
        }

        float textX = this.textX;
        float textY = this.textY;

        Paint paintForMeasureText = new Paint();

        // Line break automatically
        float textLength = paintForMeasureText.measureText(this.text);
        float lengthOfChar = textLength / (float) this.text.length();
        float restWidth = this.canvas.getWidth() - textX;  // text-align : right
        int numChars = (lengthOfChar <= 0) ? 1 : (int) Math.floor((double) (restWidth / lengthOfChar));  // The number of characters at 1 line
        int modNumChars = (numChars < 1) ? 1 : numChars;
        float y = textY;

        for (int i = 0, len = this.text.length(); i < len; i += modNumChars) {
            String substring = "";

            if ((i + modNumChars) < len) {
                substring = this.text.substring(i, (i + modNumChars));
            } else {
                substring = this.text.substring(i, len);
            }

            y += this.fontSize;

            canvas.drawText(substring, textX, y, this.textPaint);
        }
    }

    public void setSolidColor() {
        paint = new Paint();
        paint.setColor(Color.parseColor(hexColor));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*setSolidColor();
        ellipse = new RectF(
                x, // Left
                y, // Top
                getWidth() - 20, // Right
                getHeight() - 20// Bottom
        );
        canvas.drawOval(ellipse, paint);
        if (isSelected) {
            drawHighlightSquares(canvas, ellipse);
        }*/
        Path path = this.getCurrentPath();
        for (int i = 0; i < pathLists.size(); i++) {
            if (pathLists.get(i) != path) {
                pathLists.get(i).reset();
            }
        }
        //pathLists.clear();
        // Before "drawPath"
        canvas.drawColor(this.baseColor);

        if (this.bitmap != null) {
            canvas.drawBitmap(this.bitmap, 0F, 0F, new Paint());
        }
        drawer = drawer.ELLIPSE;
        switch (drawer) {
            case CIRCLE:
                path.reset();
                path.addCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - 150, Path.Direction.CCW);
                break;
            case ELLIPSE:
                path.reset();
                ellipse = new RectF(
                        x, // Left
                        y, // Top
                        getWidth() - 20, // Right
                        getHeight() - 20// Bottom
                );
                path.addOval(ellipse, Path.Direction.CCW);
        }
        //for (int i = 0; i < this.historyPointer; i++) {
        Paint paint = this.createPaint();
        canvas.drawPath(path, paint);
        //}

        this.drawText(canvas);
        this.canvas = canvas;
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
