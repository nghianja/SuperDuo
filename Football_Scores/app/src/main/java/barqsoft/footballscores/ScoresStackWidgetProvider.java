package barqsoft.footballscores;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

/**
 * The AppWidgetProvider receives only the event broadcasts that are relevant to the
 * App Widget, such as when the App Widget is updated, deleted, enabled, and disabled.
 * <p/>
 * The ScoresStackWidgetProvider gets all the widgets tied to this provider and builds an
 * intent to call the service that updates the widgets. The stack changes from a data item
 * to another data item when scrolled.
 * <p/>
 * References:
 * [1] http://innovativenetsolutions.com/2013/07/android-tutorial-appwidget-with-its-own-contentprovider-service/
 */
public class ScoresStackWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Update each of the widgets with the remote adapter
        for (int appWidgetId : appWidgetIds) {
            // Set up the intent that starts the StackViewService, which will
            // provide the views for this collection.
            Intent intent = new Intent(context, ScoresStackWidgetProvider.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            // Instantiate the RemoteViews object for the app widget layout.
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.scores_widget_stack);

            // Set up the RemoteViews object to use a RemoteViews adapter.
            // This adapter connects to a RemoteViewsService  through the specified intent.
            // This is how you populate the data.
            remoteViews.setRemoteAdapter(appWidgetId, R.id.stack_view, intent);

            // The empty view is displayed when the collection has no items.
            // It should be in the same layout used to instantiate the RemoteViews object above.
            remoteViews.setEmptyView(R.id.stack_view, R.id.empty_view);

            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

}
