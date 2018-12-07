package com.urquieta.something.game;

import com.urquieta.something.platform.InputEvent;

public class GameState {
    public InputEvent last_input;
    public InputEvent current_input;

    public GameState() {
        this.last_input    = null;
        this.current_input = null;
    }
}
