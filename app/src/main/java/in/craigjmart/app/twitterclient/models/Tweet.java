package in.craigjmart.app.twitterclient.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by admin on 4/1/14.
 */
public class Tweet extends BaseModel {
    private User user;

    public User getUser() {
        return user;
    }

    public String getBody() {
        return getString("text");
    }

    public long getId() {
        return getLong("id");
    }

    public boolean isFavorited() {
        return getBoolean("favorited");
    }

    public boolean isRetweeted() {
        return getBoolean("retweeted");
    }

    public String getDate() {
        String date;
        try {
            String dateString = getString("created_at");

            //I am clearly not doing this right, as I am turning the correct date into Jan 01, 2014.  Can't waste anymore time
            //blah, nevermind . . . I think I had capital HH which broke it
//            Log.d("DATE", dateString);
            Date d = new SimpleDateFormat("EEE MMM dd hh:mm:ss ZZZZZ yyyy", Locale.ENGLISH).parse(dateString);
//            Log.d("DATE", d.toString());

            long timeAt = d.getTime();
            long timeNow = new Date().getTime();
            long diff = timeNow - timeAt;

            long diffMinutes = Math.max((diff / (60 * 1000)), 1);
            long diffHours = Math.max((diff / (60 * 60 * 1000)), 1);
            long diffDays = Math.max((diff / (24 * 60 * 60 * 1000)), 1);

            if(diffMinutes < 50){
                date = String.valueOf(diffMinutes) + "m";
            }else if(diffHours < 18) {
                date = String.valueOf(diffHours) + "h";
            }else{
                date = String.valueOf(diffDays) + "d";
            }
//            Log.d("DATE", date);

        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        return date;
    }

    public static Tweet fromJson(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
            tweet.jsonObject = jsonObject;
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return tweet;
    }

    public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }

            Tweet tweet = Tweet.fromJson(tweetJson);
            if (tweet != null) {
                tweets.add(tweet);
            }
        }

        return tweets;
    }
}
