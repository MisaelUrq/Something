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

    public int lengthInPixels(float length) {
        int width_result  = Math.round(length * Screen.width);
        int height_result = Math.round(length * Screen.height);

        // NOTE(Misael Urquieta): This is 4, because the length if
        // given in a state from 0 - 1, and the screen length is from
        // -1 to 1.
        int result = Math.round((width_result + height_result) / 4);
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
