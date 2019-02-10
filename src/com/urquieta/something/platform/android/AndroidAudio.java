package com.urquieta.something.platform.android;

import java.io.IOException;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.content.res.AssetManager;
import android.content.res.AssetFileDescriptor;

import com.urquieta.something.platform.Sound;
import com.urquieta.something.output.OutputSystem;

public class AndroidAudio {
    SoundPool sound_pool;
    AssetManager asset_manager;

    // NOTE(Misael Urquieta): It's emtpy for the init method to make
    // the work and not collide with the PC file.
    public AndroidAudio() {

    }

    public Sound CreateSound(String filename) {
        try {
            AssetFileDescriptor descriptor = this.asset_manager.openFd(filename);
            int sound_id = this.sound_pool.load(descriptor, 0);
            Sound sound = new Sound();
            sound.Init(this.sound_pool, sound_id);
            return sound;
        } catch (IOException e) {
            OutputSystem.DebugPrint(e.toString(), OutputSystem.ERRORS);
            return null;
        }
    }

    public void Init(Activity activity) {
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        this.asset_manager = activity.getAssets();
        this.sound_pool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0); // TODO(Misael): @Deprecated
    }
}
