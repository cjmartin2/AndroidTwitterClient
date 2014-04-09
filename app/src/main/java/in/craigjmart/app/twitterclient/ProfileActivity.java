package in.craigjmart.app.twitterclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import in.craigjmart.app.twitterclient.fragments.UserTimelineFragment;
import in.craigjmart.app.twitterclient.models.User;


public class ProfileActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent i = getIntent();
        User user = (User) i.getSerializableExtra("user");

        setupProfile(user);

        UserTimelineFragment userTimeline = new UserTimelineFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        userTimeline.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.flUserTimeline, userTimeline).commit();
    }

    private void setupProfile(User user) {
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvTagLine = (TextView) findViewById(R.id.tvTagLine);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);

        tvName.setText(user.getName());
        tvTagLine.setText(user.getTagline());
        tvFollowers.setText("Followers: " + Integer.toString(user.getFollowersCount()));
        tvFollowing.setText("Following: " + Integer.toString(user.getFriendsCount()));
        ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), ivProfileImage);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
