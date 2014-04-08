package in.craigjmart.app.twitterclient.models;

import com.activeandroid.Model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 4/1/14.
 */
public class User extends Model {
    private String name;
    private String screenName;
    private String profileImageUrl;

    public User(JSONObject jsonObject) throws JSONException{
        super();
        name = jsonObject.getString("name");
        screenName = jsonObject.getString("screen_name");
        profileImageUrl = jsonObject.getString("profile_image_url");
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

    public static User fromJson(JSONObject jsonObject) {
        try {
            User u = new User(jsonObject);
            return u;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
