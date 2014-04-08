package in.craigjmart.app.twitterclient;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import in.craigjmart.app.twitterclient.fragments.HomeTimelineFragment;
import in.craigjmart.app.twitterclient.fragments.MentionsFragment;
import in.craigjmart.app.twitterclient.models.Tweet;


public class TimelineActivity extends FragmentActivity implements ActionBar.TabListener {
    public static final String USER_ID = "user_id";
    private static final int COMPOSE_REQUEST = 123;
    private String user_id = "";
    private TweetAdapter tweetAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        user_id = getIntent().getStringExtra(USER_ID);
        getProfile();

        setupNavigationTabs();

    }

    private void setupNavigationTabs() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);
        //apparently this doesn't work - need to create custom style
//        actionBar.setBackgroundDrawable(new ColorDrawable(0x6999FA));

        ActionBar.Tab tabHome = actionBar.newTab().setText("Home").setTag("HomeTimelineFragment")
                .setIcon(R.drawable.ic_home).setTabListener(this);

        ActionBar.Tab tabMentions = actionBar.newTab().setText("Mentions").setTag("MentionsTimelineFragment")
                .setIcon(R.drawable.ic_home).setTabListener(this);

        actionBar.addTab(tabHome);
        actionBar.addTab(tabMentions);
        actionBar.selectTab(tabHome);
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
                    String screen_name = (String) jsonUser.get("screen_name");
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

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
        if(tab.getTag() == "HomeTimelineFragment"){
            // set fragment to home
                fts.replace(R.id.frame_container, new HomeTimelineFragment());
        }else{
            //set fragment to mentions
            fts.replace(R.id.frame_container, new MentionsFragment());
        }
        fts.commit();
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}
