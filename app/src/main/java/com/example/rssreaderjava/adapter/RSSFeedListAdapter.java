package com.example.rssreaderjava.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rssreaderjava.R;
import com.example.rssreaderjava.RSSMainActivity;
import com.example.rssreaderjava.models.RSSFeedModel;

import java.util.List;

public class RSSFeedListAdapter extends RecyclerView.Adapter<RSSFeedListAdapter.FeedModelViewHolder> {
private final List<RSSFeedModel> mRssFeedModels;


public RSSFeedListAdapter(List<RSSFeedModel> rssFeedModels) {
        mRssFeedModels = rssFeedModels;
        }

@NonNull
@Override
public FeedModelViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.rss_item_list, parent, false);
        FeedModelViewHolder feedModelViewHolder = new FeedModelViewHolder(parent.getContext(),view);
        return feedModelViewHolder;
        }

@SuppressLint("SetTextI18n")
@Override
public void onBindViewHolder(FeedModelViewHolder holder, int position) {
final RSSFeedModel itemRecyclerView = mRssFeedModels.get(position);
        holder.bindData(itemRecyclerView, position);
//        if (itemRecyclerView.isShowSeeLater()) {
//        holder.rssFeedView.findViewById(R.id.btnSave).setVisibility(View.VISIBLE);
//        } else {
//        holder.rssFeedView.findViewById(R.id.btnSave).setVisibility(View.GONE);
//        }
        ((TextView) holder.rssFeedView.findViewById(R.id.titleText)).setText(itemRecyclerView.getTitle());
        ((TextView) holder.rssFeedView.findViewById(R.id.descriptionText)).setText(itemRecyclerView.getDescription());

        }

@Override
public int getItemCount() {
        return mRssFeedModels.size();
        }

public class FeedModelViewHolder extends RecyclerView.ViewHolder {
    private final View rssFeedView;
    protected RSSFeedModel data;
    protected int position;

    public FeedModelViewHolder(Context context, View v) {
        super(v);
        rssFeedView = v;
        Button btnSave = v.findViewById(R.id.btnSave);
        Button btnSeeMore = v.findViewById(R.id.btnSeeMore);
        btnSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(data.getLink()));
                    view.getContext().startActivity(browserIntent);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                (( RSSMainActivity) context).addListSeeLater(data);
                data.setShowSeeLater(false);
                notifyDataSetChanged();
            }
        });
    }

    public void bindData(RSSFeedModel data, int position) {
        this.data = data;
        this.position = position;
    }
}

}
