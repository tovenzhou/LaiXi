package com.example.laixi;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

import com.example.laixi.event.ProgressHandlerThread;
import com.example.laixi.model.ProgressBean;
import com.example.laixi.model.ProgressModel;

import java.util.HashMap;
import java.util.List;

public class ProgressService extends Service {
    private final IBinder binder = new LocalBinder();
    private ProgressModel progressModel;
    private ProgressHandlerThread progressHandlerThread;
    private HashMap<ProgressBean, ProgressRunnable> progressRunnableHashMap = new HashMap<>();
    private Handler handler;

    public class LocalBinder extends Binder {
        public ProgressService getService() {
            return ProgressService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        progressModel = new ProgressModel();
        progressHandlerThread = new ProgressHandlerThread("progressHandler");
        progressHandlerThread.start();
        handler = new Handler(progressHandlerThread.getLooper());
        recoverProgress();

        return Service.START_STICKY;
    }

    private void recoverProgress() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                List<ProgressBean> progressBeans = progressModel.loadData();
                for (ProgressBean progressBean : progressBeans) {
                    if (progressBean.progress < 100)
                        runProgress(progressBean);
                }
            }
        });
    }

    public void loadProgressBeans() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                progressModel.loadData();
            }
        });
    }

    public void addProgressItem() {
        final ProgressBean progressBean = new ProgressBean(progressModel.count(), 0);
        handler.post(new Runnable() {
            @Override
            public void run() {
                progressModel.add(progressBean);
            }
        });
        runProgress(progressBean);

    }

    private void runProgress(ProgressBean progressBean) {
        ProgressRunnable progressRunnable = new ProgressRunnable(progressBean);
        progressRunnableHashMap.put(progressBean, progressRunnable);
        handler.postDelayed(progressRunnable, 1000);
    }

    public void deleteProgressItem(final int position) {
        ProgressBean progressBean = progressModel.get(position);
        ProgressRunnable progressRunnable = progressRunnableHashMap.remove(progressBean);
        if (progressRunnable != null)
            handler.removeCallbacks(progressRunnable);
        handler.post(new Runnable() {
            @Override
            public void run() {
                progressModel.delete(position);
            }
        });
    }

    private class ProgressRunnable implements Runnable {
        public static final int PROGRESS_INC = 5;
        public static final int PROGRESS_INC_ODD = 10;
        private ProgressBean progressBean;

        public ProgressRunnable(ProgressBean progressBean) {
            this.progressBean = progressBean;
        }

        @Override
        public void run() {
            if (progressBean.progress >= 100)
                return;

            if (progressBean.seqNum % 2 == 0) {
                progressBean.progress = progressBean.progress + PROGRESS_INC;
            } else {
                progressBean.progress = progressBean.progress + PROGRESS_INC_ODD;
            }

            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressModel.update(progressBean);
                }
            });

            if (progressBean.progress < 100)
                handler.postDelayed(this, 1000);
        }
    }
}
