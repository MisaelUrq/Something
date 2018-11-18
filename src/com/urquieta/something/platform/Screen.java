package com.urquieta.something.platform;

import android.content.Context;

import com.urquieta.something.platform.android.AndroidScreen;
import com.urquieta.something.game.Game;


public class Screen extends AndroidScreen {

    public Screen(Game game, Context context) {
        super(game, context);
    }

    @Override
    public void beginDraw() {
        super.beginDraw();
    }

    @Override
    public void endDraw() {
        super.endDraw();
    }
}
