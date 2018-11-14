package com.urquieta.something.game;

public class Game extends Thread implements Runnable
{
    private boolean isRunning;

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

    @Override
    public void run() {
        while (true) {
            System.out.println("Hello?");
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
}
