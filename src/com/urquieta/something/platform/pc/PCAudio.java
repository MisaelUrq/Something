package com.urquieta.something.platform.pc;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import com.urquieta.something.platform.Sound;

public class PCAudio {

    public PCAudio() {

    }

    public Sound CreateSound(String filename) {
        Sound Result = new Sound();
        filename = "assets/"+filename;
        try {
            File sound_file = new File(filename);
            AudioInputStream audio = AudioSystem.getAudioInputStream(sound_file);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            Result.Init(clip);
        }
        catch (Exception e) {
            System.out.println("SOMETHING_ERROR: "+e);
        }
        return Result;
    }
}
