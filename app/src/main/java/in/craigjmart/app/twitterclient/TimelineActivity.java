package in.craigjmart.app.twitterclient;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.craigjmart.app.twitterclient.models.Tweet;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;


public class TimelineActivity extends Activity implements OnRefreshListener {
    public static final String USER_ID = "user_id";
    private static final int COMPOSE_REQUEST = 123;
    private String user_id = "";
    private ListView lvTweets;
    private PullToRefreshLayout ptrLayout;
    private TweetAdapter tweetAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        user_id = getIntent().getStringExtra(USER_ID);
        lvTweets = (ListView) findViewById(R.id.lvTweets);

        // Now find the PullToRefreshLayout to setup
        ptrLayout = (PullToRefreshLayout) findViewById(R.id.ptrLayout);
        // Now setup the PullToRefreshLayout
        setupPullToRefresh();

        getProfile();
        buildTimeline(-1, -1);

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                onRequestMore();
            }
        });
    }

    private void buildTimeline(final long lastTweetId, final long since_id) {
        CraigTwitterApp.getRestClient().getHomeTimeline(lastTweetId, since_id, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(JSONArray jsonTweets) {
//                Log.d("DEBUG", jsonTweets.toString());
                ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);

                tweetAdapter = (TweetAdapter) lvTweets.getAdapter();
                if (tweetAdapter == null) {
                    tweetAdapter = new TweetAdapter(getBaseContext(), tweets);
                    lvTweets.setAdapter(tweetAdapter);
                } else {
                    //since_id is -1 when we first build the timeline, otherwise we are trying to get the latest tweets
                    if (since_id >= 0) {
                        for (int i = 0; i < tweets.size(); i++) {
                            //assuming the tweets are in order, coming from twitter, then we are putting
                            //them in the same order at the top of the array adapter
                            tweetAdapter.insert(tweets.get(i), i);
                        }
                        ptrLayout.setRefreshComplete();
                    } else {
                        tweetAdapter.addAll(tweets);
                    }
                }
            }
        });
    }

    public void doRefresh(MenuItem menu) {
        //get fresh list of tweets
        buildTimeline(-1, -1);
    }

    public void onRequestMore() {
        //need to subtract 1 here to get 2nd last, otherwise we get a dupe
        buildTimeline(getLastTweetId() - 1, -1);
    }

    public long getLastTweetId() {
        //this "-1" is just to account for 0-based array
        return tweetAdapter.getItem(tweetAdapter.getCount()-1).getId();
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
            //insert at beginning
            tweetAdapter.insert(tweet, 0);
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
//                    Log.d("DIGGING", screen_name);
//                    Log.d("DIGGING", (String)jsonUser.get("profile_image_url"));
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



    private void setupPullToRefresh() {
        ActionBarPullToRefresh.from(this)
            // Mark All Children as pullable
            .allChildrenArePullable()
                    // Set a OnRefreshListener
            .listener(this)
                    // Finally commit the setup to our PullToRefreshLayout
            .setup(ptrLayout);
    }

    @Override
    public void onRefreshStarted(View view) {
        //get newest tweets
        long topTweet = tweetAdapter.getItem(0).getId();
        buildTimeline(-1, topTweet);
    }
}
