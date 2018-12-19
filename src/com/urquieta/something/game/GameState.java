package com.urquieta.something.game;

import com.urquieta.something.platform.InputEvent;

public class GameState {
    public static final int GAME_OVER  = (0 << 0);
    public static final int PLAYING    = (1 << 0);
    public static final int SHOW_FPS   = (1 << 1);
    public static final int HIDE_FPS   = (0 << 1);
    
    public static boolean is_menu_active   = false;
    public static volatile boolean is_game_running;
    public static int state;

    public static void SetState(int new_state) {
        GameState.state |= new_state;
    }

}
