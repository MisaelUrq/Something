package com.urquieta.something;

import com.urquieta.something.game.Game;
import com.urquieta.something.platform.Screen;
import com.urquieta.something.platform.Input;

public class PCMain
{
    public static void main(String[] args)
    {
        Game game = new Game();
        Screen game_screen = new Screen(game, null);
        Input game_input = new Input();
        game_screen.addKeyListener(game_input);
        game_screen.addMouseMotionListener(game_input);
        game_screen.addMouseListener(game_input);
        game.setScreen(game_screen);
        game.setInput(game_input);
        game.startThread();
    }
}
