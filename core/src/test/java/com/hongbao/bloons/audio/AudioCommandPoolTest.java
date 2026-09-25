package com.hongbao.bloons.audio;

import org.junit.Assert;
import org.junit.Test;

public class AudioCommandPoolTest {

    @Test
    public void testPoolObtainAndFree() {
        AudioCommandPool pool = new AudioCommandPool();
        AudioCommand cmd1 = pool.obtain();
        cmd1.initSoundPlay("music/pop.mp3", 0.5f, 1.0f, 0.0f);

        Assert.assertEquals(AudioCommandType.SOUND_PLAY, cmd1.getType());
        Assert.assertEquals("music/pop.mp3", cmd1.getSoundPath());

        pool.free(cmd1);

        AudioCommand cmd2 = pool.obtain();
        Assert.assertNull(cmd2.getType());
        Assert.assertNull(cmd2.getSoundPath());
    }
}
