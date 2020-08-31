package com.example.laixi.model;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.room.Room;

import com.example.laixi.MyApplication;
import com.example.laixi.event.ProgressItemAddDeleteEvent;
import com.example.laixi.event.ProgressUpdateEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class ProgressModel {
    public static final String PREF_COUNT = "count";
    private List<ProgressBean> progressBeans;
    private final AppDatabase db;

    public ProgressModel() {
        db = Room.databaseBuilder(MyApplication.getInstance(),
                AppDatabase.class, "database-progress").build();
    }

    public int count() {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getInstance());
        int count = defaultSharedPreferences.getInt("count", 0);
        count ++;
        defaultSharedPreferences.edit().putInt(PREF_COUNT, count).commit();
        return count;
    }

    public List<ProgressBean> loadData() {
        if (progressBeans != null)
            return progressBeans;
        progressBeans = db.progressBeanDao().getAll();
        EventBus.getDefault().post(new ProgressItemAddDeleteEvent(progressBeans));
        return progressBeans;
    }

    public List<ProgressBean> add(ProgressBean progressBean) {
        progressBeans.add(progressBean);
        EventBus.getDefault().post(new ProgressItemAddDeleteEvent(progressBeans));
        db.progressBeanDao().insertAll(progressBean);
        return progressBeans;
    }

    public List<ProgressBean> delete(int index) {
        ProgressBean progressBean = progressBeans.remove(index);
        EventBus.getDefault().post(new ProgressItemAddDeleteEvent(progressBeans));
        db.progressBeanDao().delete(progressBean);
        return progressBeans;
    }

    public ProgressBean get(int index) {
        return progressBeans.get(index);
    }

    public List<ProgressBean> update(ProgressBean progressBean) {
        EventBus.getDefault().post(new ProgressUpdateEvent(progressBeans));
        db.progressBeanDao().update(progressBean);
        return progressBeans;
    }
}
