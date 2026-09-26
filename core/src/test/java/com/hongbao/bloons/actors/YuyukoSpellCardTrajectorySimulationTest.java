package com.hongbao.bloons.actors;

import com.hongbao.bloons.entities.Bullet;
import com.hongbao.bloons.helpers.Pair;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.List;

public class YuyukoSpellCardTrajectorySimulationTest {

    public static void main(String[] args) throws Exception {
        simulateYuyukoTrajectory();
    }

    public static void simulateYuyukoTrajectory() throws Exception {
        int width = 800;
        int height = 600;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // Background
        g.setColor(new Color(30, 30, 40));
        g.fillRect(0, 0, width, height);

        // Bloon target
        float targetX = 600;
        float targetY = 300;
        g.setColor(Color.RED);
        g.fillOval((int) targetX - 15, (int) targetY - 15, 30, 30);
        g.setColor(Color.WHITE);
        g.drawString("Target Bloon (600, 300)", (int) targetX - 50, (int) targetY - 20);

        // Simulate 8 bullets spawned in Yuyuko fan pattern at x=100, y=300
        int numBullets = 8;
        Color[] bulletColors = {
            Color.CYAN, Color.MAGENTA, Color.YELLOW, Color.GREEN,
            Color.ORANGE, Color.PINK, Color.LIGHT_GRAY, new Color(180, 100, 255)
        };

        boolean allHitTarget = true;

        for (int i = 0; i < numBullets; i++) {
            float posX = 100 + (i % 2 == 0 ? -20 : 20);
            float posY = 300 + (i - numBullets / 2) * 20;

            double angle = (2 * Math.PI / numBullets) * i;
            float dx = (float) Math.cos(angle);
            float dy = (float) Math.sin(angle);

            float speed = 5.0f;
            int frames = 0;
            String spellOverride = "Yuyuko";

            List<int[]> path = new ArrayList<>();
            path.add(new int[]{(int) posX, (int) posY});

            float minDistanceToTarget = Float.MAX_VALUE;

            while (frames < 200) {
                frames++;

                // 1. Calculate pattern direction for Yuyuko
                double currentAngle = Math.atan2(dy, dx);
                double desiredAngle = (frames % 150 < 75) ?
                        currentAngle - (2 * Math.PI / 300) : currentAngle + (2 * Math.PI / 300);
                float patternDx = (float) Math.cos(desiredAngle);
                float patternDy = (float) Math.sin(desiredAngle);

                // 2. Calculate homing target vector
                float tDx = targetX - posX;
                float tDy = targetY - posY;
                float dist = (float) Math.sqrt(tDx * tDx + tDy * tDy);
                minDistanceToTarget = Math.min(minDistanceToTarget, dist);

                if (dist < 15.0f) {
                    // Hit target!
                    break;
                }

                float homingDx = tDx / dist;
                float homingDy = tDy / dist;

                // 3. Blend vectors using BulletActor helper
                float alpha = BulletActor.computeAlpha(dist, frames);
                Pair<Float, Float> blended = BulletActor.blendVectors(patternDx, patternDy, homingDx, homingDy, alpha);

                dx = blended.getFirst();
                dy = blended.getSecond();

                // Move bullet
                posX += dx * speed;
                posY += dy * speed;

                path.add(new int[]{(int) posX, (int) posY});
            }

            if (minDistanceToTarget > 20.0f) {
                allHitTarget = false;
            }

            // Draw trajectory
            g.setColor(bulletColors[i % bulletColors.length]);
            for (int p = 0; p < path.size() - 1; p++) {
                int[] p1 = path.get(p);
                int[] p2 = path.get(p + 1);
                g.drawLine(p1[0], p1[1], p2[0], p2[1]);
            }
            // Draw bullet endpoint
            int[] last = path.get(path.size() - 1);
            g.fillOval(last[0] - 4, last[1] - 4, 8, 8);
        }

        g.setColor(Color.WHITE);
        g.drawString("Yuyuko Spell Card Composite Steering Simulation", 20, 30);
        g.drawString("Pattern rotation blended with homing vectors", 20, 50);

        File outputFile = new File("/tmp/yuyuko_trajectory.png");
        ImageIO.write(image, "PNG", outputFile);
        System.out.println("Trajectory simulation complete. Image saved to " + outputFile.getAbsolutePath());

        if (!allHitTarget) {
            throw new AssertionError("Not all projectiles reached the target bloon!");
        }
        System.out.println("All Yuyuko spell card projectiles successfully curved and struck the target bloon!");
    }
}
