package com.urquieta.something.game;

import com.urquieta.something.platform.Screen;
import com.urquieta.something.platform.Renderer;
import com.urquieta.something.platform.Input;
import com.urquieta.something.platform.InputEvent;
import com.urquieta.something.game.GameState;
import com.urquieta.something.game.board.Circle;
import com.urquieta.something.game.board.GameBoard;

import com.urquieta.something.game.util.Vec2;

public class Game implements Runnable
{
    private final int  TARGET_FPS = 30;
    private final long ONE_BILLION = 1000000000;
    private final long TARGET_DELTA = ONE_BILLION / TARGET_FPS;

    private Screen main_canvas;
    private Renderer renderer;
    private Input input;
    private GameState game_state;
    private Thread thread;

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
                this.input != null);
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

    public void setScreen(Screen screen) {
        this.main_canvas = screen;
        this.renderer = new Renderer(this.main_canvas);
    }

    public void setInput(Input input) {
        this.input = input;
    }

    @Override
    public void run() {
        long   start_time = System.nanoTime();
        double delta = 0.0;
        int    fps = 0;
        long   last_fps_time = 0;

        Initalize();
        while (this.game_state.IsRunning()) {
            // Compute frame info
            long before_time   = System.nanoTime();
            long update_lenght = before_time - start_time;
            start_time = before_time;
            delta = update_lenght / ((double)TARGET_DELTA);
            last_fps_time += update_lenght;
            fps++;

            if (last_fps_time >= ONE_BILLION)
            {
                last_fps_time = 0;
                fps = 0;
            }

            // Update game.
            // NOTE(Misael Urquieta): Maybe divide this into sections?
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
                e.printStackTrace();
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
                e.printStackTrace();
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
        this.game_board = new GameBoard(this.renderer, 10, 10);
    }

    private void GameUpdate(double delta) {
        // TODO(Misael Urquieta): Clean up this, I don't think this should be like this.
        this.game_state.current_input = this.input.GetInputEvent();
        InputEvent event = this.game_state.current_input;

        switch (event.type) {
        case InputEvent.TOUCH_DRAGGED: {
            this.game_board.PlayerDragged(true);
        } break;
        default: {
            // NOTE(Misael): Do nothing for now...
            this.game_board.PlayerDragged(false);
        }
        }

        this.game_board.UpdateCursor(new Vec2(this.game_state.current_input.x,
                                              this.game_state.current_input.y));

        this.game_board.Update();

        
        this.renderer.BeginDraw();
        this.game_board.Draw();

        this.renderer.DrawCircle(game_state.current_input.x, game_state.current_input.y, 0.01f, 0xFF00FF00);
        this.renderer.EndDraw();
    }
}
