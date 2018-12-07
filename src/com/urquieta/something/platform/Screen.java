package com.urquieta.something.platform;

// @Android<>import android.content.Context;
import com.urquieta.something.platform.pc.Context;// @PC

// @Android<>import com.urquieta.something.platform.android.AndroidScreen;
import com.urquieta.something.platform.pc.PCScreen;// @PC

import com.urquieta.something.game.Game;

public class Screen extends PCScreen { // @Class
    private static final long serialVersionUID = 1L;

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

    public int XPositionInPixels(float position) {
        return Math.round((position + 1)*(float)Screen.width/2);
    }

    public int YPositionInPixels(float position) {
        return Math.round((position + 1)*(float)Screen.height/2);
    }

    public int lengthInPixels(float position) {
        int width_result  = Math.round(position * Screen.width);
        int height_result = Math.round(position * Screen.height);
        int result = Math.round((width_result + height_result) / 2);
        return result;
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
