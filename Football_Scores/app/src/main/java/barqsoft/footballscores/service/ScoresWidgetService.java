package barqsoft.footballscores.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.ScoresAdapter;
import barqsoft.footballscores.ScoresWidgetProvider;
import barqsoft.footballscores.Utilies;

/**
 * This service gets football scores from the database in a cursor and updates the widget views.
 *
 * References:
 * [1] http://www.vogella.com/tutorials/AndroidWidgets/article.html
 * [2] http://innovativenetsolutions.com/2013/07/android-tutorial-appwidget-with-its-own-contentprovider-service/
 */
public class ScoresWidgetService extends Service {

    public static int counter = 1;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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
        Cursor cursor = this.getApplicationContext().getContentResolver().query(DatabaseContract.scores_table.buildScoreWithDate(), null, null, currentDate, null);
        boolean hasMatch = (cursor != null && cursor.getCount() > 0);

        if (hasMatch) {
            cursor.moveToFirst();
            if (counter > cursor.getCount()) counter = 1;
            for (int i = 1; i < counter; i++) {
                cursor.moveToNext();
            }
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());

        int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

        // Perform this loop procedure for each App Widget
        for (int widgetId : allWidgetIds) {
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

            RemoteViews remoteViews = new RemoteViews(this.getApplicationContext().getPackageName(), R.layout.scores_list_item);

            // Set the texts and images
            remoteViews.setImageViewResource(R.id.home_crest, homeCrest);
            remoteViews.setTextViewText(R.id.home_name, homeName);
            remoteViews.setTextViewText(R.id.score_textview, scoreText);
            remoteViews.setTextViewText(R.id.data_textview, dataText);
            remoteViews.setImageViewResource(R.id.away_crest, awayCrest);
            remoteViews.setTextViewText(R.id.away_name, awayName);

            // Register an onClickListener
            Intent clickIntent = new Intent(this.getApplicationContext(), ScoresWidgetProvider.class);
            clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
            clickIntent.putExtra(ScoresWidgetProvider.EXTRA_NAME, ScoresWidgetProvider.CHANGE_MATCH);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.score_textview, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }

        cursor.close();

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
