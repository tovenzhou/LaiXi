package com.example.laixi.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ProgressBean {
    @PrimaryKey
    public int seqNum;

    @ColumnInfo(name = "progress")
    public int progress;

    public ProgressBean(int seqNum, int progress) {
        this.seqNum = seqNum;
        this.progress = progress;
    }

    public int getSeqNum() {
        return seqNum;
    }

}
