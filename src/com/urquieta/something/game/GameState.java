package com.urquieta.something.game;

import com.urquieta.something.platform.InputEvent;

public class GameState {
    public static final int GAME_OVER  = 0;
    public static final int DEBUG_MENU = 1;
    public static final int PLAYING    = 2;
    public InputEvent last_input;
    public InputEvent current_input;
    private volatile boolean is_game_running;
    public int state;

    public GameState() {
        this.last_input    = null;
        this.current_input = null;
        this.is_game_running = false;
        this.state = PLAYING;
    }

    public void SetState(int state) {
        this.state = state;
    }

    public int GetStateOfGame() {
        return state;
    }

    public boolean IsRunning() {
        return this.is_game_running;
    }

    public void SetRunning(boolean new_state) {
        this.is_game_running = new_state;
    }
}
