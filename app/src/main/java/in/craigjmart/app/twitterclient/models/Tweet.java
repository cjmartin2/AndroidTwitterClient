package in.craigjmart.app.twitterclient.models;

import com.activeandroid.Model;

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
public class Tweet extends Model {
    private User user;
    private String body;
    private long id;
    private String createdAt;

    public Tweet(JSONObject jsonObject) throws JSONException {
        super();
        user = User.fromJson(jsonObject.getJSONObject("user"));
        body = jsonObject.getString("text");
        id = jsonObject.getLong("id");
        createdAt = jsonObject.getString("created_at");
    }

    public User getUser() {
        return user;
    }

    public String getBody() {
        return body;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public long getTweetId() {
        return id;
    }

    public String getDate() {
        String date;
        try {
            String dateString = createdAt;

            //I am clearly not doing this right, as I am turning the correct date into Jan 01, 2014.  Can't waste anymore time
            //blah, nevermind . . . I think I had capital HH which broke it
//            Log.d("DATE", dateString);
            Date d = new SimpleDateFormat("EEE MMM dd hh:mm:ss ZZZZZ yyyy", Locale.ENGLISH).parse(dateString);
//            Log.d("DATE", d.toString());

            long timeAt = d.getTime();
            long timeNow = new Date().getTime();
            long diff = timeNow - timeAt;

//            Log.d("time 1", String.valueOf(timeAt));
//            Log.d("time 2", String.valueOf(timeNow));
//            Log.d("time 3", String.valueOf(diff));

            long diffSeconds = Math.max((diff / 1000), 1);
            long diffMinutes = Math.max((diff / (60 * 1000)), 1);
            long diffHours = Math.max((diff / (60 * 60 * 1000)), 1);
            long diffDays = Math.max((diff / (24 * 60 * 60 * 1000)), 1);

            if(diffSeconds < 50){
                date = String.valueOf(diffSeconds) + "s";
            }else if(diffMinutes < 50){
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
        try {
            Tweet tweet = new Tweet(jsonObject);
            return tweet;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
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
