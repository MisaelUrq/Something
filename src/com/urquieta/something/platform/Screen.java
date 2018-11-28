package com.urquieta.something.platform;

// @Android<>import android.content.Context;
import com.urquieta.something.platform.pc.Context;// @PC

// @Android<>import com.urquieta.something.platform.android.AndroidScreen;
import com.urquieta.something.platform.pc.PCScreen;// @PC

import com.urquieta.something.game.Game;

public class Screen extends PCScreen { // @Class

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

    public int widthInPixels(float position) {
        return (int)(((position + 1)*((float)Screen.width - 0.0f)-0.0f)/2);
    }

    public int heightInPixels(float position) {
        return (int)(((position + 1)*((float)Screen.height - 0.0f)-0.0f)/2);
    }

    public void SetSize(int width, int height) {
        Screen.width  = width;
        Screen.height = height;
    }

    public int GetWidth() {
        return Screen.width;
    }

    public int GetHeight() {
        return Screen.height;
    }
}
