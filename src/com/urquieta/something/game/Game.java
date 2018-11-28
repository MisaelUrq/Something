package com.urquieta.something.game;

import com.urquieta.something.platform.Screen;
import com.urquieta.something.platform.Renderer;
import com.urquieta.something.platform.Input;
import com.urquieta.something.platform.InputEvent;

// TODO(Misael): @URGENT make the PC version of the clases to see how
// it will all be handle.
public class Game extends Thread implements Runnable
{
    private boolean   isRunning;
    private final int FPS_CAP = 30;
    private final int MILLION = 1000000;

    private Screen main_canvas;
    private Renderer renderer;
    private Input input;

    public Game()
    {
        super();
        this.isRunning = false;
    }

    public void startThread()
    {
        if (this.renderer != null &&
            this.main_canvas != null &&
            this.input != null)
        {
            this.isRunning = true;
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
        double delta = 0.0;
        // TODO(Misael): IMPORTANT Make a proper frame counter!!
        while (this.isRunning) {


            this.GameUpdate(delta);

        }
    }

    public void stopThread() {
        while (this.isRunning) {
            try {
                this.join();
                this.isRunning = false;
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
