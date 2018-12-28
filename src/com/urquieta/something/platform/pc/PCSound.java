package com.urquieta.something.platform.pc;

import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;

public class PCSound {

    private Clip clip;

    public PCSound() {

    }

    public void Init(Clip clip) {
        this.clip = clip;
    }

    public void PlayLoop(float volume) {
        if (this.clip.isRunning()) {
            this.clip.stop();
        }
        this.clip.setFramePosition(0);
        this.clip.loop(Clip.LOOP_CONTINUOUSLY);
        this.clip.start();
    }

    public void Play(float volume) {
        if (this.clip.isRunning()) {
            this.clip.stop();
        }
        this.clip.setFramePosition(0);
        this.clip.start();
    }

    public void Stop() {
        if (this.clip.isRunning()) {
            this.clip.stop();
        }
    }
}
