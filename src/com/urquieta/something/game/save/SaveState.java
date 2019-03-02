package com.urquieta.something.game.save;

import com.urquieta.something.output.OutputSystem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;


// TODO(Misael): Maybe this should be a JSON or something reader...
public class SaveState {
    public static final String INFINITE_MODE = "INFINITE_MODE";
    public static final String NORMAL_MODE   = "NORMAL_MODE";


    public class LevelPaused {
        public String format;
        public String objectives;
        public char   num_objectives;
        public char[] objectives_count;
        public char[] objectives_current_score;
        public char   width;
        public char   height;
        public int    score;


        public LevelPaused() {
            this("", 0, 0, 0, 0, null, null, "");
        }

        public LevelPaused(String format, int width, int height, int score,
                           int num_objectives, char[] objectives_count, char[] objectives_current_score,
                           String objectives) {
            this.width  = (char)width;
            this.height = (char)height;
            this.format = format;
            this.num_objectives = (char)num_objectives;
            this.objectives = objectives;
            if (objectives_count != null) {
                this.objectives_count = objectives_count;
                this.objectives_current_score = objectives_current_score;
            } else {
                this.objectives_count = null;
            }
            this.score  = score;
        }

        public String toString() {
            return String.format("Score: %d\n Dimension: (%d, %d)\nObjectives(%d): (%d/%d %d/%d %d/%d) format: %s\nformat: %s",
                                 score, (int)width, (int)height, (int)num_objectives,
                                 (int)objectives_count[0], (int)objectives_current_score[0],
                                 (int)objectives_count[1], (int)objectives_current_score[1],
                                 (int)objectives_count[2], (int)objectives_current_score[2],
                                 objectives, format);
        }

        public String GetSaveFormat() {
            String result = "";
            result += width;
            result += height;
            char[] array_score = FromIntToCharArray(this.score);
            for (char c: array_score) {
                result += c;
            }
            result += format;
            result += num_objectives;
            if (objectives_count != null) {
                for (char c: objectives_count) {
                    result += c;
                }
                for (char c: objectives_current_score) {
                    result += c;
                }
            }
            result += objectives;
            return result;
        }

