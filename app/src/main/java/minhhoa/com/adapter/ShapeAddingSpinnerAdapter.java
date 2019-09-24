package minhhoa.com.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

import minhhoa.com.model.ShapeAddingModel;
import minhhoa.com.windowpaint.CommonShape;
import minhhoa.com.windowpaint.MyActivity;
import minhhoa.com.windowpaint.R;
import minhhoa.com.windowpaint.SingleFingerView;

public class ShapeAddingSpinnerAdapter extends ArrayAdapter<minhhoa.com.model.ShapeAddingModel> implements   AdapterView.OnItemSelectedListener  {
    List<minhhoa.com.model.ShapeAddingModel> IMyData;
    MyActivity IMyApp;
    public ShapeAddingSpinnerAdapter(@NonNull Context context,List<minhhoa.com.model.ShapeAddingModel> data) {
        super(context,0,data);
        IMyData=data;
        IMyApp= (MyActivity) context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        minhhoa.com.model.ShapeAddingModel item = (minhhoa.com.model.ShapeAddingModel) IMyData.get(position);
        View v= IMyApp.getLayoutInflater().inflate(R.layout.spinner_shape_adding, parent, false);
        ImageView IIMgView= (ImageView)v.findViewById(R.id.UIv_shape);
        IIMgView.setImageResource(item.image);

        return v;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ShapeAddingModel item = (ShapeAddingModel) parent.getItemAtPosition(position);
        if(item.text.equals("Add cloud shape"))
        {
            SingleFingerView shape= (SingleFingerView)IMyApp.getLayoutInflater().inflate(R.layout.fragment_single_finger, null);
            shape.getmView().setMode(CommonShape.Mode.DRAW);
            shape.getmView().setDrawer(CommonShape.Drawer.CLOUD);
            shape.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
            IMyApp.getIFlMain().addView(shape);
        }
        else
        if(item.text.equals("Add rectangle shape"))
        {
            SingleFingerView shape= (SingleFingerView)IMyApp.getLayoutInflater().inflate(R.layout.fragment_single_finger, null);
            shape.getmView().setMode(CommonShape.Mode.DRAW);
            shape.getmView().setDrawer(CommonShape.Drawer.RECTANGLE);
            shape.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
            IMyApp.getIFlMain().addView(shape);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
