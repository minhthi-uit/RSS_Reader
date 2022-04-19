package com.example.rssreaderjava;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.rssreaderjava.adapter.RSSFeedListAdapter;
import com.example.rssreaderjava.authentication.LoginActivity;
import com.example.rssreaderjava.models.RSSFeedModel;
import com.google.firebase.auth.FirebaseAuth;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class RSSMainActivity extends AppCompatActivity {

    private static final String TAG = "RSSMainActivity";
    public static List<RSSFeedModel> listSeeLater = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private EditText mEditText;
    private Button mFetchFeedButton;
    private SwipeRefreshLayout mSwipeLayout;
    private ImageView ivSeeLater, logOut;

    private RSSFeedListAdapter mRSSFeedListAdapter;
    private List<RSSFeedModel> mFeedModelList = new ArrayList<>();

    public void addListSeeLater(RSSFeedModel rssFeedModel) {
        listSeeLater.add(rssFeedModel);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_rss);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mEditText =  findViewById(R.id.rssFeedEditText);
        mFetchFeedButton =  findViewById(R.id.fetchFeedButton);

        mSwipeLayout =  findViewById(R.id.swipeRefreshLayout);

        ivSeeLater = findViewById(R.id.ivSeeLater);
        ivSeeLater.setOnClickListener( v -> {
                Intent intent = new Intent(getApplicationContext(), SeeLaterActivity.class);
                startActivity(intent);
        });

        logOut = findViewById(R.id.imgLogout);
        logOut.setOnClickListener(v -> {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

        });

        mFetchFeedButton.setOnClickListener( v -> {
                new FetchFeedTask().execute((Void) null);
        });

        mSwipeLayout.setOnRefreshListener(() -> {
            mSwipeLayout.setRefreshing(false);
        });

    }



public List<RSSFeedModel> parseFeed(InputStream inputStream) throws XmlPullParserException, IOException {

    List<RSSFeedModel> items = new ArrayList<>();

    try {
        XmlPullParser xmlPullParser = Xml.newPullParser();
        xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        xmlPullParser.setInput(inputStream, null);
        xmlPullParser.nextTag();
        mapXmlToItem(xmlPullParser, items);
        return items;
    } finally {
        inputStream.close();
    }
}

    private void mapXmlToItem(XmlPullParser xmlPullParser, List<RSSFeedModel> items) throws IOException, XmlPullParserException {
        String title = null;
        String link = null;
        String description = null;
        boolean firstLink = false;
        boolean firstDescription = false;
        boolean isItem = false;
        String linkRss = mEditText.getText().toString();
        while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
            int eventType = xmlPullParser.getEventType();

            String name = xmlPullParser.getName();
            if (name == null)
                continue;

            if (eventType == XmlPullParser.END_TAG) {
                if (name.equalsIgnoreCase("item")) {
                    isItem = false;
                }
                continue;
            }

            if (eventType == XmlPullParser.START_TAG) {
                if (name.equalsIgnoreCase("item")) {
                    isItem = true;
                    continue;
                }
            }

            String result = "";
            if (xmlPullParser.next() == XmlPullParser.TEXT) {
                result = xmlPullParser.getText();
                xmlPullParser.nextTag();
            }

            if (name.equalsIgnoreCase("title")) {
                title = result.trim();
            } else if (name.equalsIgnoreCase("link")) {
                if (!firstLink) {
                    firstLink = true;
                } else {
                    if (!result.equals(linkRss)) {
                        link = result.trim();
                    }
                }
            } else if (name.equalsIgnoreCase("description")) {
                if (!firstDescription) {
                    firstDescription = true;
                } else {

                    description = parserDescription(result);
                }
            }

            if (title != null && link != null && description != null) {
                if (isItem) {
                    RSSFeedModel item = new RSSFeedModel(title,link, description);
                    items.add(item);
                }
                title = null;
                link = null;
                description = null;
                isItem = false;
            }
        }
    }

    private String parserDescription(String result) {
        String desc;
        if (!result.contains("cafebiz.vn")) {
            int temp = 0;
            for (int i = 0; i < result.length(); i++) {
                if (result.charAt(i) == '>') {
                    temp = i;
                }
            }
            if (result.contains("></a></br>")) {
                desc = result.substring(temp + 1).trim();
            } else {
                desc = result.substring(temp).trim();
            }
        } else {
            desc = result.substring(result.indexOf("<span>") + 6, result.indexOf("</span>")).trim();
        }
        return desc;
    }

    private class FetchFeedTask extends AsyncTask<Void, Void, Boolean> {

        private String urlLink;

        @Override
        protected void onPreExecute() {
            mSwipeLayout.setRefreshing(true);
            urlLink = mEditText.getText().toString();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (TextUtils.isEmpty(urlLink))
                return false;

            try {
                if(!urlLink.startsWith("http://") && !urlLink.startsWith("https://"))
                    urlLink = "http://" + urlLink;

                URL url = new URL(urlLink);
                InputStream inputStream = url.openConnection().getInputStream();
                mFeedModelList = parseFeed(inputStream);
                return true;
            } catch (IOException e) {
                Log.e(TAG, "Error", e);
            } catch (XmlPullParserException e) {
                Log.e(TAG, "Error", e);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            mSwipeLayout.setRefreshing(false);

            if (success) {
                mRecyclerView.setAdapter(new RSSFeedListAdapter(mFeedModelList));
            } else {
                Toast.makeText(RSSMainActivity.this, "Enter a valid url", Toast.LENGTH_LONG).show();
            }
        }
    }


}
