package com.urquieta.something.game;

import com.urquieta.something.platform.Screen;
import com.urquieta.something.platform.Renderer;

// TODO(Misael): @URGENT make the PC version of the clases to see how
// it will all be handle.
public class Game extends Thread implements Runnable
{
    private boolean   isRunning;
    private final int FPS_CAP = 30;
    private final int MILLION = 1000000;

    private Screen main_canvas;
    private Renderer renderer;

    public Game()
    {
        super();
        this.isRunning = false;
    }

    public void startThread()
    {
        this.isRunning = true;
        this.start();
    }

    public void setScreen(Screen screen) {
        this.main_canvas = screen;
        this.renderer = new Renderer(this.main_canvas);
    }

    @Override
    public void run() {
        long startTime;
        long timeMiliseconds = 1000/FPS_CAP;
        long waitTime;
        int  frameCount = 0;
        long totalTime  = 0;
        long targetTime = timeMiliseconds;

        while (this.isRunning) {
            // Get the time on the frame
            startTime = System.nanoTime();

            // Game code goes here....

            this.GameUpdate();

            // Get the time at the end of the frame.
            timeMiliseconds = (System.nanoTime() - startTime)/MILLION;
            waitTime = targetTime - timeMiliseconds;
            try {
                if (waitTime > 0) {
                    this.sleep(waitTime);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            totalTime += System.nanoTime() + startTime;
            frameCount++;
            if (frameCount > FPS_CAP) {
                long avarageFPS = 1000/((totalTime/frameCount)/MILLION);
                frameCount = 0;
                totalTime  = 0;
                // TODO(Misael): Use loggin system as it will help to
                // set apart diferent types of errors and to make a
                // way to view them more easily to log on android.
                System.out.println("AvarageFPS: "+avarageFPS+".");
            }
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

    private void GameUpdate() {
        this.main_canvas.beginDraw();

        this.renderer.DrawRect(10, 10, 100, 100, 0xFFFF00FF);

        this.main_canvas.endDraw();
    }
}
