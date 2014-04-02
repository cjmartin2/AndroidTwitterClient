package in.craigjmart.app.twitterclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;


public class ComposeActivity extends Activity {
    private EditText etTweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        etTweet = (EditText) findViewById(R.id.etTweet);

        //doesn't seem like any of this stuff works for getting the keyboard to auto-show
        etTweet.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(etTweet, InputMethodManager.SHOW_IMPLICIT);
    }

    public void onTweet(View v) {
        String tweetText = etTweet.getText().toString();

        CraigTwitterApp.getRestClient().postTweet(tweetText,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject jsonTweet) {
                        Intent i = new Intent();
                        i.putExtra("jsonTweet", jsonTweet.toString());
                        setResult(RESULT_OK, i);
                        finish();
//                        Log.d("DEBUG", jsonTweet.toString());
                    }
                });
    }

    public void onCancel(View v) {
        setResult(RESULT_CANCELED, null);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.compose, menu);
        return true;
    }

}
