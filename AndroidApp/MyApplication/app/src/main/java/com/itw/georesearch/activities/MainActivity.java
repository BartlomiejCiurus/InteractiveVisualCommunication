package com.itw.georesearch.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.itw.georesearch.R;
import com.itw.georesearch.support.enums.Frequency;
import com.itw.georesearch.support.enums.Indicators;
import com.itw.georesearch.support.enums.Subjects;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    public static final String SELECT = "Select...";
    public static final String INDENT = "indent";
    public static final String PERSPECTIVE = "perspective";
    public static final String FREQUENCY = "frequency";
    public static final String FROM = "from";
    public static final String TO = "to";
    public static final String FIRST_COUNTRY = "firstCountry";
    public static final String SECOND_COUNTRY = "secondCountry";
    public static String indicatorUrl = "http://ikwservices-projectig.rhcloud.com/ikwRest/indicators.json";
    public static String subjectUrl = "http://ikwservices-projectig.rhcloud.com/ikwRest/subjects.json?indicator=";
    public static String frequencyUrl = "http://ikwservices-projectig.rhcloud.com/ikwRest/frequency.json?indicator=";
    public static String datesUrl = "http://ikwservices-projectig.rhcloud.com/ikwRest/dates.json?indicator=";
    public static String locationUrl = "http://ikwservices-projectig.rhcloud.com/ikwRest/location.json?indicator=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //set up full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getResponseListForIndicator(getApplicationContext(), (Spinner) findViewById(R.id.indentSpinner),
                (Spinner) findViewById(R.id.perspectiveSpinner), (TextView) findViewById(R.id.perspectiveTitle), indicatorUrl);


        Button button = (Button) findViewById(R.id.submit);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewResultActivity.class);
                intent.putExtra(INDENT, ((Spinner) findViewById(R.id.indentSpinner)).getSelectedItem().toString());
                intent.putExtra(PERSPECTIVE, ((Spinner) findViewById(R.id.perspectiveSpinner)).getSelectedItem().toString());
                intent.putExtra(FREQUENCY, ((Spinner) findViewById(R.id.frequencySpinner)).getSelectedItem().toString());
                intent.putExtra(FROM, ((Spinner) findViewById(R.id.fromDateSpinner)).getSelectedItem().toString());
                intent.putExtra(TO, ((Spinner) findViewById(R.id.toDateSpinner)).getSelectedItem().toString());
                intent.putExtra(FIRST_COUNTRY, ((Spinner) findViewById(R.id.firstCountrySpinner)).getSelectedItem().toString());
                intent.putExtra(SECOND_COUNTRY, ((Spinner) findViewById(R.id.secondCountrySpinner)).getSelectedItem().toString());
                startActivity(intent);
            }
        });

    }

    private List<String> getList(byte[] responseBody) {
        Gson gson = new GsonBuilder().create();
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        String decoded = "";
        try {
            decoded = new String(responseBody, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return gson.fromJson(decoded, listType);
    }

    private void getResponseListForIndicator(final Context context, final Spinner spinner, final Spinner secondSpinner, final TextView textView, String url) {
        AsyncHttpClient client = new AsyncHttpClient();
        System.out.println(url);
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                List<String> strings = getList(responseBody);
                String[] indicators = new String[strings.size() + 1];
                indicators[0] = SELECT;
                for (int i = 1; i < indicators.length; i++) {
                    indicators[i] = Indicators.valueOf(strings.get(i - 1)).toString();
                }
                ArrayAdapter<String> perspectiveArray = new ArrayAdapter<>(context, R.layout.row, indicators);
                spinner.setAdapter(perspectiveArray);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (!parent.getSelectedItem().toString().contains(SELECT)) {
                            Toast toast = Toast.makeText(getApplicationContext(), parent.getSelectedItem().toString() + " selected", Toast.LENGTH_SHORT);
                            toast.show();
                            secondSpinner.setVisibility(View.VISIBLE);
                            textView.setVisibility(View.VISIBLE);
                            getResponseListForSubject(getApplicationContext(), (Spinner) findViewById(R.id.perspectiveSpinner),
                                    (Spinner) findViewById(R.id.frequencySpinner), (TextView) findViewById(R.id.frequencyTitle),
                                    subjectUrl + Indicators.getEnumByString(parent.getSelectedItem().toString()));

                        } else {
                            secondSpinner.setVisibility(View.INVISIBLE);
                            textView.setVisibility(View.INVISIBLE);
                            findViewById(R.id.frequencySpinner).setVisibility(View.INVISIBLE);
                            findViewById(R.id.frequencyTitle).setVisibility(View.INVISIBLE);
                            findViewById(R.id.fromDate).setVisibility(View.INVISIBLE);
                            findViewById(R.id.fromDateSpinner).setVisibility(View.INVISIBLE);
                            findViewById(R.id.toDateSpinner).setVisibility(View.INVISIBLE);
                            findViewById(R.id.toDate).setVisibility(View.INVISIBLE);
                            findViewById(R.id.firstCountry).setVisibility(View.INVISIBLE);
                            findViewById(R.id.firstCountrySpinner).setVisibility(View.INVISIBLE);
                            findViewById(R.id.secondCountry).setVisibility(View.INVISIBLE);
                            findViewById(R.id.secondCountrySpinner).setVisibility(View.INVISIBLE);
                            findViewById(R.id.submit).setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast toast = Toast.makeText(getApplicationContext(), "Cannot connect to the remote server.", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }


    private void getResponseListForSubject(final Context context, final Spinner spinner, final Spinner secondSpinner, final TextView textView, String url) {
        AsyncHttpClient client = new AsyncHttpClient();
        System.out.println(url);
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                List<String> strings = getList(responseBody);
                String[] indicators = new String[strings.size() + 1];
                indicators[0] = SELECT;
                for (int i = 1; i < indicators.length; i++) {
                    if (strings.get(i - 1).equals("15_24") ||
                            strings.get(i - 1).equals("25_54") ||
                            strings.get(i - 1).equals("55_64")) {
                        indicators[i] = strings.get(i - 1).replace("_", " - ");
                    } else if (strings.get(i - 1).equals("65MORE")) {
                        indicators[i] = "More than 65";
                    } else {
                        indicators[i] = Subjects.valueOf(strings.get(i - 1)).toString();
                    }
                }
                ArrayAdapter<String> perspectiveArray = new ArrayAdapter<>(context, R.layout.row, indicators);
                spinner.setAdapter(perspectiveArray);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (!parent.getSelectedItem().toString().contains(SELECT)) {
                            Toast toast = Toast.makeText(getApplicationContext(), parent.getSelectedItem().toString() + " selected", Toast.LENGTH_SHORT);
                            toast.show();
                            secondSpinner.setVisibility(View.VISIBLE);
                            textView.setVisibility(View.VISIBLE);

                            getResponseListForFrequency(getApplicationContext(), (Spinner) findViewById(R.id.frequencySpinner),
                                    (Spinner) findViewById(R.id.fromDateSpinner), (TextView) findViewById(R.id.fromDate),
                                    frequencyUrl + (Indicators.getEnumByString(((Spinner) findViewById(R.id.indentSpinner)).getSelectedItem().toString())));

                        } else {
                            secondSpinner.setVisibility(View.INVISIBLE);
                            textView.setVisibility(View.INVISIBLE);
                            findViewById(R.id.fromDate).setVisibility(View.INVISIBLE);
                            findViewById(R.id.fromDateSpinner).setVisibility(View.INVISIBLE);
                            findViewById(R.id.toDateSpinner).setVisibility(View.INVISIBLE);
                            findViewById(R.id.toDate).setVisibility(View.INVISIBLE);
                            findViewById(R.id.firstCountry).setVisibility(View.INVISIBLE);
                            findViewById(R.id.firstCountrySpinner).setVisibility(View.INVISIBLE);
                            findViewById(R.id.secondCountry).setVisibility(View.INVISIBLE);
                            findViewById(R.id.secondCountrySpinner).setVisibility(View.INVISIBLE);
                            findViewById(R.id.submit).setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }

            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast toast = Toast.makeText(getApplicationContext(), "Cannot connect to the remote server.", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }


    private void getResponseListForFrequency(final Context context, final Spinner spinner, final Spinner secondSpinner, final TextView textView, String url) {
        AsyncHttpClient client = new AsyncHttpClient();
        System.out.println(url);
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                List<String> strings = getList(responseBody);
                String[] indicators = new String[strings.size() + 1];
                indicators[0] = SELECT;
                for (int i = 1; i < indicators.length; i++) {
                    indicators[i] = Frequency.valueOf(strings.get(i - 1)).toString();
                }
                ArrayAdapter<String> perspectiveArray = new ArrayAdapter<>(context, R.layout.row, indicators);
                spinner.setAdapter(perspectiveArray);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (!parent.getSelectedItem().toString().contains(SELECT)) {
                            Toast toast = Toast.makeText(getApplicationContext(), parent.getSelectedItem().toString() + " selected", Toast.LENGTH_SHORT);
                            toast.show();
                            secondSpinner.setVisibility(View.VISIBLE);
                            textView.setVisibility(View.VISIBLE);

                            getResponseListForDates(getApplicationContext(), (Spinner) findViewById(R.id.fromDateSpinner),
                                    (Spinner) findViewById(R.id.toDateSpinner), (TextView) findViewById(R.id.toDate),
                                    datesUrl + (Indicators.getEnumByString(((Spinner) findViewById(R.id.indentSpinner)).getSelectedItem().toString()))
                                            + "&frequency=" + Frequency.getEnumByString(parent.getSelectedItem().toString()));

                        } else {
                            secondSpinner.setVisibility(View.INVISIBLE);
                            textView.setVisibility(View.INVISIBLE);
                            findViewById(R.id.toDateSpinner).setVisibility(View.INVISIBLE);
                            findViewById(R.id.toDate).setVisibility(View.INVISIBLE);
                            findViewById(R.id.firstCountry).setVisibility(View.INVISIBLE);
                            findViewById(R.id.firstCountrySpinner).setVisibility(View.INVISIBLE);
                            findViewById(R.id.secondCountry).setVisibility(View.INVISIBLE);
                            findViewById(R.id.secondCountrySpinner).setVisibility(View.INVISIBLE);
                            findViewById(R.id.submit).setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }

            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast toast = Toast.makeText(getApplicationContext(), "Cannot connect to the remote server.", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private void getResponseListForDates(final Context context, final Spinner spinner, final Spinner secondSpinner, final TextView textView, String url) {
        AsyncHttpClient client = new AsyncHttpClient();
        System.out.println(url);
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                final List<String> strings = getList(responseBody);
                String[] indicators = new String[strings.size() + 1];
                indicators[0] = SELECT;
                for (int i = 1; i < indicators.length; i++) {
                    indicators[i] = strings.get(i - 1);
                }
                ArrayAdapter<String> perspectiveArray = new ArrayAdapter<>(context, R.layout.row, indicators);
                spinner.setAdapter(perspectiveArray);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (!parent.getSelectedItem().toString().contains(SELECT)) {
                            Toast toast = Toast.makeText(getApplicationContext(), parent.getSelectedItem().toString() + " selected", Toast.LENGTH_SHORT);
                            toast.show();
                            secondSpinner.setVisibility(View.VISIBLE);
                            textView.setVisibility(View.VISIBLE);

                            giveResponseTo(getApplicationContext(), (Spinner) findViewById(R.id.toDateSpinner),
                                    (Spinner) findViewById(R.id.firstCountrySpinner), (TextView) findViewById(R.id.firstCountry), strings, parent.getSelectedItem().toString());

                        } else {
                            secondSpinner.setVisibility(View.INVISIBLE);
                            textView.setVisibility(View.INVISIBLE);
                            findViewById(R.id.firstCountry).setVisibility(View.INVISIBLE);
                            findViewById(R.id.firstCountrySpinner).setVisibility(View.INVISIBLE);
                            findViewById(R.id.secondCountry).setVisibility(View.INVISIBLE);
                            findViewById(R.id.secondCountrySpinner).setVisibility(View.INVISIBLE);
                            findViewById(R.id.submit).setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }

            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast toast = Toast.makeText(getApplicationContext(), "Cannot connect to the remote server.", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private void giveResponseTo(Context context, Spinner spinner, final Spinner secondSpinner, final TextView textView, final List<String> strings, String choise) {
        List<String> tempIndi = new ArrayList<>();
        for (String s : strings) {
            if(s.compareToIgnoreCase(choise) >= 0)
                tempIndi.add(s);
        }
        String[] indicators = new String[tempIndi.size() + 1];
        indicators[0] = SELECT;
        for (int i = 1; i < indicators.length; i++) {
            indicators[i] = tempIndi.get(i - 1);
        }
        ArrayAdapter<String> perspectiveArray = new ArrayAdapter<>(context, R.layout.row, indicators);
        spinner.setAdapter(perspectiveArray);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!parent.getSelectedItem().toString().contains(SELECT)) {
                    Toast toast = Toast.makeText(getApplicationContext(), parent.getSelectedItem().toString() + " selected", Toast.LENGTH_SHORT);
                    toast.show();
                    secondSpinner.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.VISIBLE);
                    getResponseListForLocations(getApplicationContext(), (Spinner) findViewById(R.id.firstCountrySpinner),
                            (Spinner) findViewById(R.id.secondCountrySpinner), (TextView) findViewById(R.id.secondCountry),
                            locationUrl + (Indicators.getEnumByString(((Spinner) findViewById(R.id.indentSpinner)).getSelectedItem().toString())));

                } else {
                    secondSpinner.setVisibility(View.INVISIBLE);
                    textView.setVisibility(View.INVISIBLE);
                    findViewById(R.id.secondCountry).setVisibility(View.INVISIBLE);
                    findViewById(R.id.secondCountrySpinner).setVisibility(View.INVISIBLE);
                    findViewById(R.id.submit).setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    private void getResponseListForLocations(final Context context, final Spinner spinner, final Spinner secondSpinner, final TextView textView, String url) {
        AsyncHttpClient client = new AsyncHttpClient();
        System.out.println(url);
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                final List<String> strings = getList(responseBody);
                String[] indicators = new String[strings.size() + 1];
                indicators[0] = SELECT;
                for (int i = 1; i < indicators.length; i++) {
                    indicators[i] = strings.get(i - 1);
                }
                ArrayAdapter<String> perspectiveArray = new ArrayAdapter<>(context, R.layout.row, indicators);
                spinner.setAdapter(perspectiveArray);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (!parent.getSelectedItem().toString().contains(SELECT)) {
                            Toast toast = Toast.makeText(getApplicationContext(), parent.getSelectedItem().toString() + " selected", Toast.LENGTH_SHORT);
                            toast.show();
                            secondSpinner.setVisibility(View.VISIBLE);
                            textView.setVisibility(View.VISIBLE);

                            giveResponseTo(getApplicationContext(), (Spinner) findViewById(R.id.secondCountrySpinner), strings, parent.getSelectedItem().toString());

                        } else {
                            secondSpinner.setVisibility(View.INVISIBLE);
                            textView.setVisibility(View.INVISIBLE);
                            findViewById(R.id.submit).setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }

            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast toast = Toast.makeText(getApplicationContext(), "Cannot connect to the remote server.", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private void giveResponseTo(Context context, Spinner spinner, List<String> strings, String choise) {
        List<String> tempIndi = new ArrayList<>();
        for (String s : strings) {
            if(!(s.compareToIgnoreCase(choise) == 0))
                tempIndi.add(s);
        }
        String[] indicators = new String[tempIndi.size() + 1];
        indicators[0] = SELECT;
        for (int i = 1; i < indicators.length; i++) {
            indicators[i] = tempIndi.get(i - 1);
        }
        ArrayAdapter<String> perspectiveArray = new ArrayAdapter<>(context, R.layout.row, indicators);
        spinner.setAdapter(perspectiveArray);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!parent.getSelectedItem().toString().contains(SELECT)) {
                    Toast toast = Toast.makeText(getApplicationContext(), parent.getSelectedItem().toString() + " selected", Toast.LENGTH_SHORT);
                    toast.show();
                    findViewById(R.id.submit).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.submit).setVisibility(View.INVISIBLE);
                }
            }
        });
    }


}