        public boolean IsGamePaused() {
            return (format.length() != 0);
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


    private final String current_version = "0.0.3";
    private final String version_0_0_2  = "0.0.2";
    private File         save_parent;
    private boolean      was_a_level_being_played;
    private LevelPaused  infinite_mode_level_paused;
    private LevelPaused  normal_mode_level_paused;

    private String levels_complete; // This format is as follows
    private int    max_world_level; // Max

    public SaveState(File save_parent) {
        this.was_a_level_being_played = false;
        this.save_parent = save_parent;
        this.infinite_mode_level_paused = new LevelPaused();
        this.normal_mode_level_paused   = new LevelPaused();
    }

    public void SetLevelPaused(String level_format, int width, int height, int score, String mode,
                               int num_objectives, char[] objectives_count, char[] objectives_current_score, String objective) {
        this.was_a_level_being_played = true;
        if (mode.compareTo(INFINITE_MODE) == 0) {
            this.infinite_mode_level_paused = new LevelPaused(level_format, width, height, score, 0, null, null, "");
        } else if (mode.compareTo(NORMAL_MODE) == 0) {
            this.normal_mode_level_paused   = new LevelPaused(level_format, width, height, score,
                                                              num_objectives, objectives_count, objectives_current_score,
                                                              objective);
        } else {
            OutputSystem.DebugPrint("Mode not recognize!", OutputSystem.Levels.ERRORS);
        }
    }

    public String GetSaveFormat() {
        return current_version + "\n" + ((was_a_level_being_played) ? "1" : "0") + "\n^";
    }

    public boolean WasGameNotFinish() {
        return was_a_level_being_played;
    }

    public LevelPaused GetSavedLevel(String mode) {
        if (mode.compareTo(NORMAL_MODE) == 0 &&
            normal_mode_level_paused.format.length() > 0) {
            return normal_mode_level_paused;
        } else if (mode.compareTo(INFINITE_MODE) == 0 &&
                   infinite_mode_level_paused.format.length() > 0) {
            return infinite_mode_level_paused;
        } else {
            return null;
        }
    }

    private void LoadCurrentVersionTempFile() {
        File file = new File(this.save_parent, "temp");
        try {
            BufferedReader buffer_reader = new BufferedReader(new FileReader(file.getPath()));
            String infinite_mode_paused = buffer_reader.readLine();
            if (infinite_mode_paused.compareTo("1") == 0) {
                this.infinite_mode_level_paused.width  = (char)buffer_reader.read();
                this.infinite_mode_level_paused.height = (char)buffer_reader.read();
                char[] temp_array_score = new char[4];
                int array_size = this.infinite_mode_level_paused.width * this.infinite_mode_level_paused.height * 9;
                char[] temp_array_format = new char[array_size];
                buffer_reader.read(temp_array_score, 0, 4);
                buffer_reader.read(temp_array_format, 0, temp_array_format.length);
                this.infinite_mode_level_paused.score  = FromCharArrayToInt(temp_array_score);
                this.infinite_mode_level_paused.format = new String(temp_array_format);
                this.infinite_mode_level_paused.num_objectives = (char)buffer_reader.read();
            }

            String normal_mode_paused   = buffer_reader.readLine();
            if (normal_mode_paused.compareTo("1") == 0) {
                this.normal_mode_level_paused.width  = (char)buffer_reader.read();
                this.normal_mode_level_paused.height = (char)buffer_reader.read();
                char[] temp_array_score = new char[4];
                int array_size = this.normal_mode_level_paused.width * this.normal_mode_level_paused.height * 9;
                char[] temp_array_format = new char[array_size];
                buffer_reader.read(temp_array_score, 0, 4);
                buffer_reader.read(temp_array_format, 0, temp_array_format.length);
                this.normal_mode_level_paused.score  = FromCharArrayToInt(temp_array_score);
                this.normal_mode_level_paused.format = new String(temp_array_format);
                this.normal_mode_level_paused.num_objectives = (char)buffer_reader.read();
                char[] temp_array_objectives = new char[this.normal_mode_level_paused.num_objectives*9];
                this.normal_mode_level_paused.objectives_count = new char[this.normal_mode_level_paused.num_objectives];
                this.normal_mode_level_paused.objectives_current_score = new char[this.normal_mode_level_paused.num_objectives];
                for (int index = 0; index < this.normal_mode_level_paused.num_objectives; index++) {
                    int byte_read = buffer_reader.read();
                    this.normal_mode_level_paused.objectives_count[index] = (char)byte_read;
                }
                for (int index = 0; index < this.normal_mode_level_paused.num_objectives; index++) {
                    int byte_read = buffer_reader.read();
                    this.normal_mode_level_paused.objectives_current_score[index] = (char)byte_read;
                }
                buffer_reader.read(temp_array_objectives, 0, temp_array_objectives.length);
                this.normal_mode_level_paused.objectives = new String(temp_array_objectives);
            }

            buffer_reader.close();
        } catch (Exception e) {
            OutputSystem.DebugPrint("Could not load data from temp file: '"+file.getPath()+"'\nError Info: "+e,
                                    OutputSystem.Levels.WARNINGS);
        }
    }

    private void LoadLegacyVersionTempFile_0_0_2() {
        File file = new File(this.save_parent, "temp");
        try {
            BufferedReader buffer_reader = new BufferedReader(new FileReader(file.getPath()));
            String infinite_mode_paused = buffer_reader.readLine();
            if (infinite_mode_paused.compareTo("1") == 0) {
                this.infinite_mode_level_paused.width  = (char)buffer_reader.read();
                this.infinite_mode_level_paused.height = (char)buffer_reader.read();
                char[] temp_array_score = new char[4];
                int array_size = this.infinite_mode_level_paused.width * this.infinite_mode_level_paused.height * 9;
                char[] temp_array_format = new char[array_size];
                buffer_reader.read(temp_array_score, 0, 4);
                buffer_reader.read(temp_array_format, 0, temp_array_format.length);
                this.infinite_mode_level_paused.score  = FromCharArrayToInt(temp_array_score);
                this.infinite_mode_level_paused.format = new String(temp_array_format);
                this.infinite_mode_level_paused.num_objectives = 0;
            }

            String normal_mode_paused   = buffer_reader.readLine();
            if (normal_mode_paused.compareTo("1") == 0) {
                this.normal_mode_level_paused.width  = (char)buffer_reader.read();
                this.normal_mode_level_paused.height = (char)buffer_reader.read();
                char[] temp_array_score = new char[4];
                int array_size = this.normal_mode_level_paused.width * this.normal_mode_level_paused.height * 9;
                char[] temp_array_format = new char[array_size];
                buffer_reader.read(temp_array_score, 0, 4);
                buffer_reader.read(temp_array_format, 0, temp_array_format.length);
                this.normal_mode_level_paused.score  = FromCharArrayToInt(temp_array_score);
                this.normal_mode_level_paused.format = new String(temp_array_format);
                this.normal_mode_level_paused.num_objectives = 0;
            }

            buffer_reader.close();
        } catch (Exception e) {
            OutputSystem.DebugPrint("Could not load data from temp file: '"+file.getPath()+"'\nError Info: "+e,
                                    OutputSystem.Levels.WARNINGS);
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
                } else {
                    OutputSystem.DebugPrint("Wrong file format", OutputSystem.Levels.WARNINGS);
                }
            } break;
            case version_0_0_2: {
                String line = buffer_reader.readLine();
                if (line.compareTo("1") == 0) {
                    this.was_a_level_being_played = true;
                    LoadLegacyVersionTempFile_0_0_2();
                } else if (line.compareTo("0") == 0) {
                    this.was_a_level_being_played = false;
                } else {
                    OutputSystem.DebugPrint("Wrong file format", OutputSystem.Levels.WARNINGS);
                }
            } break;
            default:
                OutputSystem.DebugPrint("Unknow file version.", OutputSystem.Levels.WARNINGS);
            }
            buffer_reader.close();
        } catch (Exception e) {
            OutputSystem.DebugPrint("Could not save data: "+file.getPath()+"\nError Info: "+e, OutputSystem.Levels.WARNINGS);
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
                if (this.infinite_mode_level_paused.IsGamePaused()) {
                    stream.write("1\n".getBytes());
                    stream.write(this.infinite_mode_level_paused.GetSaveFormat().getBytes());
                } else {
                    stream.write("0\n".getBytes());
                }

                if (this.normal_mode_level_paused.IsGamePaused()) {
                    stream.write("1\n".getBytes());
                    stream.write(this.normal_mode_level_paused.GetSaveFormat().getBytes());
                } else {
                    stream.write("0\n".getBytes());
                }

                stream.close();
            } else {
                level_paused_file.delete();
            }

            FileOutputStream stream = new FileOutputStream(file);
            stream.write(this.GetSaveFormat().getBytes());
            stream.close();
        } catch (Exception e) {
            OutputSystem.DebugPrint("Could not save data: "+file.getPath(), OutputSystem.Levels.WARNINGS);
        }
    }
}
