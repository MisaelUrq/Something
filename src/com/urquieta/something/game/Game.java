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
    private Sound  ball_sound;

    private GameBoard game_board;

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
            this.game_state.SetRunning(true);
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

        Initalize();
        while (this.game_state.IsRunning()) {
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
        this.game_state.SetRunning(false);
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

    public void Pause() {
        this.stopThread();
    }

    public void Resume() {
        this.startThread();
    }

    // TODO(Misael): Delete this and replace with and actual button.
    private Button button_debug;

    private void Initalize() {
        this.game_board = new GameBoard(this.renderer, 10, 10);
        // 15 * 15 Max for screen. But it does not look good. For now the target if of 10 * 10.
        this.button_debug = new Button(this.renderer, new Vec2(-.9f, .9f), .2f, .2f, "Debug", 0xFF2C2C2C, 0xFFFFFFFF);
        // TODO(Misael): Make a file or something in orher to read this and other output massages.
        this.ball_sound = this.game_audio.CreateSound("Ball_Bounce.wav");
    }

    private void GameUpdate(double delta) {
        this.renderer.BeginDraw();
        this.game_state.current_input = this.input.GetInputEvent();
        InputEvent event = this.game_state.current_input;

        switch (event.type) {
        case InputEvent.TOUCH_DRAGGED: {
            // TODO(Misael): Rename this function.
            this.game_board.PlayerDragged(true);
        } break;
        default: {
            this.game_board.PlayerDragged(false);
        }
        }

        if (this.button_debug.IsPressed(event) &&
            event.type == InputEvent.TOUCH_CLIC) {
            if (this.ball_sound != null){
                this.ball_sound.Play(.5f);
            }
            if (this.game_state.GetStateOfGame() == GameState.PLAYING) {
                this.game_state.SetState(GameState.DEBUG_MENU);
            }
            else if (this.game_state.GetStateOfGame() == GameState.DEBUG_MENU) {
                this.game_state.SetState(GameState.PLAYING);
            }
        }

        this.game_board.UpdateCursor(event.cursor_position);
        this.game_board.Update(delta);
        this.game_board.Draw();
        this.button_debug.Draw();

        if (this.game_state.GetStateOfGame() == GameState.DEBUG_MENU) {
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
