package com.freego.adapter;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.freego.R;
import com.freego.app.GlobalApplication;
import com.freego.bean.HomepageContent;

import java.util.ArrayList;

public class Homepage_RecycleView_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private static final int DESTINATION_VIEW = 0;

    private static final int COLUMN_VIEW = 1;

    private static final int NOTE_VIEW = 2;

    private static final int NOTE_TITLE = 3;

    private ArrayList<HomepageContent> contents = new ArrayList<HomepageContent>();

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    Typeface typeface = Typeface.createFromAsset(GlobalApplication.getContext().getAssets(), "fonts/Helvetica LT Light.ttf");

    public Homepage_RecycleView_adapter(ArrayList<HomepageContent> contents) {
        this.contents = contents;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == DESTINATION_VIEW) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.homepage_adapter_destination_style, parent, false);
            DesViewHolder desViewHolder = new DesViewHolder(view);
            view.setOnClickListener(this);
            return desViewHolder;
        } else if (viewType == COLUMN_VIEW) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.homepage_adapter_column_style, parent, false);
            ColumnViewHolder columnViewHolder = new ColumnViewHolder(view);
            view.setOnClickListener(this);
            return columnViewHolder;
        } else if (viewType == NOTE_VIEW) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.homepage_adapter_note_style, parent, false);
            NoteViewHolder noteViewHolder = new NoteViewHolder(view);
            view.setOnClickListener(this);
            return noteViewHolder;
        } else if (viewType == NOTE_TITLE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.homepage_adapter_note_title, parent, false);
            NoteTitleViewHolder noteTitleViewHolder = new NoteTitleViewHolder(view);
            view.setOnClickListener(this);
            return noteTitleViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder.getItemViewType() == DESTINATION_VIEW) {
            ((DesViewHolder) holder).imageView.setImageResource(contents.get(position).getImageID());
            holder.itemView.setTag(contents.get(position));
        } else if (holder.getItemViewType() == COLUMN_VIEW) {
            ((DesViewHolder) holder).imageView.setImageResource(contents.get(position).getImageID());
            ((ColumnViewHolder) holder).textView.setTypeface(typeface);
            ((ColumnViewHolder) holder).textView.setText("Hot Hotels");
            holder.itemView.setTag(contents.get(position));
        } else if (holder.getItemViewType() == NOTE_VIEW) {
            ((NoteViewHolder) holder).imageView.setImageResource(contents.get(position).getImageID());
            ((NoteViewHolder) holder).textView.setText("China, bangbangda!");
            holder.itemView.setTag(contents.get(position));
        } else if (holder.getItemViewType() == NOTE_TITLE) {
            ((NoteTitleViewHolder) holder).imageView.setImageResource(contents.get(position).getImageID());
            ((NoteTitleViewHolder) holder).titleText.setTypeface(typeface);
            ((NoteTitleViewHolder) holder).titleText.setText("Hot Notes");
            ((NoteTitleViewHolder) holder).infoText.setText("China, memeda!");
            holder.itemView.setTag(contents.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return DESTINATION_VIEW;
        else if (position == 3)
            return COLUMN_VIEW;
        else if (position == 6)
            return NOTE_TITLE;
        else if (position == 7 || position == 8)
            return NOTE_VIEW;
        else
            return DESTINATION_VIEW;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null)
            mOnItemClickListener.onItemClick(v, (HomepageContent) v.getTag());
    }

    public static class DesViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public DesViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.homepage_view_image);
        }
    }

    public static class ColumnViewHolder extends DesViewHolder {

        private TextView textView;

        public ColumnViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.homepage_view_column);
        }
    }

    public static class NoteViewHolder extends DesViewHolder {

        private ImageView imageView;

        private TextView textView;

        public NoteViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.homepage_view_note);
            imageView = (ImageView) itemView.findViewById(R.id.homepage_note_destination);
        }
    }

    public static class NoteTitleViewHolder extends RecyclerView.ViewHolder {

        private TextView titleText;

        private ImageView imageView;

        private TextView infoText;

        public NoteTitleViewHolder(View itemView) {
            super(itemView);
            titleText = (TextView) itemView.findViewById(R.id.homepage_note_title);
            imageView = (ImageView) itemView.findViewById(R.id.homepage_note_title_destination);
            infoText = (TextView) itemView.findViewById(R.id.homepage_note_info);
        }
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , HomepageContent content);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}
