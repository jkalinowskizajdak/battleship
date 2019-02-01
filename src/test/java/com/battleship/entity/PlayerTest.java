package com.battleship.entity;

import com.battleship.TestUtil;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class PlayerTest {

    private Player player;

    @Before
    public void createPlayers() {
        player = Game.createFirstPlayer(new DefaultPlayerStrategy());
        player.buildBoard();
    }

    @Test
    public void IsShotStatusMissAfterShotOnEmptyPoint() {
        Optional<BoardPoint> emptyPoint = TestUtil.getBoardPoint(player, PointType.EMPTY);
        Assertions.assertThat(emptyPoint).isNotEmpty();
        ShotStatus emptyShot = shot(emptyPoint.get().getX(), emptyPoint.get().getY());
        Assertions.assertThat(emptyShot).isEqualTo(ShotStatus.MISS);
    }

    @Test
    public void IsShotStatusHitAfterShotOnShipPoint() {
        Optional<BoardPoint> twoDeckerShipPoint = TestUtil.getBoardPoint(player, ShipType.TWO_DECKER);
        Assertions.assertThat(twoDeckerShipPoint).isNotEmpty();
        ShotStatus shipShot = shot(twoDeckerShipPoint.get().getX(), twoDeckerShipPoint.get().getY());
        Assertions.assertThat(shipShot).isEqualTo(ShotStatus.HIT);
        Optional<BoardPoint> hitPoint = TestUtil.getBoardPoint(player, PointType.DECK_HIT);
        Assertions.assertThat(hitPoint).isNotEmpty();
        Predicate<Optional<BoardPoint>> pointPredicate = (boardPoint) -> {
            return boardPoint.get().getX() == twoDeckerShipPoint.get().getX() &&
                    boardPoint.get().getY() == twoDeckerShipPoint.get().getY();
        };
        Assertions.assertThat(hitPoint).matches(pointPredicate);
    }

    @Test
    public void IsShotStatusMissAfterSecondShotOnShipPoint() {
        Optional<BoardPoint> oneDeckerShipPoint = TestUtil.getBoardPoint(player, ShipType.ONE_DECKER);
        Assertions.assertThat(oneDeckerShipPoint).isNotEmpty();
        ShotStatus shipShot = shot(oneDeckerShipPoint.get().getX(), oneDeckerShipPoint.get().getY());
        Assertions.assertThat(shipShot).isEqualTo(ShotStatus.HIT);
        ShotStatus secondShipShot = shot(oneDeckerShipPoint.get().getX(), oneDeckerShipPoint.get().getY());
        Assertions.assertThat(secondShipShot).isEqualTo(ShotStatus.MISS);
    }

    @Test
    public void IsShotStatusHitAfterShotsOnAllShipPoints() {
        List<BoardPoint> shipPoints = TestUtil.getAllShipPoints(player);
        int lastIndex = shipPoints.size() - 1;
        for (int i = 0; i < lastIndex; i++) {
            BoardPoint shipPoint = shipPoints.get(i);
            player.shotOnShip(shipPoint.getX(), shipPoint.getY());
        }
        BoardPoint lastShipPoint = shipPoints.get(lastIndex);
        ShotStatus lastHitShot = shot(lastShipPoint.getX(), lastShipPoint.getY());
        Assertions.assertThat(lastHitShot).isEqualTo(ShotStatus.HIT);
    }

    private ShotStatus shot(int x, int y) {
        return player.shotOnShip(x, y).getShotStatus();
    }

}
