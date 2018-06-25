package com.alp4.vidhiwar.healthpredictordataset;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class DataList extends ArrayAdapter<measurementData> {

    private Activity context;
    private List<measurementData> dataEntryList;


    public DataList(Activity context, List<measurementData> dataEntryList) {
        super(context, R.layout.list_layout,dataEntryList);

        this.context = context;
        this.dataEntryList = dataEntryList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout,null,true);

        TextView textViewHeight =(TextView) listViewItem.findViewById(R.id.textViewHeight);
        TextView textViewShoulders =(TextView) listViewItem.findViewById(R.id.textViewShoulders);
        TextView textViewWaist =(TextView) listViewItem.findViewById(R.id.textViewWaist);
        TextView textViewHip =(TextView) listViewItem.findViewById(R.id.textViewHip);
        TextView textViewArms =(TextView) listViewItem.findViewById(R.id.textViewArms);
        TextView textViewWrist =(TextView) listViewItem.findViewById(R.id.textViewWrist);
        TextView textViewThigh =(TextView) listViewItem.findViewById(R.id.textViewThigh);
        TextView textViewAge =(TextView) listViewItem.findViewById(R.id.textViewAge);
        TextView textViewSex =(TextView) listViewItem.findViewById(R.id.textViewSex);
        TextView textViewWeight =(TextView) listViewItem.findViewById(R.id.textViewWeight);
        TextView textViewBodyType =(TextView) listViewItem.findViewById(R.id.textViewBodyType);
        ImageView imageViewBody = (ImageView) listViewItem.findViewById(R.id.imageViewBody1);

        measurementData md = dataEntryList.get(position);

        textViewAge.setText("   Age: " + String.valueOf(md.getAge()));
        textViewHeight.setText("   Height: " + String.valueOf(md.getHeight()) + " cm");
        textViewShoulders.setText("   Shoulders: " + String.valueOf(md.getShoulders()) + " cm");
        textViewWaist.setText("   Waist: " + String.valueOf(md.getWaist()) + " cm");
        textViewHip.setText("   Hip: " + String.valueOf(md.getHip()) + " cm");
        textViewArms.setText("   Arms: " + String.valueOf(md.getArms()) + " cm");
        textViewWrist.setText("   Wrist: " + String.valueOf(md.getWrist()) + " cm");
        textViewThigh.setText("   Thigh: " + String.valueOf(md.getThigh()) + " cm");
        textViewWeight.setText("   Weight: " + String.valueOf(md.getWeight()) + " kg");
        textViewSex.setText("   " + md.getSex());
        textViewBodyType.setText("   " + md.getBodyType());
        Picasso.get().load(md.getImageUrl()).into(imageViewBody);



        return listViewItem;
    }
}

