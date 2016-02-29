package mehta.com.sunshine_scratch;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {

    public final static String cityCode = "K1A 0A1";

    ArrayAdapter<String> forecastAdapter;

    TextView list_item_textview = null;

    public ForecastFragment() {
    }

    // Where fragment gets created
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // To state fragment has menu options
        setHasOptionsMenu(true);

        list_item_textview = (TextView) getActivity().findViewById(R.id.list_item_forecast_textview);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            // For debugging purpose
            // (TODO: Remove in production app)
            // Invoke background network operation
            new FetchWeatherTask().execute(cityCode);
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    // UI gets created
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Dummy data
        // Populate array
        String[] values = new String[] { "Mon 6/23 - Sunny - 31/17",
                "Tue 6/24 - Foggy - 21/8",
                "Wed 6/25 - Cloudy - 22/17",
                "Thurs 6/26 - Rainy - 18/11",
                "Fri 6/27 - Foggy - 21/10",
                "Sat 6/28 - TRAPPED IN WEATHERSTATION - 23/18",
                "Sun 6/29 - Sunny - 20/7" ,
                "Sun 6/29 - Sunny - 20/7",
                "Sun 6/29 - Sunny - 20/7"
        , "Sun 6/29 - Sunny - 20/7"};

        // Add array to arraylist
        List<String> weatherForecastList = new ArrayList<String>(Arrays.asList(values));

//        forecastAdapter = new ArrayAdapter<String>(
//                getActivity(), // The current context (this activity)
//                R.layout.fragment_main, //The ID of layout containing list
//                R.id.list_item_forecast_textview, // The ID of textview to populate
//                weatherForecastList // weather forecast list data
//                );
        // Working perfectly !!
        forecastAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.layout1,
                weatherForecastList
        );

        // Get a root view of the xml UI hierarchy
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the listview to populate
        ListView myforecastList = (ListView)rootView.findViewById(R.id.listview_forecast);

        // Bind forecast adapter(contains info about raw data) to the listview
        myforecastList.setAdapter(forecastAdapter);


        // Register a callback to be invoked when a list item is clicked
        myforecastList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                // Day forecast string
                String lForecast = forecastAdapter.getItem(position);

                // Explicit intent
                Intent lIntent =  new Intent();
                lIntent.setClass(getActivity(), DetailActivity.class);
                lIntent.putExtra(Intent.EXTRA_TEXT, lForecast);
                //Launch a new activity
                startActivity(lIntent);
            }



        });

        return rootView;
    }

    // Fetch weather information
    private class FetchWeatherTask extends AsyncTask<String, Void, String[]>
    {

        // Async tasks log tag for debugging
        final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        @Override
        protected String[] doInBackground(String... params) {

            // test: Weather forecast output string array
            String[] lResultForecast=null;

            // Weather JSON response string
            String lWeatherResponseStr = null;

            // Values for http request
            String format = "json";
            String units = "metric";
            int  numDaysForecast = 7;
            final String appId = "bd91203cfb4a2e3e7aa532a9f12651f5";
            try
            {
                // Create forecast request with city's postal code i.e. params[0]
                Uri requestUri = getRequest(format, units, numDaysForecast, appId, params[0]);
                URL lUrl = new URL(requestUri.toString());

                Log.v(LOG_TAG, "Openweather request :" + requestUri.toString());

                // Send request & weather response string
                lWeatherResponseStr = sendRequestFetchResult(lUrl);

                /* Parse json object */
                try {
                    JSONObject lWeatherJsonResponse = new JSONObject(lWeatherResponseStr);
                    JSONArray lForecastList = lWeatherJsonResponse.getJSONArray("list");

                    //length of resultant forecast array
                    lResultForecast = new String[lForecastList.length()];

                    // Put json weather list to a tmp forecast array
                    for (int i=0; i<lForecastList.length(); i++)
                    {
                        String lDay;
                        String lDescription;
                        String lMaxTemp;
                        String lMinTemp;

                        // Reference of forecast list detail
                        JSONObject lDayForecast = lForecastList.getJSONObject(i);

                        // Get date
                        Long millisecval = lDayForecast.getLong("dt");
                        lDay = getFormattedDate(millisecval);

                        //Get description
                        JSONObject lWeatherDescriptionDetails = lDayForecast.getJSONArray("weather").getJSONObject(0);
                        lDescription = lWeatherDescriptionDetails.getString("description");

                        // Get high & low temp
                        JSONObject lTempDetails = lDayForecast.getJSONObject("temp");
                        Double lMxTemp = lTempDetails.getDouble("max");
                        Double lMnTemp = lTempDetails.getDouble("min");
                        // Round off temperature values
                        lMaxTemp =  String.valueOf(Math.round(lMxTemp));
                        lMinTemp =  String.valueOf(Math.round(lMnTemp));

                        // Add individual entry into result weather forecast tile
                        lResultForecast[i] = lDay + " - " + lDescription + " - " + lMaxTemp + "/" + lMinTemp;
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.v(LOG_TAG, "Http Json Response :" + lWeatherResponseStr);
            }
            catch (IOException e)
            {
                Log.e(LOG_TAG, "Error", e);

                // If no response is received then there is no point of parsing it
                return null;
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return lResultForecast;
        }

        /**
         * Populate forecast adapter with weather forecast
         *
         * @param result - Result weather forecast array
         */
        @Override
        protected void onPostExecute(String[] result) {
            if(result != null) {
                // Remove previous forecast info
                forecastAdapter.clear();
                // Add new forecast info to adapter
                for (String forecast : result) {
                    forecastAdapter.add(forecast);
                }
            }
        }

        private String getFormattedDate(Long millisecval) {
            Date dt = new Date(millisecval);
            // Format eg: Sat Jan 17
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd");
            return sdf.format(dt);
        }

        private Uri getRequest(String format, String units, int numDaysForecast, String appId, String param) {
            // Base url request
            // (Example:http://api.openweathermap.org/data/2.5/forecast/daily?q=94043,USA
            // &mode=json&cnt=7&units=metric&appid=bd91203cfb4a2e3e7aa532a9f12651f5)
            final String FORECAST_BASE_URL =
                    "http://api.openweathermap.org/data/2.5/forecast/daily?";
            final String QUERY_PARAM = "q";
            final String FORMAT_PARAM = "mode";
            final String UNITS_PARAM = "units";
            final String DAYS_PARAM = "cnt";
            final String APPID_PARAM = "APPID";

            return Uri.parse(FORECAST_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, param) // Params[0] is city zipcode
                    .appendQueryParameter(FORMAT_PARAM, format)
                    .appendQueryParameter(UNITS_PARAM, units)
                    .appendQueryParameter(DAYS_PARAM, Integer.toString(numDaysForecast))
                    .appendQueryParameter(APPID_PARAM, appId)
                    .build();
        }

        private String sendRequestFetchResult(URL aInRequest)
        {
            // Http connection reference
            HttpURLConnection lUrlConnection = null;
            // Reads http response
            BufferedReader lReader = null;

            // Weather json string response
            String lWeatherResponseStr = null;

            try {
                // Create request to openWeatherMap, and open the connection
                lUrlConnection = (HttpURLConnection) aInRequest.openConnection();
                lUrlConnection.setRequestMethod("GET");
                lUrlConnection.connect();

                // Read the input stream into a string
                InputStream lInputStream = lUrlConnection.getInputStream();
                StringBuffer lStrBuffer = new StringBuffer();

                if(lInputStream == null)
                {
                    //Nothing to do
                    return null;
                }

                // Input stream reader - reads bytes and decodes them into characters
                lReader = new BufferedReader(new InputStreamReader(lInputStream));

                String line;

                // Store the json response in stringbuffer
                while((line = lReader.readLine()) != null)
                {
                    lStrBuffer.append(line + "\n");
                }

                if(lStrBuffer.length() == 0)
                {
                    //No point of parsing the response
                    return null;
                }

                // Set the weather infor response
                lWeatherResponseStr = lStrBuffer.toString();
            }
            catch (IOException e)
            {
                Log.e(LOG_TAG, "Error", e);
            }
            finally
            {
                // Close all resources
                if(lUrlConnection != null)
                {
                    lUrlConnection.disconnect();
                }

                if (lReader != null)
                {
                    try
                    {
                        lReader.close();
                    }
                    catch (IOException e)
                    {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            return lWeatherResponseStr;
        }

        private String[] getForecastWeather(String aInJSONResponse, int aInDayIndex)
        {
            String[] lWeatherForecastArr = null;

            return lWeatherForecastArr;

        }
    }
}
