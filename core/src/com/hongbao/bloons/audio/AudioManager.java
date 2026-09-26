package com.hongbao.bloons.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;
import com.hongbao.bloons.events.BloonPoppedEvent;
import com.hongbao.bloons.events.GameEvent;
import com.hongbao.bloons.events.GameEventBus;
import com.hongbao.bloons.events.GameEventListener;
import com.hongbao.bloons.events.GamePausedEvent;
import com.hongbao.bloons.events.GameResumedEvent;
import com.hongbao.bloons.events.LevelChangedEvent;
import com.hongbao.bloons.events.MusicToggleRequestedEvent;

public class AudioManager implements GameEventListener, Disposable {

    private Sound popSound;
    private Music titleMusic;
    private Music stageMusic;
    private Music finalBossMusic;
    private Music currentMusic;

    public AudioManager(GameEventBus eventBus) {
        if (eventBus != null) {
            eventBus.subscribe(this);
        }
        initializeAudio();
    }

    private void initializeAudio() {
        popSound = loadSound("music/pop.mp3");
        titleMusic = loadMusic("music/title.mp3");
        stageMusic = loadMusic("music/demystify_feast.mp3");
        finalBossMusic = loadMusic("music/night_falls.mp3");
    }

    private Sound loadSound(String filePath) {
        if (Gdx.audio == null || Gdx.files == null) {
            return null;
        }
        try {
            return Gdx.audio.newSound(Gdx.files.internal(filePath));
        } catch (Exception e) {
            return null;
        }
    }

    private Music loadMusic(String filePath) {
        if (Gdx.audio == null || Gdx.files == null) {
            return null;
        }
        try {
            Music music = Gdx.audio.newMusic(Gdx.files.internal(filePath));
            music.setVolume(0.5f);
            music.setLooping(true);
            return music;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void onEvent(GameEvent event) {
        if (event instanceof BloonPoppedEvent) {
            handleBloonPopped();
        } else if (event instanceof LevelChangedEvent) {
            handleLevelChanged(((LevelChangedEvent) event).getLevel());
        } else if (event instanceof MusicToggleRequestedEvent) {
            handleMusicToggle();
        } else if (event instanceof GamePausedEvent) {
            handleGamePaused();
        } else if (event instanceof GameResumedEvent) {
            handleGameResumed();
        }
    }

    private void handleBloonPopped() {
        if (popSound != null) {
            try {
                popSound.play(0.5f);
            } catch (Exception ignored) {
            }
        }
    }

    private void handleLevelChanged(int level) {
        if (level <= 0) {
            playMusicTrack(titleMusic);
        } else if (level == 40) {
            playMusicTrack(finalBossMusic);
        } else if (level == 1) {
            playMusicTrack(stageMusic);
        }
    }

    private void playMusicTrack(Music nextMusic) {
        if (nextMusic == null) {
            return;
        }
        boolean wasPlaying = true;
        if (currentMusic != null) {
            try {
                wasPlaying = currentMusic.isPlaying();
                currentMusic.stop();
            } catch (Exception ignored) {
            }
        }
        currentMusic = nextMusic;
        if (wasPlaying) {
            try {
                currentMusic.play();
            } catch (Exception ignored) {
            }
        }
    }

    private void handleMusicToggle() {
        if (currentMusic != null) {
            try {
                if (currentMusic.isPlaying()) {
                    currentMusic.pause();
                } else {
                    currentMusic.play();
                }
            } catch (Exception ignored) {
            }
        }
    }

    private void handleGamePaused() {
        if (currentMusic != null) {
            try {
                if (currentMusic.isPlaying()) {
                    currentMusic.pause();
                }
            } catch (Exception ignored) {
            }
        }
    }

    private void handleGameResumed() {
        if (currentMusic != null) {
            try {
                currentMusic.play();
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public void dispose() {
        if (popSound != null) {
            try {
                popSound.dispose();
            } catch (Exception ignored) {
            }
            popSound = null;
        }
        if (titleMusic != null) {
            try {
                titleMusic.dispose();
            } catch (Exception ignored) {
            }
            titleMusic = null;
        }
        if (stageMusic != null) {
            try {
                stageMusic.dispose();
            } catch (Exception ignored) {
            }
            stageMusic = null;
        }
        if (finalBossMusic != null) {
            try {
                finalBossMusic.dispose();
            } catch (Exception ignored) {
            }
            finalBossMusic = null;
        }
        currentMusic = null;
    }
}
