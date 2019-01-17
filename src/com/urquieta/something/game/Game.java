package com.urquieta.something.game;

import com.urquieta.something.game.GameState;
import com.urquieta.something.game.board.Circle;
import com.urquieta.something.game.board.GameBoard;
import com.urquieta.something.game.util.Vec2;
import com.urquieta.something.platform.Screen;
import com.urquieta.something.platform.Renderer;
import com.urquieta.something.platform.Input;
import com.urquieta.something.platform.InputEvent;
import com.urquieta.something.platform.Audio;
import com.urquieta.something.platform.Sound;
import com.urquieta.something.game.ui.Button;
import com.urquieta.something.game.ui.StartMenu;
import com.urquieta.something.game.ui.DebugMenu;
import com.urquieta.something.game.save.SaveState;
import com.urquieta.something.output.OutputSystem;

public class Game implements Runnable
{
    private final int  TARGET_FPS = 30;
    private final long ONE_BILLION = 1000000000;
    private final long TARGET_DELTA = ONE_BILLION / TARGET_FPS;
    private int average_fps = 0;

    private Screen main_canvas;
    private Renderer renderer;
    private Input input;
    private GameState game_state;
    private Thread thread;
    private Audio  game_audio;

    public  String    format_board = new String();
    public  boolean   is_resuming = false;
    private GameBoard game_board;
    private DebugMenu debug_menu;
    private Sound     background_sound;
    private StartMenu start_menu;
    private SaveState savefile;

    public Game()
    {
        super();
    }

    public Screen GetScreen() {
        return this.main_canvas;
    }

    public boolean IsOk() {
        return (this.renderer != null &&
                this.main_canvas != null &&
                this.input != null &&
                this.game_audio != null);
    }

    public void startThread()
    {
        if (this.IsOk())
        {
            if (this.game_state == null) {
                this.game_state = new GameState();
            }
            GameState.SetRunning(true);
            thread = new Thread(this);
            thread.start();
        }
    }

    public void Init(Screen screen, Input input, Audio audio,
                     SaveState savefile) {
        this.SetScreen(screen);
        this.SetInput(input);
        this.SetAudio(audio);
        this.SetSavefile(savefile);
    }

    @Override
    public void run() {
        long   start_time = System.nanoTime();
        double delta = 0.0;
        int    fps = 0;
        long   last_fps_time = 0;

        Initalize();
        while (GameState.IsRunning()) {
            long before_time   = System.nanoTime();
            long update_lenght = before_time - start_time;
            start_time = before_time;
            delta = update_lenght / ((double)TARGET_DELTA);
            last_fps_time += update_lenght;
            fps++;

            if (last_fps_time >= ONE_BILLION)
            {
                last_fps_time = 0;
                this.average_fps = fps;
                fps = 0;
            }

            this.GameUpdate(delta);

            double after_time = System.nanoTime();
            this.SleepThread((start_time - System.nanoTime() + TARGET_DELTA) / 1000000); // @PC_Replace: 'this' -> 'Thread'
        }
    }

    private void SleepThread(long time_to_sleep) {
        if (time_to_sleep > 0)
        {
            try {
                thread.sleep(time_to_sleep);
            }
            catch (Exception e) {
                OutputSystem.DebugPrint(e.toString(), OutputSystem.ERRORS);
            }
        }
    }

    public void stopThread() {
        GameState.SetRunning(false);
        while (true) {
            try {
                thread.join();
                break;
            }
            catch (Exception e) {
                OutputSystem.DebugPrint(e.toString(), OutputSystem.ERRORS);
            }
        }
    }

    public void Pause() {
        this.stopThread();
    }

    public void Resume() {
        this.startThread();
    }

    private void Initalize() {
        Sound collect_sound = this.game_audio.CreateSound("collect.wav");
        Sound drop_sound    = this.game_audio.CreateSound("drop.wav");
        Sound clear_color_sound = this.game_audio.CreateSound("clear_color.wav");
        // TODO(Misael): Find a better button sound.
        Sound button_sound      = this.game_audio.CreateSound("button.wav");
        // TODO(Misael): This sound does not play in Android.
        this.background_sound   = this.game_audio.CreateSound("background.wav");

        GameState.SetAllDefault();
        savefile.LoadSaveGame();

        this.debug_menu = new DebugMenu(this.renderer, button_sound);
        this.start_menu = new StartMenu(this.renderer, button_sound);
        this.background_sound.PlayLoop(1);
    }

