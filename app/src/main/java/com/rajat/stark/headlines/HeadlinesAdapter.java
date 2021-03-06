package com.rajat.stark.headlines;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rajat.stark.headlines.data.DataContract;
import com.squareup.picasso.Picasso;

/**
 * Created by stark on 9/10/16.
 */

public class HeadlinesAdapter extends RecyclerView.Adapter<HeadlinesAdapter.HeadlinesAdapterViewHolder> {

    private static final String LOG_TAG = HeadlinesAdapter.class.getSimpleName();

    private Cursor mCursor;
    private final Context mContext;

    public HeadlinesAdapter(Context context){
        mContext = context;
    }
    @Override
    public HeadlinesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(parent instanceof RecyclerView){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.headlines_list_item, parent, false);
            view.setFocusable(true);
            return new HeadlinesAdapterViewHolder(view);
        } else {
            throw new RuntimeException("Not bound to RecyclerView");
        }
    }

    @Override
    public void onBindViewHolder(HeadlinesAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);


        if(mCursor != null){
            if(mCursor.getString(HeadlinesActivity.COLUMN_SOURCE).equals(mContext.getString(R.string.time_source_name))){
                Picasso.with(mContext)
                        .load(mContext.getString(R.string.time_logo_url))
                        .into(holder.mHeadlineImage);
            } else {
                Picasso.with(mContext)
                        .load(mCursor.getString(HeadlinesActivity.COLUMN_IMAGE))
                        .into(holder.mHeadlineImage);
            }

            holder.mHeadlineSource.setText(mCursor.getString(HeadlinesActivity.COLUMN_SOURCE));
            holder.mHeadlineTitle.setText(mCursor.getString(HeadlinesActivity.COLUMN_TITLE));

        }


    }

    @Override
    public int getItemCount() {
        if(null == mCursor) return 0;
        return mCursor.getCount();
    }

    public void swapCursor(Cursor cursor){
        mCursor = cursor;
        notifyDataSetChanged();

    }


    public class HeadlinesAdapterViewHolder extends RecyclerView.ViewHolder {
        ImageView mHeadlineImage;
        TextView mHeadlineTitle;
        TextView mHeadlineSource;

        public HeadlinesAdapterViewHolder(View itemView) {
            super(itemView);
            mHeadlineImage = (ImageView) itemView.findViewById(R.id.headline_image);
            mHeadlineTitle = (TextView) itemView.findViewById(R.id.headline_title);
            mHeadlineSource = (TextView) itemView.findViewById(R.id.headline_source);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    launchDetailActivity(mHeadlineImage);
                }
            });


        }


        public void launchDetailActivity(ImageView imageView){
            int pos = getAdapterPosition();
            mCursor.moveToPosition(pos);
            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT,
                    DataContract.NewsFeedHeadlinesEntry.buildHeadlinesWithUrl(mCursor.getString(HeadlinesActivity.COLUMN_IMAGE)));

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                Bundle bundle = ActivityOptions
                        .makeSceneTransitionAnimation(
                                (Activity)mContext,
                                imageView,
                                imageView.getTransitionName()
                        ).toBundle();
                mContext.startActivity(intent, bundle);
            } else {
                mContext.startActivity(intent);
            }
        }



    }
}
