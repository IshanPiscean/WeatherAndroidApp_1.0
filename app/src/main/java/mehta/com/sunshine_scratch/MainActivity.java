package mehta.com.sunshine_scratch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    final static String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment()).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * WEATHER APP SETTINGS
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent lIntent = new Intent();
            lIntent.setClass(this, SettingsActivity2.class);
            startActivity(lIntent);

            return true;
        }
        else if(id == R.id.action_map)
        {
            SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
            // Get preferred location
            String location = preference.getString(getString
                    (R.string.pref_key_location),
                    getString(R.string.pref_default_location));
            // Show the location on map application
            showMap(location);

            return  true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * View location on a map
     * @param aInLocation
     */
    public void showMap(String aInLocation) {

        final String BASE_URL = "geo:0,0?";
        final String QUERY_PARAM = "q";

        Uri mapLocation =Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM,aInLocation)
                .build();
        Intent mapIntent = new Intent(Intent.ACTION_VIEW);
        mapIntent.setData(mapLocation);
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
        else {
            Log.d(LOG_TAG, "Couldn't find" + aInLocation + " on map");
        }
    }
}
