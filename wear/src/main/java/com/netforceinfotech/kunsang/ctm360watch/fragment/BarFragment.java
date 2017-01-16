package com.netforceinfotech.kunsang.ctm360watch.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.netforceinfotech.kunsang.ctm360watch.R;
import com.netforceinfotech.kunsang.ctm360watch.fragment.recycler_bar.MyAdapter;
import com.netforceinfotech.kunsang.ctm360watch.fragment.recycler_bar.MyData;
import com.netforceinfotech.kunsang.ctm360watch.general.UserSessionManager;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class BarFragment extends Fragment implements View.OnClickListener {


    private org.eazegraph.lib.charts.BarChart mBarChart;
    private int xPosition;
    ArrayList<MyData> myDatas = new ArrayList<>();
    private MyAdapter myAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    UserSessionManager userSessionManager;
    Context context;
    String title = "";
    private int total;

    public BarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bar, container, false);
        context = getActivity();
        userSessionManager = new UserSessionManager(context);
        JsonParser parser = new JsonParser();
        JsonObject o = parser.parse(userSessionManager.getJsonData()).getAsJsonObject();
        try {
            title = this.getArguments().getString("title");
        } catch (Exception ex) {
            showMessage("bundle error");
        }
        switch (title) {
            case "status":
                setupStatusData(o);
                break;
            case "severity":
                setupSeverityData(o);
                break;
            case "summary":
                setupSummaryData(o);
                break;

        }
        initView(view);
        setupRecycler(view);
        return view;
    }

    private void setupSummaryData(JsonObject o) {
        try {
            myDatas.clear();
        } catch (Exception ex) {

        }

        JsonObject summary = o.getAsJsonObject("summary");
        total = summary.get("total").getAsInt();
        myDatas.add(new MyData(summary.get("total").getAsInt(), "Total", ContextCompat.getColor(getActivity(), R.color.total)));
        myDatas.add(new MyData(summary.get("closed").getAsInt(), "Closed", ContextCompat.getColor(getActivity(), R.color.close)));
        myDatas.add(new MyData(summary.get("open").getAsInt(), "Open", ContextCompat.getColor(getActivity(), R.color.open)));
        myDatas.add(new MyData(summary.get("resolved").getAsInt(), "Resolved", ContextCompat.getColor(getActivity(), R.color.resolved)));

    }

    private void setupSeverityData(JsonObject o) {
        try {
            myDatas.clear();
        } catch (Exception ex) {

        }

        JsonObject severity = o.getAsJsonObject("severity");
        total = severity.get("total").getAsInt();
        myDatas.add(new MyData(severity.get("total").getAsInt(), "Total", ContextCompat.getColor(getActivity(), R.color.total)));
        myDatas.add(new MyData(severity.get("fyi").getAsInt(), "FYI", ContextCompat.getColor(getActivity(), R.color.fyi)));
        myDatas.add(new MyData(severity.get("high").getAsInt(), "High", ContextCompat.getColor(getActivity(), R.color.high)));
        myDatas.add(new MyData(severity.get("low").getAsInt(), "Low", ContextCompat.getColor(getActivity(), R.color.low)));
        myDatas.add(new MyData(severity.get("medium").getAsInt(), "Medium", ContextCompat.getColor(getActivity(), R.color.medium)));

    }

    private void setupStatusData(JsonObject o) {

        try {
            myDatas.clear();
        } catch (Exception ex) {

        }

        JsonObject status = o.getAsJsonObject("status");
        total = status.get("total").getAsInt();
        myDatas.add(new MyData(status.get("total").getAsInt(), "Total", ContextCompat.getColor(getActivity(), R.color.total)));
        myDatas.add(new MyData(status.get("closed").getAsInt(), "Closed", ContextCompat.getColor(getActivity(), R.color.close)));
        myDatas.add(new MyData(status.get("member_feedback").getAsInt(), "Member Feedback", ContextCompat.getColor(getActivity(), R.color.member_feedback)));
        myDatas.add(new MyData(status.get("monitoring").getAsInt(), "Monitoring", ContextCompat.getColor(getActivity(), R.color.monitoring)));
        myDatas.add(new MyData(status.get("new_count").getAsInt(), "New Count", ContextCompat.getColor(getActivity(), R.color.new_count)));
        myDatas.add(new MyData(status.get("resolved").getAsInt(), "Resolved", ContextCompat.getColor(getActivity(), R.color.resolved)));
        myDatas.add(new MyData(status.get("wip").getAsInt(), "WIP", ContextCompat.getColor(getActivity(), R.color.wip)));


    }

    private void showMessage(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {

        super.onResume();
    }

/*

    private void setupDummyData() {

        myDatas.add(new MyData(200, "Total", ContextCompat.getColor(getActivity(), R.color.total), 100));
        myDatas.add(new MyData(20, "High", ContextCompat.getColor(getActivity(), R.color.high), 20));
        myDatas.add(new MyData(50, "Medium", ContextCompat.getColor(getActivity(), R.color.medium), 25));
        myDatas.add(new MyData(80, "Low", ContextCompat.getColor(getActivity(), R.color.low), 40));
        myDatas.add(new MyData(50, "FYI", ContextCompat.getColor(getActivity(), R.color.fyi), 25));

        myAdapter.notifyDataSetChanged();

    }
*/

    private void initView(View view) {
        view.findViewById(R.id.linearLayoutLeft).setOnClickListener(this);
        view.findViewById(R.id.linearLayoutRight).setOnClickListener(this);
    }

    private void setupRecycler(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        myAdapter = new MyAdapter(context, myDatas, total);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(myAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linearLayoutLeft:
                try {
                    //linearLayoutManager.scrollToPositionWithOffset(linearLayoutManager.findFirstVisibleItemPosition()-1,0);
                    recyclerView.smoothScrollToPosition(linearLayoutManager.findFirstVisibleItemPosition() - 1);
                } catch (Exception ex) {
                    ex.printStackTrace();

                }

                break;
            case R.id.linearLayoutRight:
                try {//  linearLayoutManager.scrollToPositionWithOffset(linearLayoutManager.findFirstVisibleItemPosition()+1,0);
                    recyclerView.smoothScrollToPosition(linearLayoutManager.findLastVisibleItemPosition() + 1);
                } catch (Exception ex) {
                    ex.printStackTrace();

                }

                break;
        }
    }
}
