package mehta.com.sunshine_scratch;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Get a root view of the xml UI hierarchy
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        TextView lForecastView = (TextView) rootView.findViewById(R.id.detail_forecast_text);

        // Set forecast string in detail fragment textview
        if(lForecastView != null) {
            lForecastView.setText(DetailActivity.lForecast);
        }

        return rootView;
    }
}
