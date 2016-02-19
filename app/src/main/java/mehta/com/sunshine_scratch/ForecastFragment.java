package mehta.com.sunshine_scratch;

import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {

    public final static String cityCode = "145001";

    public ForecastFragment() {
    }

    // Where fragment gets created
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // To state fragment has menu options
        setHasOptionsMenu(true);
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
                "Sun 6/29 - Sunny - 20/7" };

        // Add array to arraylist
        List<String> weatherForecastlist = new ArrayList<String>();

        for(int i= 0; i < values.length; ++i){
            weatherForecastlist.add(values[i]);
        }

        ArrayAdapter<String> forecastAdapter = new ArrayAdapter<String>(
                getActivity(), // The current context (this activity)
                R.layout.fragment_main, //The ID of list layout
                R.id.list_item_forecast_textview2, // The ID of textview to populate
                weatherForecastlist // weather forecast list data
                );

        // Get a root view of the xml UI hierarchy
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the listview to populate
        ListView myforecastList = (ListView)rootView.findViewById(R.id.listview_forecast);

        // Bind forecast adapter(contains info about raw data) to the listview
        myforecastList.setAdapter(forecastAdapter);

        // Invoke background network operation
        new FetchWeatherTask().execute(cityCode);

        return rootView;
    }

    // Fetch weather information
    private class FetchWeatherTask extends AsyncTask<String, Void, String>
    {

        // Async tasks log tag for debugging
        final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection lUrlConnection = null;
            BufferedReader lReader = null;

            // Weather JSON response string
            String lWeatherResponseStr = null;

            // Values for http request
            String format = "json";
            String units = "metric";
            int numDaysForecast = 7;
            final String appId = "bd91203cfb4a2e3e7aa532a9f12651f5";
            try
            {
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

                Uri requestUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, params[0]) // Params[0] is city zipcode
                        .appendQueryParameter(FORMAT_PARAM, format)
                        .appendQueryParameter(UNITS_PARAM, units)
                        .appendQueryParameter(DAYS_PARAM, Integer.toString(numDaysForecast))
                        .appendQueryParameter(APPID_PARAM, appId)
                        .build();

                // OpenWeather map query (Src: http://openweathermap.org/API)
                URL lUrl = new URL(requestUri.toString());

                Log.v(LOG_TAG, "Openweather request :" + requestUri.toString());

                // Create request to openWeatherMap, and open the connection
                lUrlConnection = (HttpURLConnection)lUrl.openConnection();
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

                Log.v(LOG_TAG, "Http Json Response :" + lWeatherResponseStr);
            }
            catch (IOException e)
            {
                Log.e(LOG_TAG, "Error", e);

                // If no response is recieved then there is no point of parsing it
                return null;
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
    }
}
