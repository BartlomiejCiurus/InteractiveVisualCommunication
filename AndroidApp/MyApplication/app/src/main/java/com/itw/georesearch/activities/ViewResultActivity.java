package com.itw.georesearch.activities;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.itw.georesearch.R;
import com.itw.georesearch.model.Ikw;
import com.itw.georesearch.support.enums.Frequency;
import com.itw.georesearch.support.enums.Indicators;
import com.itw.georesearch.support.enums.Subjects;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import cz.msebera.android.httpclient.Header;

public class ViewResultActivity extends AppCompatActivity {

    private static String resultUrl = "http://ikwservices-projectig.rhcloud.com/ikwRest/result.json?indicator=";


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        savedInstanceState = getIntent().getExtras();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_result);

        TextView textView = (TextView) findViewById(R.id.country1title);
        TextView textView2 = (TextView) findViewById(R.id.country2title);

        textView.setText(savedInstanceState.getString(MainActivity.FIRST_COUNTRY));
        textView2.setText(savedInstanceState.getString(MainActivity.SECOND_COUNTRY));

        String indicator = Indicators.getEnumByString(savedInstanceState.getString(MainActivity.INDENT));
        String subject = Subjects.getEnumByString(savedInstanceState.getString(MainActivity.PERSPECTIVE));
        String frequency = Frequency.getEnumByString(savedInstanceState.getString(MainActivity.FREQUENCY));

        TextView textView3 = (TextView) findViewById(R.id.perspective1);
        TextView textView4 = (TextView) findViewById(R.id.perspective2);
        TextView textView5 = (TextView) findViewById(R.id.desc1);
        TextView textView6 = (TextView) findViewById(R.id.desc2);

        textView3.setText(savedInstanceState.getString(MainActivity.PERSPECTIVE));
        textView4.setText(savedInstanceState.getString(MainActivity.PERSPECTIVE));
        textView5.setText(savedInstanceState.getString(MainActivity.INDENT));
        textView6.setText(savedInstanceState.getString(MainActivity.INDENT));

        String result1url = resultUrl + indicator + "&subject="
                + subject + "&location=" + savedInstanceState.getString(MainActivity.FIRST_COUNTRY)
                + "&frequency=" + frequency;

        String result2url = resultUrl + indicator + "&subject="
                + subject + "&location=" + savedInstanceState.getString(MainActivity.SECOND_COUNTRY)
                + "&frequency=" + frequency;

        System.out.println(result1url);
        System.out.println(result2url);
        getResponse(result1url, (TextView) findViewById(R.id.result1));
        getResponse(result2url, (TextView) findViewById(R.id.result2));


    }

    private void getResponse(String url, final TextView textView){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                List<Double> ikws = getList(responseBody);
                Double aDouble = (double) 0;
                for (Double d : ikws) {
                    aDouble = aDouble + d;
                }
                textView.setText(String.valueOf(aDouble / ikws.size()));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast toast = Toast.makeText(getApplicationContext(), "Cannot connect to the remote server.", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private List<Double> getList(byte[] responseBody) {
        Gson gson = new GsonBuilder().create();
        Type listType = new TypeToken<List<Double>>() {
        }.getType();
        String decoded = "";
        try {
            decoded = new String(responseBody, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return gson.fromJson(decoded, listType);
    }

}
