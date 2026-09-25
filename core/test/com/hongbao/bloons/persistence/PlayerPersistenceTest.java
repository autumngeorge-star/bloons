package com.hongbao.bloons.persistence;

import com.hongbao.bloons.Player;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PlayerPersistenceTest {

    @Test
    public void testPlayerStateIntegration() {
        InMemoryStorageAdapter persistence = new InMemoryStorageAdapter();

        Player player = new Player(1000, 100);
        player.earnMoney(500);
        player.decreaseHealth(20);

        PlayerData dataToSave = new PlayerData(player.getMoney(), player.getHealth(), 3);
        persistence.saveData(dataToSave);

        PlayerData loadedData = persistence.loadData();
        Player newPlayer = new Player(loadedData.getMoney(), loadedData.getHealth());

        assertEquals(1500, newPlayer.getMoney());
        assertEquals(80, newPlayer.getHealth());
    }
}
