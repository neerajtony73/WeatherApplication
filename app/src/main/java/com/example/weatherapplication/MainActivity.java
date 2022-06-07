package com.example.weatherapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    URL url;
    HttpURLConnection httpURLConnection;

    String finalResult;
    Button getWeatherButton;
    EditText cityTextField;
    String cityName;
    TextView displayWeather,displayWeatherInformation;

    public void getWeather(View view){
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.cityTextField.getWindowToken(),0);
        cityName = cityTextField.getText().toString();
        displayWeatherInformation.setText(null);
        displayWeather.setText(null);
        WeatherApp weatherApp = new WeatherApp();

        try {
            String encodedCityName = URLEncoder.encode(cityName,"UTF-8");
            finalResult = weatherApp.execute("http://api.openweathermap.org/data/2.5/weather?q="+encodedCityName+"&appid=a9a02ee94f500bd3a0c2fc6cf0027c78").get();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try {
            JSONObject jsonObject = new JSONObject(finalResult);
            String weather = jsonObject.getString("weather");
            // Log.i("weather",weather);
            JSONArray jsonArray = new JSONArray(weather);
            for(int i=0; i < jsonArray.length();i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                Log.i("main",jsonObject1.getString("main"));
                displayWeather.setText(jsonObject1.getString("main"));
                Log.i("description",jsonObject1.getString("description"));
                displayWeatherInformation.setText(jsonObject1.getString("description"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    public class WeatherApp extends AsyncTask<String, Void, String>
    {



        @Override
        protected String doInBackground(String... args) {
            String result ="";
            try {

                url = new URL(args[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();

                while(data != -1){
                    char c = (char) data;
                    result+=c;
                    data = inputStreamReader.read();
                }

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Couldn't get the weather info!",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            return result;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getWeatherButton = findViewById(R.id.button);
        cityTextField = findViewById(R.id.editTextTextPersonName);
        displayWeather = findViewById(R.id.textView2);

        displayWeatherInformation = findViewById(R.id.textView3);
        displayWeatherInformation.setText(null);
        displayWeather.setText(null);


    }
}