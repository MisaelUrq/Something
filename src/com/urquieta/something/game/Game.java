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
    private final int TARGET_FPS = 30;
    private final int TARGET_DELTA = 1000  / TARGET_FPS;

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
        double start_time = (double)System.nanoTime() / (double)1000000000;
        double delta = 0.0;

        while (this.is_running) {
            double before_time = (double)System.nanoTime() / (double)1000000000;

            this.GameUpdate(delta);

            double after_time = (double)System.nanoTime() / (double)1000000000;
            delta = (after_time - before_time);

            if (TARGET_DELTA > delta) {
                this.SleepThread((long)(TARGET_DELTA - delta)); // @PC_Replace: 'this' -> 'Thread'
            }
        }
    }

    private void SleepThread(long time_to_sleep) {
        try {
            this.sleep(time_to_sleep);
        }
        catch (Exception e) {
            e.printStackTrace();
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

    private void GameUpdate(double delta) {
        InputEvent input_event = this.input.GetInputEvent();
        float x = input_event.x;
        float y = input_event.y;
        float width  = x + 0.5f;
        float height = y - 0.5f;

        this.renderer.BeginDraw();
        this.renderer.DrawRect(x, y, width, height, 0xFFFF0000);
        this.renderer.EndDraw();
    }
}
