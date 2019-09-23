package minhhoa.com.windowpaint;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

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
                //((CommonShape) ((SingleFingerView) findViewById(R.id.tiv)).getmView()).onTouchEvent(motionEvent);
                for(int i=1;i< ((ViewGroup)findViewById(R.id.fl_main)).getChildCount();i++) {
                    ((SingleFingerView) ((ViewGroup) findViewById(R.id.fl_main)).getChildAt(((ViewGroup) findViewById(R.id.fl_main)).getChildCount() - 1)).getmView().onTouchEvent(motionEvent);
                }
                return true;
            }
        });
        findViewById(R.id.rl_yellow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //((CommonShape) ((SingleFingerView) findViewById(R.id.tiv)).getmView()).setHexColor("#ffff00");
            }
        });
        findViewById(R.id.rl_black).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //((CommonShape) ((SingleFingerView) findViewById(R.id.tiv)).getmView()).setHexColor("#000000");
            }
        });
        findViewById(R.id.rl_add_shape).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               getFragmentManager().beginTransaction().add(findViewById(R.id.fl_main).getId(),new SingleFingerFragment()).commit();
            }
        });
    }
}
