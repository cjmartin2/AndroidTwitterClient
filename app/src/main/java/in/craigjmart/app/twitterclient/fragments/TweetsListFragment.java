package in.craigjmart.app.twitterclient.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import in.craigjmart.app.twitterclient.EndlessScrollListener;
import in.craigjmart.app.twitterclient.R;
import in.craigjmart.app.twitterclient.TweetAdapter;
import in.craigjmart.app.twitterclient.models.Tweet;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;


/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TweetsListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the  factory method to
 * create an instance of this fragment.
 *
 */
public abstract class TweetsListFragment extends Fragment implements OnRefreshListener {

    private OnFragmentInteractionListener mListener;
    protected PullToRefreshLayout ptrLayout;
    protected TweetAdapter tweetAdapter;
    protected ListView lvTweets;
    protected static final int NO_SINCE_ID = -1;
    protected static final int NO_LAST_TWEET_ID = -1;

    public TweetsListFragment() {
        // Required empty public constructor
    }

    public TweetAdapter getTweetAdapter(){
        return tweetAdapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tweets_list, container, false);

        // find the PullToRefreshLayout to setup
        ptrLayout = (PullToRefreshLayout) view.findViewById(R.id.ptrLayout);
        lvTweets = (ListView) view.findViewById(R.id.lvTweets);

        tweetAdapter = new TweetAdapter(getActivity(), new ArrayList<Tweet>());
        lvTweets.setAdapter(tweetAdapter);

        // Now setup the PullToRefreshLayout
        setupPullToRefresh();

        buildTimeline(NO_LAST_TWEET_ID, NO_SINCE_ID);

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                onRequestMore();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    private void setupPullToRefresh() {
        ActionBarPullToRefresh.from(getActivity())
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
        long topTweet = tweetAdapter.getItem(0).getTweetId();
        buildTimeline(NO_LAST_TWEET_ID, topTweet);
    }

    public void onRequestMore() {
        //need to subtract 1 here to get 2nd last, otherwise we get a dupe
        buildTimeline(getLastTweetId() - 1, NO_SINCE_ID);
    }

    public long getLastTweetId() {
        //this "-1" is just to account for 0-based array
        return tweetAdapter.getItem(tweetAdapter.getCount()-1).getTweetId();
    }

    protected abstract void buildTimeline(final long lastTweetId, final long since_id);

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
