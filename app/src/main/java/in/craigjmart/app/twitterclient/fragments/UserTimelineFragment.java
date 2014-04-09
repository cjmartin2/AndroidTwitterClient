package in.craigjmart.app.twitterclient.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.craigjmart.app.twitterclient.CraigTwitterApp;
import in.craigjmart.app.twitterclient.EndlessScrollListener;
import in.craigjmart.app.twitterclient.models.Tweet;
import in.craigjmart.app.twitterclient.models.User;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserTimelineFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 *
 */
public class UserTimelineFragment extends TweetsListFragment {
    private User user;

    public UserTimelineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        user = (User) bundle.getSerializable("user");

        buildTimeline(NO_LAST_TWEET_ID, NO_SINCE_ID);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                onRequestMore();
            }
        });
    }

    @Override
    protected void buildTimeline(long lastTweetId, long since_id) {
        CraigTwitterApp.getRestClient().getUserTimeline(user.getUserId(), lastTweetId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONArray jsonTweets) {
                tweetAdapter.addAll(Tweet.fromJson(jsonTweets));
            }

            @Override
            public void onFailure(Throwable throwable, JSONObject jsonObject) {
                super.onFailure(throwable, jsonObject);
                String error;
                try {
                    error = jsonObject.getJSONArray("errors").getJSONObject(0).getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                    error = "Error fetching HomeTimeline; also, couldn't extract error code";
                }
                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
