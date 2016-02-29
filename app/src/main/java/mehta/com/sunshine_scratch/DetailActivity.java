package mehta.com.sunshine_scratch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


public class DetailActivity extends AppCompatActivity {

    // Forecast to be displayed on detailed fragment
    public static String lForecast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // // Get weather forecast from intent
        if (getIntent() != null & getIntent().hasExtra(Intent.EXTRA_TEXT))
        {
        lForecast = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailActivityFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    // WEATHER SETTINGS for detail activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent lIntent = new Intent();
            lIntent.setClass(this, SettingsActivity.class);

            startActivity(lIntent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
