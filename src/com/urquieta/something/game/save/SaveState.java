package com.urquieta.something.game.save;

import com.urquieta.something.output.OutputSystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;

// TODO(Misael): Maybe this should be a JSON or something reader...
public class SaveState {

    public class LevelPaused {
        public String format;
        public String mode;
        public char   width;
        public char   height;
        public int    score;

        public LevelPaused() {
            this("", 0, 0, 0, "");
        }

        public LevelPaused(String format, int width, int height, int score, String mode) {
            this.width  = (char)width;
            this.height = (char)height;
            this.format = format;
            this.score  = score;
            this.mode   = mode;
        }

        public String toString() {
            return "score: " + score + "\n" +
                "Dimension: (" + (int)width + ","+(int)height+")\n" +
                "format: " + format + "\n" +
                "mode: " + mode;
        }

        public String GetSaveFormat() {
            String result = "";
            result += width;
            result += height;
            char[] array_score = FromIntToCharArray(this.score);
            for (char c: array_score) {
                result += c;
            }
            result += format +mode;
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


    private final String current_version = "0.0.1";
    private File         save_parent;
    private boolean      was_a_level_being_played;
    private LevelPaused  level_paused;

    private String levels_complete; // This format is as follows
    private int    max_world_level; // Max

    public SaveState(File save_parent) {
        this.was_a_level_being_played = false;
        this.save_parent = save_parent;
        this.level_paused = new LevelPaused();
    }

    public void SetLevelPaused(String level_format, int width, int height, int score, String mode) {
        this.was_a_level_being_played = true;
        this.level_paused = new LevelPaused(level_format, width, height, score, mode);
    }

    public String GetSaveFormat() {
        return current_version + "\n" + ((was_a_level_being_played) ? "1" : "0") + "\n^";
    }

    public boolean WasGameNotFinish() {
        return was_a_level_being_played;
    }

    public LevelPaused GetSavedLevel() {
        return this.level_paused;
    }

    private void LoadCurrentVersionTempFile() {
        File file = new File(this.save_parent, "temp");
        try {
            BufferedReader buffer_reader = new BufferedReader(new FileReader(file.getPath()));
            this.level_paused.width  = (char)buffer_reader.read();
            this.level_paused.height = (char)buffer_reader.read();
            char[] temp_array_score = new char[4];
            char[] temp_array_format = new char[this.level_paused.width * this.level_paused.height * 9];
            buffer_reader.read(temp_array_score, 0, 4);
            buffer_reader.read(temp_array_format, 0, temp_array_format.length);
            this.level_paused.mode = buffer_reader.readLine();
            this.level_paused.score  = FromCharArrayToInt(temp_array_score);
            this.level_paused.format = new String(temp_array_format);
            buffer_reader.close();
        } catch (Exception e) {
            OutputSystem.DebugPrint("Could not load data from temp file: '"+file.getPath()+"'\nError Info: "+e,
                                    OutputSystem.WARNINGS);
        }
    }

    public void LoadSaveGame() {
        File file = new File(this.save_parent, "general");
        try {
            if (file.exists() == false) { return; }
            BufferedReader buffer_reader = new BufferedReader(new FileReader(file.getPath()));
            String file_version = buffer_reader.readLine();

            switch (file_version) {
            case current_version: {
                String line = buffer_reader.readLine();
                if (line.compareTo("1") == 0) {
                    this.was_a_level_being_played = true;
                    LoadCurrentVersionTempFile();
                } else if (line.compareTo("0") == 0) {
                    this.was_a_level_being_played = false;
                    // TODO(Misael): Don't do anything?
                } else {
                    OutputSystem.DebugPrint("Wrong file format", OutputSystem.WARNINGS);
                }
            } break;
            default:
                OutputSystem.DebugPrint("Unknow file version.", OutputSystem.WARNINGS);
            }
            buffer_reader.close();
        } catch (Exception e) {
            OutputSystem.DebugPrint("Could not save data: "+file.getPath()+"\nError Info: "+e, OutputSystem.WARNINGS);
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
