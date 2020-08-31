package com.example.laixi.view;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.laixi.R;
import com.example.laixi.model.ProgressBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MainListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int ITEM_TYPE_HEADER = 0;
    public static final int ITEM_TYPE_CONTENT = 1;
    public static final int mHeaderCount = 1;

    private List<ProgressBean> progressBeans;
    private final IMainListAdapter iMainListAdapter;

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View itemView, final IMainListAdapter iMainListAdapter) {
            super(itemView);
            itemView.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iMainListAdapter.addItem();
                }
            });
        }
    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {
        private final TextView seqTextView;
        private final ProgressBar progressBar;

        public ContentViewHolder(View v, final IMainListAdapter iMainListAdapter) {
            super(v);
            seqTextView = (TextView) v.findViewById(R.id.seq);
            Button deleteBtn = (Button) v.findViewById(R.id.deleteBtn);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (iMainListAdapter == null)
                        return;
                    iMainListAdapter.deleteItem(getAdapterPosition() - mHeaderCount);
                }
            });
            progressBar = (ProgressBar) v.findViewById(R.id.progress);
        }

        public TextView getSeqTextView() {
            return seqTextView;
        }

        public ProgressBar getProgressBar() {
            return progressBar;
        }
    }

    public MainListAdapter(List<ProgressBean> progressBeans, IMainListAdapter iMainListAdapter) {
        this.progressBeans = progressBeans;
        this.iMainListAdapter = iMainListAdapter;
    }

    public void setData(List<ProgressBean> progressBeans) {
        this.progressBeans = progressBeans;
        notifyDataSetChanged();
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int viewType) {
        if (viewType == ITEM_TYPE_HEADER) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.recycle_header, viewGroup, false);
            return new HeaderViewHolder(v, iMainListAdapter);
        }
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_list, viewGroup, false);

        return new ContentViewHolder(v, iMainListAdapter);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder viewHolder, final int position) {
        if (viewHolder instanceof ContentViewHolder) {
            ContentViewHolder holder = (ContentViewHolder) viewHolder;
            ProgressBean progressBean = progressBeans.get(position - mHeaderCount);
            holder.getSeqTextView().setText(Integer.toString(progressBean.seqNum));
            holder.getProgressBar().setProgress(progressBean.progress);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mHeaderCount != 0 && position < mHeaderCount) {
            return ITEM_TYPE_HEADER;
        } else {
            return ITEM_TYPE_CONTENT;
        }
    }

    @Override
    public int getItemCount() {
        return progressBeans.size() + mHeaderCount;
    }
}