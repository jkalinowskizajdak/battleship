package com.battleship.control;

import com.battleship.boundary.dto.GameStatusDTO;
import com.battleship.boundary.dto.MoveDTO;
import com.battleship.boundary.dto.ShotStatusDTO;
import com.battleship.entity.Player;
import com.battleship.entity.ShotResult;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class GameUtil {

    public static final String GAME_PATH = "/battleship/game";
    private static final int X_POSITION_IN_MOVE_TEXT = 1;
    private static final int Y_POSITION_IN_MOVE_TEXT = 0;
    private static final String MOVE_REGEX = "^([A-J])(10|[1-9])$";
    private static final Map<Character, Integer> Y_VALUES = ImmutableMap.<Character, Integer>builder()
            .put('A', 0)
            .put('B', 1)
            .put('C', 2)
            .put('D', 3)
            .put('E', 4)
            .put('F', 5)
            .put('G', 6)
            .put('H', 7)
            .put('I', 8)
            .put('J', 9)
            .build();

    private GameUtil() {

    }

    public static GameStatusDTO getGameStatusDTO(Player sourcePlayer, int opponentScore) {
        return new GameStatusDTO(sourcePlayer.getGameStatus(), sourcePlayer.getPoints(), opponentScore);
    }

    public static int getY(MoveDTO moveDTO) {
        String position = moveDTO.getPosition();
        return Y_VALUES.get(position.toCharArray()[Y_POSITION_IN_MOVE_TEXT]);
    }

    public static int getX(MoveDTO moveDTO) {
        String position = moveDTO.getPosition().substring(X_POSITION_IN_MOVE_TEXT);
        int x = Integer.valueOf(position);
        return x - 1;
    }

    public static boolean isMoveValid(MoveDTO moveDTO) {
        return moveDTO.getPosition().matches(MOVE_REGEX);
    }

    public static ShotStatusDTO getShotStatusDTO(ShotResult result) {
        return new ShotStatusDTO(result.getShotStatus(),
                result.getShipType().orElse(null), result.getSunken().orElse(null));
    }

    public static Map<Character, Integer> getYValues() {
        return Y_VALUES;
    }

}
