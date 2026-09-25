package com.hongbao.bloons.audio;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;
import com.hongbao.bloons.events.ApplicationDisposeEvent;
import com.hongbao.bloons.events.GameEventBus;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class AudioAssetRegistry {

    private static final AudioAssetRegistry INSTANCE = new AudioAssetRegistry();

    private final Set<Disposable> managedAssets = ConcurrentHashMap.newKeySet();

    public AudioAssetRegistry() {
        this(GameEventBus.getInstance());
    }

    public AudioAssetRegistry(GameEventBus eventBus) {
        if (eventBus != null) {
            eventBus.subscribe(ApplicationDisposeEvent.class, this::onApplicationDispose);
        }
    }

    public static AudioAssetRegistry getInstance() {
        return INSTANCE;
    }

    public <T extends Disposable> T register(T asset) {
        if (asset != null) {
            managedAssets.add(asset);
        }
        return asset;
    }

    public Sound registerSound(Sound sound) {
        return register(sound);
    }

    public Music registerMusic(Music music) {
        return register(music);
    }

    public void unregister(Disposable asset) {
        if (asset != null) {
            managedAssets.remove(asset);
        }
    }

    public void onApplicationDispose(ApplicationDisposeEvent event) {
        disposeAll();
    }

    public void disposeAll() {
        for (Disposable asset : managedAssets) {
            if (asset != null) {
                try {
                    asset.dispose();
                } catch (Exception e) {
                    System.err.println("Error disposing audio asset: " + e.getMessage());
                }
            }
        }
        managedAssets.clear();
    }

    public int getManagedAssetCount() {
        return managedAssets.size();
    }
}
