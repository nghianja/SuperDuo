package barqsoft.footballscores.service;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.ScoresAdapter;
import barqsoft.footballscores.ScoresWidgetItem;
import barqsoft.footballscores.Utilies;

/**
 * This service class is for a collection view widget that supports the usage of a StackView.
 * This service requires the android.permission.BIND_REMOTEVIEWS permission.
 *
 * References:
 * [1] http://innovativenetsolutions.com/2013/07/android-tutorial-appwidget-with-its-own-contentprovider-service/
 */
public class ScoresStackWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        private int count = 0;
        private List<ScoresWidgetItem> scoresWidgetItems = new ArrayList<>();
        private Context context;
        private int appWidgetId;

        public StackRemoteViewsFactory(Context context, Intent intent) {
            this.context = context;
            appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        public void onCreate() {
            // Get current date
            String[] currentDate = new String[1];
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            currentDate[0] = dateFormat.format(date);

            // Get the cursor to a database query
            Cursor cursor = context.getContentResolver().query(DatabaseContract.scores_table.buildScoreWithDate(), null, null, currentDate, null);

            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();

                ScoresWidgetItem scoresWidgetItem = new ScoresWidgetItem();
                scoresWidgetItem.homeCrest = Utilies.getTeamCrestByTeamName(cursor.getString(ScoresAdapter.COL_HOME));
                scoresWidgetItem.homeName = cursor.getString(ScoresAdapter.COL_HOME);
                scoresWidgetItem.scoreText = Utilies.getScores(cursor.getInt(ScoresAdapter.COL_HOME_GOALS), cursor.getInt(ScoresAdapter.COL_AWAY_GOALS));
                scoresWidgetItem.dataText = cursor.getString(ScoresAdapter.COL_MATCHTIME);
                scoresWidgetItem.awayCrest = Utilies.getTeamCrestByTeamName(cursor.getString(ScoresAdapter.COL_AWAY));
                scoresWidgetItem.awayName = cursor.getString(ScoresAdapter.COL_AWAY);
                scoresWidgetItem.matchId = cursor.getInt(ScoresAdapter.COL_ID);

                scoresWidgetItems.add(scoresWidgetItem);
                count++;
            }

            cursor.close();
        }

        public int getCount() {
            return count;
        }

        public void onDataSetChanged() {}

        public RemoteViews getViewAt(int position) {
            // Construct a remote views item based on the app widget item XML file,
            // and set the text based on the position.
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.scores_list_item);

            // Set the texts and images
            remoteViews.setImageViewResource(R.id.home_crest, scoresWidgetItems.get(position).homeCrest);
            remoteViews.setTextViewText(R.id.home_name, scoresWidgetItems.get(position).homeName);
            remoteViews.setTextViewText(R.id.score_textview, scoresWidgetItems.get(position).scoreText);
            remoteViews.setTextViewText(R.id.data_textview, scoresWidgetItems.get(position).dataText);
            remoteViews.setImageViewResource(R.id.away_crest, scoresWidgetItems.get(position).awayCrest);
            remoteViews.setTextViewText(R.id.away_name, scoresWidgetItems.get(position).awayName);

            return remoteViews;
        }

        public int getViewTypeCount() {
            return 1;
        }

        public long getItemId(int position) {
            return scoresWidgetItems.get(position).matchId;
        }

        public void onDestroy() {
            scoresWidgetItems.clear();
        }

        public RemoteViews getLoadingView() {
            return null;
        }

        public boolean hasStableIds() {
            return true;
        }
    }
}
