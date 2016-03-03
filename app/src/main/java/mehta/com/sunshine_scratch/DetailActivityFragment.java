package mehta.com.sunshine_scratch;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    String mForecast;

    String LOG_TAG = DetailActivityFragment.class.getSimpleName();

    public DetailActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //This Fragment will add to menu items
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mForecast = DetailActivity.Forecast;

        // Get a root view of the xml UI hierarchy
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        TextView lForecastView = (TextView) rootView.findViewById(R.id.detail_forecast_text);

        // Set forecast string in detail fragment textview
        if(lForecastView != null) {
            lForecastView.setText(mForecast);
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_weather_detail, menu);

        // Access share item from detail menu xml
        MenuItem item = menu.findItem(R.id.menu_action_share);

        // Helper object for share action
        ShareActionProvider lShareActionProvider =(ShareActionProvider) MenuItemCompat
                .getActionProvider(item);

        if(lShareActionProvider != null)
        {
            lShareActionProvider.setShareIntent(onShareClick());
//            Intent lShareIntent = createShareIntent();

            // Limits list of applications that can share data
            // Thanks to this
            // link: http://stackoverflow.com/questions/9730243/how-to-filter-specific-apps-for-action-send-intent-and-set-a-different-text-for
//            PackageManager packageManager = getActivity().getPackageManager();
//            Intent openInChooser = Intent.createChooser
//                    (lShareIntent, getResources().getString(R.string.action_share));
//
//            List<ResolveInfo> resInfo = packageManager.que

        }
        else
        {
            Log.d(LOG_TAG,"Share action provider for menu item is null");
        }
    }

//    // Sharable weather forecast intent
//    private Intent createShareIntent() {
//        PackageManager pm = getActivity().getPackageManager();
//        // FORECAST INTENT TO BE SENT
//        Intent sendIntent = new Intent(Intent.ACTION_SEND);
//        sendIntent.setType("text/plain");
//        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
//
//
//        Intent openInChooser = Intent.createChooser
//                (sendIntent, mForecast);
//
//        List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
//        List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();
//
//        for(int i = 0; i < resInfo.size(); i++)
//        {
//            ResolveInfo ri = resInfo.get(i);
//            String appPackageName  = ri.activityInfo.packageName;
//
//            if(appPackageName.contains("com.whatsapp") || appPackageName.contains("com.android.mms") )
//            {
//                Intent intent = new Intent();
//                intent.setComponent(new ComponentName(appPackageName, ri.activityInfo.name));
//                intent.setAction(Intent.ACTION_SEND);
//                intent.setType("text/plain");
//                sendIntent.putExtra(Intent.EXTRA_TEXT, mForecast);
//
//                // Make individual custom label
//                intentList.add(new LabeledIntent(intent, appPackageName, ri.loadLabel(pm),
//                        ri.icon));
//            }
//
//
//        }
//
//        LabeledIntent[] extraIntents = intentList.toArray(new LabeledIntent[intentList.size()]);
//
//        openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
//
//
//        return openInChooser;
//
//    }

    public Intent onShareClick(){
        List<Intent> targetShareIntents=new ArrayList<Intent>();
        Intent shareIntent=new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        List<ResolveInfo> resInfos=getActivity().getPackageManager().queryIntentActivities(shareIntent, 0);
        if(!resInfos.isEmpty()){
            System.out.println("Have package");
            for(ResolveInfo resInfo : resInfos)
            {
                String packageName=resInfo.activityInfo.packageName;
                Log.i("Package Name", packageName);
                if(packageName.contains("com.whatsapp") || packageName.contains("com.android.mms"))
                {
                    Intent intent=new Intent();
                    intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, mForecast);
                    intent.setPackage(packageName);
                    targetShareIntents.add(intent);
                }
            }
            if(!targetShareIntents.isEmpty()){
                System.out.println("Have Intent");
                Intent chooserIntent=Intent.createChooser(targetShareIntents.remove(0), "Choose app to share");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetShareIntents.toArray(new Parcelable[]{}));
                return chooserIntent;
            }else{
                System.out.println("Do not Have Intent");
            }
        }

        return null;
    }


}
