package com.urquieta.something.game.save;

import com.urquieta.something.output.OutputSystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileInputStream;

// TODO(Misael): Maybe this should be a JSON or something reader...
public class SaveState {

    public class LevelPaused {
        public String format;
        public String mode;
        public char   width;
        public char   height;
        public int    score;

        public LevelPaused(String format, int width, int height, int score, String mode) {
            this.width  = (char)width;
            this.height = (char)height;
            this.format = format;
            this.score  = score;
            this.mode   = mode;
        }

        public String GetSaveFormat() {
            String result = width + "" + height;
            char[] array_score = FromIntToCharArray(this.score);
            for (char c: array_score) {
                result += c;
            }
            result += format + mode;
            return result;
        }
    }

    public int FromCharArrayToInt(char[] data) {
        if (data.length == 4) {
            int result = ((data[0] << 0)  |
                          (data[1] << 8)  |
                          (data[2] << 16) |
                          (data[3] << 24));
            return result;
        } else {
            return 0;
        }
    }

    public char[] FromIntToCharArray(int data) {
        char[] result = new char[4];
        result[0] = (char)(data >> 0);
        result[1] = (char)(data >> 8);
        result[2] = (char)(data >> 16);
        result[3] = (char)(data >> 24);
        return result;
    }


    private final String current_version = "0.0.1\n";
    private File         save_parent;
    private boolean      was_a_level_being_played;
    private LevelPaused  level_paused;

    private String levels_complete; // This format is as follows
    private int    max_world_level; // Max

    public SaveState(File save_parent) {
        this.save_parent = save_parent;
    }

    public void SetLevelPaused(String level_format, int width, int height, int score, String mode) {
        this.was_a_level_being_played = true;
        this.level_paused = new LevelPaused(level_format, width, height, score, mode);
    }

    public String GetSaveFormat() {
        return current_version + ((was_a_level_being_played) ? "1" : "0");
    }

    public void ReadSaveFormat(String format) {
        String save_version = format.substring(0, current_version.length());
        this.was_a_level_being_played = (format.charAt(current_version.length()+1) == 1) ? true : false;
        System.out.println(save_version);
        System.out.println(was_a_level_being_played);
    }

    public void LoadSaveGame() {
        File file = new File(this.save_parent, "general");
        try {
            if (file.exists()) {
                FileInputStream stream = new FileInputStream(file);
                String format = ""; //stream.read();
                ReadSaveFormat(format);
            }

        } catch (Exception e) {
            OutputSystem.DebugPrint("Could not save data: "+file.getPath(), 1);
        }
    }

    public void SaveGame() {
        File file = new File(this.save_parent, "general");
        try {

            file.createNewFile();
            File level_paused_file = new File(this.save_parent, "temp");
            if (this.was_a_level_being_played) {
                level_paused_file.createNewFile();
                FileOutputStream stream = new FileOutputStream(level_paused_file);
                stream.write(this.level_paused.GetSaveFormat().getBytes());
                stream.close();
            } else {
                level_paused_file.delete();
            }

            FileOutputStream stream = new FileOutputStream(file);
            stream.write(this.GetSaveFormat().getBytes());
            stream.close();
        } catch (Exception e) {
            OutputSystem.DebugPrint("Could not save data: "+file.getPath(), 1);
        }
    }
}
