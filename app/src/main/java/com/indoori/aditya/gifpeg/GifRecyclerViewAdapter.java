package com.indoori.aditya.gifpeg;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cunoraz.gifview.library.GifView;

/**
 * Created by aditya on 05-06-2017.
 */

public class GifRecyclerViewAdapter extends RecyclerView.Adapter<GifRecyclerViewAdapter.ViewHolder>{
    final private ListItemClickListener mOnClickListener;
    final private String[] subTitleArray;
    final private int[] gifArray;
    final private int NumberOfItems;
    final private Context context;

    public GifRecyclerViewAdapter(Context context, int[] gifArray, String[] subTitleArray, ListItemClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
        this.subTitleArray = subTitleArray;
        this.gifArray = gifArray;
        NumberOfItems = gifArray.length;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder;
        View view;
        LayoutInflater layoutInflater;
        Context context;

        context = parent.getContext();
        layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.gif_recycler_view_item,parent,false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return NumberOfItems;
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex, int viewCode, ImageView gifView);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linearLayout;
        private TextView subTitle;
        private ImageView gifView;
        public ViewHolder(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.gif_linearLayout);
            gifView = (ImageView) itemView.findViewById(R.id.gif_gifView);
            subTitle = (TextView) itemView.findViewById(R.id.gif_textView);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = getAdapterPosition();
                    mOnClickListener.onListItemClick(clickedPosition,0,gifView);
                }
            });
        }

        void bind(int Position){
            Glide.with(context).load(gifArray[Position]).into(gifView);
            subTitle.setText(subTitleArray[Position]);
        }
    }
}