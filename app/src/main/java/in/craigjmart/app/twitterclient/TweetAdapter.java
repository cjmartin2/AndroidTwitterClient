package in.craigjmart.app.twitterclient;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import in.craigjmart.app.twitterclient.models.Tweet;
import in.craigjmart.app.twitterclient.models.User;

/**
 * Created by admin on 4/1/14.
 */
public class TweetAdapter extends ArrayAdapter<Tweet>{
    public TweetAdapter(Context context, List<Tweet> tweets) {
        super(context, 0, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_tweet, null);
        }

        final Tweet tweet = getItem(position);

        final User u = tweet.getUser();
        ImageView imageView = (ImageView) view.findViewById(R.id.ivProfile);
        ImageLoader.getInstance().displayImage(u.getProfileImageUrl(), imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ProfileActivity.class);
                i.putExtra("user", u);
                getContext().startActivity(i);
            }
        });

        TextView nameView = (TextView) view.findViewById(R.id.tvName);
        String formattedName = "<b>" + u.getName() + " </b><small><font color='#777777'>@"+
                u.getScreenName() + "</font><small>";
        nameView.setText(Html.fromHtml(formattedName));

        TextView dateView = (TextView) view.findViewById(R.id.tvDate);
        dateView.setText(tweet.getDate());

        TextView bodyView = (TextView) view.findViewById(R.id.tvBody);
        bodyView.setText(Html.fromHtml(tweet.getBody()));

        return view;
    }
}
