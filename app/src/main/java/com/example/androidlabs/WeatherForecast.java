package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.os.Bundle;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherForecast extends AppCompatActivity {
    ProgressBar progressBar;
    ImageView currentWeatherView;
    TextView currentTempView;
    TextView minTempView;
    TextView maxTempView;
    TextView uvRateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        ForecastQuery fq = new ForecastQuery();
        fq.execute("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric");
        currentWeatherView = findViewById(R.id.currentWeather);
        currentTempView = findViewById(R.id.currentTemp);
        minTempView = findViewById(R.id.minTemp);
        maxTempView = findViewById(R.id.maxTemp);
        uvRateView = findViewById(R.id.uvRate);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);


    }

    private class ForecastQuery extends AsyncTask<String, Integer, String> {

        String uvRate = "";
        String minTemp = "";
        String maxTemp = "";
        String currentTemp = "";
        Bitmap currentWeather;
        String iconName="";

        @Override
        public String doInBackground(String... args) {
            try {

                URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream response = urlConnection.getInputStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( response  , "UTF-8");

                int eventType = xpp.getEventType();

                while(eventType != XmlPullParser.END_DOCUMENT)
                {

                    if(eventType == XmlPullParser.START_TAG)
                    {
                        //If you get here, then you are pointing at a start tag
                        if(xpp.getName().equals("temperature"))
                        {
                            currentTemp = xpp.getAttributeValue(null,"value");
                            publishProgress(25);
                            minTemp = xpp.getAttributeValue(null,"min");
                            publishProgress(50);
                            maxTemp = xpp.getAttributeValue(null,"max");
                            publishProgress(75);
                        }

                        else if(xpp.getName().equals("weather"))
                        {
                            iconName = xpp.getAttributeValue(null,"icon");
                        }
                    }
                    eventType = xpp.next();
                }
                URL imgURL = new URL("https://openweathermap.org/img/w/" + iconName + ".png");
                HttpURLConnection urlConnection2 = (HttpURLConnection) imgURL.openConnection();
                urlConnection2.connect();
                int responseCode = urlConnection2.getResponseCode();
                if (responseCode == 200) {
                    currentWeather = BitmapFactory.decodeStream(urlConnection2.getInputStream());
                    Log.d("icon location:", "downloading it ");
                }
                FileOutputStream outputStream = openFileOutput(iconName + ".png", Context.MODE_PRIVATE);
                currentWeather.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                outputStream.flush();
                outputStream.close();

//                FileInputStream fis = null;
//                try { fis = openFileInput(imagefile); }
//                catch (FileNotFoundException e) {    e.printStackTrace();  }
//                Bitmap bm = BitmapFactory.decodeStream(fis);


                URL UVurl = new URL("https://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389");
                HttpURLConnection UVConnection = (HttpURLConnection) UVurl.openConnection();
                response = UVConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString();

                JSONObject uvReport = new JSONObject(result);
                double uvRating = uvReport.getDouble("value");
                uvRate = uvReport.getString("value");

                Log.i("MainActivity", "The uv is now: " + uvRating) ;

            } catch (MalformedURLException e) {
                e.printStackTrace();
             } catch (IOException e) {
                e.printStackTrace();
             } catch (Exception e) {
                e.printStackTrace();
            }
            return "Finished";
        }

        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        public void onProgressUpdate(Integer ... values)
        {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
        }

        public void onPostExecute(String fromDoInBackground)
        {
            currentTempView.append(currentTemp);
            minTempView.append(minTemp);
            maxTempView.append(maxTemp);
            uvRateView.append(uvRate);
            currentWeatherView.setImageBitmap(currentWeather);
            progressBar.setVisibility(View.INVISIBLE );
            Log.i("HTTP", fromDoInBackground);
        }
    }
    }

