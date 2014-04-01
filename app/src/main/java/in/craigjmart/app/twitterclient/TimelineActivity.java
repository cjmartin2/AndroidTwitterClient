package in.craigjmart.app.twitterclient;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.craigjmart.app.twitterclient.models.Tweet;


public class TimelineActivity extends Activity {
    public static final String USER_ID = "user_id";
    private static final int COMPOSE_REQUEST = 123;
    private String user_id = "";
    private ListView lvTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        user_id = getIntent().getStringExtra(USER_ID);
        lvTweets = (ListView) findViewById(R.id.lvTweets);

        getProfile();
        buildTimeline(-1);

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                onRequestMore();
            }
        });
    }

    private void buildTimeline(final long lastTweetId) {
        CraigTwitterApp.getRestClient().getHomeTimeline(lastTweetId, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(JSONArray jsonTweets) {
//                Log.d("DEBUG", jsonTweets.toString());
                ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);

                TweetAdapter adapter = (TweetAdapter) lvTweets.getAdapter();
                if (adapter == null) {
                    adapter = new TweetAdapter(getBaseContext(), tweets);
                    lvTweets.setAdapter(adapter);
                } else {
                    adapter.addAll(tweets);
                }
            }
        });
    }

    public void onRequestMore() {
        //need to subtract 1 here to get 2nd last, otherwise we get a dupe
        buildTimeline(getLastTweetId() - 1);
    }

    public long getLastTweetId() {
        TweetAdapter adapter = (TweetAdapter)lvTweets.getAdapter();
        //this "-1" is just to account for 0-based array
        return adapter.getItem(adapter.getCount()-1).getId();
    }

    public void onCompose(MenuItem miCompose){
        Intent i = new Intent(this, ComposeActivity.class);
        startActivityForResult(i, COMPOSE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent i) {
        if (resultCode == RESULT_OK  && requestCode == COMPOSE_REQUEST) {
            JSONObject jsonTweet;
            try {
                jsonTweet = new JSONObject(i.getExtras().getString("jsonTweet"));
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }

            Tweet tweet = Tweet.fromJson(jsonTweet);
            TweetAdapter adapter = (TweetAdapter) lvTweets.getAdapter();
            //insert at beginning
            adapter.insert(tweet, 0);
        }
    }

    private void getProfile() {
        //currently have no plans to use this, was just doing it to help in the group chat
        CraigTwitterApp.getRestClient().getProfile(user_id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject jsonUser) {
                try {
//                    Log.d("DIGGING", jsonUser.toString());
                    String screen_name = (String)jsonUser.get("screen_name");
                    Log.d("DIGGING", screen_name);
                    Log.d("DIGGING", (String)jsonUser.get("profile_image_url"));
                    ActionBar ab = getActionBar();
                    ab.setTitle("@" + screen_name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timeline, menu);
        return true;
    }
}
