package barqsoft.footballscores;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import barqsoft.footballscores.service.MyFetchService;
import barqsoft.footballscores.service.ScoresWidgetService;

/**
 * The AppWidgetProvider receives only the event broadcasts that are relevant to the
 * App Widget, such as when the App Widget is updated, deleted, enabled, and disabled.
 * <p/>
 * The ScoresWidgetProvider gets all the widgets tied to this provider and builds an
 * intent to call the service that updates the widgets.
 * <p/>
 * References:
 * [1] http://www.vogella.com/tutorials/AndroidWidgets/article.html
 * [2] http://innovativenetsolutions.com/2013/07/android-tutorial-appwidget-with-its-own-contentprovider-service/
 * [3] http://stackoverflow.com/questions/23220757/android-widget-onclick-listener-for-several-buttons
 */
public class ScoresWidgetProvider extends AppWidgetProvider {

    private static final String TAG = "ScoresWidgetProvider";

    public static final String UPDATE_SCORES = "barqsoft.footballscores.service.MyFetchService";
    public static final String CHANGE_MATCH = "barqsoft.footballscores.service.ScoreWidgetService";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getBooleanExtra(UPDATE_SCORES, false)) {
            Log.d(TAG, UPDATE_SCORES);
            Intent service_start = new Intent(context.getApplicationContext(), MyFetchService.class);
            context.startService(service_start);
        } else if (intent.getBooleanExtra(CHANGE_MATCH, false)) {
            Log.d(TAG, CHANGE_MATCH);
            ScoresWidgetService.counter++;
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "Get all ids");
        ComponentName thisWidget = new ComponentName(context, ScoresWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        Log.d(TAG, "Build the intent to call the service");
        Intent intent = new Intent(context.getApplicationContext(), ScoresWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

        Log.d(TAG, "Update the widgets via the service");
        context.startService(intent);
    }

}
