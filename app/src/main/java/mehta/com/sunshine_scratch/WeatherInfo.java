package mehta.com.sunshine_scratch;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ishan on 2016-02-17.
 */
public class WeatherInfo {

    public static String fetchCityWeather(String aInURL)
    {
        HttpURLConnection lUrlConnection = null;
        BufferedReader lReader = null;

        // Weather response string
        String lWeatherResponseStr = null;

        try
        {
            // OpenWeather map query (Src: http://openweathermap.org/API)
            URL lUrl = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7&appid=bd91203cfb4a2e3e7aa532a9f12651f5");

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
        }
        catch (IOException e)
        {
            Log.e("WeatherInfo", "Error", e);

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
                    Log.e("WeatherInfo", "Error closing stream", e);
                }
            }

        }

        return lWeatherResponseStr;
    }
}
