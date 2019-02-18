package com.urquieta.something.game;

import com.urquieta.something.platform.InputEvent;

import com.urquieta.something.game.GameModes;

public class GameState {
    public static final int GAME_OVER  = (0 << 0);
    public static final int PLAYING    = (1 << 0);
    public static final int SHOW_FPS   = (1 << 1);
    public static final int HIDE_FPS   = (0 << 1);

    public static boolean is_menu_active   = false;
    public static boolean restart_level_requested = false;
    public static int state;
    public static GameModes current_mode = GameModes.START_MENU;
    public static boolean randomize = false;

    private static volatile boolean is_game_running;


    public static void SetGameOver() {
        state = state & (~PLAYING);
    }

    public static void SetToPlaying() {
        state = state | PLAYING;
    }

    public static boolean IsGameOver() {
        return ((state & PLAYING) == GAME_OVER);
    }

    public static void SetAllDefault() {
        state = 0;
        is_menu_active = false;
        restart_level_requested = false;
        current_mode   = GameModes.START_MENU;
    }

    public static void SetRunning(boolean is_running) {
        is_game_running = is_running;
    }

    public static boolean IsRunning() {
        return is_game_running;
    }

    public static void ToggleState(int new_state) {
        state ^= new_state;
    }
}
