package com.urquieta.something;

import com.urquieta.something.game.Game;
import com.urquieta.something.platform.Screen;

public class PCMain
{
    static public void main(String[] args)
    {
        Game game = new Game();
        Screen gameScreen = new Screen(game, null);
        game.setScreen(gameScreen);
        game.startThread();
    }
}
