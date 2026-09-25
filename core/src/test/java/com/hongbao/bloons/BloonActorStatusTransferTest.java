package com.hongbao.bloons;

import com.hongbao.bloons.actors.BloonActor;
import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.helpers.BloonStatusEffect;

import java.util.List;
import java.util.Set;

public class BloonActorStatusTransferTest {

    public static void main(String[] args) {
        testConstructorStatusTransfer();
        testDeepCopyIndependence();
        testSpeedModificationCalculation();
        testStatusEffectDurationDecayAndExpiration();
        testBloonManagerPopBloonPropagation();
        testMultiChildActorPropagation();
        System.out.println("ALL BLOON ACTOR STATUS TRANSFER TESTS PASSED SUCCESSFULLY!");
    }

    private static void testConstructorStatusTransfer() {
        Bloon parentBloon = new Bloon(Bloon.Color.BLUE, 2, false, false);
        BloonActor parentActor = new BloonActor(parentBloon, 100f, 100f, null);
        
        BloonStatusEffect slow = new BloonStatusEffect("SlowEffect", 0.5f, 5.0f);
        parentActor.addStatusEffect(slow);

        Bloon childBloon = new Bloon(Bloon.Color.RED, 1, false, false);
        BloonActor childActor = new BloonActor(childBloon, 100f, 100f, parentActor);

        List<BloonStatusEffect> childEffects = childActor.getStatusEffects();
        assert childEffects.size() == 1 : "Expected 1 status effect transferred to child actor, found: " + childEffects.size();
        
        BloonStatusEffect childEffect = childEffects.get(0);
        assert "SlowEffect".equals(childEffect.getName()) : "Expected status effect name SlowEffect, found: " + childEffect.getName();
        assert Math.abs(childEffect.getSpeedMultiplier() - 0.5f) < 0.0001f : "Expected speedMultiplier 0.5, found: " + childEffect.getSpeedMultiplier();
        assert Math.abs(childEffect.getDuration() - 5.0f) < 0.0001f : "Expected duration 5.0, found: " + childEffect.getDuration();
        
        System.out.println("[PASS] testConstructorStatusTransfer");
    }

    private static void testDeepCopyIndependence() {
        Bloon parentBloon = new Bloon(Bloon.Color.BLUE, 2, false, false);
        BloonActor parentActor = new BloonActor(parentBloon, 100f, 100f, null);
        
        BloonStatusEffect slow = new BloonStatusEffect("SlowEffect", 0.5f, 5.0f);
        parentActor.addStatusEffect(slow);

        Bloon childBloonA = new Bloon(Bloon.Color.RED, 1, false, false);
        BloonActor childActorA = new BloonActor(childBloonA, 100f, 100f, parentActor);

        Bloon childBloonB = new Bloon(Bloon.Color.RED, 1, false, false);
        BloonActor childActorB = new BloonActor(childBloonB, 100f, 100f, parentActor);

        // Update status effects on Child A only
        childActorA.updateStatusEffects(2.0f);

        assert Math.abs(childActorA.getStatusEffects().get(0).getDuration() - 3.0f) < 0.0001f : "Child A duration should be 3.0, found: " + childActorA.getStatusEffects().get(0).getDuration();
        assert Math.abs(childActorB.getStatusEffects().get(0).getDuration() - 5.0f) < 0.0001f : "Child B duration should remain 5.0, found: " + childActorB.getStatusEffects().get(0).getDuration();
        assert Math.abs(parentActor.getStatusEffects().get(0).getDuration() - 5.0f) < 0.0001f : "Parent duration should remain 5.0, found: " + parentActor.getStatusEffects().get(0).getDuration();

        System.out.println("[PASS] testDeepCopyIndependence");
    }

    private static void testSpeedModificationCalculation() {
        Bloon bloon = new Bloon(Bloon.Color.RED, 1, false, false);
        BloonActor actor = new BloonActor(bloon, 100f, 100f, null);

        float baseSpeed = bloon.getSpeed(); // RED bloon base speed = 5
        assert Math.abs(actor.getEffectiveSpeed() - baseSpeed) < 0.0001f : "Base effective speed should match bloon speed (" + baseSpeed + "), found: " + actor.getEffectiveSpeed();

        BloonStatusEffect slow = new BloonStatusEffect("Slow", 0.5f, 10.0f);
        actor.addStatusEffect(slow);
        assert Math.abs(actor.getEffectiveSpeed() - (baseSpeed * 0.5f)) < 0.0001f : "Effective speed should be halved to " + (baseSpeed * 0.5f) + ", found: " + actor.getEffectiveSpeed();

        BloonStatusEffect freeze = new BloonStatusEffect("Freeze", 0.2f, 5.0f);
        actor.addStatusEffect(freeze);
        assert Math.abs(actor.getEffectiveSpeed() - (baseSpeed * 0.5f * 0.2f)) < 0.0001f : "Effective speed with stacked modifiers should be " + (baseSpeed * 0.1f) + ", found: " + actor.getEffectiveSpeed();

        System.out.println("[PASS] testSpeedModificationCalculation");
    }

