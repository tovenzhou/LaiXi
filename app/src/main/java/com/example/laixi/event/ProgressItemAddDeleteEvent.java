package com.example.laixi.event;

import com.example.laixi.model.ProgressBean;

import java.util.List;

public class ProgressItemAddDeleteEvent {

    public final List<ProgressBean> progressBeans;

    public ProgressItemAddDeleteEvent(List<ProgressBean> progressBeans) {
        this.progressBeans = progressBeans;
    }

}
