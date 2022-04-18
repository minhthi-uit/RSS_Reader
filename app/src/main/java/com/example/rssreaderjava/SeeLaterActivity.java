package com.example.rssreaderjava;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.rssreaderjava.adapter.RSSFeedListAdapter;

public class SeeLaterActivity extends AppCompatActivity {

    public  RecyclerView mRecyclerView;
    public ImageView ivBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_later);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mRecyclerView = findViewById(R.id.rvlistSeeLater);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        RSSFeedListAdapter rssFeedListAdapter = new RSSFeedListAdapter(RSSMainActivity.listSeeLater);
        mRecyclerView.setAdapter(rssFeedListAdapter);

        ivBack = findViewById(R.id.backHome);
        ivBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, RSSMainActivity.class);
            setResult(Activity.RESULT_OK, intent);
            finish();
        });
    }
}