    private static void testStatusEffectDurationDecayAndExpiration() {
        Bloon bloon = new Bloon(Bloon.Color.GREEN, 3, false, false);
        BloonActor actor = new BloonActor(bloon, 100f, 100f, null);

        BloonStatusEffect tempSlow = new BloonStatusEffect("TempSlow", 0.5f, 3.0f);
        actor.addStatusEffect(tempSlow);

        actor.updateStatusEffects(2.0f);
        assert actor.getStatusEffects().size() == 1 : "Status effect should still be active after 2s decay";
        assert Math.abs(actor.getEffectiveSpeed() - (bloon.getSpeed() * 0.5f)) < 0.0001f : "Effective speed should remain modified while active";

        actor.updateStatusEffects(1.5f); // Total decay 3.5s > 3.0s duration
        assert actor.getStatusEffects().isEmpty() : "Expired status effect should be removed";
        assert Math.abs(actor.getEffectiveSpeed() - bloon.getSpeed()) < 0.0001f : "Effective speed should revert to base speed after status expiration";

        System.out.println("[PASS] testStatusEffectDurationDecayAndExpiration");
    }

    private static void testBloonManagerPopBloonPropagation() {
        BloonManager bloonManager = new BloonManager(null, null);

        Bloon parentBloon = new Bloon(Bloon.Color.BLUE, 2, false, false);
        BloonActor parentActor = new BloonActor(parentBloon, 100f, 100f, null);
        
        BloonStatusEffect slowEffect = new BloonStatusEffect("StunSlow", 0.4f, 8.0f);
        bloonManager.applyStatusEffect(parentActor, slowEffect);
        bloonManager.getOnstageBloons().add(parentActor);

        // Pop parent bloon (BLUE -> RED)
        bloonManager.popBloon(parentActor, 1);

        Set<BloonActor> onstageBloons = bloonManager.getOnstageBloons();
        assert onstageBloons.size() == 1 : "Expected 1 child bloon actor onstage after popping, found: " + onstageBloons.size();

        BloonActor childActor = onstageBloons.iterator().next();
        assert childActor.getBloon().getColor() == Bloon.Color.RED : "Expected child bloon color RED";
        assert childActor.getStatusEffects().size() == 1 : "Child bloon should inherit 1 status effect";
        assert Math.abs(childActor.getEffectiveSpeed() - (childActor.getBloon().getSpeed() * 0.4f)) < 0.0001f : "Child bloon effective speed should reflect inherited status modifier";

        System.out.println("[PASS] testBloonManagerPopBloonPropagation");
    }

    private static void testMultiChildActorPropagation() {
        BloonManager bloonManager = new BloonManager(null, null);

        // ZEBRA bloon pops into 2 BLACK bloons
        Bloon parentBloon = new Bloon(Bloon.Color.ZEBRA, 7, false, false);
        BloonActor parentActor = new BloonActor(parentBloon, 200f, 200f, null);

        BloonStatusEffect slowEffect = new BloonStatusEffect("AreaSlow", 0.3f, 12.0f);
        parentActor.addStatusEffect(slowEffect);
        bloonManager.getOnstageBloons().add(parentActor);

        bloonManager.popBloon(parentActor, 1);

        Set<BloonActor> onstage = bloonManager.getOnstageBloons();
        assert !onstage.isEmpty() : "Child bloons should be spawned after popping parent";

        for (BloonActor child : onstage) {
            assert child.getStatusEffects().size() == 1 : "Every child bloon actor should inherit parent status effect";
            assert Math.abs(child.getEffectiveSpeed() - (child.getBloon().getSpeed() * 0.3f)) < 0.0001f : "Every child bloon effective speed should be modified";
        }

        System.out.println("[PASS] testMultiChildActorPropagation");
    }
}
