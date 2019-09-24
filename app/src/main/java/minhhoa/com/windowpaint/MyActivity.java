package minhhoa.com.windowpaint;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import minhhoa.com.adapter.ShapeAddingSpinnerAdapter;
import minhhoa.com.model.ShapeAddingModel;

public class MyActivity extends Activity {
    RelativeLayout IRlAddShape;
    RelativeLayout IRlYellow;
    RelativeLayout IRlBlack;
    FrameLayout IFlMain;
    Spinner ISnShape;
    public FrameLayout getIFlMain() {
        return IFlMain;
    }
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main);
        InIt();
        InItISnShape();
        setEvent();
    }
    private void InIt()
    {
        IFlMain= findViewById(R.id.fl_main);
        IRlAddShape=findViewById(R.id.rl_add_shape);
        IRlYellow=findViewById(R.id.rl_yellow);
        IRlBlack=findViewById(R.id.rl_black);
        ISnShape=findViewById(R.id.ushape_spinner);
    }
    private void InItISnShape()
    {
        List<ShapeAddingModel> list = new ArrayList<ShapeAddingModel>();
        ShapeAddingModel model1=new ShapeAddingModel();
        model1.text="Add cloud shape";
        model1.image=R.drawable.cloudsymbol;
        list.add(model1);
        ShapeAddingModel model2=new ShapeAddingModel();
        model2.text="Add rectangle shape";
        model2.image=R.drawable.rectangle_symbol;
        list.add(model2);

        ShapeAddingSpinnerAdapter dataAdapter = new ShapeAddingSpinnerAdapter(this, list);
        //dataAdapter.setDropDownViewResource(R.layout.spinner_shape_adding);
        ISnShape.setAdapter(dataAdapter);
        ISnShape.setOnItemSelectedListener(dataAdapter);
    }
    private void setEvent() {
        IRlYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //((CommonShape) ((SingleFingerView) findViewById(R.id.tiv)).getmView()).setHexColor("#ffff00");
            }
        });
        IRlBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //((CommonShape) ((SingleFingerView) findViewById(R.id.tiv)).getmView()).setHexColor("#000000");
            }
        });
        IRlAddShape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               getFragmentManager().beginTransaction().add(findViewById(R.id.fl_main).getId(),new SingleFingerFragment()).commit();
            }
        });
    }
}
