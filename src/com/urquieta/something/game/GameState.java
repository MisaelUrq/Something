package com.urquieta.something.game;

import com.urquieta.something.platform.InputEvent;

public class GameState {
    public InputEvent last_input;
    public InputEvent current_input;
    private volatile boolean is_game_running; 
    
    public GameState() {
        this.last_input    = null;
        this.current_input = null;
        this.is_game_running = false;
    }

    public boolean IsRunning() {
        return this.is_game_running;
    }

    public void SetRunning(boolean new_state) {
        this.is_game_running = new_state;
    }
}
