package com.hongbao.bloons;

import com.hongbao.bloons.entities.Bloon;
import com.hongbao.bloons.factories.BloonFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class BloonAtlasTest {

    @Test
    public void testGetAtlasRegionName() {
        Bloon red = BloonFactory.createRedBloon();
        Assert.assertEquals("red_bloon", red.getAtlasRegionName());

        Bloon redCamo = BloonFactory.createRedCamoBloon();
        Assert.assertEquals("red_camo_bloon", redCamo.getAtlasRegionName());

        Bloon redCamoRegen = BloonFactory.createRedCamoRegenBloon();
        Assert.assertEquals("red_camo_regrowth_bloon", redCamoRegen.getAtlasRegionName());

        Bloon redRegen = BloonFactory.createRedRegenBloon();
        Assert.assertEquals("red_regrowth_bloon", redRegen.getAtlasRegionName());

        Bloon moab = BloonFactory.createMOAB();
        Assert.assertEquals("moab_bloon", moab.getAtlasRegionName());

        Bloon bfb = BloonFactory.createBFB();
        Assert.assertEquals("bfb_bloon", bfb.getAtlasRegionName());

        Bloon zomg = BloonFactory.createZOMG();
        Assert.assertEquals("zomg_bloon", zomg.getAtlasRegionName());
    }

    @Test
    public void testAllBloonVariantsExistInAtlasFile() throws IOException {
        File atlasFile = new File("bloons.atlas");
        if (!atlasFile.exists()) {
            atlasFile = new File("assets/bloons.atlas");
        }
        if (!atlasFile.exists()) {
            atlasFile = new File("core/assets/bloons.atlas");
        }
        Assert.assertTrue("bloons.atlas file should exist", atlasFile.exists());

        String atlasContent = new String(Files.readAllBytes(atlasFile.toPath()));

        List<Bloon> sampleBloons = new ArrayList<>();
        sampleBloons.add(BloonFactory.createRedBloon());
        sampleBloons.add(BloonFactory.createRedCamoBloon());
        sampleBloons.add(BloonFactory.createRedRegenBloon());
        sampleBloons.add(BloonFactory.createRedCamoRegenBloon());
        sampleBloons.add(BloonFactory.createBlueBloon());
        sampleBloons.add(BloonFactory.createGreenBloon());
        sampleBloons.add(BloonFactory.createYellowBloon());
        sampleBloons.add(BloonFactory.createPinkBloon());
        sampleBloons.add(BloonFactory.createBlackBloon());
        sampleBloons.add(BloonFactory.createLeadBloon());
        sampleBloons.add(BloonFactory.createZebraBloon());
        sampleBloons.add(BloonFactory.createRainbowBloon());
        sampleBloons.add(BloonFactory.createCeramicBloon());
        sampleBloons.add(BloonFactory.createMOAB());
        sampleBloons.add(BloonFactory.createBFB());
        sampleBloons.add(BloonFactory.createZOMG());

        for (Bloon bloon : sampleBloons) {
            String regionName = bloon.getAtlasRegionName();
            Assert.assertTrue("Atlas should contain region: " + regionName, atlasContent.contains(regionName));
        }
    }
}
