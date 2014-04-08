package in.craigjmart.app.twitterclient.fragments;

import android.os.Bundle;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;

import in.craigjmart.app.twitterclient.CraigTwitterApp;
import in.craigjmart.app.twitterclient.models.Tweet;

/**
 * Created by admin on 4/8/14.
 */
public class MentionsFragment extends TweetsListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void buildTimeline(long lastTweetId, final long since_id) {
        CraigTwitterApp.getRestClient().getMentions(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(JSONArray jsonTweets) {
//                Log.d("DEBUG", jsonTweets.toString());
                ArrayList<Tweet> tweets = Tweet.fromJson(jsonTweets);

                //this is all duplicated code between mentions & home, should abstract out
                if (tweets.size() > 0) {
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
                } else {
                    //the pull to refresh returned nothing
                    ptrLayout.setRefreshComplete();
                }
            }
        });
    }
}
