package com.battleship;

import com.battleship.boundary.dto.MoveDTO;
import com.battleship.control.GameUtil;
import com.battleship.entity.BoardPoint;
import com.battleship.entity.Player;
import com.battleship.entity.PointType;
import com.battleship.entity.ShipType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestUtil {

    private TestUtil() {

    }

    public static Optional<BoardPoint> getBoardPoint(Player player, PointType pointType) {
        return getBoardPointsStream(player)
                .filter(boardPoint -> pointType == boardPoint.getType())
                .findAny();
    }

    public static Optional<BoardPoint> getBoardPoint(Player player, ShipType shipType) {
        return getBoardPointsStream(player)
                .filter(boardPoint -> PointType.DECK_ACTIVE == boardPoint.getType())
                .filter(boardPoint -> shipType == boardPoint.getShip().getType())
                .findAny();
    }

    public static List<BoardPoint> getAllShipPoints(Player player) {
        return getBoardPointsStream(player)
                .filter(boardPoint -> PointType.DECK_ACTIVE == boardPoint.getType())
                .collect(Collectors.toList());
    }

    public static Stream<BoardPoint> getBoardPointsStream(Player player) {
        return Arrays.stream(player.getBoard().getBoard())
                .flatMap(Stream::of);
    }

    public static Optional<BoardPoint> getEmptyPoint(Player player) {
        return getBoardPointsStream(player)
                .filter(boardPoint -> PointType.EMPTY == boardPoint.getType())
                .findAny();
    }

    public static MoveDTO getMoveDTO(int x, int y) {
        Character textY = GameUtil.getYValues().entrySet()
                .stream()
                .filter(value -> value.getValue() == y)
                .findAny()
                .get()
                .getKey();
        int textX = x + 1;
        String position = "" + textY + textX;
        return new MoveDTO(position);
    }
}
