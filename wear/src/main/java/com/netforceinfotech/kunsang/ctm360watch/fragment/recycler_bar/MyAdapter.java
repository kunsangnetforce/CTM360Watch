package com.netforceinfotech.kunsang.ctm360watch.fragment.recycler_bar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.netforceinfotech.kunsang.ctm360watch.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by John on 8/29/2016.
 */
public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int YOU = 0;
    private static final int THEM = 1;
    private final LayoutInflater inflater;
    private List<MyData> itemList;
    private Context context;
    ArrayList<Boolean> booleanGames = new ArrayList<>();
    MyHolder viewHolder;
    int total;


    public MyAdapter(Context context, List<MyData> itemList, int total) {
        this.itemList = itemList;
        this.total = total;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_bar, parent, false);
        viewHolder = new MyHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        MyHolder myHolder = (MyHolder) holder;
        MyData myData = itemList.get(position);
        myHolder.textViewCount.setText(myData.number + "");
        myHolder.textViewName.setText(myData.name);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getHeight(myData.number, total));
        myHolder.view1.setLayoutParams(layoutParams);
        myHolder.view1.setBackgroundColor(myData.color);


    }

    private int getHeight(int number, int total) {
        return (number * 70) / total;

    }


    @Override
    public int getItemCount() {
        return itemList.size();
        // return itemList.size();
//        return itemList.size();
    }


}