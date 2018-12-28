package com.urquieta.something.game;

import com.urquieta.something.platform.InputEvent;

public class GameState {
    public static final int GAME_OVER  = (0 << 0);
    public static final int PLAYING    = (1 << 0);
    public static final int SHOW_FPS   = (1 << 1);
    public static final int HIDE_FPS   = (0 << 1);

    public static final int START_MENU      = 0;
    public static final int INFINITE_MODE   = 1;
    public static final int NORMAL_MODE     = 2;
    public static final int EXIT            = 3;

    public static boolean is_menu_active   = false;
    private static volatile boolean is_game_running;
    public static int state;
    public static int current_mode = START_MENU;


    public static void SetAllDefault() {
        state = 0;
        is_menu_active = false;
        current_mode   = START_MENU;
    }

    public static void SetRunning(boolean is_running) {
        is_game_running = is_running;
    }

    public static boolean IsRunning() {
        return is_game_running;
    }

    public static void SetState(int new_state) {
        GameState.state |= new_state;
    }
}
