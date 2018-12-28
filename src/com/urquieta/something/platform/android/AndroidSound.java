package com.urquieta.something.platform.android;

import android.media.SoundPool;

public class AndroidSound {
    int id;
    SoundPool pool;

    public AndroidSound() {

    }

    public void Init(SoundPool pool, int id) {
        this.pool = pool;
        this.id   = id;
    }

    public void PlayLoop(float volume) {
        this.pool.stop(this.id);
        this.pool.play(this.id, volume, volume, 0, -1, 1);
    }

    public void Play(float volume) {
        this.pool.stop(this.id);
        this.pool.play(this.id, volume, volume, 0, 0, 1); // id, left channel volume, right channel volume, priority, loop, playback rate...
    }

    public void Dispose() {
        this.pool.unload(id);
    }

    public void Stop() {
        this.pool.stop(this.id);
    }
}
