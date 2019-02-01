package com.battleship.entity;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ShipTest {

    @Test
    public void IsOneDeckerShipSunkenAfterShot() {
        Ship oneDeckerShip = new Ship(ShipType.ONE_DECKER);
        oneDeckerShip.shot();
        Assertions.assertThat(oneDeckerShip.isSunken()).isTrue();
    }

    @Test
    public void IsNotTwoDeckerShipSunkenAfterShot() {
        Ship twoDeckerShip = new Ship(ShipType.TWO_DECKER);
        twoDeckerShip.shot();
        Assertions.assertThat(twoDeckerShip.isSunken()).isFalse();
    }

    @Test
    public void IsDecksCountReducedAfterShotOnThreeDeckerShip() {
        Ship threeDeckerShip = new Ship(ShipType.THREE_DECKER);
        threeDeckerShip.shot();
        Assertions.assertThat(threeDeckerShip.getActiveDecks()).isEqualTo(ShipType.THREE_DECKER.getDecks() - 1);
    }

    @Test
    public void IsNoneDecksAfterLastShotOnFourDeckerShip() {
        Ship fourDeckerShip = new Ship(ShipType.FOUR_DECKER);
        for (int i = 0; i < ShipType.FOUR_DECKER.getDecks(); i++) {
            fourDeckerShip.shot();
        }
        Assertions.assertThat(fourDeckerShip.getActiveDecks()).isEqualTo(0);
    }

}
