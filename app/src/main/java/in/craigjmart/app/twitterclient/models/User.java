package in.craigjmart.app.twitterclient.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by admin on 4/1/14.
 */
public class User implements Serializable  {
    private String name;
    private String screenName;
    private String profileImageUrl;
    private int statusesCount;
    private int followersCount;
    private int friendsCount;

    public int getStatusesCount() {
        return statusesCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public static User fromJson(JSONObject jsonObject) throws JSONException {
        try {
            User u = new User();

            u.name = jsonObject.getString("name");
            u.screenName = jsonObject.getString("screen_name");
            u.profileImageUrl = jsonObject.getString("profile_image_url");
            u.statusesCount = jsonObject.getInt("statuses_count");
            u.followersCount = jsonObject.getInt("followers_count");
            u.friendsCount = jsonObject.getInt("friends_count");

            return u;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
