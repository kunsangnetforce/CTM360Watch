package com.netforceinfotech.kunsang.ctm360watch.fragment.recycler_bar;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.netforceinfotech.kunsang.ctm360watch.R;


/**
 * Created by John on 9/7/2016.
 */
public class MyHolder extends RecyclerView.ViewHolder {


    View view1;
    TextView textViewCount,textViewName;
    View view;

    public MyHolder(View itemView) {
        super(itemView);
        //implementing onClickListener
        view = itemView;
        view1= (View) view.findViewById(R.id.view);
        textViewCount= (TextView) view.findViewById(R.id.textViewCount);
        textViewName= (TextView) view.findViewById(R.id.textViewName);

      /*  RotateAnimation ranim = (RotateAnimation) AnimationUtils.loadAnimation(view.getContext(), R.anim.rotate);
        ranim.setFillAfter(true); //For the textview to remain at the same place after the rotation
        textViewTime.setAnimation(ranim);*/

    }
}
