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

    public void Init(Screen screen, Input input, Audio audio) {
        this.SetScreen(screen);
        this.SetInput(input);
        this.SetAudio(audio);
    }

    @Override
    public void run() {
        long   start_time = System.nanoTime();
        double delta = 0.0;
        int    fps = 0;
        long   last_fps_time = 0;

        Initalize(format_board);
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
                // TODO(Misael): Change this for an actual loggin system.
                System.out.println("SOMETHING_ERROR: "+e);
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
                // TODO(Misael): Change this for an actual loggin system.
                System.out.println("SOMETHING_ERROR: "+e);
            }
        }
    }

    public String ToFileFormat() {
        return this.game_board.ToFileFormat();
    }

    public void Pause() {
        this.stopThread();
    }

    public void Resume(String format) {
        this.format_board = format;
        this.startThread();
    }

    private void Initalize(String board_format) {
        Sound collect_sound = this.game_audio.CreateSound("collect.wav");
        Sound drop_sound    = this.game_audio.CreateSound("drop.wav");
        Sound clear_color_sound = this.game_audio.CreateSound("clear_color.wav");
        // TODO(Misael): Find a better button sound.
        Sound button_sound      = this.game_audio.CreateSound("button.wav");
        this.background_sound   = this.game_audio.CreateSound("background.wav");

        // NOTE(Misael): Make the sound initalize in another function maybe?
        if (board_format.length() >= 10) {
            this.game_board = new GameBoard(this.renderer, board_format,
                                            collect_sound, drop_sound, clear_color_sound);
        } else {
            this.game_board = new GameBoard(this.renderer, 10, 10,
                                            collect_sound, drop_sound, clear_color_sound);
        }

        this.debug_menu = new DebugMenu(this.renderer, button_sound);

        GameState.SetAllDefault();
        GameState.state ^= GameState.PLAYING;
        this.start_menu = new StartMenu(this.renderer, button_sound);
        this.background_sound.PlayLoop(1);
    }

    private void GameUpdate(double delta) {
        if ((GameState.state & GameState.PLAYING) == GameState.GAME_OVER) {
            // TODO(Misael): I don't like this, the sound should not
            // be loaded again... or maybe? When we have a concept of levels maybe, right now, NO!!
            Sound collect_sound     = this.game_audio.CreateSound("collect.wav");
            Sound drop_sound        = this.game_audio.CreateSound("drop.wav");
            Sound clear_color_sound = this.game_audio.CreateSound("clear_color.wav");
            this.game_board = new GameBoard(this.renderer, 10, 10,
                                            collect_sound, drop_sound, clear_color_sound);
            GameState.state ^= GameState.PLAYING;
            return;
        }

        // TODO(Misael): On android we got an exception saying that comming from this call... this:
        // java.lang.IllegalStateException: Surface has already been released.
 	// at android.view.Surface.checkNotReleasedLocked(Surface.java:564)
 	// at android.view.Surface.lockCanvas(Surface.java:306)
 	// at android.view.SurfaceView$3.internalLockCanvas(SurfaceView.java:1043)
 	// at android.view.SurfaceView$3.lockCanvas(SurfaceView.java:1003)
 	// at com.urquieta.something.platform.android.AndroidScreen.beginDraw(AndroidScreen.java:43)
 	// at com.urquieta.something.platform.Screen.beginDraw(Screen.java:21)
 	// at com.urquieta.something.platform.Renderer.BeginDraw(Renderer.java:81)
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
            if (GameState.is_menu_active == false) {
                switch (event.type) {
                case InputEvent.TOUCH_DRAGGED: {
                    // TODO(Misael): Rename this function.
                    this.game_board.PlayerDragged(true);
                } break;
                default: {
                    this.game_board.PlayerDragged(false);
                }
                }

                this.game_board.UpdateCursor(event.cursor_position);
                this.game_board.Update(delta);
            }
            this.game_board.Draw();
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
}
