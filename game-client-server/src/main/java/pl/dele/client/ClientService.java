package pl.dele.client;

import javafx.scene.paint.Color;
import pl.dele.ServerRequest;
import pl.dele.cards.CardRole;
import pl.dele.teams.PlayerType;
import pl.dele.teams.TeamColor;

final class ClientService {

    static Color getCardColor(CardRole cardRole){
        switch (cardRole){
            case RED_TEAM:
                return Color.FIREBRICK;
            case BLUE_TEAM:
                return Color.LIGHTBLUE;
            case NEUTRAL:
                return Color.WHITESMOKE;
            default:
                return Color.GRAY;
        }
    }

    static CardRole stringToCardRole(String cardType){
        switch (cardType){
            case "RED_TEAM":
                return CardRole.RED_TEAM;
            case "BLUE_TEAM":
                return CardRole.BLUE_TEAM;
            case "NEUTRAL":
                return CardRole.NEUTRAL;
            default:
                return CardRole.BLACK_CARD;
        }
    }

    static String joinToTeam(TeamColor teamColor, PlayerType playerType){
        StringBuilder sb = new StringBuilder();
        sb.append(ServerRequest.JOIN).append(System.lineSeparator())
          .append(teamColor).append(System.lineSeparator())
          .append(playerType).append(System.lineSeparator());
        return sb.toString().trim();
    }

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
