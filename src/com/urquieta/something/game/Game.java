package com.urquieta.something.game;

import com.urquieta.something.platform.Screen;
import com.urquieta.something.platform.Renderer;
import com.urquieta.something.platform.Input;
import com.urquieta.something.platform.InputEvent;

// TODO(Misael): @URGENT make the PC version of the clases to see how
// it will all be handle.
public class Game extends Thread implements Runnable
{
    private boolean   is_running;
    private final int  TARGET_FPS = 30;
    private final long ONE_BILLION = 1000000000;
    private final long TARGET_DELTA = ONE_BILLION / TARGET_FPS;

    private Screen main_canvas;
    private Renderer renderer;
    private Input input;

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
        float y_position = -0.9f;
        for (int index = 0; index < height_count; index++) {
            float x_position = -0.9f;
            y_position += 0.3f;
            for (int jndex = 0; jndex < width_count; jndex++) {
                x_position += 0.3f;
                this.renderer.DrawCircle(x_position, y_position, 0.03f, 0xFFFF0000);
            }
        }
    }

    private void GameUpdate(double delta) {
        InputEvent input_event = this.input.GetInputEvent();
        this.renderer.BeginDraw();
        this.DrawGameBoard(5, 6);
        this.renderer.DrawPoint(input_event.x, input_event.y, 0xFF00FF00);
        this.renderer.EndDraw();
    }
}
