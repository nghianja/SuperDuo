package barqsoft.footballscores;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The AppWidgetProvider receives only the event broadcasts that are relevant to the
 * App Widget, such as when the App Widget is updated, deleted, enabled, and disabled.
 * <p/>
 * References:
 * [1] http://www.vogella.com/tutorials/AndroidWidgets/article.html
 * [2] http://innovativenetsolutions.com/2013/07/android-tutorial-appwidget-with-its-own-contentprovider-service/
 */
public class ScoresWidgetProvider extends AppWidgetProvider {

    private static int counter = 1;

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // RemoteViews attributes
        int homeCrest = Utilies.getTeamCrestByTeamName(null);
        String homeName = "Home";
        String scoreText = "0 - 0";
        String dataText = "";
        int awayCrest = Utilies.getTeamCrestByTeamName(null);
        String awayName = "Away";

        // Get current date
        String[] currentDate = new String[1];
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        currentDate[0] = dateFormat.format(date);

        // Get the cursor to a database query
        Cursor cursor = context.getContentResolver().query(DatabaseContract.scores_table.buildScoreWithDate(), null, null, currentDate, null);
        boolean hasMatch = (cursor != null && cursor.getCount() > 0);

        if (hasMatch) {
            cursor.moveToFirst();
            if (counter > cursor.getCount()) counter = 1;
            for (int i = 1; i < counter; i++) {
                cursor.moveToNext();
            }
            counter++;
        }

        // Get all ids
        //ComponentName thisWidget = new ComponentName(context, ScoresWidgetProvider.class);
        //int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        // Perform this loop procedure for each App Widget
        for (int widgetId : appWidgetIds) {
            if (hasMatch) {
                homeCrest = Utilies.getTeamCrestByTeamName(cursor.getString(ScoresAdapter.COL_HOME));
                homeName = cursor.getString(ScoresAdapter.COL_HOME);
                scoreText = Utilies.getScores(cursor.getInt(ScoresAdapter.COL_HOME_GOALS), cursor.getInt(ScoresAdapter.COL_AWAY_GOALS));
                dataText = cursor.getString(ScoresAdapter.COL_MATCHTIME);
                awayCrest = Utilies.getTeamCrestByTeamName(cursor.getString(ScoresAdapter.COL_AWAY));
                awayName = cursor.getString(ScoresAdapter.COL_AWAY);

                if (cursor.isLast()) {
                    cursor.moveToFirst();
                } else {
                    cursor.moveToNext();
                }
            }

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.scores_list_item);

            // Set the texts and images
            remoteViews.setImageViewResource(R.id.home_crest, homeCrest);
            remoteViews.setTextViewText(R.id.home_name, homeName);
            remoteViews.setTextViewText(R.id.score_textview, scoreText);
            remoteViews.setTextViewText(R.id.data_textview, dataText);
            remoteViews.setImageViewResource(R.id.away_crest, awayCrest);
            remoteViews.setTextViewText(R.id.away_name, awayName);

            // Register an onClickListener
            Intent intent = new Intent(context, ScoresWidgetProvider.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.score_textview, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }

        cursor.close();
    }

}
