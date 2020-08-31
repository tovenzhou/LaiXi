package com.example.laixi.event;

import com.example.laixi.model.ProgressBean;

import java.util.List;

public class ProgressUpdateEvent {

    public final List<ProgressBean> progressBeans;

    public ProgressUpdateEvent(List<ProgressBean> progressBeans) {
        this.progressBeans = progressBeans;
    }

}
