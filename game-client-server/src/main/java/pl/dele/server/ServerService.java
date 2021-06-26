package pl.dele.server;

import pl.dele.teams.PlayerType;
import pl.dele.teams.TeamColor;

final class ServerService {

    static TeamColor getTeamColor(String teamString){
        switch (teamString){
            case "RED_TEAM":
                return TeamColor.RED_TEAM;
            default:
                return TeamColor.BLUE_TEAM;
        }
    }

    static PlayerType getPlayerType(String typeString){
        switch (typeString){
            case "SPYMASTER":
                return PlayerType.SPYMASTER;
            default:
                return PlayerType.OPERATIVE;
        }
    }

}
