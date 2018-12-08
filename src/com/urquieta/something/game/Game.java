package com.urquieta.something.game;

import com.urquieta.something.platform.Screen;
import com.urquieta.something.platform.Renderer;
import com.urquieta.something.platform.Input;
import com.urquieta.something.platform.InputEvent;
import com.urquieta.something.game.GameState;
import com.urquieta.something.game.board.Circle;

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

    private void DrawGameBoard(int width_count, int height_count, float circles_proportion) {
        // TODO(Misael]): Find a way to make this dependent on the circle proportion.
        float padding_x = 0.15f;
        float padding_y = 0.15f;
        // TODO(Misael): On android this gets kind of wrong and it move them a little to the left.
        float start_x_position = -((padding_x * (float)(width_count+1)  / 2.0f));
        float start_y_position =  ((padding_y * (float)(height_count+1) / 2.0f));
        float y_position = start_y_position;
        float radius = ((circles_proportion / this.main_canvas.GetWidth()) +
                        (circles_proportion / this.main_canvas.GetHeight())) / 2;
        int color = 0xFFA090F7;
        for (int index = 0; index < height_count; index++) {
            float x_position = start_x_position;
            y_position -= padding_y;
            for (int jndex = 0; jndex < width_count; jndex++) {
                x_position += padding_x;
                Circle circle = new Circle(this.renderer, x_position, y_position, radius, color);
                if (circle.HasCollide(this.game_state.current_input.x,
                                      this.game_state.current_input.y)) {
                    circle.SetColor(0xFFFF0000);
                    circle.DEBUG_DrawPostionLocation();
                }
                circle.Draw();
                circle.DEBUG_DrawRectOverArea();
            }
        }
    }

    private void GameUpdate(double delta) {
        // TODO(Misael Urquieta): Clean up this, I don't think this should be like this.
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
        this.DrawGameBoard(5, 5, 10);

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
