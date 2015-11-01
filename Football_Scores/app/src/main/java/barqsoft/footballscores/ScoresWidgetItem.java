package barqsoft.footballscores;

/**
 * Class to hold details of a football match.
 */
public class ScoresWidgetItem {

    public int homeCrest;
    public String homeName;
    public String scoreText;
    public String dataText;
    public int awayCrest;
    public String awayName;
    public int matchId;

    public ScoresWidgetItem() {
        homeCrest = Utilities.getTeamCrestByTeamName(null);
        homeName = "Home";
        scoreText = "0 - 0";
        dataText = "";
        awayCrest = Utilities.getTeamCrestByTeamName(null);
        awayName = "Away";
        matchId = 0;
    }
    
}
