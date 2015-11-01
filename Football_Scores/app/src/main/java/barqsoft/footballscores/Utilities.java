package barqsoft.footballscores;

import android.content.Context;
import android.util.Log;

/**
 * Created by yehya khaled on 3/3/2015.
 */
public class Utilities {

    public static String TAG = "Utilities";
    public static final int SERIE_A = 401;
    public static final int PREMIER_LEAGUE = 398;
    public static final int CHAMPIONS_LEAGUE = 405;
    public static final int PRIMERA_DIVISION = 399;
    public static final int BUNDESLIGA = 394;

    public static String getLeague(Context context, int league_num) {
        Log.d(TAG, String.valueOf(league_num));
        switch (league_num) {
            case SERIE_A:
                return context.getString(R.string.serie_a);
            case PREMIER_LEAGUE:
                return context.getString(R.string.premier_league);
            case CHAMPIONS_LEAGUE:
                return context.getString(R.string.champions_league);
            case PRIMERA_DIVISION:
                return context.getString(R.string.primera_division);
            case BUNDESLIGA:
                return context.getString(R.string.bundesliga);
            default:
                return context.getString(R.string.unknown_league);
        }
    }

    public static String getMatchDay(Context context, int match_day, int league_num) {
        if (league_num == CHAMPIONS_LEAGUE) {
            if (match_day <= 6) {
                return context.getString(R.string.group_stage);
            } else if (match_day == 7 || match_day == 8) {
                return context.getString(R.string.first_knockout);
            } else if (match_day == 9 || match_day == 10) {
                return context.getString(R.string.quarter_final);
            } else if (match_day == 11 || match_day == 12) {
                return context.getString(R.string.semi_final);
            } else {
                return context.getString(R.string.final_match);
            }
        } else {
            return context.getString(R.string.match_day) + String.valueOf(match_day);
        }
    }

    public static String getScores(int home_goals, int awaygoals) {
        if (home_goals < 0 || awaygoals < 0) {
            return " - ";
        } else {
            return String.valueOf(home_goals) + " - " + String.valueOf(awaygoals);
        }
    }

    public static int getTeamCrestByTeamName(String teamname) {
        Log.d(TAG, teamname);
        if (teamname == null) {
            return R.drawable.no_icon;
        }
        switch (teamname) {
            //This is the set of icons that are currently in the app. Feel free to find and add more as you go.
            case "Arsenal FC":
                return R.drawable.arsenal;
            case "Aston Villa FC":
                return R.drawable.aston_villa;
            case "Chelsea FC":
                return R.drawable.chelsea;
            case "Everton FC":
                return R.drawable.everton_fc_logo1;
            case "Leicester City FC":
                return R.drawable.leicester_city_fc_hd_logo;
            case "Liverpool FC":
                return R.drawable.liverpool;
            case "Manchester City FC":
                return R.drawable.manchester_city;
            case "Manchester United FC":
                return R.drawable.manchester_united;
            case "Newcastle United FC":
                return R.drawable.newcastle_united;
            case "Southampton FC":
                return R.drawable.southampton_fc;
            case "Stoke City FC":
                return R.drawable.stoke_city;
            case "Sunderland AFC":
                return R.drawable.sunderland;
            case "Swansea City FC":
                return R.drawable.swansea_city_afc;
            case "Tottenham Hotspur FC":
                return R.drawable.tottenham_hotspur;
            case "West Bromwich Albion FC":
                return R.drawable.west_bromwich_albion_hd_logo;
            case "West Ham United FC":
                return R.drawable.west_ham;
            default:
                return R.drawable.no_icon;
        }
    }
}
