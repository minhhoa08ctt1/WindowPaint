package minhhoa.com.windowpaint;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class MyActivity extends Activity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main);
        setEvent();
    }

    private void setEvent() {
        findViewById(R.id.fl_main).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ((CommonShape) ((SingleFingerView) findViewById(R.id.tiv)).getmView()).onTouchEvent(motionEvent);
                return true;
            }
        });
        findViewById(R.id.rl_yellow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CommonShape) ((SingleFingerView) findViewById(R.id.tiv)).getmView()).setHexColor("#ffff00");
            }
        });
    }
}
