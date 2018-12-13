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

    public void Play(float volume) {
        this.pool.play(this.id, volume, volume, 0, 0, 1);
    }

    public void Dispose() {
        this.pool.unload(id);
    }
}
