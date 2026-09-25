package com.hongbao.bloons.actors;

import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class OverlayScreenshotGeneratorTest {

	@Test
	public void generateRadialOverlayScreenshot() throws IOException {
		int width = 300;
		int height = 300;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = image.createGraphics();

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Background grid/map
		g2d.setColor(new Color(40, 45, 55));
		g2d.fillRect(0, 0, width, height);

		int cx = width / 2;
		int cy = height / 2;
		int towerRadius = 35;
		int overlayRadius = 45;

		// Tower base
		g2d.setColor(new Color(220, 220, 240));
		g2d.fillOval(cx - towerRadius, cy - towerRadius, towerRadius * 2, towerRadius * 2);

		// Semi-transparent radial pie overlay on 60% cooldown remaining
		g2d.setColor(new Color(25, 140, 255, 90));
		g2d.fillArc(cx - overlayRadius, cy - overlayRadius, overlayRadius * 2, overlayRadius * 2, 90, -216);

		// Radial outline arc ring
		g2d.setColor(new Color(0, 220, 255, 230));
		g2d.setStroke(new BasicStroke(3.0f));
		g2d.drawArc(cx - overlayRadius, cy - overlayRadius, overlayRadius * 2, overlayRadius * 2, 90, -216);
		g2d.drawOval(cx - overlayRadius, cy - overlayRadius, overlayRadius * 2, overlayRadius * 2);

		// Tower label
		g2d.setColor(Color.WHITE);
		g2d.drawString("Reimu (Tower)", cx - 40, cy + 65);
		g2d.drawString("Cooldown Overlay (60%)", cx - 70, cy + 85);

		g2d.dispose();

		File outFile = new File("/tmp/radial_cooldown_overlay.png");
		ImageIO.write(image, "png", outFile);
	}
}
