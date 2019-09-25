package minhhoa.com.windowpaint;

import android.view.MotionEvent;
import android.view.View;

public class EraserTouchListener implements View.OnTouchListener {
    float dX;
    float dY;
    public EraserTouchListener()
    {
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:

                dX = v.getX() - event.getRawX();
                dY = v.getY() - event.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:

                v.animate()
                        .x(event.getRawX() + dX)
                        .y(event.getRawY() + dY)
                        .setDuration(0)
                        .start();
                break;
            default:
                return false;
        }
        return true;
    }
}
