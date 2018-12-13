package com.urquieta.something;

import com.urquieta.something.game.Game;
import com.urquieta.something.platform.Screen;
import com.urquieta.something.platform.Input;
import com.urquieta.something.platform.Audio;

public class PCMain
{
    public static void main(String[] args)
    {
        Game   game        = new Game();
        Screen game_screen = new Screen(game, null);
        Input  game_input  = new Input();
        Audio  game_audio  = new Audio();
        game_screen.addKeyListener(game_input);
        game_screen.addMouseMotionListener(game_input);
        game_screen.addMouseListener(game_input);
        game.Init(game_screen, game_input, game_audio);
        game.startThread();
    }
}
