package com.urquieta.something.game;

import com.urquieta.something.platform.Screen;
import com.urquieta.something.platform.Renderer;
import com.urquieta.something.platform.Input;
import com.urquieta.something.platform.InputEvent;
import com.urquieta.something.game.GameState;

public class Game extends Thread implements Runnable
{
    private boolean   is_running;
    private final int  TARGET_FPS = 30;
    private final long ONE_BILLION = 1000000000;
    private final long TARGET_DELTA = ONE_BILLION / TARGET_FPS;

    private Screen main_canvas;
    private Renderer renderer;
    private Input input;
    private GameState game_state;

    public Game()
    {
        super();
        this.is_running = false;
    }

    public void startThread()
    {
        if (this.renderer != null &&
            this.main_canvas != null &&
            this.input != null)
        {
            this.is_running = true;
            this.start();
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
        int fps = 0;
        long last_fps_time = 0;

        this.Initialize();
        while (this.is_running) {
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

            this.GameUpdate(delta);

            double after_time = System.nanoTime();
            this.SleepThread((start_time - System.nanoTime() + TARGET_DELTA) / 1000000); // @PC_Replace: 'this' -> 'Thread'
        }
    }

    private void SleepThread(long time_to_sleep) {
        if (time_to_sleep > 0)
        {
            try {
                this.sleep(time_to_sleep);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stopThread() {
        while (this.is_running) {
            try {
                this.join();
                this.is_running = false;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void DrawGameBoard(int width_count, int height_count) {
        float padding_x = 0.15f;
        float padding_y = 0.15f;
        float start_x_position = -((padding_x * (float)(width_count+1)  / 2.0f));
        float start_y_position =  ((padding_y * (float)(height_count+1) / 2.0f));
        float y_position = start_y_position;
        for (int index = 0; index < height_count; index++) {
            float x_position = start_x_position;
            y_position -= padding_y;
            for (int jndex = 0; jndex < width_count; jndex++) {
                x_position += padding_x;
                this.renderer.DrawCircle(x_position, y_position, 0.02f, 0xFFAA89A4);
            }
        }
    }

    private void Initialize() {
        game_state = new GameState();
    }

    private void GameUpdate(double delta) {
        this.game_state.current_input = this.input.GetInputEvent();
        InputEvent event = this.game_state.current_input;

        if (event.type == InputEvent.TOUCH_DOWN) {
            this.game_state.last_input = new InputEvent();
            this.game_state.last_input.x = event.x;
            this.game_state.last_input.y = event.y;
            this.game_state.last_input.type = event.type;
        }

        if (event.type != InputEvent.TOUCH_DRAGGED) {
            this.game_state.last_input = new InputEvent();
            this.game_state.last_input.x = event.x;
            this.game_state.last_input.y = event.y;
            this.game_state.last_input.type = event.type;
       }

        this.renderer.BeginDraw();
        this.DrawGameBoard(5, 5);

        if (game_state.current_input.type == InputEvent.TOUCH_DRAGGED) {
            this.renderer.DrawLine(game_state.last_input.x, game_state.last_input.y,
                                   game_state.current_input.x, game_state.current_input.y,
                                   0xFF0000FF);
        }

        this.renderer.DrawLine( 0f, 1f, 0f, -1f, 0xFFFF0000);
        this.renderer.DrawLine(-1f, 0f, 1f,  0f, 0xFFFF0000);
        // Position of the last input.
        this.renderer.DrawCircle(game_state.current_input.x, game_state.current_input.y, 0.01f, 0xFF00FF00);
        this.renderer.EndDraw();
    }
}
