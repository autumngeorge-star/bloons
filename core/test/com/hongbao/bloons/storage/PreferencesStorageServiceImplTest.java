package com.hongbao.bloons.storage;

import com.badlogic.gdx.Preferences;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PreferencesStorageServiceImplTest {

    private Preferences preferencesMock;
    private PreferencesStorageServiceImpl storageService;

    @Before
    public void setUp() {
        preferencesMock = Mockito.mock(Preferences.class);
        storageService = new PreferencesStorageServiceImpl(preferencesMock);
    }

    @Test
    public void testLoadHighScore() {
        when(preferencesMock.getInteger("high_score", 0)).thenReturn(500);

        int highScore = storageService.loadHighScore();

        assertEquals(500, highScore);
        verify(preferencesMock).getInteger("high_score", 0);
    }

    @Test
    public void testSaveHighScore() {
        storageService.saveHighScore(750);

        verify(preferencesMock).putInteger("high_score", 750);
        verify(preferencesMock).flush();
    }

    @Test
    public void testClear() {
        storageService.clear();

        verify(preferencesMock).clear();
        verify(preferencesMock).flush();
    }
}
