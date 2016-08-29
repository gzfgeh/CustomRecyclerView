package com.gzfgeh.recyclerviewdemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gzfgeh.customrecyclerview.BaseViewHolder;
import com.gzfgeh.customrecyclerview.CustomRecyclerAdapter;
import com.gzfgeh.customrecyclerview.CustomRecyclerView;
import com.gzfgeh.customrecyclerview.CustomSwipeRefreshLayout;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

public class MainActivity extends AutoLayoutActivity implements CustomSwipeRefreshLayout.OnRefreshListener,CustomRecyclerAdapter.OnLoadMoreListener {

    private CustomRecyclerView recyclerView;
    private CustomRecyclerAdapter adapter;
    List<String> data = new ArrayList<>();
    int pageSize = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        recyclerView = (CustomRecyclerView) findViewById(R.id.recyclerView);
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CustomRecyclerAdapter<String>(this, R.layout.layout) {

            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.text_view, item);
            }
        };

        adapter.addHeader(new CustomRecyclerAdapter.ItemView() {
            @Override
            public View onCreateView(ViewGroup parent) {
                View view =  LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout, parent, false);
                ((TextView)view.findViewById(R.id.text_view)).setText("HEADER header");
                return view;
            }

            @Override
            public void onBindView(View headerView) {}
        });
        recyclerView.setAdapterWithProgress(adapter);
        recyclerView.setRefreshListener(this);
        adapter.setMore(R.layout.view_more, pageSize, this);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        Observable.timer(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .map(new Func1<Long, Object>() {
                    @Override
                    public Object call(Long aLong) {

                        data.clear();
                        for(int i=0; i<pageSize; i++){
                            data.add(i + "");
                        }
                        adapter.clear();
                        adapter.addAll(data);

                        return data;
                    }
                }).subscribe();
    }


    @Override
    public void onLoadMore() {

        Observable.timer(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .map(new Func1<Long, Object>() {
                    @Override
                    public Object call(Long aLong) {

                        if (data.size() < pageSize + 10){
                            for(int i=pageSize; i<pageSize*2; i++){
                                data.add(i + "");
                            }
                            adapter.addAll(data);
                        }else{
                            for(int i=pageSize*2; i<pageSize*2 + 10; i++){
                                data.add(i + "");
                            }
                            adapter.addAll(data);
                        }

                        return data;
                    }
                }).subscribe();




    }
}
