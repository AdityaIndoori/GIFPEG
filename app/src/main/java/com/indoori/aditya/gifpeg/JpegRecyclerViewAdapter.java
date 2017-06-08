package com.indoori.aditya.gifpeg;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by aditya on 03-06-2017.
 */

public class JpegRecyclerViewAdapter extends RecyclerView.Adapter<JpegRecyclerViewAdapter.ViewHolderClass> {
    final private ListItemClickListener mOnClickListener;
    final private String[] subTitleArray;
    final private int[] imageArray;
    final private int NumberOfItems;
    final private Context context;


    public JpegRecyclerViewAdapter(Context context,int[] imageArray,String[] subTitleArray,ListItemClickListener mOnClickListener) {
        this.imageArray = imageArray;
        this.subTitleArray = subTitleArray;
        NumberOfItems = imageArray.length;
        this.mOnClickListener = mOnClickListener;
        this.context = context;
    }


    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex, int viewCode, ImageView imageView);
    }

    @Override
    public ViewHolderClass onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolderClass viewHolder;
        View view;
        LayoutInflater layoutInflater;
        Context context;

        context = parent.getContext();
        layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.jpeg_recycler_view_item,parent,false);
        viewHolder = new ViewHolderClass(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderClass holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return NumberOfItems;
    }

    class ViewHolderClass extends RecyclerView.ViewHolder {
        private LinearLayout linearLayout;
        private TextView subTitle;
        private ImageView imageView;

        public ViewHolderClass(final View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.jpeg_linearLayout);
            imageView = (ImageView) itemView.findViewById(R.id.jpeg_imageView);
            subTitle = (TextView) itemView.findViewById(R.id.jpeg_textView);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int clickedPosition = getAdapterPosition();
                    mOnClickListener.onListItemClick(clickedPosition,0,imageView);
                }
            });

        }

        void bind(int Position){
            //Assign the data to those view Instances Here:
            Glide.with(context).load(imageArray[Position]).into(imageView);
            subTitle.setText(subTitleArray[Position]);
        }
    }
}
