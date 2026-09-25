package com.hongbao.bloons;

import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.entities.Bullet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EntityMovementTest {

    @ParameterizedTest
    @ValueSource(floats = {5.0f, 10.0f, 15.0f, 20.0f, 25.0f})
    @DisplayName("Bullet distance traveled increment calculation across velocities")
    void testBulletMovementCalculation(float velocity) {
        Bullet bullet = new Bullet(velocity, 1, 1, 100f, false, "red_spell_card.png");
        float initialDistance = bullet.getDistanceTraveled();
        bullet.incrementDistanceTraveled();

        float expectedDistance = initialDistance + (velocity / 5.0f);
        assertEquals(expectedDistance, bullet.getDistanceTraveled(), 0.001f);
    }

    @ParameterizedTest
    @CsvSource({
        "RED, 5",
        "BLUE, 6",
        "GREEN, 7",
        "YELLOW, 8",
        "PINK, 9",
        "BFB, 3",
        "ZOMG, 2"
    })
    @DisplayName("Bloon movement calculation across color velocities")
    void testBloonMovementCalculation(Bloon.Color color, int expectedSpeed) {
        Bloon bloon = new Bloon(color, 1, false, false);
        assertEquals(expectedSpeed, bloon.getSpeed());

        int initialDistance = bloon.getDistanceTravelled();
        bloon.incrementDistanceTravelled();
        assertEquals(initialDistance + expectedSpeed, bloon.getDistanceTravelled());
    }
}
