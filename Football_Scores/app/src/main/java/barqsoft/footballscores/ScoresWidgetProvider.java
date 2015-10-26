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
 *
 * The ScoresWidgetProvider gets all the widgets tied to this provider and builds an
 * intent to call the service that updates the widgets.
 *
 * References:
 * [1] http://www.vogella.com/tutorials/AndroidWidgets/article.html
 * [2] http://innovativenetsolutions.com/2013/07/android-tutorial-appwidget-with-its-own-contentprovider-service/
 * [3] http://stackoverflow.com/questions/23220757/android-widget-onclick-listener-for-several-buttons
 */
public class ScoresWidgetProvider extends AppWidgetProvider {

    private static final String TAG = "ScoresWidgetProvider";

    public static final String EXTRA_NAME = "barqsoft.footballscores.action";
    public static final String UPDATE_SCORES = "MyFetchService";
    public static final String CHANGE_MATCH = "ScoreWidgetService";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getStringExtra(EXTRA_NAME);
        if (action != null) {
            switch (action) {
                case UPDATE_SCORES:
                    Intent service_start = new Intent(context.getApplicationContext(), MyFetchService.class);
                    context.startService(service_start);
                    break;
                case CHANGE_MATCH:
                    Log.d(TAG, "Clicked for next match");
                    ScoresWidgetService.counter++;
                    break;
            }
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Get all ids
        ComponentName thisWidget = new ComponentName(context, ScoresWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        // Build the intent to call the service
        Intent intent = new Intent(context.getApplicationContext(), ScoresWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

        // Update the widgets via the service
        context.startService(intent);
    }

}
