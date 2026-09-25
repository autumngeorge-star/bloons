package com.hongbao.bloons;

import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.actors.BulletActor;
import com.hongbao.bloons.actors.GirlActor;
import com.hongbao.bloons.actors.SpellCardActor;

public interface SoundEventListener {

    default void onTowerPlaced(GirlActor girlActor) {
        onTowerPlaced();
    }

    default void onTowerPlaced() {
    }

    default void onTowerUpgraded(GirlActor girlActor) {
        onTowerUpgraded();
    }

    default void onTowerUpgraded() {
    }

    default void onTowerSold(GirlActor girlActor) {
        onTowerSold();
    }

    default void onTowerSold() {
    }

    default void onSpellActivated(SpellCardActor spellCardActor) {
        onSpellActivated();
    }

    default void onSpellActivated() {
    }

    default void onBloonDamaged(BloonActor bloonActor, int damage) {
        onBloonDamaged();
    }

    default void onBloonDamaged() {
    }

    default void onBloonPopped(BloonActor bloonActor, int damage) {
        onBloonPopped();
    }

    default void onBloonPopped() {
    }

    default void onBulletFired(BulletActor bulletActor) {
        onBulletFired();
    }

    default void onBulletFired() {
    }
}
