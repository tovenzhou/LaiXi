package com.example.laixi;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.laixi.event.ProgressItemAddDeleteEvent;
import com.example.laixi.event.ProgressUpdateEvent;
import com.example.laixi.model.ProgressBean;
import com.example.laixi.view.IMainListAdapter;
import com.example.laixi.view.MainListAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class MainActivityJava extends AppCompatActivity implements IMainListAdapter {
    private RecyclerView recyclerView;
    private MainListAdapter mainListAdapter;

    private ProgressService progressService;
    private boolean bound = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    private void initData() {
        mainListAdapter = new MainListAdapter(new ArrayList<ProgressBean>(), this);
        recyclerView.setAdapter(mainListAdapter);
        progressService.loadProgressBeans();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, ProgressService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        unbindService(connection);
        bound = false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onProgressUpdateEvent(ProgressUpdateEvent event) {
        mainListAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onProgressItemAddDeleteEvent(ProgressItemAddDeleteEvent event) {
        mainListAdapter.setData(new ArrayList<ProgressBean>(event.progressBeans));
    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            ProgressService.LocalBinder binder = (ProgressService.LocalBinder) service;
            progressService = binder.getService();
            initData();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };

    @Override
    public void deleteItem(int adapterPosition) {
        progressService.deleteProgressItem(adapterPosition);
        mainListAdapter.notifyDataSetChanged();
    }

    @Override
    public void addItem() {
        progressService.addProgressItem();
        mainListAdapter.notifyDataSetChanged();
    }
}