    private void GameUpdate(double delta) {
        this.renderer.BeginDraw();
        InputEvent event = this.input.GetInputEvent();

        switch (GameState.current_mode) {
        case GameState.START_MENU: {
            if (GameState.is_menu_active == false) {
                this.start_menu.UpdateEvent(event);
                this.start_menu.Update(delta);
            }
            this.start_menu.Draw();
        } break;
        case GameState.INFINITE_MODE: {
            SaveState.LevelPaused level = this.savefile.GetSavedLevel(SaveState.INFINITE_MODE);
            if (GameState.IsGameOver() && level != null) {
                Sound collect_sound = this.game_audio.CreateSound("collect.wav");
                Sound drop_sound    = this.game_audio.CreateSound("drop.wav");
                Sound clear_color_sound = this.game_audio.CreateSound("clear_color.wav");
                this.game_board = new GameBoard(this.renderer,
                                                level.width, level.height, level.score, level.format,
                                                collect_sound, drop_sound, clear_color_sound);
                GameState.SetToPlaying();
            }
            else if (GameState.restart_level_requested) {
                GameState.restart_level_requested = false;
                this.game_board.InitNewBoard(10, 10);
                GameState.SetToPlaying();
            } else if (this.game_board == null) {
                Sound collect_sound = this.game_audio.CreateSound("collect.wav");
                Sound drop_sound    = this.game_audio.CreateSound("drop.wav");
                Sound clear_color_sound = this.game_audio.CreateSound("clear_color.wav");
                this.game_board = new GameBoard(this.renderer, 10, 10,
                                                collect_sound, drop_sound, clear_color_sound);
                GameState.SetToPlaying();
            }

            if (GameState.is_menu_active == false) {
                this.game_board.UpdateEvent(event);
                this.game_board.Update(delta);
            }

            if (this.game_board.WasExitRequested()) {
                GameState.SetGameOver();
                SaveGame();
                this.game_board = null;
                break;
            }
            this.game_board.Draw();
        } break;
        case GameState.NORMAL_MODE: {
            SaveState.LevelPaused level = this.savefile.GetSavedLevel(SaveState.NORMAL_MODE);
            if (GameState.IsGameOver() && level != null) {
                Sound collect_sound = this.game_audio.CreateSound("collect.wav");
                Sound drop_sound    = this.game_audio.CreateSound("drop.wav");
                Sound clear_color_sound = this.game_audio.CreateSound("clear_color.wav");
                this.game_board = new GameBoard(this.renderer,
                                                level.width, level.height, level.score, level.format,
                                                collect_sound, drop_sound, clear_color_sound);
                this.game_board.DEBUG_InitDummyGoals();
                GameState.SetToPlaying();
            }
            else if (GameState.restart_level_requested) {
                GameState.restart_level_requested = false;
                this.game_board.InitNewBoard(5, 5);
                this.game_board.DEBUG_InitDummyGoals();
                GameState.SetToPlaying();
            } else if (this.game_board == null) {
                Sound collect_sound = this.game_audio.CreateSound("collect.wav");
                Sound drop_sound    = this.game_audio.CreateSound("drop.wav");
                Sound clear_color_sound = this.game_audio.CreateSound("clear_color.wav");
                this.game_board = new GameBoard(this.renderer, 5, 5,
                                                collect_sound, drop_sound, clear_color_sound);
                this.game_board.DEBUG_InitDummyGoals();
                GameState.SetToPlaying();

            }

            if (GameState.is_menu_active == false) {
                this.game_board.UpdateEvent(event);
                this.game_board.Update(delta);
            }

            if (this.game_board.WasExitRequested()) {
                GameState.SetGameOver();
                SaveGame();
                this.game_board = null;
                break;
            }
            this.game_board.Draw();
        } break;
        case GameState.EXIT: {
            // TODO(Misael): If you select this the game won't
            // save... And on android I don't really think its the
            // right way, Why? I don't know, I don't really care, it's
            // stupid that you have to use a special function to exit
            // your program. I really hope the're other ways to exits
            // than just calling from the MainActivity.
            SaveGame();
            System.exit(0);
        } break;
        default:
            this.renderer.DrawText("Comming soon! I hope...", new Vec2(-.5f, 0), 0xffcd1076);
        }

        this.debug_menu.UpdateEvent(event);
        this.debug_menu.Update(delta);
        this.debug_menu.Draw();

        if ((GameState.state & GameState.SHOW_FPS) != 0) {
            String format_output = String.format("Delta: %.10f - FPS: %2d", delta, this.average_fps);
            this.renderer.DrawText(format_output, new Vec2(-1, -1), 0xFF000000);
        }

        this.renderer.EndDraw();
    }

    public void SaveGame() {
        if (GameState.current_mode == GameState.INFINITE_MODE) {
            this.savefile.SetLevelPaused(this.game_board.ToFileFormat(),
                                         this.game_board.GetWidth(), this.game_board.GetHeight(),
                                         this.game_board.GetScore(), SaveState.INFINITE_MODE);
        } else if (GameState.current_mode == GameState.NORMAL_MODE) {
            this.savefile.SetLevelPaused(this.game_board.ToFileFormat(),
                                         this.game_board.GetWidth(), this.game_board.GetHeight(),
                                         this.game_board.GetScore(), SaveState.NORMAL_MODE);
        }
        GameState.current_mode = GameState.START_MENU;
        this.savefile.SaveGame();
    }

    private void SetAudio(Audio audio) {
        this.game_audio = audio;
    }

    private void SetScreen(Screen screen) {
        this.main_canvas = screen;
        this.renderer = new Renderer(this.main_canvas);
    }

    private void SetInput(Input input) {
        this.input = input;
    }

    private void SetSavefile(SaveState savefile) {
        this.savefile = savefile;
    }
}
